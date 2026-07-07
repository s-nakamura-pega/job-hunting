package sn.tools.db.execute;

public final class FullOuterJoin extends SqlBuilder<FullOuterJoin> {

	public FullOuterJoin() {
		sql.append(" FULL OUTER JOIN ");
	}

	@Override
	public FullOuterJoin appendWithValidate(String table, boolean isValid, Object... unused) {
		if (!isValid)
			return this;
		return super.appendWithValidate(table, true);
	}

	public FullOuterJoin append(String table) {
		return appendWithValidate(table, true);
	}

	public FullOuterJoin on(On onClause) {
		return super.append(onClause);
	}

	@Override
	public void clear() {
		super.clear();
		sql.append(" FULL OUTER JOIN ");
	}
}
