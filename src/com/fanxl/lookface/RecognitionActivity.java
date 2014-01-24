package com.fanxl.lookface;

import java.io.ByteArrayOutputStream;
import org.json.JSONException;
import org.json.JSONObject;
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
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

public class RecognitionActivity extends Activity {

	private ImageView iv_main_picture;
	private TextView et_title_text;
	private int phoneHeight;
	private int phoneWight;
	private Bitmap img = null;
	private int scale = 1;
	private static final int CHOOSE_PICTURE = 1;
	private String picturePath = null;
	private String jsonRst = null;
	private boolean b = false;
	private LinearLayout ll_back_main;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.recognition);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);

		iv_main_picture = (ImageView) findViewById(R.id.iv_main_picture);
		et_title_text = (TextView) findViewById(R.id.et_title_text);
		et_title_text.setText("人脸检测");
		ll_back_main = (LinearLayout) findViewById(R.id.ll_back_main);
		ll_back_main.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(RecognitionActivity.this,
						MainStartActivity.class));
				finish();
			}
		});

		// 得到手机屏幕的宽高
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		phoneHeight = wm.getDefaultDisplay().getHeight();
		phoneWight = wm.getDefaultDisplay().getWidth();

	}

	// 选择照片按钮的点击事件
	public void choice(View view) {
		showPicturePicker(RecognitionActivity.this);
	}

	// 查看识别之后的详细信息
	public void more(View view) {
		if (b) {
			// 按查看信息按钮，使用intent跳转到人脸识别页面
			Intent intent = new Intent(RecognitionActivity.this,
					RecognitionResult.class);
			// 用intent携带数据到人脸识别页面，传递给人脸识别的tv_secondview;
			intent.putExtra("jsonRst", jsonRst);
			// 跳转到人脸识别页面
			startActivity(intent);
		} else {
			Toast.makeText(this, "请先进行人脸识别", Toast.LENGTH_SHORT).show();
		}
	}

	// 查看按钮的点击事件，将图片资源提交到服务器，拿到结果
	public void check(View view) {

		// 判断是否选择了照片
		if (img != null) {
			FaceppDetect faceppDetect = new FaceppDetect();
			faceppDetect.setDetectCallback(new DetectCallback() {

				public void detectResult(JSONObject rst) {
					jsonRst = rst.toString();

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

							// change percent value to the real size
							x = x / 100 * img.getWidth();
							w = w / 100 * img.getWidth() * 0.7f;
							y = y / 100 * img.getHeight();
							h = h / 100 * img.getHeight() * 0.7f;

							b = true;

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
						RecognitionActivity.this.runOnUiThread(new Runnable() {

							public void run() {
								// 把得到的新的bitmap放到控件中
								iv_main_picture.setImageBitmap(img);
								Toast.makeText(RecognitionActivity.this,
										"发现了" + count + "张脸",
										Toast.LENGTH_SHORT).show();
							}
						});

					} catch (JSONException e) {
						e.printStackTrace();
						RecognitionActivity.this.runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(RecognitionActivity.this,
										"请求错误", Toast.LENGTH_SHORT).show();
							}
						});
					}
				}
			});
			faceppDetect.detect(img);
		} else {
			Toast.makeText(RecognitionActivity.this, "照片不能为空",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void showPicturePicker(Context context) {
		// final boolean crop = isCrop;
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

				Cursor cursor = getContentResolver().query(uri, filePathColumn,
						null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				// 得到选择图片的路径
				picturePath = cursor.getString(columnIndex);
				cursor.close();
				System.out.println("dizhi:" + picturePath);

				img = getBitmap(picturePath);
				iv_main_picture.setImageBitmap(img);
			}
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

	// 提交照片到网上进行解析
	private class FaceppDetect {
		DetectCallback callback = null;

		public void setDetectCallback(DetectCallback detectCallback) {
			callback = detectCallback;
		}

		public void detect(final Bitmap img) {

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
						parameters
								.setAttribute("gender,age,race,smiling,glass");
						JSONObject result = httpRequests
								.detectionDetect(parameters);
						// finished , then call the callback function
						if (callback != null) {
							callback.detectResult(result);
						}
					} catch (FaceppParseException e) {
						e.printStackTrace();
						RecognitionActivity.this.runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(RecognitionActivity.this,
										"网络错误", Toast.LENGTH_SHORT).show();
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
