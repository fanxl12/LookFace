package com.fanxl.lookface;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.fanxl.lookface.domain.Candidate;
import com.fanxl.lookface.jsonService.JsonService;

public class SearchActivity extends Activity {

	private ImageView iv_search_picture, iv_search_1, iv_search_2, iv_search_3,
			iv_search_4, iv_search_5, iv_search_6, iv_search_7, iv_search_8,
			iv_search_9;
	protected static final int Error = 9;
	private TextView tv_name_1, tv_name_2, tv_name_3, tv_name_4, tv_name_5,
			tv_name_6, tv_name_7, tv_name_8, tv_name_9, et_title_text;
	private List<String> nameList = null;
	private static final int CHOOSE_PICTURE = 1;
	private String picturePath = null;
	private Bitmap img = null;
	private int phoneHeight;
	private int phoneWight;
	private int scale = 1;
	private LinearLayout ll_back_main;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				Bitmap bitmap = (Bitmap) msg.obj;
				iv_search_1.setImageBitmap(bitmap);
			} else if (msg.what == 1) {
				Bitmap bitmap = (Bitmap) msg.obj;
				iv_search_2.setImageBitmap(bitmap);
			} else if (msg.what == 2) {
				Bitmap bitmap = (Bitmap) msg.obj;
				iv_search_3.setImageBitmap(bitmap);
			} else if (msg.what == 3) {
				Bitmap bitmap = (Bitmap) msg.obj;
				iv_search_4.setImageBitmap(bitmap);
			} else if (msg.what == 4) {
				Bitmap bitmap = (Bitmap) msg.obj;
				iv_search_5.setImageBitmap(bitmap);
			} else if (msg.what == 5) {
				Bitmap bitmap = (Bitmap) msg.obj;
				iv_search_6.setImageBitmap(bitmap);
			} else if (msg.what == 6) {
				Bitmap bitmap = (Bitmap) msg.obj;
				iv_search_7.setImageBitmap(bitmap);
			} else if (msg.what == 7) {
				Bitmap bitmap = (Bitmap) msg.obj;
				iv_search_8.setImageBitmap(bitmap);
			} else if (msg.what == 8) {
				Bitmap bitmap = (Bitmap) msg.obj;
				iv_search_9.setImageBitmap(bitmap);
			} else if (msg.what == Error) {
				Toast.makeText(SearchActivity.this, "��ʾͼƬʧ��",
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 10) {
				nameList = (List<String>) msg.obj;
				tv_name_1.setText(nameList.get(0));
				tv_name_2.setText(nameList.get(1));
				tv_name_3.setText(nameList.get(2));
				tv_name_4.setText(nameList.get(3));
				tv_name_5.setText(nameList.get(4));
				tv_name_6.setText(nameList.get(5));
				tv_name_7.setText(nameList.get(6));
				tv_name_8.setText(nameList.get(7));
				tv_name_9.setText(nameList.get(8));
			}
		}
	};

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.search);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
		
		
		et_title_text = (TextView) findViewById(R.id.et_title_text);
		et_title_text.setText("��������");
		ll_back_main = (LinearLayout) findViewById(R.id.ll_back_main);
		ll_back_main.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SearchActivity.this, MainStartActivity.class));
				finish();
			}
		});

		// �õ��ֻ���Ļ�Ŀ��
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		phoneHeight = wm.getDefaultDisplay().getHeight();
		phoneWight = wm.getDefaultDisplay().getWidth();

		iv_search_picture = (ImageView) findViewById(R.id.iv_search_picture);
		iv_search_1 = (ImageView) findViewById(R.id.iv_search_1);
		iv_search_2 = (ImageView) findViewById(R.id.iv_search_2);
		iv_search_3 = (ImageView) findViewById(R.id.iv_search_3);
		iv_search_4 = (ImageView) findViewById(R.id.iv_search_4);
		iv_search_5 = (ImageView) findViewById(R.id.iv_search_5);
		iv_search_6 = (ImageView) findViewById(R.id.iv_search_6);
		iv_search_7 = (ImageView) findViewById(R.id.iv_search_7);
		iv_search_8 = (ImageView) findViewById(R.id.iv_search_8);
		iv_search_9 = (ImageView) findViewById(R.id.iv_search_9);
		tv_name_1 = (TextView) findViewById(R.id.tv_name_1);
		tv_name_2 = (TextView) findViewById(R.id.tv_name_2);
		tv_name_3 = (TextView) findViewById(R.id.tv_name_3);
		tv_name_4 = (TextView) findViewById(R.id.tv_name_4);
		tv_name_5 = (TextView) findViewById(R.id.tv_name_5);
		tv_name_6 = (TextView) findViewById(R.id.tv_name_6);
		tv_name_7 = (TextView) findViewById(R.id.tv_name_7);
		tv_name_8 = (TextView) findViewById(R.id.tv_name_8);
		tv_name_9 = (TextView) findViewById(R.id.tv_name_9);

	}

	public void searchResult(final List<Candidate> list) {

		for (int i = 0; i < 9; i++) {
			final int num = i;
			new Thread() {
				public void run() {
					try {
						// ��һ�������
						HttpClient client = new DefaultHttpClient();
						// �����ַ
						Candidate cd = list.get(num);
						String path = "http://fanxl12.dydisk.com/"
								+ cd.getTag();
						System.out.println("path:" + path);
						HttpGet httpGet = new HttpGet(path);
						// �ûس�
						HttpResponse response = client.execute(httpGet);
						int code = response.getStatusLine().getStatusCode();
						if (code == 200) {
							InputStream is = response.getEntity().getContent();
							Bitmap bitmap = BitmapFactory.decodeStream(is);
							Message msg = new Message();
							msg.what = num;
							msg.obj = bitmap;
							handler.sendMessage(msg);
						} else {
							Message msg = new Message();
							msg.what = Error;
							handler.sendMessage(msg);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				};
			}.start();
		}
	}

	public void getSearch(final String face_id) {

		new Thread(new Runnable() {

			public void run() {
				HttpRequests httpRequests = new HttpRequests(
						"f0d4ca0e4634482088cdd1023b9699e1",
						"h1D8DIiDCuE8QPQcIwSV-QUzVa6YOgw5", true, false);

				try {
					PostParameters parameters = new PostParameters();
					parameters.setKeyFaceId(face_id);
					System.out.println("������face_id:" + face_id);
					parameters.setFacesetName("fanxl");
					parameters.setCount(9);
					JSONObject result = httpRequests
							.recognitionSearch(parameters);
					String json = result.toString();
					List<Candidate> list = JsonService.getsearch(json);
					nameList = new ArrayList<String>();
					int i = 1;
					for (Candidate cd : list) {
						String tag = cd.getTag();
						int end = tag.indexOf("/");
						String name = i + "."
								+ tag.subSequence(0, end).toString();
						nameList.add(name);
						i++;
					}
					searchResult(list);
					Message msg = handler.obtainMessage();
					msg.what = 10;
					msg.obj = nameList;
					handler.sendMessage(msg);
				} catch (FaceppParseException e) {
					e.printStackTrace();
					SearchActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(SearchActivity.this, "�������",
									Toast.LENGTH_SHORT).show();
						}
					});
				}

			}
		}).start();
	}

	// ѡ����Ƭ��ť�ĵ���¼�
	public void GetPic(View view) {
		showPicturePicker(SearchActivity.this);
	}

	// �鿴��ť�ĵ���¼�����ͼƬ��Դ�ύ�����������õ����
	public void getId(View view) {

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
						float x, y, w, h;
						// �õ���������
						x = (float) rst.getJSONArray("face").getJSONObject(0)
								.getJSONObject("position")
								.getJSONObject("center").getDouble("x");
						y = (float) rst.getJSONArray("face").getJSONObject(0)
								.getJSONObject("position")
								.getJSONObject("center").getDouble("y");

						// �õ����Ĵ�С
						w = (float) rst.getJSONArray("face").getJSONObject(0)
								.getJSONObject("position").getDouble("width");
						h = (float) rst.getJSONArray("face").getJSONObject(0)
								.getJSONObject("position").getDouble("height");

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

						String face_id = rst.getJSONArray("face").getJSONObject(0).getString("face_id");
						System.out.println("face_id:"+face_id);
						getSearch(face_id);
						// �����޸�֮���µ�bitmap
						img = bitmap;
						// ����һ���̲߳����뵽UI�߳��������޸�UI
						SearchActivity.this.runOnUiThread(new Runnable() {

							public void run() {
								// �ѵõ����µ�bitmap�ŵ��ؼ���
								iv_search_picture.setImageBitmap(img);
							}
						});

					} catch (JSONException e) {
						e.printStackTrace();
						SearchActivity.this.runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(SearchActivity.this, "�������",
										Toast.LENGTH_SHORT).show();
							}
						});
					}
				}
			});
			faceppDetect.detect(img);
		} else {
			Toast.makeText(SearchActivity.this, "��Ƭ����Ϊ��", Toast.LENGTH_SHORT)
					.show();
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
				iv_search_picture.setImageBitmap(img);
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
						SearchActivity.this.runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(SearchActivity.this, "�������",
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
