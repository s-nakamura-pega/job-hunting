package sn.tools.db.execute;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import sn.tools.db.annotation.DBColumn;
import sn.tools.db.connect.DBConnector;
import sn.tools.function.functions.ObjectCreator;
import sn.tools.function.uncheck.Uncheck;
import sn.tools.function.uncheck.Uncheck.ThrowableConsumer;
import sn.tools.function.uncheck.Uncheck.ThrowableFunction;
import sn.tools.function.uncheck.Uncheck.ThrowableSupplier;

public class DBExecutor {

	private static final Map<Class<?>, Map<String, WeakReference<Field>>> fieldCache = Collections
			.synchronizedMap(new WeakHashMap<>());
	private static final Map<Class<?>, Map<String, WeakReference<Method>>> methodCache = Collections
			.synchronizedMap(new WeakHashMap<>());

	private final DBConnector connector;

	public DBExecutor(DBConnector connector) {
		this.connector = connector;
	}

	private synchronized <R> R execute(String sql, Function<Connection, R> connFunc) {
		ThrowableSupplier<R> supplier = () -> {
			try (Connection conn = connector.getConnection()) {
				return connFunc.apply(conn);
			}
		};
		return Uncheck.wrapSupplier(supplier).get();
	}

	private <R> R execute(String sql, Function<PreparedStatement, R> psFunc, Object... params) {
		ThrowableFunction<Connection, R> connFunc = conn -> {
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				IntStream.rangeClosed(1, params.length)
						.forEach(i -> Uncheck.wrapRunnable(() -> ps.setObject(i, params[i - 1])).run());
				return psFunc.apply(ps);
			}
		};
		return execute(sql, Uncheck.wrapFunction(connFunc));
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
		return execute(sql, Uncheck.wrapFunction(psFunc), params);
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
		Class<R> clazz = creator.getCreateClass();
		Map<String, WeakReference<Field>> fieldMap = fieldCache.computeIfAbsent(clazz,
				c -> Arrays.stream(c.getFields()).filter(f -> f.isAnnotationPresent(DBColumn.class)).collect(
						Collectors.toMap(f -> f.getAnnotation(DBColumn.class).value(), f -> new WeakReference<>(f))));
		Map<String, WeakReference<Method>> methodMap = methodCache.computeIfAbsent(clazz,
				c -> Arrays.stream(c.getMethods()).filter(m -> m.isAnnotationPresent(DBColumn.class)).collect(
						Collectors.toMap(m -> m.getAnnotation(DBColumn.class).value(), m -> new WeakReference<>(m))));
		BiFunction<Set<String>, ResultSet, R> packFunc = (s, rs) -> {
			R ret = creator.create();
			ThrowableConsumer<String> injectFunc = label -> {
				if (fieldMap.containsKey(label)) {
					Field f = fieldMap.get(label).get();
					if (f != null) {
						f.set(ret, rs.getObject(label));
					}
				}
				if (methodMap.containsKey(label)) {
					Method m = methodMap.get(label).get();
					if (m != null) {
						m.invoke(ret, rs.getObject(label));
					}
				}
			};
			s.forEach(Uncheck.wrapConsumer(injectFunc));
			return ret;
		};
		return query(sql, packFunc, params);
	}

	public Integer update(String sql, Object... params) {
		ThrowableFunction<PreparedStatement, Integer> psFunc = ps -> ps.executeUpdate();
		return execute(sql, Uncheck.wrapFunction(psFunc), params);
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

}
