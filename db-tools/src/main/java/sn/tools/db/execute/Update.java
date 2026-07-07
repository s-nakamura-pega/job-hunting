package sn.tools.db.execute;

public final class Update extends SqlBuilder<Update> {

	private boolean hasSet = false;

	public Update() {
		sql.append(" UPDATE ");
	}

	public Update table(String table) {
		return super.appendWithValidate(table, true);
	}

	public Update set(String columnEq, Object... binds) {
		if (hasSet) {
			sql.append(", ");
		} else {
			sql.append(" SET ");
		}
		hasSet = true;

		return super.appendWithValidate(columnEq, true, binds);
	}

	@Override
	public void clear() {
		super.clear();
		sql.append(" UPDATE ");
		hasSet = false;
	}
}
