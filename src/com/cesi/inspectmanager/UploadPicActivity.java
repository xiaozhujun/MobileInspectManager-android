package com.cesi.inspectmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class UploadPicActivity extends Activity {
	private Button bt1;
	private Button bt2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_pic);
		bt1=(Button) findViewById(R.id.takeViaCamera);
		bt1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent1=new Intent(UploadPicActivity.this, ViaCameraActivity.class);
				startActivity(intent1);
			}
		});
		
		bt2=(Button) findViewById(R.id.takeViaLocal);
		bt2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent2=new Intent(UploadPicActivity.this, ViaLocalActivity.class);
				startActivity(intent2);
			}
		});
	}
}
