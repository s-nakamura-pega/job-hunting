package sn.tools.dom.dto;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

public class DomElementWrapper implements Element {

	private final Element element;

	public DomElementWrapper(Element element) {
		this.element = element;
	}

	// TODO 機能追加

	@Override
	public Node appendChild(Node arg0) throws DOMException {
		return element.appendChild(arg0);
	}

	@Override
	public Node cloneNode(boolean arg0) {
		return element.cloneNode(arg0);
	}

	@Override
	public short compareDocumentPosition(Node arg0) throws DOMException {
		return element.compareDocumentPosition(arg0);
	}

	@Override
	public NamedNodeMap getAttributes() {
		return element.getAttributes();
	}

	@Override
	public String getBaseURI() {
		return element.getBaseURI();
	}

	@Override
	public NodeList getChildNodes() {
		return element.getChildNodes();
	}

	@Override
	public Object getFeature(String arg0, String arg1) {
		return element.getFeature(arg0, arg1);
	}

	@Override
	public Node getFirstChild() {
		return element.getFirstChild();
	}

	@Override
	public Node getLastChild() {
		return element.getLastChild();
	}

	@Override
	public String getLocalName() {
		return element.getLocalName();
	}

	@Override
	public String getNamespaceURI() {
		return element.getNamespaceURI();
	}

	@Override
	public Node getNextSibling() {
		return element.getNextSibling();
	}

	@Override
	public String getNodeName() {
		return element.getNodeName();
	}

	@Override
	public short getNodeType() {
		return element.getNodeType();
	}

	@Override
	public String getNodeValue() throws DOMException {
		return element.getNodeValue();
	}

	@Override
	public Document getOwnerDocument() {
		return element.getOwnerDocument();
	}

	@Override
	public Node getParentNode() {
		return element.getParentNode();
	}

	@Override
	public String getPrefix() {
		return element.getPrefix();
	}

	@Override
	public Node getPreviousSibling() {
		return element.getPreviousSibling();
	}

	@Override
	public String getTextContent() throws DOMException {
		return element.getTextContent();
	}

	@Override
	public Object getUserData(String arg0) {
		return element.getUserData(arg0);
	}

	@Override
	public boolean hasAttributes() {
		return element.hasAttributes();
	}

	@Override
	public boolean hasChildNodes() {
		return element.hasChildNodes();
	}

	@Override
	public Node insertBefore(Node arg0, Node arg1) throws DOMException {
		return element.insertBefore(arg0, arg1);
	}

	@Override
	public boolean isDefaultNamespace(String arg0) {
		return element.isDefaultNamespace(arg0);
	}

	@Override
	public boolean isEqualNode(Node arg0) {
		return element.isEqualNode(arg0);
	}

	@Override
	public boolean isSameNode(Node arg0) {
		return element.isSameNode(arg0);
	}

	@Override
	public boolean isSupported(String arg0, String arg1) {
		return element.isSupported(arg0, arg1);
	}

	@Override
	public String lookupNamespaceURI(String arg0) {
		return element.lookupNamespaceURI(arg0);
	}

	@Override
	public String lookupPrefix(String arg0) {
		return element.lookupPrefix(arg0);
	}

	@Override
	public void normalize() {
		element.normalize();
	}

	@Override
	public Node removeChild(Node arg0) throws DOMException {
		return element.removeChild(arg0);
	}

	@Override
	public Node replaceChild(Node arg0, Node arg1) throws DOMException {
		return element.replaceChild(arg0, arg1);
	}

	@Override
	public void setNodeValue(String arg0) throws DOMException {
		element.setNodeValue(arg0);
	}

	@Override
	public void setPrefix(String arg0) throws DOMException {
		element.setPrefix(arg0);
	}

	@Override
	public void setTextContent(String arg0) throws DOMException {
		element.setTextContent(arg0);
	}

	@Override
	public Object setUserData(String arg0, Object arg1, UserDataHandler arg2) {
		return element.setUserData(arg0, arg1, arg2);
	}

	@Override
	public String getAttribute(String name) {
		return element.getAttribute(name);
	}

	@Override
	public String getAttributeNS(String namespaceURI, String localName) throws DOMException {
		return element.getAttributeNS(namespaceURI, localName);
	}

	@Override
	public Attr getAttributeNode(String name) {
		return element.getAttributeNode(name);
	}

	@Override
	public Attr getAttributeNodeNS(String namespaceURI, String localName) throws DOMException {
		return element.getAttributeNodeNS(namespaceURI, localName);
	}

	@Override
	public NodeList getElementsByTagName(String name) {
		return element.getElementsByTagName(name);
	}

	@Override
	public NodeList getElementsByTagNameNS(String namespaceURI, String localName) throws DOMException {
		return element.getElementsByTagNameNS(namespaceURI, localName);
	}

	@Override
	public TypeInfo getSchemaTypeInfo() {
		return element.getSchemaTypeInfo();
	}

	@Override
	public String getTagName() {
		return element.getTagName();
	}

	@Override
	public boolean hasAttribute(String name) {
		return element.hasAttribute(name);
	}

	@Override
	public boolean hasAttributeNS(String namespaceURI, String localName) throws DOMException {
		return element.hasAttributeNS(namespaceURI, localName);
	}

	@Override
	public void removeAttribute(String name) throws DOMException {
		element.removeAttribute(name);
	}

	@Override
	public void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
		element.removeAttributeNS(namespaceURI, localName);
	}

	@Override
	public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
		return element.removeAttributeNode(oldAttr);
	}

	@Override
	public void setAttribute(String name, String value) throws DOMException {
		element.setAttribute(name, value);
	}

	@Override
	public void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {
		element.setAttributeNS(namespaceURI, qualifiedName, value);
	}

	@Override
	public Attr setAttributeNode(Attr newAttr) throws DOMException {
		return element.setAttributeNode(newAttr);
	}

	@Override
	public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
		return element.setAttributeNodeNS(newAttr);
	}

	@Override
	public void setIdAttribute(String name, boolean isId) throws DOMException {
		element.setIdAttribute(name, isId);
	}

	@Override
	public void setIdAttributeNS(String namespaceURI, String localName, boolean isId) throws DOMException {
		element.setIdAttributeNS(namespaceURI, localName, isId);
	}

	@Override
	public void setIdAttributeNode(Attr idAttr, boolean isId) throws DOMException {
		element.setIdAttributeNode(idAttr, isId);
	}

}
