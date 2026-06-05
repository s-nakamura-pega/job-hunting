package sn.tools.xml.dom;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import sn.tools.function.uncheck.Uncheck;
import sn.tools.function.uncheck.Uncheck.ExceptionHandler;
import sn.tools.function.uncheck.Uncheck.ThrowableRunnable;
import sn.tools.function.uncheck.Uncheck.ThrowableSupplier;
import sn.tools.function.uncheck.Uncheck.VoidExceptionHandler;

public class DocumentUtils {

	public static Document emptyDocument(boolean namespaceAware) {
		return Uncheck.wrapSupplier(() -> {
			DocumentBuilderFactory factory = createFactory(namespaceAware);
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.newDocument();
		}).get();
	}

	public static Document read(URL url, boolean namespaceAware) {
		ThrowableSupplier<Document> supplier = () -> {
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			try (InputStream is = conn.getInputStream();) {
				DocumentBuilderFactory factory = createFactory(namespaceAware);
				DocumentBuilder builder = factory.newDocumentBuilder();
				return builder.parse(is);
			}
		};
		ExceptionHandler<Document> handler = e -> {
			if (e instanceof IOException ioe) {
				throw new UncheckedIOException(ioe);
			}
			throw new RuntimeException(e);
		};
		return Uncheck.wrapSupplier(supplier, handler).get();
	}

	private static DocumentBuilderFactory createFactory(boolean namespaceAware) {
		return Uncheck.wrapSupplier(() -> {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(namespaceAware);
			factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
			factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
			factory.setExpandEntityReferences(false);
			return factory;
		}).get();
	}

	public static String toString(Document document) {
		return Uncheck.wrapSupplier(() -> {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(document), new StreamResult(writer));
			return writer.toString();
		}).get();
	}

	public static void write(Document document, Path path) {
		ThrowableRunnable runnable = () -> Files.write(path, toString(document).getBytes(StandardCharsets.UTF_8));
		VoidExceptionHandler handler = e -> {
			if (e instanceof IOException ioe) {
				throw new UncheckedIOException(ioe);
			}
			throw new RuntimeException(e);
		};
		Uncheck.wrapRunnable(runnable, handler).run();
	}

}
