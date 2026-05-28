package sn.tools.xml.dom;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

/** org.w3c.dom.Elementクラスをラッパーするクラス */
public class DomElementWrapper implements Element {

	/** 元となるXMLエレメント */
	private final Element element;

	/**
	 * コンストラクタ
	 * 
	 * @param element 元となるXMLエレメント
	 */
	public DomElementWrapper(Element element) {
		this.element = Objects.requireNonNull(element, "element must not be null");
	}

	/**
	 * 小要素の内からエレメント要素のリストを取得
	 * 
	 * @return エレメント要素のリスト
	 */
	public List<Element> getChildElementList() {
		return getChildElementList(elem -> true);
	}

	/**
	 * 小要素の内から名前空間の一致するエレメント要素のリストを取得
	 * 
	 * @param namespaceURI 名前空間
	 * @return エレメント要素のリスト
	 */
	public List<Element> getChildElementListNS(String namespaceURI) {
		return getChildElementList(elem -> Objects.equals(elem.getNamespaceURI(), namespaceURI));
	}

	/**
	 * 小要素の内からタグ名の一致するエレメント要素のリストを取得<br>
	 * 名前空間がある場合は名前空間の指定なしとして、一致するものなしとみなす。
	 * 
	 * @param localName タグ名
	 * @return エレメント要素のリスト
	 */
	public List<Element> getChildElementList(String localName) {
		return getChildElementList(null, localName);
	}

	/**
	 * 小要素の内からタグ名の一致するエレメント要素のリストを取得<br>
	 * 名前空間がある場合は名前空間の指定なしとして、一致するものなしとみなす。
	 * 
	 * @param localName タグ名
	 * @return エレメント要素のリスト
	 */
	public Optional<Element> getChildElement(String localName) {
		return getChildElementList(localName).stream().findFirst();
	}

	/**
	 * 小要素の内から名前空間とタグ名の一致するエレメント要素のリストを取得
	 * 
	 * @param namespaceURI 名前空間
	 * @param localName    タグ名
	 * @return エレメント要素のリスト
	 */
	public List<Element> getChildElementList(String namespaceURI, String localName) {
		return getChildElementList(elem -> Objects.equals(elem.getNamespaceURI(), namespaceURI)
				&& Objects.equals(elem.getLocalName(), localName));
	}

	/**
	 * 小要素の内から名前空間とタグ名の一致するエレメント要素のリストを取得
	 * 
	 * @param namespaceURI 名前空間
	 * @param localName    タグ名
	 * @return エレメント要素のリスト
	 */
	public Optional<Element> getChildElement(String namespaceURI, String localName) {
		return getChildElementList(namespaceURI, localName).stream().findFirst();
	}

	/**
	 * 小要素の内から条件に一致するエレメント要素のリストを取得
	 * 
	 * @param predicate 判定処理
	 * @return エレメント要素のリスト
	 */
	public List<Element> getChildElementList(Predicate<Element> predicate) {
		Objects.requireNonNull(predicate, "predicate must not be null");
		Function<Node, Optional<Element>> convertNodeFunction = node -> node instanceof Element element
				&& predicate.test(element)
						? Optional.of(element)
						: Optional.empty();
		return getChildNodeList(convertNodeFunction);
	}

	/**
	 * 小要素をリストで取得
	 * 
	 * @return 小要素のリスト
	 */
	public List<Node> getChildNodeList() {
		return getChildNodeList(node -> Optional.of(node));
	}

	/**
	 * 各小要素に変換処理を行い、結果をリストで取得
	 * 
	 * @param <T>                 変換後の型
	 * @param convertNodeFunction 要素変換処理
	 * @return 小要素を変換したリスト
	 */
	public <T extends Node> List<T> getChildNodeList(Function<Node, Optional<T>> convertNodeFunction) {
		Objects.requireNonNull(convertNodeFunction, "convertNodeFunction must not be null");
		List<T> childNodeList = new ArrayList<>();
		childNodeListForeach(node -> convertNodeFunction.apply(node).ifPresent(childNodeList::add));
		return childNodeList;
	}

