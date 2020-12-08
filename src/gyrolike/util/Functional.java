package gyrolike.util;

import java.util.function.Consumer;

public class Functional {
	private Functional() {}

	public static interface CodeBlock extends MaybeThrower<Exception> {
		public static Runnable toRunnable(CodeBlock cb) {
			return cb.toRunnable();
		}
	}

	public static interface MaybeThrower<T extends Throwable> {
		void run() throws T;

		default Runnable toRunnable() {
			return () -> {
				try {
					this.run();
				} catch(RuntimeException e) {
					throw e;
				} catch(Throwable e) {
					throw new RuntimeException(e);
				}
			};
		}

		public static Runnable toRunnable(MaybeThrower<?> cb) {
			return cb.toRunnable();
		}
		public static <T extends Throwable> Runnable toRunnable(MaybeThrower<T> cb, Class<T> z) {
			return cb.toRunnable();
		}
	}

	public static interface MaybeConsumer<T, E extends Throwable> {
		void run(T v) throws E;

		default Consumer<T> toConsumer() {
			return v -> {
				try {
					this.run(v);
				} catch(RuntimeException e) {
					throw e;
				} catch(Throwable e) {
					throw new RuntimeException(e);
				}
			};
		}
	}
}
