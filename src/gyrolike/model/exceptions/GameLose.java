package gyrolike.model.exceptions;

@SuppressWarnings("serial")
public class GameLose extends GameEnd {
	private String reason;

	public GameLose(String reason) {
		super();
		this.reason = reason;
	}

	public String getReason() {
		return this.reason;
	}
}
