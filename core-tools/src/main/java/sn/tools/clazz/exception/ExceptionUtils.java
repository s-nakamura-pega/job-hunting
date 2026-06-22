package sn.tools.clazz.exception;

import java.util.function.Predicate;

public class ExceptionUtils {

	public static Exception getRootCause(Exception e) {
		return getRootCause(e, _ -> false);
	}

	public static Exception getRootCause(Exception e, Predicate<Exception> predicate) {
		if (e == null || predicate == null) {
			return null;
		}
		Exception current = e;
		while (current != null) {
			// 1. 現在の階層が条件に合致したら、その時点で即返却
			if (predicate.test(current)) {
				return current;
			}
			// 2. 次の階層の型を安全にチェック
			if (current.getCause() instanceof Exception nextException) {
				current = nextException;
			} else {
				// 次の原因が null、または Exception 以外の型なら、
				// 現在の current を大元として確定し、ループを抜ける
				break;
			}
		}
		// 3. 確定した大元の例外を返す（出口はここ1箇所！）
		return current;
	}

}