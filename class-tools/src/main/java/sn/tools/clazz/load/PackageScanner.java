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
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

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
				Path startPath = Paths.get(filePath);
				classList.addAll(findClassesFromDirectory(startPath, packageName, predicate));
			} else if ("jar".equals(protocol)) {
				classList.addAll(findClassesFromJar(resource, packageName, predicate));
			}
		}
		return classList;
	}

	/**
	 * 通常のディレクトリからクラスを探索
	 */
	private static List<Class<?>> findClassesFromDirectory(Path startPath, String packageName,
			Predicate<Class<?>> predicate) throws IOException, ClassNotFoundException {
		List<Class<?>> classList = new ArrayList<>();
		if (!Files.exists(startPath)) {
			return classList;
		}
		try (Stream<Path> walk = Files.walk(startPath)) {
			walk.filter(Files::isRegularFile).filter(path -> path.toString().endsWith(".class")).forEach(path -> {
				try {
					Path relativePath = startPath.relativize(path);
					String subPackageAndClassName = relativePath.toString()
							.replace(startPath.getFileSystem().getSeparator(), ".")
							.substring(0, relativePath.toString().length() - 6);

					String className = packageName
							+ (subPackageAndClassName.isEmpty() ? "" : "." + subPackageAndClassName);
					Class<?> clazz = Class.forName(className);

					if (predicate.test(clazz)) {
						classList.add(clazz);
					}
				} catch (ClassNotFoundException e) {
					throw new IllegalStateException(e);
				}
			});
		} catch (IllegalStateException e) {
			if (e.getCause() instanceof ClassNotFoundException) {
				throw (ClassNotFoundException) e.getCause();
			}
			throw e;
		}
		return classList;
	}

	/**
	 * JARファイルからクラスを探索
	 */
	private static List<Class<?>> findClassesFromJar(URL jarUrl, String packageName, Predicate<Class<?>> predicate)
			throws IOException, ClassNotFoundException {
		List<Class<?>> classList = new ArrayList<>();
		String packagePathPrefix = packageName.replace('.', '/') + "/";
		JarURLConnection jarURLConnection = (JarURLConnection) jarUrl.openConnection();
		try (JarFile jarFile = jarURLConnection.getJarFile()) {
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				String entryName = entry.getName();
				if (entryName.startsWith(packagePathPrefix) && entryName.endsWith(".class")) {
					String className = entryName.substring(0, entryName.length() - 6).replace('/', '.');
					Class<?> clazz = Class.forName(className);

					if (predicate.test(clazz)) {
						classList.add(clazz);
					}
				}
			}
		}
		return classList;
	}

}