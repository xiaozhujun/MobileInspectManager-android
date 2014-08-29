package com.csei.inspectmanager;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.cesi.inspectmanager.R;
import com.csei.parseXml.ParseXml;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ShowFileActivity extends Activity{
	private ListView fileShowListView;
	private String cardType;
	private String path;
	private ParseXml p = new ParseXml();
	private List<String> list = new ArrayList<String>();
	private String[] S1={};
	private String writeData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showfile);
		fileShowListView=(ListView) findViewById(R.id.showfilelistview);
		Intent intent = getIntent();
		cardType=intent.getStringExtra("cardType");


		if(cardType.equals("tags")){

			//Ω‚Œˆtags

			path=Environment.getExternalStorageDirectory().getPath()+File.separator+"tags.xml";
			list = p.parseTagXml(path);
			S1 = listToString(list);
			List<String> showList = new ArrayList<String>();
			for(int i=0;i<S1.length;i++){
				String[] strings = S1[i].split(",");
				showList.add(strings[3]+":"+strings[1]);
			}
			ArrayAdapter<String> myadAdapter = new ArrayAdapter<String>(ShowFileActivity.this,R.layout.item_writecard,showList);
			fileShowListView.setAdapter(myadAdapter);
		}else{
			path=Environment.getExternalStorageDirectory().getPath()+File.separator+"Employer.xml";
			list = p.parseEmployers(path);
			S1 = listToString(list);
			List<String> showList = new ArrayList<String>();
			for(int i=0;i<S1.length;i++){
				String[] strings = S1[i].split(",");
				showList.add(strings[3]+":"+strings[1]);
			}
			ArrayAdapter<String> myadAdapter = new ArrayAdapter<String>(ShowFileActivity.this,R.layout.item_writecard,showList);
			fileShowListView.setAdapter(myadAdapter);
			//Ω‚ŒˆEmployer
		}
		fileShowListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				writeData = S1[position];
				Intent intent =  new Intent(ShowFileActivity.this,WriteActivity.class);
				intent.putExtra("writeData", writeData);
				intent.putExtra("cardType", cardType);
				startActivity(intent);
			}
		});
	}

	protected String[] listToString(List<String> list){
		String string="";
		Iterator<String> it = list.iterator();
		while(it.hasNext()){
			string+=it.next();
		}
		String[] strings=string.split(" ");
			
		return strings;
	}
}
