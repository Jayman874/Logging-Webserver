package nz.ac.wgtn.swen301.resthome4logs.server;

public class LogEvent {
	
	private String id;
	private String message;
	private String timestamp;
	private String thread;
	private String logger;
	private String level;
	private String errorDetails;

	public LogEvent(String id, String message, String timestamp, String thread, String logger, String level, String errorDetails) {
		this.id = id;
		this.message = message;
		this.timestamp = timestamp;
		this.thread = thread;
		this.logger = logger;
		this.level = level;
		this.errorDetails = errorDetails;
	}

	public String getId() {
		return this.id;
	}

	public String getMessage() {
		return this.message;
	}

	public String getTimestamp() {
		return this.timestamp;
	}

	public String getThread() {
		return this.thread;
	}

	public String getLogger() {
		return this.logger;
	}

	public String getLevel() {
		return this.level;
	}

	public String getErrorDetails() {
		return this.errorDetails;
	}
}
