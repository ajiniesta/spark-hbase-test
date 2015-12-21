package com.iniesta.shtest;

import java.io.Serializable;

public class Deal implements Serializable{

	private static final long serialVersionUID = 1279260451215813184L;
	private int id;
	private long tsStart;
	private long tsEnd;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getTsStart() {
		return tsStart;
	}
	public void setTsStart(long tsStart) {
		this.tsStart = tsStart;
	}
	public long getTsEnd() {
		return tsEnd;
	}
	public void setTsEnd(long tsEnd) {
		this.tsEnd = tsEnd;
	}
	@Override
	public String toString() {
		return "Deal [id=" + id + ", tsStart=" + tsStart + ", tsEnd=" + tsEnd + "]";
	}
	
}
