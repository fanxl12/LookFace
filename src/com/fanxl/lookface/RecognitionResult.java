package com.fanxl.lookface;

import java.util.List;
import com.fanxl.lookface.domain.Age;
import com.fanxl.lookface.domain.Attribute;
import com.fanxl.lookface.domain.Face;
import com.fanxl.lookface.domain.Gender;
import com.fanxl.lookface.domain.Glass;
import com.fanxl.lookface.domain.Person;
import com.fanxl.lookface.domain.Race;
import com.fanxl.lookface.domain.Smiling;
import com.fanxl.lookface.jsonService.JsonService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RecognitionResult extends Activity {
	
	private TextView  tv_second_search, tv_title_text;
	private String ageRange = null;
	private String sex = null;
	private String ren = null;
	private String smile = "";
	private String glassText = "";
	private LinearLayout ll_back_main;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.recognitionresult);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
		
		tv_title_text = (TextView) findViewById(R.id.et_title_text);
		tv_title_text.setText("识别结果");
		ll_back_main = (LinearLayout) findViewById(R.id.ll_back_main);
		ll_back_main.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(RecognitionResult.this, RecognitionActivity.class));
				finish();
			}
		});
		tv_second_search = (TextView) findViewById(R.id.tv_second_search);
		
		// getIntent将该项目中包含的原始intent检索出来，
		Intent intent = this.getIntent();
		// getExtras()得到intent所附带的额外数据
		Bundle bundle = intent.getExtras();
		String jsonRst = bundle.getString("jsonRst");
		getPerson(jsonRst);
	}
	
	public void getPerson(String jsonRst){
		Person person = JsonService.getResult(jsonRst);
		List<Face> list = person.getFace();
		for(Face face:list){
			Attribute attribute = face.getAttribute();
			Race race = attribute.getRace();
			ren = race.getValue();
			if("Asian".equals(ren)){
				ren = "黄种人。";
			}else if("White".equals(ren)){
				ren = "白种人。";
			}else{
				ren = "黑种人。";
			}
			Age age = attribute.getAge();
			int age1 = age.getValue();
			int range = age.getRange();
			ageRange = (age1-range)+"~"+(age1+range)+"岁";
			Gender gender = attribute.getGender();
			sex = gender.getValue();
			if("Male".equals(sex)){
				if(age1 < 20){
					sex = "男生";
				}
				sex = "男士";
			}else{
				if(age1 < 20){
					sex = "女生";
				}
				sex = "女士";
			}
			
			Smiling smiling = attribute.getSmiling();
			double smil = smiling.getValue();
			System.out.println("微笑度："+smil);
		    if(smil > 50.0){
		    	smile = "看你微笑的样子，我知道你是一个喜欢笑的人。";
		    }
			Glass glass = attribute.getGlass();
			String value = glass.getValue();
			double confidence = glass.getConfidence();
			if("None".equals(value) & confidence > 50.0){
				glassText = "";
			}else{
				glassText = "哇,你戴着眼镜的样子也是蛮酷的嘛。";
			}
		}
		String text = "\t\t\t根据我们详细的检测和分析，我知道你是一位："+sex+"，年龄大概在"+ageRange+"之间，"+ren+smile+glassText;
		tv_second_search.setText(text);
		System.out.println("解析完毕的结果："+person.toString());
	}

	
	

	

}
