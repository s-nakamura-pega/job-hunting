package sn.tools.db.execute;

public final class OrderBy extends SqlBuilder<OrderBy> {

	public OrderBy() {
		sql.append(" ORDER BY ");
	}

	@Override
	public OrderBy appendWithValidate(String order, boolean isValid, Object... unused) {
		if (!isValid) {
			return this;
		}
		return super.appendWithValidate(order, true);
	}

	public OrderBy append(String order) {
		return appendWithValidate(order, true);
	}

	@Override
	public void clear() {
		super.clear();
		sql.append(" ORDER BY ");
	}

}
