package com.csei.inspectmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.cesi.inspectmanager.R;
import com.csei.inspectmanager.ShowFileActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

public class WriteCardActivity extends Activity {
	private Button writeTagsButton;
	private Button writeEmployerButton;
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_card);
		writeTagsButton=(Button) findViewById(R.id.writeTags);
		writeEmployerButton=(Button) findViewById(R.id.writeEmployer);
		
		progressDialog = ProgressDialog.show(this, "加载写卡资料", "正在加载写卡资料，请稍后...");
		ReadURL("http://59.69.74.249:8080/MyApp/tags.xml");
		progressDialog.dismiss();
		
		writeTagsButton.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WriteCardActivity.this,ShowFileActivity.class);
				intent.putExtra("cardType", "tags");
				startActivity(intent);
			}
		});
		writeEmployerButton.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WriteCardActivity.this,ShowFileActivity.class);
				intent.putExtra("cardType", "Employer");
				startActivity(intent);
			}
		});
	}
	protected void ReadURL(String url) {
		new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... params) {
				try {
					URL url = new URL(params[0]);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					
					connection.setDoInput(true);
					connection.setDoOutput(true);
					connection.setRequestMethod("GET");
					
					InputStream iStream = connection.getInputStream();
					InputStreamReader isReader = new InputStreamReader(iStream,"utf-8");
					BufferedReader bReader = new BufferedReader(isReader);
					String line;
					StringBuilder builder = new StringBuilder();
					while((line=bReader.readLine())!=null){
						builder.append(line);
					}
					bReader.close();
					isReader.close();
					iStream.close();
					return builder.toString();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(String result) {
	
				OutputFormat format=OutputFormat.createPrettyPrint();
				String ENCODING="UTF-8";
				format.setEncoding(ENCODING);
				format.setNewlines(true);
				
				try {
					org.dom4j.Document document = DocumentHelper.parseText(result);
					String filename = Environment.getExternalStorageDirectory().getPath()+File.separator+"tags.xml";
					File file = new File(filename);
					file.createNewFile();
					FileOutputStream out = new FileOutputStream(filename);
					XMLWriter writer = new XMLWriter(out,format);
					writer.write(document);
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (DocumentException e) {
					e.printStackTrace();
				}
				super.onPostExecute(result);
			}

			@Override
			protected void onProgressUpdate(Void... values) {
				super.onProgressUpdate(values);
			}

			@SuppressLint("NewApi") @Override
			protected void onCancelled(String result) {
				super.onCancelled(result);
			}

			@Override
			protected void onCancelled() {
				super.onCancelled();
			}
			
		}.execute(url);
	}
	}

