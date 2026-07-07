package sn.tools.db.execute;

import java.util.Arrays;
import java.util.Collections;

public final class Insert extends SqlBuilder<Insert> {

	private boolean hasColumns = false;
	private boolean hasValues = false;

	public Insert() {
		sql.append(" INSERT INTO ");
	}

	public Insert into(String table) {
		return super.appendWithValidate(table, true);
	}

	public Insert columns(String... columns) {
		sql.append(" (");
		sql.append(String.join(", ", columns));
		sql.append(") ");
		hasColumns = true;
		return this;
	}

	public Insert values(Object... binds) {
		if (!hasColumns) {
			throw new IllegalStateException("columns() を先に呼んでください");
		}

		String placeholders = String.join(", ", Collections.nCopies(binds.length, "?"));
		sql.append(" VALUES (").append(placeholders).append(") ");

		bindList.addAll(Arrays.asList(binds));
		setHasValues(true);
		return this;
	}

	@Override
	public void clear() {
		super.clear();
		sql.append(" INSERT INTO ");
		hasColumns = false;
		setHasValues(false);
	}

	public boolean isHasValues() {
		return hasValues;
	}

	public void setHasValues(boolean hasValues) {
		this.hasValues = hasValues;
	}

}
