package sn.tools.xml.bind.creator;

import java.util.ArrayList;

import org.w3c.dom.Element;

import sn.tools.xml.dom.DomElementWrapper;

public class XmlObjectCreator extends ArrayList<XmlObjectCreator.ConstructorArgument<?>> {

    private final DomElementWrapper element;

    public XmlObjectCreator(Element element) {
        this.element = new DomElementWrapper(element);
    }

    public static record ConstructorArgument<T>(Class<T> clazz, T value) {
    }

}
