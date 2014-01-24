package com.fanxl.lookface.domain;


public class Similarity {

	private String session_id;
	private double similarity;
	private CompSimil component_similarity;
	public String getSession_id() {
		return session_id;
	}
	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}
	public double getSimilarity() {
		return similarity;
	}
	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}
	public CompSimil getComponent_similarity() {
		return component_similarity;
	}
	public void setComponent_similarity(CompSimil component_similarity) {
		this.component_similarity = component_similarity;
	}
	
	@Override
	public String toString() {
		return "Similarity [session_id=" + session_id + ", similarity="
				+ similarity + ", component_similarity=" + component_similarity
				+ "]";
	}
	
}
