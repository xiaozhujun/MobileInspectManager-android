package com.csei.inspectmanager;

import com.cesi.inspectmanager.R;
import com.csei.inspectmanager.ShowFileActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WriteCardActivity extends Activity {
	private Button writeTagsButton;
	private Button writeEmployerButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_card);
		writeTagsButton=(Button) findViewById(R.id.writeTags);
		writeEmployerButton=(Button) findViewById(R.id.writeEmployer);
		writeTagsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WriteCardActivity.this,ShowFileActivity.class);
				intent.putExtra("cardType", "tags");
				startActivity(intent);
			}
		});
		writeEmployerButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WriteCardActivity.this,ShowFileActivity.class);
				intent.putExtra("cardType", "Employer");
				startActivity(intent);
			}
		});
	}
	}

