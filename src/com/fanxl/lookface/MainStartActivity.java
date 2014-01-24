package com.fanxl.lookface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainStartActivity extends Activity implements OnClickListener {

	private FrameLayout fl_face_recognition, fl_face_search, fl_face_compare,
			fl_face_about;
	private static final int EXIT_TIME = 2000; // ���ΰ����ؼ��ļ���ж�
	private long firstExitTime = 0L; // ���������һ�ΰ����ؼ���ʱ��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		fl_face_recognition = (FrameLayout) findViewById(R.id.fl_face_recognition);
		fl_face_recognition.setOnClickListener(this);
		fl_face_search = (FrameLayout) findViewById(R.id.fl_face_search);
		fl_face_search.setOnClickListener(this);
		fl_face_compare = (FrameLayout) findViewById(R.id.fl_face_compare);
		fl_face_compare.setOnClickListener(this);
		fl_face_about=(FrameLayout)findViewById(R.id.fl_face_about);
		fl_face_about.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fl_face_recognition:
			startActivity(new Intent(MainStartActivity.this,
					RecognitionActivity.class));
			break;
		case R.id.fl_face_search:
			startActivity(new Intent(MainStartActivity.this,
					SearchActivity.class));
			break;
		case R.id.fl_face_compare:
			startActivity(new Intent(MainStartActivity.this,
					CompareActivity.class));
			break;
		case R.id.fl_face_about:
			startActivity(new Intent(MainStartActivity.this,
					Aboutus.class));
			break;

		}
	}

	// �˳�����ʱ����ʾ
	@Override
	public void onBackPressed() {// ���Ƿ��ؼ�
		long curTime = System.currentTimeMillis();
		if (curTime - firstExitTime < EXIT_TIME) {// ���ΰ����ؼ���ʱ��С��2����˳�Ӧ��
			finish();
		} else {
			Toast.makeText(this, "�ٰ�һ���˳�", Toast.LENGTH_SHORT).show();
			firstExitTime = curTime;
		}
	}

}
