package sn.tools.xml.bind.creator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import org.w3c.dom.Element;

import sn.tools.xml.bind.annotation.InjectXmlAttribute;
import sn.tools.xml.bind.annotation.InjectXmlElement;
import sn.tools.xml.bind.annotation.InjectXmlTextContent;
import sn.tools.xml.dom.DomElementWrapper;

public class XmlObjectCreator<T> {

    private final DomElementWrapper element;
    private final Class<T> clazz;
    private final List<Method> attributeAndTextMethods = new ArrayList<>();
    private final List<Method> elementMethods = new ArrayList<>();
    private final List<ConstructorArgument<?>> constructorArgs = new ArrayList<>();

    public XmlObjectCreator(Element element, Class<T> clazz) {
        this.element = new DomElementWrapper(element);
        this.clazz = clazz;
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(InjectXmlAttribute.class) ||
                    method.isAnnotationPresent(InjectXmlTextContent.class)) {
                attributeAndTextMethods.add(method);
            }
            if (method.isAnnotationPresent(InjectXmlElement.class)) {
                elementMethods.add(method);
            }
        }
    }

    public T create() {
        try {
            Constructor<T> constructor = clazz.getConstructor(
                    constructorArgs.stream().map(ConstructorArgument::clazz).toArray(Class<?>[]::new));
            Object[] values = constructorArgs.stream()
                    .map(ConstructorArgument::value)
                    .toArray();
            T instance = constructor.newInstance(values);
            injectValues(instance);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void injectValues(T t) {
        for (Method method : attributeAndTextMethods) {
            InjectXmlAttribute ixa = method.getDeclaredAnnotation(InjectXmlAttribute.class);
            if (ixa != null) {
                if (element.hasAttributeNS(ixa.namespaceURI(), ixa.value())) {
                    injectStringValues(t, method, element.getAttributeNS(ixa.namespaceURI(), ixa.value()));
                }
            } else {
                injectStringValues(t, method, element.getTextContent().trim());
            }
        }
        Consumer<Element> consumer = elem -> {
            for (Method method : elementMethods) {
                InjectXmlElement ixe = method.getDeclaredAnnotation(InjectXmlElement.class);
                if (ixe != null) {
                    if (Objects.equals(elem.getNamespaceURI(), ixe.namespaceURI())
                            && Objects.equals(elem.getLocalName(), ixe.value())) {
                        injectElementValues(t, method, elem);
                    }
                }
            }
        };
        element.childElementListForeach(consumer);
    }

    private void injectStringValues(T t, Method method, String value) {
        Class<?>[] argsClasses = method.getParameterTypes();
        if (argsClasses.length != 1 || !String.class.equals(argsClasses[0])) {
            throw new IllegalArgumentException("属性値はString型で受け取る必要があります");
        }
        try {
            method.invoke(t, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void injectElementValues(T t, Method method, Element element) {
        Class<?>[] argsClasses = method.getParameterTypes();
        if (argsClasses.length != 1 || !Element.class.equals(argsClasses[0])) {
            throw new IllegalArgumentException("要素値はElement型で受け取る必要があります");
        }
        try {
            method.invoke(t, element);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public <R> boolean addConstructorArgument(Class<R> clazz, R value) {
        return constructorArgs.add(new ConstructorArgument<>(clazz, value));
    }

    public static record ConstructorArgument<R>(Class<R> clazz, R value) {
    }

}
