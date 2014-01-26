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
	private String picturePath = null; // ѡ��ͼƬ·��
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
			tv_four_eye.setText("�۾���"+list.get(1)+"%");
			tv_four_mouth.setText("��ͣ�"+list.get(2)+"%");
			tv_four_nose.setText("���ӣ�"+list.get(3)+"%");
			tv_four_eyebrow.setText("üë��"+list.get(4)+"%");
			tv_four_simil.setText("��������ƶȣ�"+list.get(0)+"%");
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
		tv_title_text.setText("ʶ����");
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

		// �õ��ֻ���Ļ�Ŀ��
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		phoneHeight = wm.getDefaultDisplay().getHeight();
		phoneWight = wm.getDefaultDisplay().getWidth();
	}

	// ѡ����Ƭ
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_four_left: // ѡ����ߵ���Ƭ
			showPicturePicker(this);
			choice = PICTURE_LEFT;
			break;
		case R.id.bt_four_right: // ѡ���ұߵ���Ƭ
			if(face_id1 != null){
				showPicturePicker(this);
				choice = PICTURE_RIGHT;
			}else{
				Toast.makeText(this, "���ȼ����Ƭ1", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.bt_four_loadright:
			if(img != null){
				check(PICTURE_RIGHT);
			}else{
				Toast.makeText(this, "����ѡ����Ƭ2", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.bt_four_loadleft:
			if(img != null){
				check(PICTURE_LEFT);
			}else{
				Toast.makeText(this, "����ѡ����Ƭ1", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.bt_four_check:
			if(face_id2 != null){
				compare();
			}else{
				Toast.makeText(this, "���ȼ����Ƭ2", Toast.LENGTH_SHORT).show();
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
					System.out.println("�������ƣ�"+similarity);
					CompSimil cs = sm.getComponent_similarity();
					double eye = cs.getEye();
					System.out.println("�۾����ƣ�"+eye);
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
							Toast.makeText(CompareActivity.this, "�������",
									Toast.LENGTH_SHORT).show();
						}
					});
				}

			}
		}).start();
	}
	

	public void showPicturePicker(Context context) {
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

				Cursor cursor = getContentResolver().query(uri,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				// �õ�ѡ��ͼƬ��·��
				picturePath = cursor.getString(columnIndex);
				cursor.close();
				img = getBitmap(picturePath);
				setPicture();
			}
		}
	}

	// ȡ����Ƭ����Ƭ���õ��ؼ���
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

	// �鿴��ť�ĵ���¼�����ͼƬ��Դ�ύ�����������õ����
	public void check(final int choice) {

		// �ж��Ƿ�ѡ������Ƭ
		if (img != null) {
			FaceppDetect faceppDetect = new FaceppDetect();
			faceppDetect.setDetectCallback(new DetectCallback() {

				public void detectResult(JSONObject rst) {

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
							//�õ�face_id;
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

						// �����޸�֮���µ�bitmap
						img = bitmap;
						// ����һ���̲߳����뵽UI�߳��������޸�UI
						CompareActivity.this.runOnUiThread(new Runnable() {

							public void run() {
								// �ѵõ����µ�bitmap�ŵ��ؼ���
								setPicture();
							}
						});

					} catch (JSONException e) {
						e.printStackTrace();
						CompareActivity.this.runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(CompareActivity.this, "�������",
										Toast.LENGTH_SHORT).show();
							}
						});
					}
				}
			});
			faceppDetect.detect(img);
		} else {
			Toast.makeText(CompareActivity.this, "��Ƭ����Ϊ��",Toast.LENGTH_SHORT).show();
		}
	}

	// �ύ��Ƭ�����Ͻ��н���
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
								Toast.makeText(CompareActivity.this, "�������",
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
