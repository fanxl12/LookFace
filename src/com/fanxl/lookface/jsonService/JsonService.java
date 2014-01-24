package com.fanxl.lookface.jsonService;

import java.lang.reflect.Type;
import java.util.List;

import android.util.Log;

import com.fanxl.lookface.domain.Candidate;
import com.fanxl.lookface.domain.Person;
import com.fanxl.lookface.domain.SearchResult;
import com.fanxl.lookface.domain.Similarity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonService {
	
	public static Person getResult(String jsonRst){
		Log.v("rst���ݣ�", jsonRst);
		Gson gson = new Gson();
		Person person = new Person();
		Type type = new TypeToken<Person>(){}.getType();
		person = gson.fromJson(jsonRst, type);
		System.out.println("������ϵĽ����"+person.toString());
		return person;
	}

	//�������������õ�������
	public static List<Candidate> getsearch(String jsonRst){
		Gson gson = new Gson();
		SearchResult srs = new SearchResult();
		srs = gson.fromJson(jsonRst, new TypeToken<SearchResult>(){}.getType());
		System.out.println("�������������ϣ�"+srs.toString());
		return srs.getList();
	}
	
	//�����Ա������õ�������
	public static Similarity getSimil(String jsonRst){
		Gson gson = new Gson();
		Similarity sm = new Similarity();
		sm = gson.fromJson(jsonRst, new TypeToken<Similarity>(){}.getType());
		return sm;
	}

}
