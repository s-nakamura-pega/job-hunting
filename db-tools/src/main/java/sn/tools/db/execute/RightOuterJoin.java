package sn.tools.db.execute;

public final class RightOuterJoin extends SqlBuilder<RightOuterJoin> {

	public RightOuterJoin() {
		sql.append(" RIGHT OUTER JOIN ");
	}

	@Override
	public RightOuterJoin appendWithValidate(String table, boolean isValid, Object... unused) {
		if (!isValid)
			return this;
		return super.appendWithValidate(table, true);
	}

	public RightOuterJoin append(String table) {
		return appendWithValidate(table, true);
	}

	public RightOuterJoin on(On onClause) {
		return super.append(onClause);
	}

	@Override
	public void clear() {
		super.clear();
		sql.append(" RIGHT OUTER JOIN ");
	}
}
