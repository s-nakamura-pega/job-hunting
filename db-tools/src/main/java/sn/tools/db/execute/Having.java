package sn.tools.db.execute;

import java.util.Arrays;
import java.util.Collections;

public final class Having extends SqlBuilder<Having> {

	private boolean hasAndCondition = false;

	public Having() {
		sql.append(" HAVING true ");
	}

	@Override
	public Having appendWithValidate(String condition, boolean isValid, Object... binds) {
		if (!isValid) {
			return this;
		}
		hasAndCondition = true;
		return super.appendWithValidate("AND " + condition, true, binds);
	}

	@Override
	public Having append(String condition, Object... binds) {
		return appendWithValidate(condition, !hasNullBinds(binds), binds);
	}

	public Having appendOrWithValidate(String condition, boolean isValid, Object... binds) {
		return hasAndCondition ? (Having) super.appendWithValidate("OR " + condition, isValid, binds)
				: appendWithValidate(condition, isValid, binds);
	}

	public Having appendOr(String condition, Object... binds) {
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
		sql.append(" HAVING true ");
		hasAndCondition = false;
	}
}
