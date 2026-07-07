package sn.tools.db.execute;

public final class InnerJoin extends SqlBuilder<InnerJoin> {

	public InnerJoin() {
		sql.append(" INNER JOIN ");
	}

	@Override
	public InnerJoin appendWithValidate(String table, boolean isValid, Object... unused) {
		if (!isValid) {
			return this;
		}
		return super.appendWithValidate(table, true);
	}

	public InnerJoin append(String table) {
		return appendWithValidate(table, true);
	}

	public InnerJoin on(On onClause) {
		return super.append(onClause);
	}

	@Override
	public void clear() {
		super.clear();
		sql.append(" INNER JOIN ");
	}

}
