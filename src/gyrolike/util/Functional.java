package gyrolike.util;

public class Functional {
	private Functional() {}

	public static interface CodeBlock {
		void run() throws Exception;

		default Runnable toRunnable() {
			return () -> {
				try {
					this.run();
				} catch(RuntimeException e) {
					throw e;
				} catch(Exception e) {
					throw new RuntimeException(e);
				}
			};
		}

		public static Runnable toRunnable(CodeBlock cb) {
			return cb.toRunnable();
		}
	}
}
