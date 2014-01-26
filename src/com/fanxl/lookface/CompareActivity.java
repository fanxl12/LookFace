package com.fanxl.lookface;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.fanxl.lookface.domain.CompSimil;
import com.fanxl.lookface.domain.Similarity;
import com.fanxl.lookface.jsonService.JsonService;

public class CompareActivity extends Activity implements OnClickListener {

	private Button bt_four_left, bt_four_right, bt_four_check,bt_four_loadleft,bt_four_loadright;
	private TextView tv_four_eye,tv_four_eyebrow,tv_four_mouth,tv_four_nose,tv_four_simil, tv_title_text,tv_back_main;
	private ImageView iv_four_left, iv_four_right;
	private static final int CHOOSE_PICTURE = 1;
	private String picturePath = null; // 选择图片路径
	private Bitmap img = null;
	private int phoneHeight;
	private int phoneWight;
	private int scale = 1;
	private static final int PICTURE_LEFT = 0;
	private static final int PICTURE_RIGHT = 1;
	private int choice = 0;
	private String face_id1 = null;
	private String face_id2 = null;
	private List<Double> list = null;
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			list = (List<Double>) msg.obj;
			tv_four_eye.setText("眼睛："+list.get(1)+"%");
			tv_four_mouth.setText("嘴巴："+list.get(2)+"%");
			tv_four_nose.setText("鼻子："+list.get(3)+"%");
			tv_four_eyebrow.setText("眉毛："+list.get(4)+"%");
			tv_four_simil.setText("整体的相似度："+list.get(0)+"%");
		}
	};

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.compare);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
		
		tv_title_text = (TextView) findViewById(R.id.et_title_text);
		tv_title_text.setText("识别结果");
		tv_back_main = (TextView) findViewById(R.id.tv_back_main);
		tv_back_main.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(CompareActivity.this, MainStartActivity.class));
				finish();
			}
		});
		
		bt_four_left = (Button) findViewById(R.id.bt_four_left);
		bt_four_left.setOnClickListener(this);
		bt_four_right = (Button) findViewById(R.id.bt_four_right);
		bt_four_right.setOnClickListener(this);
		iv_four_left = (ImageView) findViewById(R.id.iv_four_left);
		iv_four_right = (ImageView) findViewById(R.id.iv_four_right);
		bt_four_check = (Button) findViewById(R.id.bt_four_check);
		bt_four_check.setOnClickListener(this);
		bt_four_loadleft = (Button) findViewById(R.id.bt_four_loadleft);
		bt_four_loadleft.setOnClickListener(this);
		bt_four_loadright = (Button) findViewById(R.id.bt_four_loadright);
		bt_four_loadright.setOnClickListener(this);
		tv_four_eye = (TextView) findViewById(R.id.tv_four_eye);
		tv_four_eyebrow = (TextView) findViewById(R.id.tv_four_eyebrow);
		tv_four_mouth = (TextView) findViewById(R.id.tv_four_mouth);
		tv_four_nose = (TextView) findViewById(R.id.tv_four_nose);
		tv_four_simil = (TextView) findViewById(R.id.tv_four_simil);

		// 得到手机屏幕的宽高
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		phoneHeight = wm.getDefaultDisplay().getHeight();
		phoneWight = wm.getDefaultDisplay().getWidth();
	}

	// 选择照片
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_four_left: // 选择左边的照片
			showPicturePicker(this);
			choice = PICTURE_LEFT;
			break;
		case R.id.bt_four_right: // 选择右边的照片
			if(face_id1 != null){
				showPicturePicker(this);
				choice = PICTURE_RIGHT;
			}else{
				Toast.makeText(this, "请先检测照片1", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.bt_four_loadright:
			if(img != null){
				check(PICTURE_RIGHT);
			}else{
				Toast.makeText(this, "请先选择照片2", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.bt_four_loadleft:
			if(img != null){
				check(PICTURE_LEFT);
			}else{
				Toast.makeText(this, "请先选择照片1", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.bt_four_check:
			if(face_id2 != null){
				compare();
			}else{
				Toast.makeText(this, "请先检测照片2", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	private void compare() {
		
		new Thread(new Runnable() {

			public void run() {
				HttpRequests httpRequests = new HttpRequests(
						"f0d4ca0e4634482088cdd1023b9699e1",
						"h1D8DIiDCuE8QPQcIwSV-QUzVa6YOgw5", true, false);

				try {
					PostParameters parameters = new PostParameters();
					parameters.setFaceId1(face_id1);
					parameters.setFaceId2(face_id2);
					parameters.setFacesetName("fanxl");
					parameters.setCount(9);
					JSONObject rst = httpRequests.recognitionCompare(parameters);
					Similarity sm = JsonService.getSimil(rst.toString());
					list = new ArrayList<Double>();
					double similarity = sm.getSimilarity();
					System.out.println("整体相似："+similarity);
					CompSimil cs = sm.getComponent_similarity();
					double eye = cs.getEye();
					System.out.println("眼睛相似："+eye);
					double mouth = cs.getMouth();
					double nose = cs.getNose();
					double eyebrow = cs.getEyebrow();
					list.add(similarity);
					list.add(eye);
					list.add(mouth);
					list.add(nose);
					list.add(eyebrow);
					Message msg = handler.obtainMessage();
					msg.obj = list;
					handler.sendMessage(msg);
                    
				} catch (FaceppParseException e) {
					e.printStackTrace();
					CompareActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(CompareActivity.this, "网络错误",
									Toast.LENGTH_SHORT).show();
						}
					});
				}

			}
		}).start();
	}
	

	public void showPicturePicker(Context context) {
		// 创建一个dialog对话框，选择照片来源
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("图片来源");
		builder.setNegativeButton("取消", null);
		builder.setItems(new String[] { "相册" },
				new DialogInterface.OnClickListener() {
					// 返回的类型码
					int REQUEST_CODE;

					@Override
					public void onClick(DialogInterface dialog, int which) {

						// 从系统相册里面选择照片
						Intent openAlbumIntent = new Intent(
								Intent.ACTION_GET_CONTENT);
						REQUEST_CODE = CHOOSE_PICTURE;
						openAlbumIntent.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");
						startActivityForResult(openAlbumIntent, REQUEST_CODE);

					}
				});
		builder.create().show();
	}

	// 选择照片得到返回结果，把图片设置到控件里面
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			if (data != null) {
				Uri uri = data.getData(); // 得到图片的路径
				// 就图片的Uri地址变成一个路径地址
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(uri,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				// 得到选择图片的路径
				picturePath = cursor.getString(columnIndex);
				cursor.close();
				img = getBitmap(picturePath);
				setPicture();
			}
		}
	}

	// 取得照片将照片设置到控件中
	public void setPicture() {
		switch (choice) {
		case PICTURE_LEFT:
			iv_four_left.setImageBitmap(img);
			break;
		case PICTURE_RIGHT:
			iv_four_right.setImageBitmap(img);
			break;
		}
	}

	// 对图片进行优化处理，避免对大的照片出现内存溢出问题
	public Bitmap getBitmap(String picturePath) {
		// 图片解析参数配置
		BitmapFactory.Options opts = new Options();
		// 不去真的解析图片，只是获取图片的头部信息，如宽高等
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(picturePath, opts);
		int imageHeight = opts.outHeight;
		int imageWidth = opts.outWidth;
		// 计算缩放比例
		int scaleX = imageWidth / phoneWight;
		int scaleY = imageHeight / phoneHeight;
		if (scaleX > scaleY & scaleY >= 1) {
			scale = scaleX;
		}
		if (scaleY > scaleX & scaleX >= 1) {
			scale = scaleY;
		}
		// 真的解析
		opts.inJustDecodeBounds = false;
		opts.inSampleSize = scale;
		img = BitmapFactory.decodeFile(picturePath, opts);
		return img;
	}

	// 查看按钮的点击事件，将图片资源提交到服务器，拿到结果
	public void check(final int choice) {

		// 判断是否选择了照片
		if (img != null) {
			FaceppDetect faceppDetect = new FaceppDetect();
			faceppDetect.setDetectCallback(new DetectCallback() {

				public void detectResult(JSONObject rst) {

					// 创建一个红色的画笔
					Paint paint = new Paint();
					paint.setColor(Color.RED);
					paint.setStrokeWidth(Math.max(img.getWidth(),
							img.getHeight()) / 100f);

					// 创建跟原图一样大的画布
					Bitmap bitmap = Bitmap.createBitmap(img.getWidth(),
							img.getHeight(), img.getConfig());
					Canvas canvas = new Canvas(bitmap);
					canvas.drawBitmap(img, new Matrix(), null);

					try {
						// 得到人脸的个数
						final int count = rst.getJSONArray("face").length();
						for (int i = 0; i < count; ++i) {
							float x, y, w, h;
							// 得到脸的中心
							x = (float) rst.getJSONArray("face")
									.getJSONObject(i).getJSONObject("position")
									.getJSONObject("center").getDouble("x");
							y = (float) rst.getJSONArray("face")
									.getJSONObject(i).getJSONObject("position")
									.getJSONObject("center").getDouble("y");

							// 得到脸的大小
							w = (float) rst.getJSONArray("face")
									.getJSONObject(i).getJSONObject("position")
									.getDouble("width");
							h = (float) rst.getJSONArray("face")
									.getJSONObject(i).getJSONObject("position")
									.getDouble("height");
							//得到face_id;
							switch (choice) {
							  case PICTURE_LEFT:
								face_id1 = rst.getJSONArray("face").getJSONObject(i).getString("face_id");
								System.out.println("face_id1:"+face_id1);
								break;
							  case PICTURE_RIGHT:
								  face_id2 = rst.getJSONArray("face").getJSONObject(i).getString("face_id");
								  System.out.println("face_id2:"+face_id2);
								  break;
							}
							

							// change percent value to the real size
							x = x / 100 * img.getWidth();
							w = w / 100 * img.getWidth() * 0.7f;
							y = y / 100 * img.getHeight();
							h = h / 100 * img.getHeight() * 0.7f;

							// draw the box to mark it out
							canvas.drawLine(x - w, y - h, x - w, y + h, paint);
							canvas.drawLine(x - w, y - h, x + w, y - h, paint);
							canvas.drawLine(x + w, y + h, x - w, y + h, paint);
							canvas.drawLine(x + w, y + h, x + w, y - h, paint);

							// backBit = Bitmap.createBitmap(img, (int)(x-w),
							// (int)(y-h), (int)(x+w), (int)(y+h));
						}

						// 保存修改之后新的bitmap
						img = bitmap;
						// 创建一个线程并加入到UI线程里面来修改UI
						CompareActivity.this.runOnUiThread(new Runnable() {

							public void run() {
								// 把得到的新的bitmap放到控件中
								setPicture();
							}
						});

					} catch (JSONException e) {
						e.printStackTrace();
						CompareActivity.this.runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(CompareActivity.this, "请求错误",
										Toast.LENGTH_SHORT).show();
							}
						});
					}
				}
			});
			faceppDetect.detect(img);
		} else {
			Toast.makeText(CompareActivity.this, "照片不能为空",Toast.LENGTH_SHORT).show();
		}
	}

	// 提交照片到网上进行解析
	private class FaceppDetect {
		DetectCallback callback = null;

		public void setDetectCallback(DetectCallback detectCallback) {
			callback = detectCallback;
		}
		
		public void detect(final Bitmap image) {

			new Thread(new Runnable() {

				public void run() {
					HttpRequests httpRequests = new HttpRequests(
							"f0d4ca0e4634482088cdd1023b9699e1",
							"h1D8DIiDCuE8QPQcIwSV-QUzVa6YOgw5", true, false);

					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					float scale = Math.min(
							1,
							Math.min(600f / img.getWidth(),
									600f / img.getHeight()));
					Matrix matrix = new Matrix();
					matrix.postScale(scale, scale);

					Bitmap imgSmall = Bitmap.createBitmap(img, 0, 0,
							img.getWidth(), img.getHeight(), matrix, false);

					imgSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
					byte[] array = stream.toByteArray();

					try {
						// detect
						PostParameters parameters = new PostParameters();
						parameters.setImg(array);
						JSONObject result = httpRequests
								.detectionDetect(parameters);
						// finished , then call the callback function
						if (callback != null) {
							callback.detectResult(result);
						}
					} catch (FaceppParseException e) {
						e.printStackTrace();
						CompareActivity.this.runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(CompareActivity.this, "网络错误",
										Toast.LENGTH_SHORT).show();
							}
						});
					}

				}
			}).start();
		}
	}

	interface DetectCallback {
		void detectResult(JSONObject rst);
	}
}
