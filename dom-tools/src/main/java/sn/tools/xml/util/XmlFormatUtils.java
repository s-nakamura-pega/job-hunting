package sn.tools.xml.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

public final class XmlFormatUtils {

	private XmlFormatUtils() {
	}

	public static String format(String xml) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(false);
			factory.setCoalescing(true);
			factory.setIgnoringElementContentWhitespace(true);
			factory.setNamespaceAware(true);

			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(xml)));
			doc.normalizeDocument();

			StringWriter writer = new StringWriter();
			writeNode(doc, writer, 0);
			return writer.toString();

		} catch (Exception e) {
			return xml; // 失敗時は元のXMLを返す
		}
	}

	private static void writeNode(Node node, StringWriter writer, int indent) {
		switch (node.getNodeType()) {
		case Node.DOCUMENT_NODE:
			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			Node child = node.getFirstChild();
			while (child != null) {
				writeNode(child, writer, 0);
				child = child.getNextSibling();
			}
			break;

		case Node.ELEMENT_NODE:
			indent(writer, indent);
			writer.write("<" + node.getNodeName());

			// 属性
			NamedNodeMap attrs = node.getAttributes();
			for (int i = 0; i < attrs.getLength(); i++) {
				Node a = attrs.item(i);
				writer.write(" " + a.getNodeName() + "=\"" + a.getNodeValue() + "\"");
			}

			Node firstChild = node.getFirstChild();
			if (firstChild == null) {
				writer.write("/>\n");
			} else {
				writer.write(">");
				boolean hasElementChild = hasElementChild(node);

				if (hasElementChild)
					writer.write("\n");

				Node childNode = firstChild;
				while (childNode != null) {
					writeNode(childNode, writer, indent + 1);
					childNode = childNode.getNextSibling();
				}

				if (hasElementChild)
					indent(writer, indent);
				writer.write("</" + node.getNodeName() + ">\n");
			}
			break;

		case Node.TEXT_NODE:
			String text = node.getNodeValue().trim();
			if (!text.isEmpty()) {
				writer.write(text);
			}
			break;

		case Node.COMMENT_NODE:
			indent(writer, indent);
			writer.write("<!-- " + node.getNodeValue() + " -->\n");
			break;
		}
	}

	private static boolean hasElementChild(Node node) {
		Node child = node.getFirstChild();
		while (child != null) {
			if (child.getNodeType() == Node.ELEMENT_NODE)
				return true;
			child = child.getNextSibling();
		}
		return false;
	}

	private static void indent(StringWriter writer, int indent) {
		for (int i = 0; i < indent; i++)
			writer.write("\t");
	}
}
