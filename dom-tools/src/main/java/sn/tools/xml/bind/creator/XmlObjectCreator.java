package sn.tools.xml.bind.creator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import org.w3c.dom.Element;
import sn.tools.clazz.exception.ExceptionUtils;
import sn.tools.function.uncheck.Uncheck;
import sn.tools.function.uncheck.Uncheck.ExceptionHandler;
import sn.tools.function.uncheck.Uncheck.ThrowableRunnable;
import sn.tools.function.uncheck.Uncheck.ThrowableSupplier;
import sn.tools.function.uncheck.Uncheck.VoidExceptionHandler;
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
		ThrowableSupplier<T> supplier = () -> {
			Constructor<T> constructor = clazz
					.getConstructor(constructorArgs.stream().map(ConstructorArgument::clazz).toArray(Class<?>[]::new));
			Object[] values = constructorArgs.stream().map(ConstructorArgument::value).toArray();
			T instance = constructor.newInstance(values);
			injectValues(instance);
			return instance;
		};
		ExceptionHandler<T> handler = e -> {
			throw new RuntimeException(ExceptionUtils.getRootCause(e));
		};
		return Uncheck.wrapSupplier(supplier, handler).get();
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
					for (String name : ixe.value()) {
						if (Objects.equals(elem.getNamespaceURI(), namespaceURI)
								&& Objects.equals(elem.getLocalName(), name)) {
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

    public <R> boolean addConstructorArgument(Class<R> clazz, R value) {
        return constructorArgs.add(new ConstructorArgument<>(clazz, value));
    }

    public static record ConstructorArgument<R>(Class<R> clazz, R value) {
    }

}
