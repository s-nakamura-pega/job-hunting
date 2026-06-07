package sn.tools.clazz.load;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.stream.Stream;

import static sn.tools.function.uncheck.Uncheck.wrapFunction;
import static sn.tools.file.jar.JarFiles.walk;

public class PackageScanner {

	public static List<Class<?>> getClassList(String packageName) throws IOException, ClassNotFoundException {
		return getClassList(packageName, clazz -> true);
	}

	public static List<Class<?>> getClassList(String packageName, Predicate<Class<?>> predicate)
			throws IOException, ClassNotFoundException {
		List<Class<?>> classList = new ArrayList<>();
		if (packageName == null || packageName.isEmpty() || predicate == null) {
			return classList;
		}
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			String protocol = resource.getProtocol();
			if ("file".equals(protocol)) {
				String filePath = URLDecoder.decode(resource.getFile(), StandardCharsets.UTF_8);
				classList.addAll(findClassesFromDirectory(Paths.get(filePath), packageName, predicate, classLoader));
			} else if ("jar".equals(protocol)) {
				classList.addAll(findClassesFromJar(resource, packageName, predicate, classLoader));
			}
		}
		return classList;
	}

	private static List<Class<?>> findClassesFromDirectory(Path startPath, String packageName,
			Predicate<Class<?>> predicate, ClassLoader classLoader) throws IOException {
		if (!Files.exists(startPath)) {
			return List.of();
		}
		String separator = startPath.getFileSystem().getSeparator();
		try (Stream<Path> walk = Files.walk(startPath)) {
			return walk.filter(Files::isRegularFile).filter(path -> path.toString().endsWith(".class")).map(path -> {
				Path relativePath = startPath.relativize(path);
				String subPackageAndClassName = relativePath.toString().replace(separator, ".").substring(0,
						relativePath.toString().length() - 6);
				return packageName + (subPackageAndClassName.isEmpty() ? "" : "." + subPackageAndClassName);
			}).map(getLoadClassFunction(classLoader)).filter(predicate).toList();
		}
	}

	private static List<Class<?>> findClassesFromJar(URL jarUrl, String packageName, Predicate<Class<?>> predicate,
			ClassLoader classLoader) throws IOException {
		String packagePathPrefix = packageName.replace('.', '/') + "/";
		JarURLConnection jarURLConnection = (JarURLConnection) jarUrl.openConnection();
		Path jarPath = Paths.get(jarURLConnection.getJarFileURL().getPath());
		try (Stream<JarEntry> walk = walk(jarPath)) {
			return walk.filter(entry -> !entry.isDirectory()).map(JarEntry::getName)
					.filter(name -> name.startsWith(packagePathPrefix) && name.endsWith(".class"))
					.map(name -> name.substring(0, name.length() - 6).replace('/', '.'))
					.map(getLoadClassFunction(classLoader)).filter(predicate).toList();
		}
	}

	// クラス初期化を防ぐためのブリッジメソッド
	private static Function<String, Class<?>> getLoadClassFunction(ClassLoader classLoader) {
		return wrapFunction(className -> Class.forName(className, false, classLoader));
	}

}