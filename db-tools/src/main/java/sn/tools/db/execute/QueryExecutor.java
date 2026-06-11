package sn.tools.db.execute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sn.tools.clazz.creator.ObjectCreator;
import sn.tools.db.response.DBResponse;

public class QueryExecutor implements DBActionExecutor<List<DBResponse>> {

	private final DBExecutor executor;
	private String sql;
	private final List<ObjectCreator<?>> creatorList = new ArrayList<>();
	private final List<Object> paramList = new ArrayList<>();

	public QueryExecutor(DBExecutor executor) {
		this.executor = executor;
	}

	public QueryExecutor setSql(String sql) {
		this.sql = sql;
		return this;
	}

	public QueryExecutor addCreators(ObjectCreator<?>... creators) {
		creatorList.addAll(Arrays.asList(creators));
		return this;
	}

	public QueryExecutor addCreator(ObjectCreator<?> creator) {
		creatorList.add(creator);
		return this;
	}

	public QueryExecutor addParams(Object... params) {
		paramList.addAll(Arrays.asList(params));
		return this;
	}

	public QueryExecutor addParam(Object param) {
		paramList.add(param);
		return this;
	}

	@Override
	public List<DBResponse> execute() {
		if (sql == null) {
			throw new IllegalArgumentException("SQLが設定されていません。");
		}
		try {
			return executor.query(sql, List.copyOf(creatorList), paramList.toArray());
		} finally {
			this.sql = null;
			this.creatorList.clear();
			this.paramList.clear();
		}
	}

}
