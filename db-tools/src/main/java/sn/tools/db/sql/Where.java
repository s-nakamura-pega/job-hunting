package sn.tools.db.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class Where {

	private final StringBuilder sql = new StringBuilder(" WHERE true ");
	private final List<Object> bindList = new ArrayList<>();
	private boolean hasAndCondition = false;

	public Where add(String condition, Predicate<Object[]> ignoreCondition, Object... binds) {
		if (ignoreCondition.test(binds)) {
			return this;
		}
		checkBindCount(condition, binds);
		sql.append("AND " + condition + " ");
		if (binds != null) {
			bindList.addAll(Arrays.asList(binds));
		}
		hasAndCondition = true;
		return this;
	}

	public Where add(String condition, Object... binds) {
		return add(condition, Where::hasNullBinds, binds);
	}

	public Where addOr(String condition, Predicate<Object[]> ignoreCondition, Object... binds) {
		if (!hasAndCondition) {
			throw new IllegalArgumentException("条件が設定されていません。");
		}
		if (ignoreCondition.test(binds)) {
			return this;
		}
		checkBindCount(condition, binds);
		sql.append("OR " + condition + " ");
		if (binds != null) {
			bindList.addAll(Arrays.asList(binds));
		}
		return this;
	}

	public Where addOr(String condition, Object... binds) {
		return addOr(condition, Where::hasNullBinds, binds);
	}

	private void checkBindCount(String condition, Object[] binds) {
		if (isInvalidBindCount(condition, binds)) {
			throw new IllegalArgumentException(
					String.format("bindの指定が不正です。[condition: %s, bind_count: %d]", condition, binds.length));
		}
	}

	private boolean isInvalidBindCount(String condition, Object[] binds) {
		return condition.chars().filter(c -> c == '?').count() != binds.length;
	}

	public Object[] getBinds() {
		return bindList.toArray();
	}

	public List<Object> getBindList() {
		return List.copyOf(bindList);
	}

	@Override
	public String toString() {
		return sql.toString();
	}

	public static boolean hasNullBinds(Object[] binds) {
		return binds == null || Arrays.stream(binds).anyMatch(b -> b == null);
	}

	public static String createInCondition(String column, int size) {
		String bindsStr = String.join(", ", Collections.nCopies(size, "?"));
		return String.format("%s IN (%s)", column, bindsStr);
	}

}
