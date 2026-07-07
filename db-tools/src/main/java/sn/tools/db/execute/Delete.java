package sn.tools.db.execute;

public final class Delete extends SqlBuilder<Delete> {

	public Delete() {
		sql.append(" DELETE FROM ");
	}

	public Delete from(String table) {
		return super.appendWithValidate(table, true);
	}

	@Override
	public void clear() {
		super.clear();
		sql.append(" DELETE FROM ");
	}

}
