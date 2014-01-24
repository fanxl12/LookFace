package com.fanxl.lookface.domain;

public class Face {
	
	private Position position;
	private Attribute attribute;
	private String tag;
	private String face_id;
	
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public Attribute getAttribute() {
		return attribute;
	}
	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getFace_id() {
		return face_id;
	}
	public void setFace_id(String face_id) {
		this.face_id = face_id;
	}
	@Override
	public String toString() {
		return "Face [position=" + position + ", attribute=" + attribute
				+ ", tag=" + tag + ", face_id=" + face_id + "]";
	}
}
