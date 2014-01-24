package com.fanxl.lookface.domain;

public class Candidate {
	
	private String face_id;
	private double similarity;
	private String tag;
	
	public String getFace_id() {
		return face_id;
	}
	public void setFace_id(String face_id) {
		this.face_id = face_id;
	}
	public double getSimilarity() {
		return similarity;
	}
	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	@Override
	public String toString() {
		return "Candidate [face_id=" + face_id + ", similarity=" + similarity
				+ ", tag=" + tag + "]";
	}
	

}
