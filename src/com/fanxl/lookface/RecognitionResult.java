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
		tv_title_text.setText("ʶ����");
		ll_back_main = (LinearLayout) findViewById(R.id.ll_back_main);
		ll_back_main.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(RecognitionResult.this, RecognitionActivity.class));
				finish();
			}
		});
		tv_second_search = (TextView) findViewById(R.id.tv_second_search);
		
		// getIntent������Ŀ�а�����ԭʼintent����������
		Intent intent = this.getIntent();
		// getExtras()�õ�intent�������Ķ�������
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
				ren = "�����ˡ�";
			}else if("White".equals(ren)){
				ren = "�����ˡ�";
			}else{
				ren = "�����ˡ�";
			}
			Age age = attribute.getAge();
			int age1 = age.getValue();
			int range = age.getRange();
			ageRange = (age1-range)+"~"+(age1+range)+"��";
			Gender gender = attribute.getGender();
			sex = gender.getValue();
			if("Male".equals(sex)){
				if(age1 < 20){
					sex = "����";
				}
				sex = "��ʿ";
			}else{
				if(age1 < 20){
					sex = "Ů��";
				}
				sex = "Ůʿ";
			}
			
			Smiling smiling = attribute.getSmiling();
			double smil = smiling.getValue();
			System.out.println("΢Ц�ȣ�"+smil);
		    if(smil > 50.0){
		    	smile = "����΢Ц�����ӣ���֪������һ��ϲ��Ц���ˡ�";
		    }
			Glass glass = attribute.getGlass();
			String value = glass.getValue();
			double confidence = glass.getConfidence();
			if("None".equals(value) & confidence > 50.0){
				glassText = "";
			}else{
				glassText = "��,������۾�������Ҳ��������";
			}
		}
		String text = "\t\t\t����������ϸ�ļ��ͷ�������֪������һλ��"+sex+"����������"+ageRange+"֮�䣬"+ren+smile+glassText;
		tv_second_search.setText(text);
		System.out.println("������ϵĽ����"+person.toString());
	}

	
	

	

}
