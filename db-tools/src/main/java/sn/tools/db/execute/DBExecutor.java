package sn.tools.db.execute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import sn.tools.db.connect.DBConnector;
import sn.tools.function.uncheck.Uncheck;
import sn.tools.function.uncheck.Uncheck.ThrowableFunction;
import sn.tools.function.uncheck.Uncheck.ThrowableSupplier;

public class DBExecutor {

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

	public <R> List<R> query(String sql, BiFunction<Set<String>, ResultSet, R> packFunc, Object... params) {
		ThrowableFunction<PreparedStatement, List<R>> psFunc = ps -> {
			List<R> ret = new ArrayList<>();
			try (ResultSet rs = ps.executeQuery()) {
				Set<String> labelSet = getLabelSet(rs);
				while (rs.next()) {
					ret.add(packFunc.apply(labelSet, rs));
				}
			}
			return ret;
		};
		return execute(sql, Uncheck.wrapFunction(psFunc), params);
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
