package sn.tools.xml.bind.creator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import org.w3c.dom.Element;

import sn.tools.clazz.creator.AbstractObjectCreator;
import sn.tools.clazz.exception.ExceptionUtils;
import sn.tools.function.uncheck.Uncheck;
import sn.tools.function.uncheck.Uncheck.ThrowableRunnable;
import sn.tools.function.uncheck.Uncheck.VoidExceptionHandler;
import sn.tools.xml.bind.annotation.InjectXmlAttribute;
import sn.tools.xml.bind.annotation.InjectXmlElement;
import sn.tools.xml.bind.annotation.InjectXmlTextContent;
import sn.tools.xml.dom.DomElementWrapper;

public class XmlObjectCreator<T> extends AbstractObjectCreator<T>{

    private final DomElementWrapper element;
    private final List<Method> attributeAndTextMethods = new ArrayList<>();
    private final List<Method> elementMethods = new ArrayList<>();
    private Consumer<T> consumer;

    public XmlObjectCreator(Element element, Class<T> clazz) {
        super(clazz);
    	this.element = new DomElementWrapper(element);
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

	public void setPreDecorateProcess(Consumer<T> consumer) {
		this.consumer = consumer;
	}

	@Override
	protected void decorate(T instance) {
		if (consumer != null) {
			consumer.accept(instance);
		}
		injectValues(instance);
	}

    private void injectValues(T t) {
        for (Method method : attributeAndTextMethods) {
            InjectXmlAttribute ixa = method.getDeclaredAnnotation(InjectXmlAttribute.class);
            if (ixa != null) {
                String namespaceURI = ixa.namespaceURI().isBlank() ? null : ixa.namespaceURI();
				for (String name : ixa.value()) {
					if (element.hasAttributeNS(namespaceURI, name)) {
						injectStringValues(t, method, element.getAttributeNS(namespaceURI, name));
						break;
					}
				}
            } else {
                injectStringValues(t, method, element.getTextContent().trim());
            }
        }
		Consumer<Element> consumer = elem -> {
			for (Method method : elementMethods) {
				InjectXmlElement ixe = method.getDeclaredAnnotation(InjectXmlElement.class);
				if (ixe != null) {
					String namespaceURI = ixe.namespaceURI().isBlank() ? null : ixe.namespaceURI();
					for (String nameRegex : ixe.value()) {
						if (Objects.equals(elem.getNamespaceURI(), namespaceURI)
								&& elem.getTagName().matches(String.format("^%s$", nameRegex))) {
							injectElementValues(t, method, elem);
							break;
						}
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
		ThrowableRunnable runnable = () -> method.invoke(t, value);
		VoidExceptionHandler handler = e -> {
			throw new RuntimeException(ExceptionUtils.getRootCause(e));
		};
		Uncheck.wrapRunnable(runnable, handler).run();
	}

	private void injectElementValues(T t, Method method, Element element) {
		Class<?>[] argsClasses = method.getParameterTypes();
		if (argsClasses.length != 1 || !Element.class.equals(argsClasses[0])) {
			throw new IllegalArgumentException("要素値はElement型で受け取る必要があります");
		}
		ThrowableRunnable runnable = () -> method.invoke(t, element);
		VoidExceptionHandler handler = e -> {
			throw new RuntimeException(ExceptionUtils.getRootCause(e));
		};
		Uncheck.wrapRunnable(runnable, handler).run();
	}

}
