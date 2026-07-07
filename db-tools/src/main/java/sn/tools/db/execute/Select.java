package sn.tools.db.execute;

public final class Select extends SqlBuilder<Select> {

	private boolean hasColumn = false;

	public Select() {
		sql.append(" SELECT ");
	}

	/**
	 * super.appendWithValidate を使う SELECT 専用 append binds は使わないので空配列を渡す
	 */
	@Override
	public Select appendWithValidate(String column, boolean isValid, Object... unused) {
		if (!isValid) {
			return this;
		}
		if (hasColumn) {
			sql.append(", ");
		}
		hasColumn = true;
		return super.appendWithValidate(column, true);
	}

	public Select append(String column) {
		return appendWithValidate(column, true);
	}

	@Override
	public void clear() {
		super.clear();
		sql.append(" SELECT ");
		hasColumn = false;
	}

}
