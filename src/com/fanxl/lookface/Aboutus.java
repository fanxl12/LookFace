package com.fanxl.lookface;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Aboutus extends Activity implements OnClickListener{
	
	private ImageView iv_oboutus_1, iv_oboutus_2, iv_oboutus_3;
	private RelativeLayout rl_oboutus_1, rl_oboutus_2, rl_oboutus_3;
	private TextView tv_oboutus_1, tv_oboutus_2, tv_oboutus_3,et_title_text,tv_back_main;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.right_obout_us);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
		
		et_title_text = (TextView) findViewById(R.id.et_title_text);
		et_title_text.setText("¹ØÓÚÈí¼þ");
		tv_back_main = (TextView) findViewById(R.id.tv_back_main);
		tv_back_main.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Aboutus.this, MainStartActivity.class));
				finish();
			}
		});
		
		
		tv_oboutus_1 = (TextView) findViewById(R.id.tv_oboutus_1);
		tv_oboutus_2 = (TextView) findViewById(R.id.tv_oboutus_2);
		tv_oboutus_3 = (TextView) findViewById(R.id.tv_oboutus_3);
		
		iv_oboutus_1 = (ImageView) findViewById(R.id.iv_oboutus_1);
		iv_oboutus_1.setBackgroundResource(R.drawable.mm_submenu_normal);
		iv_oboutus_2 = (ImageView) findViewById(R.id.iv_oboutus_2);
		iv_oboutus_2.setBackgroundResource(R.drawable.mm_submenu_normal);
		iv_oboutus_3 = (ImageView) findViewById(R.id.iv_oboutus_3);
		iv_oboutus_3.setBackgroundResource(R.drawable.mm_submenu_normal);
		
		rl_oboutus_1 = (RelativeLayout) findViewById(R.id.rl_oboutus_1);
		rl_oboutus_1.setOnClickListener(this);
		rl_oboutus_2 = (RelativeLayout) findViewById(R.id.rl_oboutus_2);
		rl_oboutus_2.setOnClickListener(this);
		rl_oboutus_3 = (RelativeLayout) findViewById(R.id.rl_oboutus_3);
		rl_oboutus_3.setBackgroundResource(R.drawable.ic_preference_last_normal);
		rl_oboutus_3.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_oboutus_1:
			if(tv_oboutus_1.getVisibility() == View.GONE){
				iv_oboutus_1.setBackgroundResource(R.drawable.mm_submenu_change);
				tv_oboutus_1.setVisibility(View.VISIBLE);
				tv_oboutus_2.setVisibility(View.GONE);
				tv_oboutus_3.setVisibility(View.GONE);
				iv_oboutus_2.setBackgroundResource(R.drawable.mm_submenu_normal);
				iv_oboutus_3.setBackgroundResource(R.drawable.mm_submenu_normal);
				
			}else if(tv_oboutus_1.getVisibility() == View.VISIBLE){
				tv_oboutus_1.setVisibility(View.GONE);
				iv_oboutus_1.setBackgroundResource(R.drawable.mm_submenu_normal);
			}
			break;
		case R.id.rl_oboutus_2:
			if(tv_oboutus_2.getVisibility() == View.GONE){
				iv_oboutus_2.setBackgroundResource(R.drawable.mm_submenu_change);
				tv_oboutus_2.setVisibility(View.VISIBLE);
				tv_oboutus_1.setVisibility(View.GONE);
				tv_oboutus_3.setVisibility(View.GONE);
				iv_oboutus_1.setBackgroundResource(R.drawable.mm_submenu_normal);
				iv_oboutus_3.setBackgroundResource(R.drawable.mm_submenu_normal);
				rl_oboutus_3.setBackgroundResource(R.drawable.ic_preference_last_normal);
			}else if(tv_oboutus_2.getVisibility() == View.VISIBLE){
				tv_oboutus_2.setVisibility(View.GONE);
				iv_oboutus_2.setBackgroundResource(R.drawable.mm_submenu_normal);
			}
			break;
		case R.id.rl_oboutus_3:
			if(tv_oboutus_3.getVisibility() == View.GONE){
				iv_oboutus_3.setBackgroundResource(R.drawable.mm_submenu_change);
				rl_oboutus_3.setBackgroundResource(R.drawable.ic_preference_normal);
				tv_oboutus_3.setVisibility(View.VISIBLE);
				tv_oboutus_3.setBackgroundResource(R.drawable.ic_preference_last_normal);
				tv_oboutus_2.setVisibility(View.GONE);
				tv_oboutus_1.setVisibility(View.GONE);
				iv_oboutus_2.setBackgroundResource(R.drawable.mm_submenu_normal);
				iv_oboutus_1.setBackgroundResource(R.drawable.mm_submenu_normal);
				
			}else if(tv_oboutus_3.getVisibility() == View.VISIBLE){
				tv_oboutus_3.setVisibility(View.GONE);
				rl_oboutus_3.setBackgroundResource(R.drawable.ic_preference_last_normal);
				iv_oboutus_3.setBackgroundResource(R.drawable.mm_submenu_normal);
			}
			break;

		
		}
		
	}
}
