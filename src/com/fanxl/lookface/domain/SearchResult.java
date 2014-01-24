package com.fanxl.lookface.domain;

import java.util.List;

public class SearchResult {
	
	private List<Candidate> candidate;
	private String session_id;
	private int response_code;
	
	public int getResponse_code() {
		return response_code;
	}
	public void setResponse_code(int response_code) {
		this.response_code = response_code;
	}
	public List<Candidate> getList() {
		return candidate;
	}
	public void setList(List<Candidate> candidate) {
		this.candidate = candidate;
	}
	public String getSession_id() {
		return session_id;
	}
	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}
	@Override
	public String toString() {
		return "SearchResult [list=" + candidate + ", session_id=" + session_id
				+ ", response_code=" + response_code + "]";
	}

}
