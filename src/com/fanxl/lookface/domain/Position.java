package com.fanxl.lookface.domain;

public class Position {
	
	private MouthRight mouth_right;
	private MouthLeft mouth_left;
	private Center center;
	private double height;
	private double width;
	private Nose nose;
	private EyeRight eye_right;
	private EyeLeft eye_left;
	public MouthRight getMouth_right() {
		return mouth_right;
	}
	public void setMouth_right(MouthRight mouth_right) {
		this.mouth_right = mouth_right;
	}
	public MouthLeft getMouth_left() {
		return mouth_left;
	}
	public void setMouth_left(MouthLeft mouth_left) {
		this.mouth_left = mouth_left;
	}
	public Center getCenter() {
		return center;
	}
	public void setCenter(Center center) {
		this.center = center;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public Nose getNose() {
		return nose;
	}
	public void setNose(Nose nose) {
		this.nose = nose;
	}
	public EyeRight getEye_right() {
		return eye_right;
	}
	public void setEye_right(EyeRight eye_right) {
		this.eye_right = eye_right;
	}
	public EyeLeft getEye_left() {
		return eye_left;
	}
	public void setEye_left(EyeLeft eye_left) {
		this.eye_left = eye_left;
	}
	@Override
	public String toString() {
		return "Position [mouth_right=" + mouth_right + ", mouth_left="
				+ mouth_left + ", center=" + center + ", height=" + height
				+ ", width=" + width + ", nose=" + nose + ", eye_right="
				+ eye_right + ", eye_left=" + eye_left + "]";
	}
	

}
