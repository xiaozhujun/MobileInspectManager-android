package com.csei.inspectmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONObject;

import com.cesi.inspectmanager.R;
import com.csei.client.CasClient;
import com.csei.inspectmanager.LoginActivity.MyThread;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ViaCameraActivity extends Activity {
	private String name = DateFormat.format("yyyyMMdd_hhmmss",
			Calendar.getInstance(Locale.CHINA))
			+ ".jpg";
	private Button upload;
	private Button cancel;
	private Handler handler;
	private ProgressDialog progressDialog;
	private Builder alertDialog;
	private String diviceNum;
	private String tagArea;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_via_camera);
		
		Intent intent1=this.getIntent();
		diviceNum=intent1.getStringExtra("diviceNum");
		tagArea=intent1.getStringExtra("tagArea");
		
		
		
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {

				File dir = new File(Environment.getExternalStorageDirectory()
						+ "/image");
				if (!dir.exists())
					dir.mkdirs();
				Intent intent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				File file = new File(dir, name);
				Uri uri = Uri.fromFile(file);
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				startActivityForResult(intent, 1);
			} catch (ActivityNotFoundException e) {
				// TODO: handle exception
				Toast.makeText(ViaCameraActivity.this, "没有找到存储目录",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(ViaCameraActivity.this, "没有存储卡", Toast.LENGTH_SHORT)
					.show();
		}

		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("提示");
		progressDialog.setMessage("正在上传...");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setCancelable(false);
		alertDialog = new Builder(this);
		alertDialog.setTitle("提示");
		alertDialog.setPositiveButton("确认",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						alertDialog.create().dismiss();
						finish();
					}
				});

		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					progressDialog.dismiss();
					alertDialog.setTitle("提示").setMessage("上传成功").show();
					finish();
					break;
				case 1:
					progressDialog.dismiss();
					alertDialog.setTitle("提示").setMessage("上传失败，请稍后重试").show();
					finish();
					break;
				case 2:
					progressDialog.setProgress(msg.arg1);
					break;
				default:
					break;
				}
			}

		};

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/image/", name);
			try {
				// Uri uri = Uri.parse(android.provider.MediaStore.Images.Media
				// .insertImage(getContentResolver(),
				// file.getAbsolutePath(), null, null));
				// Bitmap
				// bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),
				// uri);
				FileInputStream fis = new FileInputStream(
						Environment.getExternalStorageDirectory() + "/image/"
								+ name);
				Bitmap bitmap = BitmapFactory.decodeStream(fis);
				((ImageView) findViewById(R.id.imageView1))
						.setImageBitmap(bitmap);
				upload = (Button) findViewById(R.id.upload);
				cancel = (Button) findViewById(R.id.cancel);

				upload.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						(new Thread(new MyThread())).start();
						progressDialog.show();
					}
				});

				cancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}
				});

			} catch (FileNotFoundException e) {
				// TODO: handle exception
				e.printStackTrace();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	class MyThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = Message.obtain();
			try {
				String result = CasClient.getInstance().doSendFile2(
						getResources().getString(R.string.UP_LOAD_FILE),
						Environment.getExternalStorageDirectory() + "/image/"
								+ name);
				int code = Integer.parseInt((new JSONObject(result)
						.getJSONObject("code")).toString());
				if (code == 200) {
					msg.what = 0;
				} else {
					msg.what = 1;
				}
			} catch (Exception e) {
				// TODO: handle exception
				msg.what = 1;
				e.printStackTrace();
			}
		}

	}

}
