package com.example.zaizai.activity;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.example.zaizai.R;
import com.example.zaizai.util.FaceUtil;

/**
 * ��������ʾ��
 *
 * @author iFlytek &nbsp;&nbsp;&nbsp;<a href="http://http://www.xfyun.cn/">Ѷ��������</a>
 */
public class OnlineFace extends Activity implements OnClickListener {
	private final int REQUEST_PICTURE_CHOOSE = 1;
	private final int REQUEST_CAMERA_IMAGE = 2;
	
	private Bitmap mImage = null;
	private byte[] mImageData = null;
	// authidΪ6-18���ַ����ȣ�����Ψһ��ʶ�û�
	private String mAuthid = null;
	private Toast mToast;
	// ���ȶԻ���
	private ProgressDialog mProDialog;
	// ���յõ�����Ƭ�ļ�
	private File mPictureFile;
	// FaceRequest���󣬼���������ʶ��ĸ��ֹ���
	private FaceRequest mFaceRequest;
	
	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.onlineface);
		
		// �ڳ�����ڴ�����appid����ʼ��SDK
		SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));
		findViewById(R.id.online_pick).setOnClickListener(OnlineFace.this);
		findViewById(R.id.online_reg).setOnClickListener(OnlineFace.this);
		//findViewById(R.id.online_verify).setOnClickListener(OnlineFace.this);
		findViewById(R.id.online_camera).setOnClickListener(OnlineFace.this);
		//findViewById(R.id.online_detect).setOnClickListener(OnlineFace.this);
		//findViewById(R.id.online_align).setOnClickListener(OnlineFace.this);
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		
		//��ȡ��½״̬�����ע�������ֱ�ӽ�����Ƶ��֤�
		SharedPreferences pref = getSharedPreferences("launch",
				MODE_PRIVATE);
		Boolean isFirst =pref.getBoolean("firstlaunch", true);
		String idtem = pref.getString("authorid", null);
		if(isFirst == false){
			Intent intent=new Intent(OnlineFace.this,com.example.zaizai.activity.VideoVerify.class);
			intent.putExtra("id", idtem);
			startActivity(intent);
			finish();
		}

		
		mProDialog = new ProgressDialog(this);
		mProDialog.setCancelable(true);
		mProDialog.setTitle("���Ժ�");
		
		mProDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// cancel���ȿ�ʱ,ȡ�����ڽ��еĲ���
				if (null != mFaceRequest) {
					mFaceRequest.cancel();
				}
			}
		});
		
		mFaceRequest = new FaceRequest(this);
	}

	private void register(JSONObject obj) throws JSONException {
		int ret = obj.getInt("ret");
		if (ret != 0) {
			showTip("ע��ʧ��");
			return;
		}
		if ("success".equals(obj.get("rst"))) {
			showTip("ע��ɹ�");
			
			//��ס��¼״̬
			SharedPreferences.Editor editor=getSharedPreferences("launch", MODE_PRIVATE).edit(); 
			editor.putBoolean("firstlaunch", false); 
			editor.putString("authorid", mAuthid);
			editor.commit();
			
			Intent intent = new Intent(OnlineFace.this,com.example.zaizai.MainActivity.class);
			startActivity(intent);
			finish();
			
		} else {
			showTip("ע��ʧ��");
		}
	}

	private void verify(JSONObject obj) throws JSONException {
		int ret = obj.getInt("ret");
		if (ret != 0) {
			showTip("��֤ʧ��");
			return;
		}
		if ("success".equals(obj.get("rst"))) {
			if (obj.getBoolean("verf")) {
				showTip("ͨ����֤����ӭ������");
				//�ش����ݣ���½�ɹ�
				Intent intent = new Intent();
				intent.putExtra("launch_return", 1);
				setResult(RESULT_OK,intent);
				//finish();
			} else {
				showTip("��֤��ͨ��");
			}
		} else {
			showTip("��֤ʧ��");
		}
	}

	private void detect(JSONObject obj) throws JSONException {
		int ret = obj.getInt("ret");
		if (ret != 0) {
			showTip("���ʧ��");
			return;
		}

		if ("success".equals(obj.get("rst"))) {
			JSONArray faceArray = obj.getJSONArray("face");

			Paint paint = new Paint();
			paint.setColor(Color.RED);
			paint.setStrokeWidth(Math.max(mImage.getWidth(), mImage.getHeight()) / 100f);

			Bitmap bitmap = Bitmap.createBitmap(mImage.getWidth(),
					mImage.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			canvas.drawBitmap(mImage, new Matrix(), null);
			for (int i = 0; i < faceArray.length(); i++) {
				float x1 = (float) faceArray.getJSONObject(i)
						.getJSONObject("position").getDouble("left");
				float y1 = (float) faceArray.getJSONObject(i)
						.getJSONObject("position").getDouble("top");
				float x2 = (float) faceArray.getJSONObject(i)
						.getJSONObject("position").getDouble("right");
				float y2 = (float) faceArray.getJSONObject(i)
						.getJSONObject("position").getDouble("bottom");
				paint.setStyle(Style.STROKE);
				canvas.drawRect(new Rect((int)x1, (int)y1, (int)x2, (int)y2), 
						paint);
			}

			mImage = bitmap;
			((ImageView) findViewById(R.id.online_img)).setImageBitmap(mImage);
		} else {
			showTip("���ʧ��");
		}
	}

	@SuppressWarnings("rawtypes")
	private void align(JSONObject obj) throws JSONException {
		int ret = obj.getInt("ret");
		if (ret != 0) {
			showTip("�۽�ʧ��");
			return;
		}
		if ("success".equals(obj.get("rst"))) {
			Paint paint = new Paint();
			paint.setColor(Color.BLUE);
			paint.setStrokeWidth(Math.max(mImage.getWidth(), mImage.getHeight()) / 100f);

			Bitmap bitmap = Bitmap.createBitmap(mImage.getWidth(),
					mImage.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			canvas.drawBitmap(mImage, new Matrix(), null);

			JSONArray faceArray = obj.getJSONArray("result");
			for (int i = 0; i < faceArray.length(); i++) {
				JSONObject landmark = faceArray.getJSONObject(i).getJSONObject(
						"landmark");
				Iterator it = landmark.keys();
				while (it.hasNext()) {
					String key = (String) it.next();
					JSONObject postion = landmark.getJSONObject(key);
					canvas.drawPoint((float) postion.getDouble("x"),
							(float) postion.getDouble("y"), paint);
				}
			}

			mImage = bitmap;
			((ImageView) findViewById(R.id.online_img)).setImageBitmap(mImage);
		} else {
			showTip("�۽�ʧ��");
		}
	}
	
	private RequestListener mRequestListener = new RequestListener() {

		@Override
		public void onEvent(int eventType, Bundle params) {
		}

		@Override
		public void onBufferReceived(byte[] buffer) {
			if (null != mProDialog) {
				mProDialog.dismiss();
			}

			try {
				String result = new String(buffer, "utf-8");
				Log.d("FaceDemo", result);
				
				JSONObject object = new JSONObject(result);
				String type = object.optString("sst");
				if ("reg".equals(type)) {
					register(object);
				} else if ("verify".equals(type)) {
					verify(object);
				} else if ("detect".equals(type)) {
					detect(object);
				} else if ("align".equals(type)) {
					align(object);
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO: handle exception
			}
		}

		@Override
		public void onCompleted(SpeechError error) {
			if (null != mProDialog) {
				mProDialog.dismiss();
			}

			if (error != null) {
				switch (error.getErrorCode()) {
				case ErrorCode.MSP_ERROR_ALREADY_EXIST:
					showTip("authid�Ѿ���ע�ᣬ�����������");
					break;
					
				default:
					showTip(error.getPlainDescription(true));
					break;
				}
			}
		}
	};

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.online_pick:
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_PICK);
			startActivityForResult(intent, REQUEST_PICTURE_CHOOSE);
			break;
		case R.id.online_reg:
			mAuthid = ((EditText) findViewById(R.id.online_authid)).getText().toString();
			if (TextUtils.isEmpty(mAuthid)) {
				showTip("authid����Ϊ��");
				return;
			}
			
			if (null != mImageData) {
				mProDialog.setMessage("ע����...");
				mProDialog.show();
				// �����û���ʶ����ʽΪ6-18���ַ�������ĸ�����֡��»�����ɣ����������ֿ�ͷ�����ܰ����ո񣩡�
				// ��������ʱ���ƶ˽�ʹ���û��豸���豸ID����ʶ�ն��û���
				mFaceRequest.setParameter("property","del"); 
				mFaceRequest.setParameter(SpeechConstant.AUTH_ID, mAuthid);
				mFaceRequest.setParameter(SpeechConstant.WFR_SST, "reg");
				mFaceRequest.sendRequest(mImageData, mRequestListener);
			} else {
				showTip("��ѡ��ͼƬ����ע��");
			}
			break;
		/*case R.id.online_verify:
			mAuthid = ((EditText) findViewById(R.id.online_authid)).getText().toString();
			if (TextUtils.isEmpty(mAuthid)) {
				showTip("authid����Ϊ��");
				return;
			} 
			
			if (null != mImageData) {
				mProDialog.setMessage("��֤��...");
				mProDialog.show();
				// �����û���ʶ����ʽΪ6-18���ַ�������ĸ�����֡��»�����ɣ����������ֿ�ͷ�����ܰ����ո񣩡�
				// ��������ʱ���ƶ˽�ʹ���û��豸���豸ID����ʶ�ն��û���
				mFaceRequest.setParameter(SpeechConstant.AUTH_ID, mAuthid);
				mFaceRequest.setParameter(SpeechConstant.WFR_SST, "verify");
				mFaceRequest.sendRequest(mImageData, mRequestListener);
			} else {
				showTip("��ѡ��ͼƬ������֤");
			}
			break;
		case R.id.online_detect:
			if (null != mImageData) {
				mProDialog.setMessage("�����...");
				mProDialog.show();
				mFaceRequest.setParameter(SpeechConstant.WFR_SST, "detect");
				mFaceRequest.sendRequest(mImageData, mRequestListener);
			} else {
				showTip("��ѡ��ͼƬ���ټ��");
			}
			break;
		case R.id.online_align:
			if (null != mImageData) {
				mProDialog.setMessage("�۽���...");
				mProDialog.show();
				mFaceRequest.setParameter(SpeechConstant.WFR_SST, "align");
				mFaceRequest.sendRequest(mImageData, mRequestListener);
			} else {
				showTip("��ѡ��ͼƬ���پۼ�");
			}
			break;*/
		case R.id.online_camera:
			// ����������պ���Ƭ����·��
			mPictureFile = new File(Environment.getExternalStorageDirectory(), 
					"picture" + System.currentTimeMillis()/1000 + ".jpg");
			// ��������,�����浽��ʱ�ļ�
			Intent mIntent = new Intent();
			mIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			mIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPictureFile));
			mIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
			startActivityForResult(mIntent, REQUEST_CAMERA_IMAGE);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		
		String fileSrc = null;
		if (requestCode == REQUEST_PICTURE_CHOOSE) {
			if ("file".equals(data.getData().getScheme())) {
				// ��Щ�Ͱ汾���ͷ��ص�UriģʽΪfile
				fileSrc = data.getData().getPath();
			} else {
				// Uriģ��Ϊcontent
				String[] proj = {MediaStore.Images.Media.DATA};
				Cursor cursor = getContentResolver().query(data.getData(), proj,
						null, null, null);
				cursor.moveToFirst();
				int idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				fileSrc = cursor.getString(idx);
				cursor.close();
			}
			// ��ת��ͼƬ�ü�ҳ��
			FaceUtil.cropPicture(this,Uri.fromFile(new File(fileSrc)));
		} else if (requestCode == REQUEST_CAMERA_IMAGE) {
			if (null == mPictureFile) {
				showTip("����ʧ�ܣ�������");
				return;
			}
			
			fileSrc = mPictureFile.getAbsolutePath();
			updateGallery(fileSrc);
			// ��ת��ͼƬ�ü�ҳ��
			FaceUtil.cropPicture(this,Uri.fromFile(new File(fileSrc)));
		} else if (requestCode == FaceUtil.REQUEST_CROP_IMAGE) {
			// ��ȡ��������
			Bitmap bmp = data.getParcelableExtra("data");
			// ���������ݲ�Ϊnull�����������أ���ֹ�ü�ʱδ����������
			if(null != bmp){
				FaceUtil.saveBitmapToFile(OnlineFace.this, bmp);
			}
			// ��ȡͼƬ����·��
			fileSrc = FaceUtil.getImagePath(OnlineFace.this);
			// ��ȡͼƬ�Ŀ�͸�
			Options options = new Options();
			options.inJustDecodeBounds = true;
			mImage = BitmapFactory.decodeFile(fileSrc, options);
			
			// ѹ��ͼƬ
			options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max(
					(double) options.outWidth / 1024f,
					(double) options.outHeight / 1024f)));
			options.inJustDecodeBounds = false;
			mImage = BitmapFactory.decodeFile(fileSrc, options);
			
			
			// ��mImageBitmapΪ����ͼƬ��Ϣ����������ȡ
			if(null == mImage) {
				showTip("ͼƬ��Ϣ�޷�������ȡ��");
				return;
			}
			
			// �����ֻ����ͼƬ����ת����������ת�Ƕ�
			int degree = FaceUtil.readPictureDegree(fileSrc);
			if (degree != 0) {
				// ��ͼƬ��תΪ���ķ���
				mImage = FaceUtil.rotateImage(degree, mImage);
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			//�ɸ�������������״����ͼƬ����ѹ��
			mImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
			mImageData = baos.toByteArray();
			
			((ImageView) findViewById(R.id.online_img)).setImageBitmap(mImage);
		}
		
	}

	@Override
	public void finish() {
		if (null != mProDialog) {
			mProDialog.dismiss();
		}
		super.finish();
	}

	private void updateGallery(String filename) {
		MediaScannerConnection.scanFile(this, new String[] {filename}, null,
				new MediaScannerConnection.OnScanCompletedListener() {
					
					@Override
					public void onScanCompleted(String path, Uri uri) {

					}
				});
	}

	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}
	
}
