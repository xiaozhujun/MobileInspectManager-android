package com.cesi.inspectmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.cesi.client.CasClient;
import com.cesi.inspectmanager.ViaCameraActivity.MyThread;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ViaLocalActivity extends Activity {
	private List<File> fileList;
	private FileInputStream fis;
	private ProgressDialog progressDialog;
	private Builder alertDialog;
	private String name;
	private Handler handler;
	private ListView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_via_local);
		String status=Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				File f=new File(Environment.getExternalStorageDirectory()+"/image");
				fileList=FindFile(f, ".jpg");
			} catch (ActivityNotFoundException e) {
				// TODO: handle exception
				Toast.makeText(ViaLocalActivity.this, "没有找到存储目录", Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	
		listview=(ListView) findViewById(R.id.listView_local);
		listview.setAdapter(new MyAdapter(this, fileList));
		
		
		
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
		//接收message并设置进度条
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
	
	
	
	//在image目录下寻找.jpg文件
	private static List<File> FindFile(File file,String key){
		List<File> list=new ArrayList<File>();
		if(file.isDirectory()){
			File[] allFile=file.listFiles();
			for(File temp:allFile){
				if (temp.isFile()&&temp.getName().toLowerCase().lastIndexOf(key)>-1) {
					list.add(temp);
				}
			}
		}
		return list;
	}
	
	//MyAdapter的设置
	class MyAdapter extends BaseAdapter{
		private List<File> list;
		private Context context;
		private LayoutInflater inflater;

		public  MyAdapter(Context context,List<File> list) {
			// TODO Auto-generated constructor stub
			this.list=list;
			this.inflater=LayoutInflater.from(context);
			this.context=context;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if(convertView==null)
			{
				holder=new ViewHolder();
				convertView=inflater.inflate(R.layout.item, null);
				holder.image=(ImageView) convertView.findViewById(R.id.image);
				holder.name=(TextView)convertView. findViewById(R.id.name);
				holder.upload=(Button)convertView. findViewById(R.id.upload);
				convertView.setTag(holder);
			}
			holder=(ViewHolder) convertView.getTag();
			//图片缩小为原本的1/8
			BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();  
			bmpFactoryOptions.inSampleSize = 8; 
			Bitmap bitmap=BitmapFactory.decodeFile(list.get(position).getPath(), bmpFactoryOptions);
			Log.i("fdsfds", bitmap.toString());
			holder.image.setImageBitmap(bitmap);
			holder.name.setText(list.get(position).getName());
			holder.upload.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					name=list.get(position).getName();
					(new Thread(new MyThread())).start();
					progressDialog.show();
					
				}
			});
			
			
			return convertView;
		}
		
		public class ViewHolder{
			public ImageView image;
			public TextView name;
			public Button upload;
		}
		
	}
	
	//利用线程上传文件
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
