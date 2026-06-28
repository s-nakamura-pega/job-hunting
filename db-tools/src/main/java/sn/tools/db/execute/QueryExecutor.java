package sn.tools.db.execute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sn.tools.clazz.creator.ObjectCreator;
import sn.tools.db.response.DBResponse;

public final class QueryExecutor extends SqlBuilder<QueryExecutor> implements DBActionExecutor<List<DBResponse>> {

	private final DBExecutor executor;
	private final List<ObjectCreator<?>> creatorList = new ArrayList<>();

	public QueryExecutor(DBExecutor executor) {
		this.executor = executor;
	}

	public QueryExecutor addCreators(ObjectCreator<?>... creators) {
		creatorList.addAll(Arrays.asList(creators));
		return this;
	}

	public QueryExecutor addCreator(ObjectCreator<?> creator) {
		creatorList.add(creator);
		return this;
	}

	@Override
	public List<DBResponse> execute() {
		if (sql.length() < 1) {
			throw new IllegalArgumentException("SQLが設定されていません。");
		}
		try {
			return executor.query(toString(), List.copyOf(creatorList), getBinds());
		} finally {
			clear();
		}
	}

	@Override
	public void clear() {
		super.clear();
		this.creatorList.clear();
	}

}
