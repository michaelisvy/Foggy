package org.beijingair.controller;

import org.hibernate.validator.constraints.NotEmpty;


public class SearchFormInfo {
	
	@NotEmpty private String start;
	private String end;
	private String range;

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

}
