package sn.tools.function.uncheck;

import java.util.function.*;

public class Uncheck {

	// Runnable
	public static Runnable wrapRunnable(ThrowableRunnable runnable) {
		return wrapRunnable(runnable, e -> {
			throw new RuntimeException(e);
		});
	}

	public static Runnable wrapRunnable(ThrowableRunnable runnable, Consumer<Exception> consumer) {
		return () -> {
			try {
				runnable.run();
			} catch (Exception e) {
				consumer.accept(e);
			}
		};
	}

	// Supplier
	public static <R> Supplier<R> wrapSupplier(ThrowableSupplier<R> supplier) {
		return wrapSupplier(supplier, e -> {
			throw new RuntimeException(e);
		});
	}

	public static <R> Supplier<R> wrapSupplier(ThrowableSupplier<R> supplier, Consumer<Exception> consumer) {
		return () -> {
			try {
				return supplier.get();
			} catch (Exception e) {
				consumer.accept(e);
				return null;
			}
		};
	}

	// Consumer
	public static <T> Consumer<T> wrapConsumer(ThrowableConsumer<T> consumer) {
		return wrapConsumer(consumer, e -> {
			throw new RuntimeException(e);
		});
	}

	public static <T> Consumer<T> wrapConsumer(ThrowableConsumer<T> consumer, Consumer<Exception> exceptionConsumer) {
		return t -> {
			try {
				consumer.accept(t);
			} catch (Exception e) {
				exceptionConsumer.accept(e);
			}
		};
	}

	// BiConsumer
	public static <T, U> BiConsumer<T, U> wrapBiConsumer(ThrowableBiConsumer<T, U> consumer) {
		return wrapBiConsumer(consumer, e -> {
			throw new RuntimeException(e);
		});
	}

	public static <T, U> BiConsumer<T, U> wrapBiConsumer(ThrowableBiConsumer<T, U> consumer,
			Consumer<Exception> exceptionConsumer) {
		return (t, u) -> {
			try {
				consumer.accept(t, u);
			} catch (Exception e) {
				exceptionConsumer.accept(e);
			}
		};
	}

	// Function
	public static <T, R> Function<T, R> wrapFunction(ThrowableFunction<T, R> function) {
		return wrapFunction(function, e -> {
			throw new RuntimeException(e);
		});
	}

	public static <T, R> Function<T, R> wrapFunction(ThrowableFunction<T, R> function, Consumer<Exception> consumer) {
		return t -> {
			try {
				return function.apply(t);
			} catch (Exception e) {
				consumer.accept(e);
				return null;
			}
		};
	}

	// BiFunction
	public static <T, U, R> BiFunction<T, U, R> wrapBiFunction(ThrowableBiFunction<T, U, R> bifunction) {
		return wrapBiFunction(bifunction, e -> {
			throw new RuntimeException(e);
		});
	}

	public static <T, U, R> BiFunction<T, U, R> wrapBiFunction(ThrowableBiFunction<T, U, R> bifunction,
			Consumer<Exception> consumer) {
		return (t, u) -> {
			try {
				return bifunction.apply(t, u);
			} catch (Exception e) {
				consumer.accept(e);
				return null;
			}
		};
	}

	// Predicate
	public static <T> Predicate<T> wrapPredicate(ThrowablePredicate<T> predicate) {
		return wrapPredicate(predicate, e -> {
			throw new RuntimeException(e);
		});
	}

	public static <T> Predicate<T> wrapPredicate(ThrowablePredicate<T> predicate, Consumer<Exception> consumer) {
		return t -> {
			try {
				return predicate.test(t);
			} catch (Exception e) {
				consumer.accept(e);
				return false;
			}
		};
	}

	// BiPredicate
	public static <T, U> BiPredicate<T, U> wrapBiPredicate(ThrowableBiPredicate<T, U> predicate) {
		return wrapBiPredicate(predicate, e -> {
			throw new RuntimeException(e);
		});
	}

	public static <T, U> BiPredicate<T, U> wrapBiPredicate(ThrowableBiPredicate<T, U> predicate,
			Consumer<Exception> consumer) {
		return (t, u) -> {
			try {
				return predicate.test(t, u);
			} catch (Exception e) {
				consumer.accept(e);
				return false;
			}
		};
	}

	// インターフェース定義
	@FunctionalInterface
	public interface ThrowableRunnable {
		void run() throws Exception;
	}

	@FunctionalInterface
	public interface ThrowableSupplier<R> {
		R get() throws Exception;
	}

	@FunctionalInterface
	public interface ThrowableConsumer<T> {
		void accept(T t) throws Exception;
	}

	@FunctionalInterface
	public interface ThrowableBiConsumer<T, U> {
		void accept(T t, U u) throws Exception;
	}

	@FunctionalInterface
	public interface ThrowableFunction<T, R> {
		R apply(T t) throws Exception;
	}

	@FunctionalInterface
	public interface ThrowableBiFunction<T, U, R> {
		R apply(T t, U u) throws Exception;
	}

	@FunctionalInterface
	public interface ThrowablePredicate<T> {
		boolean test(T t) throws Exception;
	}

	@FunctionalInterface
	public interface ThrowableBiPredicate<T, U> {
		boolean test(T t, U u) throws Exception;
	}
}