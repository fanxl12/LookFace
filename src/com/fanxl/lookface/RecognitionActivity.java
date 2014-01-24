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
		et_title_text.setText("�������");
		ll_back_main = (LinearLayout) findViewById(R.id.ll_back_main);
		ll_back_main.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(RecognitionActivity.this,
						MainStartActivity.class));
				finish();
			}
		});

		// �õ��ֻ���Ļ�Ŀ��
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		phoneHeight = wm.getDefaultDisplay().getHeight();
		phoneWight = wm.getDefaultDisplay().getWidth();

	}

	// ѡ����Ƭ��ť�ĵ���¼�
	public void choice(View view) {
		showPicturePicker(RecognitionActivity.this);
	}

	// �鿴ʶ��֮�����ϸ��Ϣ
	public void more(View view) {
		if (b) {
			// ���鿴��Ϣ��ť��ʹ��intent��ת������ʶ��ҳ��
			Intent intent = new Intent(RecognitionActivity.this,
					RecognitionResult.class);
			// ��intentЯ�����ݵ�����ʶ��ҳ�棬���ݸ�����ʶ���tv_secondview;
			intent.putExtra("jsonRst", jsonRst);
			// ��ת������ʶ��ҳ��
			startActivity(intent);
		} else {
			Toast.makeText(this, "���Ƚ�������ʶ��", Toast.LENGTH_SHORT).show();
		}
	}

	// �鿴��ť�ĵ���¼�����ͼƬ��Դ�ύ�����������õ����
	public void check(View view) {

		// �ж��Ƿ�ѡ������Ƭ
		if (img != null) {
			FaceppDetect faceppDetect = new FaceppDetect();
			faceppDetect.setDetectCallback(new DetectCallback() {

				public void detectResult(JSONObject rst) {
					jsonRst = rst.toString();

					// ����һ����ɫ�Ļ���
					Paint paint = new Paint();
					paint.setColor(Color.RED);
					paint.setStrokeWidth(Math.max(img.getWidth(),
							img.getHeight()) / 100f);

					// ������ԭͼһ����Ļ���
					Bitmap bitmap = Bitmap.createBitmap(img.getWidth(),
							img.getHeight(), img.getConfig());
					Canvas canvas = new Canvas(bitmap);
					canvas.drawBitmap(img, new Matrix(), null);

					try {
						// �õ������ĸ���
						final int count = rst.getJSONArray("face").length();
						for (int i = 0; i < count; ++i) {
							float x, y, w, h;
							// �õ���������
							x = (float) rst.getJSONArray("face")
									.getJSONObject(i).getJSONObject("position")
									.getJSONObject("center").getDouble("x");
							y = (float) rst.getJSONArray("face")
									.getJSONObject(i).getJSONObject("position")
									.getJSONObject("center").getDouble("y");

							// �õ����Ĵ�С
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

						// �����޸�֮���µ�bitmap
						img = bitmap;
						// ����һ���̲߳����뵽UI�߳��������޸�UI
						RecognitionActivity.this.runOnUiThread(new Runnable() {

							public void run() {
								// �ѵõ����µ�bitmap�ŵ��ؼ���
								iv_main_picture.setImageBitmap(img);
								Toast.makeText(RecognitionActivity.this,
										"������" + count + "����",
										Toast.LENGTH_SHORT).show();
							}
						});

					} catch (JSONException e) {
						e.printStackTrace();
						RecognitionActivity.this.runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(RecognitionActivity.this,
										"�������", Toast.LENGTH_SHORT).show();
							}
						});
					}
				}
			});
			faceppDetect.detect(img);
		} else {
			Toast.makeText(RecognitionActivity.this, "��Ƭ����Ϊ��",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void showPicturePicker(Context context) {
		// final boolean crop = isCrop;
		// ����һ��dialog�Ի���ѡ����Ƭ��Դ
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("ͼƬ��Դ");
		builder.setNegativeButton("ȡ��", null);
		builder.setItems(new String[] { "���" },
				new DialogInterface.OnClickListener() {
					// ���ص�������
					int REQUEST_CODE;

					@Override
					public void onClick(DialogInterface dialog, int which) {

						// ��ϵͳ�������ѡ����Ƭ
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

	// ѡ����Ƭ�õ����ؽ������ͼƬ���õ��ؼ�����
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			if (data != null) {
				Uri uri = data.getData(); // �õ�ͼƬ��·��
				// ��ͼƬ��Uri��ַ���һ��·����ַ
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(uri, filePathColumn,
						null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				// �õ�ѡ��ͼƬ��·��
				picturePath = cursor.getString(columnIndex);
				cursor.close();
				System.out.println("dizhi:" + picturePath);

				img = getBitmap(picturePath);
				iv_main_picture.setImageBitmap(img);
			}
		}
	}

	// ��ͼƬ�����Ż���������Դ����Ƭ�����ڴ��������
	public Bitmap getBitmap(String picturePath) {
		// ͼƬ������������
		BitmapFactory.Options opts = new Options();
		// ��ȥ��Ľ���ͼƬ��ֻ�ǻ�ȡͼƬ��ͷ����Ϣ�����ߵ�
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(picturePath, opts);
		int imageHeight = opts.outHeight;
		int imageWidth = opts.outWidth;
		// �������ű���
		int scaleX = imageWidth / phoneWight;
		int scaleY = imageHeight / phoneHeight;
		if (scaleX > scaleY & scaleY >= 1) {
			scale = scaleX;
		}
		if (scaleY > scaleX & scaleX >= 1) {
			scale = scaleY;
		}
		// ��Ľ���
		opts.inJustDecodeBounds = false;
		opts.inSampleSize = scale;
		img = BitmapFactory.decodeFile(picturePath, opts);
		return img;
	}

	// �ύ��Ƭ�����Ͻ��н���
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
										"�������", Toast.LENGTH_SHORT).show();
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
