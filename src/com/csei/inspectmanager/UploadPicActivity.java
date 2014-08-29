package com.csei.inspectmanager;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.cesi.inspectmanager.R;
import com.csei.entity.Listable;
import com.csei.entity.Tag;
import com.csei.service.RFIDService;

public class UploadPicActivity extends Activity {
	private Button bt1;
	private Button bt2;
	private String cardType="0x02";
	private String activity="com.csei.inspectmanager.ViaCameraActivity";
	private String deviceNum;
	private String tagArea;
	private ProgressDialog progressDialog;
	private Timer timerDialog;
	private int MSG_FLAG=1;
	private int MSG_OVER=2;
	private String userId;
	
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what==MSG_FLAG) {
				
			}else if (msg.what==MSG_OVER) {
				Toast.makeText(getApplicationContext(), "未检测到标签卡，请重试", Toast.LENGTH_SHORT).show();
			}
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent=this.getIntent();
		userId=intent.getStringExtra(userId);
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("提示");
		progressDialog.setMessage("正在读卡...");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(false);
		
		
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
				
				progressDialog.show();
				timerDialog=new Timer();
				timerDialog.schedule(new TimerTask() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						progressDialog.cancel();
						Message msg=new Message();
						msg.what=MSG_OVER;
						handler.sendMessage(msg);
					}
				}, 7000);
				Intent intent2=new Intent(UploadPicActivity.this, RFIDService.class);
				intent2.putExtra("cardType", cardType);
				intent2.putExtra("activity", activity);
				startService(intent2);
			}
		});
	}
	
	
	
	
	private class MyReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Listable listable=intent.getParcelableExtra("listable");
			if (listable==null) {
				Toast.makeText(UploadPicActivity.this, "读卡失败", Toast.LENGTH_SHORT).show();
				return;
			}else if (!(listable instanceof Tag)) {
				Toast.makeText(UploadPicActivity.this, "卡片类型错误", Toast.LENGTH_SHORT).show();
			}
			Tag tag=(Tag) listable;
			progressDialog.cancel();
			timerDialog.cancel();
			
			deviceNum=tag.getDeviceNum();
			tagArea=tag.getTagArea();
			
			Intent intent3=new Intent(UploadPicActivity.this,ViaCameraActivity.class);
			intent3.putExtra("diviceNum", deviceNum);
			intent3.putExtra("tagArea", tagArea);
			intent3.putExtra("userId", userId);
			startActivity(intent3);
		}
		
	}
	
}
