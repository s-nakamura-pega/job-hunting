package sn.tools.db.execute;

public final class Merge extends SqlBuilder<Merge> {

	private boolean hasMatched = false;
	private boolean hasNotMatched = false;

	public Merge() {
		sql.append(" MERGE INTO ");
	}

	public Merge into(String table) {
		return super.appendWithValidate(table, true);
	}

	public Merge using(String table) {
		sql.append(" USING ");
		return super.appendWithValidate(table, true);
	}

	public Merge on(On onClause) {
		sql.append(' ');
		return super.append(onClause);
	}

	public Merge whenMatched(Update updateClause) {
		sql.append(" WHEN MATCHED THEN ");
		sql.append(" UPDATE ");
		sql.append(updateClause.toString().replaceFirst("UPDATE ", ""));
		bindList.addAll(updateClause.getBindList());
		setHasMatched(true);
		return this;
	}

	public Merge whenNotMatched(Insert insertClause) {
		sql.append(" WHEN NOT MATCHED THEN ");
		sql.append(" INSERT ");
		sql.append(insertClause.toString().replaceFirst("INSERT INTO ", ""));
		bindList.addAll(insertClause.getBindList());
		setHasNotMatched(true);
		return this;
	}

	@Override
	public void clear() {
		super.clear();
		sql.append(" MERGE INTO ");
		setHasMatched(false);
		setHasNotMatched(false);
	}

	public boolean isHasMatched() {
		return hasMatched;
	}

	public void setHasMatched(boolean hasMatched) {
		this.hasMatched = hasMatched;
	}

	public boolean isHasNotMatched() {
		return hasNotMatched;
	}

	public void setHasNotMatched(boolean hasNotMatched) {
		this.hasNotMatched = hasNotMatched;
	}

}
