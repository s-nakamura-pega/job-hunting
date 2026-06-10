package sn.tools.db.execute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateExecutor {

	private final DBExecutor executor;
	private String sql;
	private final List<Object> paramList = new ArrayList<>();

	public UpdateExecutor(DBExecutor executor) {
		this.executor = executor;
	}

	public UpdateExecutor setSql(String sql) {
		this.sql = sql;
		return this;
	}

	public UpdateExecutor addParams(Object... params) {
		paramList.addAll(Arrays.asList(params));
		return this;
	}

	public UpdateExecutor addParam(Object param) {
		paramList.add(param);
		return this;
	}

	public int execute() {
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
