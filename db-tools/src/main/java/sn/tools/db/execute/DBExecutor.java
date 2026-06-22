package sn.tools.db.execute;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import sn.tools.db.annotation.DBColumn;
import sn.tools.db.annotation.DBTable;
import sn.tools.db.connect.DBConnector;
import sn.tools.db.response.DBResponse;
import sn.tools.clazz.creator.ObjectCreator;
import sn.tools.function.uncheck.Uncheck;
import sn.tools.function.uncheck.Uncheck.ThrowableConsumer;
import sn.tools.function.uncheck.Uncheck.ThrowableFunction;
import sn.tools.function.uncheck.Uncheck.ThrowableSupplier;

public class DBExecutor {

	private final DBConnector connector;

	public DBExecutor(DBConnector connector) {
		this.connector = connector;
	}

	private <R> R execute(Function<Connection, R> connFunc, boolean isTransaction) {
		ThrowableSupplier<R> supplier = () -> {
			try (Connection conn = connector.getConnection()) {
				conn.setAutoCommit(!isTransaction);
				try {
					R ret = connFunc.apply(conn);
					if (isTransaction) {
						conn.commit();
					}
					return ret;
				} catch (Exception e) {
					if (isTransaction) {
						conn.rollback();
					}
					throw e;
				}
			}
		};
		return Uncheck.wrapSupplier(supplier).get();
	}

	private <R> R execute(String sql, Function<PreparedStatement, R> psFunc, boolean isTransaction, Object... params) {
		ThrowableFunction<Connection, R> connFunc = conn -> {
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				IntStream.rangeClosed(1, params.length)
						.forEach(i -> Uncheck.wrapRunnable(() -> ps.setObject(i, params[i - 1])).run());
				return psFunc.apply(ps);
			}
		};
		return execute(Uncheck.wrapFunction(connFunc), isTransaction);
	}

	private <R> List<R> query(String sql, BiFunction<Set<String>, ResultSet, R> packFunc, Object... params) {
		ThrowableFunction<PreparedStatement, List<R>> psFunc = ps -> {
			List<R> ret = new ArrayList<>();
			try (ResultSet rs = ps.executeQuery()) {
				Set<String> labelStream = getLabelSet(rs);
				while (rs.next()) {
					ret.add(packFunc.apply(labelStream, rs));
				}
			}
			return ret;
		};
		return execute(sql, Uncheck.wrapFunction(psFunc), false, params);
	}

	public List<Map<String, Object>> query(String sql, Object... params) {
		BiFunction<Set<String>, ResultSet, Map<String, Object>> packFunc = (s, rs) -> {
			Map<String, Object> ret = new HashMap<>();
			s.forEach(label -> ret.put(label, Uncheck.wrapSupplier(() -> rs.getObject(label)).get()));
			return ret;
		};
		return query(sql, packFunc, params);
	}

	public <R> List<R> query(String sql, ObjectCreator<R> creator, Object... params) {
		List<DBResponse> responseList = query(sql, List.of(creator), params);
		return responseList.stream().map(dbr -> dbr.get(creator.getCreateClass())).toList();
	}

	public List<DBResponse> query(String sql, List<ObjectCreator<?>> creatorList, Object... params) {
		Map<ObjectCreator<?>, InjectTargets> packMap = new HashMap<>(creatorList.size());
		for (ObjectCreator<?> creator : creatorList) {
			Class<?> clazz = creator.getCreateClass();
			Map<String, Field> fieldMap = Arrays.stream(clazz.getFields())
					.filter(f -> f.isAnnotationPresent(DBColumn.class))
					.collect(Collectors.toMap(f -> f.getAnnotation(DBColumn.class).value(), f -> f));
			Map<String, Method> methodMap = Arrays.stream(clazz.getMethods())
					.filter(m -> m.isAnnotationPresent(DBColumn.class)).filter(m -> m.getParameterCount() == 1)
					.collect(Collectors.toMap(m -> m.getAnnotation(DBColumn.class).value(), m -> m));
			packMap.put(creator, new InjectTargets(fieldMap, methodMap));
		}
		BiFunction<Set<String>, ResultSet, DBResponse> packFunc = (s, rs) -> {
			DBResponse response = new DBResponse();
			packMap.forEach((creator, targets) -> {
				Object value = creator.create();
				Map<String, Field> fieldMap = targets.fieldMap();
				Map<String, Method> methodMap = targets.methodMap();
				ThrowableConsumer<String> injectFunc = label -> {
					if (fieldMap.containsKey(label)) {
						Field f = fieldMap.get(label);
						if (f != null) {
							f.set(value, rs.getObject(label));
						}
					}
					if (methodMap.containsKey(label)) {
						Method m = methodMap.get(label);
						if (m != null) {
							m.invoke(value, rs.getObject(label));
						}
					}
				};
				s.forEach(Uncheck.wrapConsumer(injectFunc));
				response.put(creator.getCreateClass(), value);
			});
			return response;
		};
		return query(sql, packFunc, params);
	}

	public Integer update(String sql, Object... params) {
		ThrowableFunction<PreparedStatement, Integer> psFunc = ps -> ps.executeUpdate();
		return execute(sql, Uncheck.wrapFunction(psFunc), true, params);
	}

	private Set<String> getLabelSet(ResultSet rs) {
		ThrowableSupplier<Set<String>> supplier = () -> {
			ResultSetMetaData rsmd = rs.getMetaData();
			return IntStream.rangeClosed(1, rsmd.getColumnCount())
					.mapToObj(i -> Uncheck.wrapSupplier(() -> rsmd.getColumnLabel(i)).get())
					.collect(Collectors.toCollection(LinkedHashSet::new));
		};
		return Uncheck.wrapSupplier(supplier).get();
	}

	private record InjectTargets(Map<String, Field> fieldMap, Map<String, Method> methodMap) {
	}

	public int insert(Object object) {
		Class<?> clazz = object.getClass();
		if (!clazz.isAnnotationPresent(DBTable.class)) {
			return -1;
		}
		DBTable dbTable = clazz.getAnnotation(DBTable.class);
		Map<String, Object> paramMap = new LinkedHashMap<>();
		Arrays.stream(clazz.getFields()).filter(f -> f.isAnnotationPresent(DBColumn.class)).forEach(f -> {
			String col = f.getAnnotation(DBColumn.class).value();
			Object val = Uncheck.wrapSupplier(() -> f.get(object)).get();
			if (val != null) {
				paramMap.put(col, val);
			}
		});
		Arrays.stream(clazz.getMethods()).filter(m -> m.isAnnotationPresent(DBColumn.class))
				.filter(m -> m.getParameterCount() == 0).forEach(m -> {
					String col = m.getAnnotation(DBColumn.class).value();
					if (!paramMap.containsKey(col)) {
						Object val = Uncheck.wrapSupplier(() -> m.invoke(object)).get();
						if (val != null) {
							paramMap.put(col, val);
						}
					}
				});
		if (paramMap.isEmpty()) {
			return -1;
		}
		List<String> labelList = new ArrayList<>(paramMap.keySet());
		List<String> bindList = labelList.stream().map(_ -> "?").toList();
		List<Object> valueList = new ArrayList<>(paramMap.values());
		String sql = String.format("INSERT INTO %s(%s) VALUES(%s)",
				dbTable.value(),
				String.join(",", labelList),
				String.join(",", bindList));
		return update(sql, valueList.toArray());
	}

}
