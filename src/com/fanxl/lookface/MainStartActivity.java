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
	private static final int EXIT_TIME = 2000; // 两次按返回键的间隔判断
	private long firstExitTime = 0L; // 用来保存第一次按返回键的时间

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

	// 退出程序时的提示
	@Override
	public void onBackPressed() {// 覆盖返回键
		long curTime = System.currentTimeMillis();
		if (curTime - firstExitTime < EXIT_TIME) {// 两次按返回键的时间小于2秒就退出应用
			finish();
		} else {
			Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
			firstExitTime = curTime;
		}
	}

}
