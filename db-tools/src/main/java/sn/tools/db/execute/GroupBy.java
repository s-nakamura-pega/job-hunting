package sn.tools.db.execute;

public final class GroupBy extends SqlBuilder<GroupBy> {

	private boolean hasColumn = false;

	public GroupBy() {
		sql.append(" GROUP BY ");
	}

	@Override
	public GroupBy appendWithValidate(String column, boolean isValid, Object... unused) {
		if (!isValid) {
			return this;
		}

		// カンマ区切り管理
		if (hasColumn) {
			sql.append(", ");
		}
		hasColumn = true;

		// GROUP BY は bind を使わないので空配列で super を呼ぶ
		return super.appendWithValidate(column, true);
	}

	public GroupBy append(String column) {
		return appendWithValidate(column, true);
	}

	@Override
	public GroupBy append(SqlBuilder<?> builder) {
		if (hasColumn) {
			sql.append(", ");
		}
		hasColumn = true;

		// builder の SQL をそのまま追加
		sql.append(builder.toString().trim());
		return this;
	}

	@Override
	public void clear() {
		super.clear();
		sql.append(" GROUP BY ");
		hasColumn = false;
	}

}
