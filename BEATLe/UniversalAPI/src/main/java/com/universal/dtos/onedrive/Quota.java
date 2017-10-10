package com.universal.dtos.onedrive;

public class Quota {
	private long deleted;
	private long remaining;
	private String state;
	private long total;
	private long used;

	public long getDeleted() {
		return deleted;
	}

	public void setDeleted(long deleted) {
		this.deleted = deleted;
	}

	public long getRemaining() {
		return remaining;
	}

	public void setRemaining(long remaining) {
		this.remaining = remaining;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getUsed() {
		return used;
	}

	public void setUsed(long used) {
		this.used = used;
	}

	public enum state {
		NORMAL("normal"),
		NEARING("nearing"),
		CRITICAL("critical"),
		EXCEEDED("exceeded");
		private String value;
		private state(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
	}
}