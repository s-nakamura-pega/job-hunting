package sn.tools.file.jar;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import static sn.tools.function.uncheck.Uncheck.wrapRunnable;

public class JarFiles {

	public static Stream<JarEntry> walk(Path path) {
		try {
			JarFile jar = new JarFile(path.toFile());
			return jar.stream().onClose(wrapRunnable(jar::close));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

}
