package sn.tools.db.execute;

public final class LeftOuterJoin extends SqlBuilder<LeftOuterJoin> {

	public LeftOuterJoin() {
		sql.append(" LEFT OUTER JOIN ");
	}

	@Override
	public LeftOuterJoin appendWithValidate(String table, boolean isValid, Object... unused) {
		if (!isValid) {
			return this;
		}
		return super.appendWithValidate(table, true);
	}

	public LeftOuterJoin append(String table) {
		return appendWithValidate(table, true);
	}

	public LeftOuterJoin on(On onClause) {
		return super.append(onClause);
	}

	@Override
	public void clear() {
		super.clear();
		sql.append(" LEFT OUTER JOIN ");
	}

}
