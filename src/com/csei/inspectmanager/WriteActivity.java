package com.csei.inspectmanager;
import com.cesi.inspectmanager.R;
import com.csei.entity.Employer;
import com.csei.entity.Listable;
import com.csei.entity.Tag;
import com.csei.service.RFIDService;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WriteActivity extends Activity{
	private TextView writeDataText;
	private Button writeCardButton;
	private String writeData;
	private String cardType;
	private String activity="com.csei.inspectmanager.WriteActivity";
	private myBroadcastReceiver myBC;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write);
		writeDataText=(TextView) findViewById(R.id.writedatatextview);
		writeCardButton=(Button) findViewById(R.id.writecardbutton);
		Intent intent = getIntent();
		writeDataText.setText(intent.getStringExtra("writeData"));
		writeData=intent.getStringExtra("writeData");
		cardType=intent.getStringExtra("cardType");
		
		writeCardButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				sendCmd(writeData);
				
			}
		});
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		myBC = new myBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.csei.inspectmanager.WriteActivity");
		registerReceiver(myBC, filter);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub		
		super.onResume();
	}
	
	private class myBroadcastReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String receiveData = intent.getStringExtra("result");
			Toast.makeText(getApplicationContext(), receiveData, Toast.LENGTH_LONG).show();
		}
	} 
	private void sendCmd(String writeData){
		try {
			Intent intent = new Intent(WriteActivity.this,RFIDService.class);
			Listable listable = null;
			if(cardType.equals("tags")){
				//如果匹配为tags写入tags卡
				intent.putExtra("cardType", "0x02");
				listable = new Tag(writeData);
			}else if(cardType.equals("Employer")){
				//写入employer卡
				intent.putExtra("cardType", "0x01");
				listable = new Employer(writeData);
			}
			intent.putExtra("listable", listable);
			intent.putExtra("activity", activity);
			startService(intent);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
