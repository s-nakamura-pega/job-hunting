package sn.tools.db.execute;

import java.util.Arrays;
import java.util.Collections;

public final class Where extends SqlBuilder<Where> {

	private boolean hasAndCondition = false;

	public Where() {
		sql.append(" WHERE true ");
	}

	@Override
	public Where appendWithValidate(String condition, boolean isValid, Object... binds) {
		if (!isValid) {
			return this;
		}
		hasAndCondition = true;
		return super.appendWithValidate("AND " + condition, true, binds);
	}

	@Override
	public Where append(String condition, Object... binds) {
		return appendWithValidate(condition, !hasNullBinds(binds), binds);
	}

	public Where appendOrWithValidate(String condition, boolean isValid, Object... binds) {
		return hasAndCondition ? (Where) super.appendWithValidate("OR " + condition, isValid, binds)
				: appendWithValidate(condition, isValid, binds);
	}

	public Where appendOr(String condition, Object... binds) {
		return appendOrWithValidate(condition, !hasNullBinds(binds), binds);
	}

	public static boolean hasNullBinds(Object[] binds) {
		return binds == null || Arrays.stream(binds).anyMatch(b -> b == null);
	}

	public static String createInCondition(String column, int size) {
		String bindsStr = String.join(", ", Collections.nCopies(size, "?"));
		return String.format("%s IN (%s)", column, bindsStr);
	}

	@Override
	public void clear() {
		super.clear();
		sql.append(" WHERE true ");
		hasAndCondition = false;
	}

}
