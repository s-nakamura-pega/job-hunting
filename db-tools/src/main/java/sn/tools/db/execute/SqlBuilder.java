package sn.tools.db.execute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public sealed abstract class SqlBuilder<T extends SqlBuilder<?>>
		permits QueryExecutor, UpdateExecutor, Where, Select, From, InnerJoin, LeftOuterJoin, On, OrderBy, GroupBy,
		RightOuterJoin, FullOuterJoin, Having, Update, Insert, Delete, Merge {

	protected final StringBuilder sql = new StringBuilder();
	protected final List<Object> bindList = new ArrayList<>();

	@SuppressWarnings("unchecked")
	public T appendWithValidate(String condition, boolean isValid, Object... binds) {
		if (!isValid) {
			return (T) this;
		}
		checkBindCount(condition, binds);
		sql.append(' ').append(condition).append(' ');
		bindList.addAll(Arrays.asList(binds));
		return (T) this;
	}

	public T append(String condition, Object... binds) {
		return appendWithValidate(condition, true, binds);
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

	@SuppressWarnings("unchecked")
	public T append(SqlBuilder<?> condition, boolean isValid) {
		if (!isValid) {
			return (T) this;
		}
		append(condition.toString().trim(), condition.getBinds());
		return (T) this;
	}

	public T append(SqlBuilder<?> condition) {
		return append(condition, true);
	}

	public String watchParameters() {
		return String.format("SQL: %s, Binds: %s", sql.toString(), bindList.toString());
	}

	@Override
	public String toString() {
		return sql.toString();
	}

	public void clear() {
		sql.setLength(0);
		bindList.clear();
	}
}
