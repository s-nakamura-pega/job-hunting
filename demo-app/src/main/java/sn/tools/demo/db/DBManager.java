package sn.tools.demo.db;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sn.tools.db.connect.DBConnector;
import sn.tools.db.execute.DBExecutor;
import sn.tools.db.execute.DeleteExecutor;
import sn.tools.db.execute.InsertExecutor;
import sn.tools.db.execute.QueryExecutor;
import sn.tools.db.execute.UpdateExecutor;
import sn.tools.xml.bind.creator.XmlObjectCreator;
import sn.tools.xml.dom.DocumentUtils;

public class DBManager {

	private static final Path DB_PATH = Path.of("demo.db");
	private static final Path INIT_SQL = Path.of("init.sql");

	public static void initDB() {
		try {
			Files.deleteIfExists(DB_PATH);
			String sql = Files.readString(INIT_SQL);
			String[] statements = sql.split(";");
			UpdateExecutor exec = getUpdateExecutor();
			for (String stmt : statements) {
				String s = stmt.trim();
				if (!s.isEmpty()) {
					exec.setSql(s).execute();
				}
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public static DBExecutor getDBExecutor() {
		Document doc = DocumentUtils.emptyDocument(true);
		Element root = doc.createElement("config");
		doc.appendChild(root);
		Element url = doc.createElementNS(null, "url");
		url.setTextContent("jdbc:sqlite:" + DB_PATH);
		root.appendChild(url);
		DBConnector dbConn = new XmlObjectCreator<>(root, DBConnector.class).create();
		return new DBExecutor(dbConn);
	}

	public static QueryExecutor getQueryExecutor() {
		return new QueryExecutor(getDBExecutor());
	}

	public static UpdateExecutor getUpdateExecutor() {
		return new UpdateExecutor(getDBExecutor());
	}

	public static InsertExecutor getInsertExecutor() {
		return new InsertExecutor(getDBExecutor());
	}

	public static DeleteExecutor getDeleteExecutor() {
		return new DeleteExecutor(getDBExecutor());
	}

}
