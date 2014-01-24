package com.fanxl.lookface.domain;

public class Attribute {
	
	private Race race;
	private Gender gender;
	private Smiling smiling;
	private Age age;
	private Glass glass;
	
	public Glass getGlass() {
		return glass;
	}
	public void setGlass(Glass glass) {
		this.glass = glass;
	}
	public Race getRace() {
		return race;
	}
	public void setRace(Race race) {
		this.race = race;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public Smiling getSmiling() {
		return smiling;
	}
	public void setSmiling(Smiling smiling) {
		this.smiling = smiling;
	}
	public Age getAge() {
		return age;
	}
	public void setAge(Age age) {
		this.age = age;
	}
	

}
