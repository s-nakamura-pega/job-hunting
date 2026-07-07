package sn.tools.db.execute;

public final class From extends SqlBuilder<From> {

	public From() {
		sql.append(" FROM ");
	}

	@Override
	public From appendWithValidate(String table, boolean isValid, Object... unused) {
		if (!isValid) {
			return this;
		}
		// FROM は bind を使わないので空配列で super を呼ぶ
		return super.appendWithValidate(table, true);
	}

	public From append(String table) {
		return appendWithValidate(table, true);
	}

	@Override
	public void clear() {
		super.clear();
		sql.append(" FROM ");
	}

}
