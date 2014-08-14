package com.cesi.inspectmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ShowActivity extends Activity {
	String name=DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA))+".jpg";
	Button upload;
	Button cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show);
		String status=Environment.getExternalStorageState();
		if(status.equals(Environment.MEDIA_MOUNTED)){
			try {

				File dir =new File(Environment.getExternalStorageDirectory()+"/image");
				if(!dir.exists())dir.mkdirs();
				Intent intent=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				File file=new File(dir,name);
				Uri uri=Uri.fromFile(file);
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				startActivityForResult(intent,1);
			} catch (ActivityNotFoundException e) {
				// TODO: handle exception
				Toast.makeText(ShowActivity.this, "没有找到存储目录", Toast.LENGTH_SHORT).show();
			}
		}else {
			Toast.makeText(ShowActivity.this, "没有存储卡", Toast.LENGTH_SHORT).show();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK)
		{
			File file=new File(Environment.getExternalStorageDirectory()+"/image/",name);
			try {
				Uri uri=Uri.parse(android.provider.MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(), null, null));
//				Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
				FileInputStream fis = new FileInputStream(Environment.getExternalStorageDirectory()+"/image/"+name);
		          Bitmap bitmap= BitmapFactory.decodeStream(fis);
				((ImageView)findViewById(R.id.imageView1)).setImageBitmap(bitmap);
				upload=(Button) findViewById(R.id.upload);
				cancel=(Button) findViewById(R.id.cancel);
				
				upload.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
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
			}catch (Exception e) {
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
	
}
