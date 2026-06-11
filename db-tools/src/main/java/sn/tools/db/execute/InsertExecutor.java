package sn.tools.db.execute;

public class InsertExecutor implements DBActionExecutor<Integer> {

	private final DBExecutor executor;
	private Object entity;

	public InsertExecutor(DBExecutor executor) {
		this.executor = executor;
	}

	public InsertExecutor setEntity(Object entity) {
		this.entity = entity;
		return this;
	}

	@Override
	public Integer execute() {
		if (entity == null) {
			throw new IllegalArgumentException("挿入対象エンティティが設定されていません。");
		}
		try {
			return executor.insert(entity); // int → Integer にオートボクシング
		} finally {
			this.entity = null;
		}
	}
}