	/**
	 * 子要素をループ処理する
	 * 
	 * @param consumer 処理
	 */
	public void childElementListForeach(Consumer<Element> consumer) {
		childNodeListForeach(node -> {
			if (node instanceof Element element) {
				consumer.accept(element);
			}
		});
	}

	/**
	 * 子要素をループ処理する
	 * 
	 * @param consumer 処理
	 */
	public void childNodeListForeach(Consumer<Node> consumer) {
		NodeList childNodes = getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			consumer.accept(childNodes.item(i));
		}
	}

	/**
	 * 子要素をこのラッパーでラップしたリストを取得
	 * 
	 * @return DomElementWrapper のリスト
	 */
	public List<DomElementWrapper> getChildElementWrapperList() {
		return getChildElementList().stream().map(DomElementWrapper::new).collect(Collectors.toList());
	}

	/**
	 * 名前で子要素を1つ取得してラップして返す
	 */
	public Optional<DomElementWrapper> getChildElementWrapper(String localName) {
		return getChildElement(localName).map(DomElementWrapper::new);
	}

	/**
	 * 名前空間と名前で子要素を1つ取得してラップして返す
	 */
	public Optional<DomElementWrapper> getChildElementWrapper(String namespaceURI, String localName) {
		return getChildElement(namespaceURI, localName).map(DomElementWrapper::new);
	}

	/**
	 * 属性をリストで取得
	 *
	 * @param
	 * @return Attr のリスト
	 */
	public List<Attr> getAttributeList(String namespaceURI) {
		List<Attr> list = new ArrayList<>();
		attributeListForeach(namespaceURI, (Consumer<Attr>) list::add);
		return list;
	}

	/**
	 * 属性をリストで取得
	 *
	 * @return Attr のリスト
	 */
	public List<Attr> getAttributeList() {
		List<Attr> list = new ArrayList<>();
		getAttributeList(null);
		return list;
	}

	/**
	 * 属性を順に処理する
	 *
	 * @param namespaceURI 名前空間
	 * @param consumer     属性ごとに呼ばれる処理
	 */
	public void attributeListForeach(String namespaceURI, Consumer<Attr> consumer) {
		Objects.requireNonNull(consumer, "consumer must not be null");
		NamedNodeMap attrs = element.getAttributes();
		for (int i = 0; i < attrs.getLength(); i++) {
			Node node = attrs.item(i);
			if (Objects.equals(namespaceURI, node.getNamespaceURI()) && node instanceof Attr attr) {
				consumer.accept(attr);
			}
		}
	}

	/**
	 * 属性を順に処理する
	 *
	 * @param consumer 属性ごとに呼ばれる処理
	 */
	public void attributeListForeach(Consumer<Attr> consumer) {
		attributeListForeach(null, consumer);
	}

	/**
	 * 属性を順に処理する
	 *
	 * @param namespaceURI 名前空間
	 * @param consumer     属性ごとに呼ばれる処理
	 */
	public void attributeListForeach(String namespaceURI, BiConsumer<String, String> biConsumer) {
		Objects.requireNonNull(biConsumer, "biConsumer must not be null");
		attributeListForeach(namespaceURI, attr -> biConsumer.accept(attr.getName(), attr.getValue()));
	}

	/**
	 * 属性を順に処理する
	 *
	 * @param consumer 属性ごとに呼ばれる処理
	 */
	public void attributeListForeach(BiConsumer<String, String> biConsumer) {
		attributeListForeach(null, biConsumer);
	}

	@Override
	public String toString() {
		String name = element.getNodeName();
		String text = element.getTextContent();
		if (text == null) {
			return "DomElementWrapper[" + name + "]";
		}
		String shortText = text.length() > 64 ? text.substring(0, 64) + "..." : text;
		return "DomElementWrapper[" + name + "]:" + shortText;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o instanceof DomElementWrapper other) {
			return Objects.equals(this.element, other.element);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(element);
	}

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
