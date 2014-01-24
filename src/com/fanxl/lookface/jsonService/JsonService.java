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
		Log.v("rst数据：", jsonRst);
		Gson gson = new Gson();
		Person person = new Person();
		Type type = new TypeToken<Person>(){}.getType();
		person = gson.fromJson(jsonRst, type);
		System.out.println("解析完毕的结果："+person.toString());
		return person;
	}

	//解析搜索人脸得到的数据
	public static List<Candidate> getsearch(String jsonRst){
		Gson gson = new Gson();
		SearchResult srs = new SearchResult();
		srs = gson.fromJson(jsonRst, new TypeToken<SearchResult>(){}.getType());
		System.out.println("搜索结果解析完毕："+srs.toString());
		return srs.getList();
	}
	
	//解析对比人脸得到的数据
	public static Similarity getSimil(String jsonRst){
		Gson gson = new Gson();
		Similarity sm = new Similarity();
		sm = gson.fromJson(jsonRst, new TypeToken<Similarity>(){}.getType());
		return sm;
	}

}
