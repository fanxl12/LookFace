package com.fanxl.lookface.domain;

public class CompSimil {

    private double eye;
    private double mouth;
    private double nose;
    private double eyebrow;
    
	@Override
	public String toString() {
		return "CompSimil [eye=" + eye + ", mouth=" + mouth + ", nose=" + nose
				+ ", eyebrow=" + eyebrow + "]";
	}
	public double getEye() {
		return eye;
	}
	public void setEye(double eye) {
		this.eye = eye;
	}
	public double getMouth() {
		return mouth;
	}
	public void setMouth(double mouth) {
		this.mouth = mouth;
	}
	public double getNose() {
		return nose;
	}
	public void setNose(double nose) {
		this.nose = nose;
	}
	public double getEyebrow() {
		return eyebrow;
	}
	public void setEyebrow(double eyebrow) {
		this.eyebrow = eyebrow;
	}
    

}
