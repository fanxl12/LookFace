package com.fanxl.lookface.domain;

import java.util.ArrayList;
import java.util.List;

public class Person {
	
	private String session_id;
	private int img_height;
	private int img_width;
	private String img_id;
	private String url = null;
	private int response_code;
	private List<Face> face = new ArrayList<Face>();
	
	public String getSession_id() {
		return session_id;
	}
	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}
	public int getImg_height() {
		return img_height;
	}
	public void setImg_height(int img_height) {
		this.img_height = img_height;
	}
	public int getImg_width() {
		return img_width;
	}
	public void setImg_width(int img_width) {
		this.img_width = img_width;
	}
	public String getImg_id() {
		return img_id;
	}
	public void setImg_id(String img_id) {
		this.img_id = img_id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getResponse_code() {
		return response_code;
	}
	public void setResponse_code(int response_code) {
		this.response_code = response_code;
	}
	public List<Face> getFace() {
		return face;
	}
	public void setFace(List<Face> face) {
		this.face = face;
	}
	@Override
	public String toString() {
		return "Person [session_id=" + session_id + ", img_height="
				+ img_height + ", img_width=" + img_width + ", img_id="
				+ img_id + ", url=" + url + ", response_code=" + response_code
				+ ", face=" + face + "]";
	}
	

}
