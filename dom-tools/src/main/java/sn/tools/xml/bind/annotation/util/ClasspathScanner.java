package sn.tools.xml.bind.annotation.util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ClasspathScanner {

    private ClasspathScanner() {
    }

    public static <T extends Annotation> List<Class<?>> findAnnotatedClasses(String packageName, Class<T> annotation)
            throws IOException {
        return findAnnotatedClasses(packageName, annotation, a -> true);
    }

    public static <T extends Annotation> List<Class<?>> findAnnotatedClasses(String packageName, Class<T> annotation,
            Predicate<T> filter) throws IOException {
        String path = packageName.replace('.', '/');
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = cl.getResources(path);
        List<Class<?>> result = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {
                try {
                    Path dir = Paths.get(URLDecoder.decode(url.getPath(), "UTF-8"));
                    try (Stream<Path> stream = Files.walk(dir)) {
                        List<Path> classes = stream.filter(p -> p.toString().endsWith(".class"))
                                .collect(Collectors.toList());
                        for (Path p : classes) {
                            String rel = dir.relativize(p).toString().replace(FileSystems.getDefault().getSeparator(),
                                    "/");
                            String className = packageName + "." + rel.substring(0, rel.length() - 6).replace('/', '.');
                            tryLoadAndCheck(className, annotation, result, cl, filter);
                        }
                    }
                } catch (Exception e) {
                    // decode/IO/URI などの例外は上位で扱ってください
                }
            } else if ("jar".equals(protocol) || url.toExternalForm().startsWith("jar:")) {
                JarURLConnection conn = (JarURLConnection) url.openConnection();
                try (JarFile jar = conn.getJarFile()) {
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if (name.startsWith(path) && name.endsWith(".class")) {
                            String className = name.substring(0, name.length() - 6).replace('/', '.');
                            tryLoadAndCheck(className, annotation, result, cl, filter);
                        }
                    }
                }
            } else {
                // other protocols (vfs, etc.) は実装依存。ClassGraph を検討。
            }
        }
        return result;
    }

    private static <T extends Annotation> void tryLoadAndCheck(String className, Class<T> annotation,
            List<Class<?>> result, ClassLoader cl, Predicate<T> filter) {
        try {
            Class<?> cls = Class.forName(className, false, cl);
            if (cls.isAnnotationPresent(annotation) && filter.test(cls.getAnnotation(annotation))) {
                result.add(cls);
            }
        } catch (Throwable t) {
            // クラス読み込み失敗は無視またはログ出力（副作用や依存解決に注意）
        }
    }

}
