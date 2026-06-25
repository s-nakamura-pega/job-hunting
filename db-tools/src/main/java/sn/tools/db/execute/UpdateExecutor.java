package sn.tools.db.execute;

import sn.tools.db.sql.SqlBuilder;

public class UpdateExecutor extends SqlBuilder<UpdateExecutor> implements DBActionExecutor<Integer> {

	private final DBExecutor executor;

	public UpdateExecutor(DBExecutor executor) {
		this.executor = executor;
	}

	@Override
	public Integer execute() {
		if (sql.length() < 1) {
			throw new IllegalArgumentException("SQLが設定されていません。");
		}
		try {
			return executor.update(toString(), getBinds());
		} finally {
			clear();
		}
	}

}
