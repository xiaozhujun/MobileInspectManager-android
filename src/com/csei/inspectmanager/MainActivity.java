package com.csei.inspectmanager;



import com.cesi.inspectmanager.R;
import com.csei.util.Informations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
public class MainActivity extends Activity {
	Button writecard;
	Button uoloadpic;
	private String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Bundle bundle = getIntent().getExtras();
		Informations userinfo = (Informations) bundle
				.getSerializable("userinfo");
		((TextView)findViewById(R.id.textView1)).setText("Hello! "+userinfo.userName);
		userId=Long.toString(userinfo.id);
		
		writecard=(Button) findViewById(R.id.writecard);
		uoloadpic=(Button) findViewById(R.id.uoloadpic);
		writecard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent1=new Intent(MainActivity.this, WriteCardActivity.class);
				intent1.putExtra("userId", userId);
				startActivity(intent1);
			}
		});
		uoloadpic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent2=new Intent(MainActivity.this, UploadPicActivity.class);
				intent2.putExtra("userId", userId);
				startActivity(intent2);
			}
		});
	}
}
