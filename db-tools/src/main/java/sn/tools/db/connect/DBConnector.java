package sn.tools.db.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

import sn.tools.function.uncheck.Uncheck;
import sn.tools.xml.bind.annotation.InjectXmlElement;

public class DBConnector {

	private String url;
	
	private final Properties properties = new Properties();

	@InjectXmlElement("driver")
	public void setDriver(Element driver) throws ClassNotFoundException, DOMException {
		Class.forName(driver.getTextContent().trim());
	}

	@InjectXmlElement("url")
	public void setUrl(Element url) {
		this.url = url.getTextContent().trim();
	}

	@InjectXmlElement("property")
	public void setProperty(Element element) {
		setProperty(element.getAttribute("name"), element.getAttribute("value"));
	}

	public void setProperty(String name, String value) {
		properties.setProperty(name, value);
	}

	public Connection getConnection() {
		return Uncheck.wrapSupplier(() -> DriverManager.getConnection(url, properties)).get();
	}

}
