package sn.tools.db.execute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeleteExecutor implements DBActionExecutor<Integer> {

	private final DBExecutor executor;
	private String sql;
	private final List<Object> paramList = new ArrayList<>();

	public DeleteExecutor(DBExecutor executor) {
		this.executor = executor;
	}

	public DeleteExecutor setSql(String sql) {
		this.sql = sql;
		return this;
	}

	public DeleteExecutor addParams(Object... params) {
		paramList.addAll(Arrays.asList(params));
		return this;
	}

	public DeleteExecutor addParam(Object param) {
		paramList.add(param);
		return this;
	}

	@Override
	public Integer execute() {
		if (sql == null) {
			throw new IllegalArgumentException("SQLが設定されていません。");
		}
		try {
			return executor.update(sql, paramList.toArray());
		} finally {
			this.sql = null;
			this.paramList.clear();
		}
	}
}
