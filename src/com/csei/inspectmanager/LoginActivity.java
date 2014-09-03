package com.csei.inspectmanager;

import org.json.JSONException;
import org.json.JSONObject;

import com.cesi.client.CasClient;
import com.cesi.inspectmanager.R;
import com.csei.util.Informations;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private Button button;
	private EditText passwords;
	private EditText username;
	private String name;
	private String pswords;
	private CheckBox cb;
	private Informations informations = null;
	private JSONObject jsonObject;
	private String number1;
	private String role1;
	private String roleNum1;
	private String name1;
	private String userName1;
	private String id1;
	private String image1;
	private String sex1;
	private String userRole1;

	private Handler handler;

	// private
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		passwords = (EditText) findViewById(R.id.passwords);
		username = (EditText) findViewById(R.id.username);
		
		passwords.setTransformationMethod(PasswordTransformationMethod
				.getInstance());

		passwords.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_ENTER
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					name = username.getText().toString().trim();
					pswords = passwords.getText().toString().trim();
					new Thread(new MyThread()).start();
				}
				return false;
			}
		});

		cb = (CheckBox) findViewById(R.id.show);
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (cb.isChecked()) {
					passwords
							.setTransformationMethod(HideReturnsTransformationMethod
									.getInstance());
				} else {
					passwords
							.setTransformationMethod(PasswordTransformationMethod
									.getInstance());
				}
			}
		});

		button = (Button) findViewById(R.id.submit);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				name = username.getText().toString().trim();
				pswords = passwords.getText().toString().trim();

				new Thread(new MyThread()).start();
			}
		});

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				switch (msg.what) {
				case 1:
					Intent intent1 = new Intent(LoginActivity.this,
							MainActivity.class);
					Bundle bundle1 = new Bundle();
					bundle1.putSerializable("userinfo", (Informations) msg.obj);
					intent1.putExtras(bundle1);
					startActivity(intent1);
					break;
				case 2:
					String abc = "用户名或密码错误";
					Toast.makeText(getApplicationContext(), abc,
							Toast.LENGTH_SHORT).show();
					break;

				}

			}
		};

	}

	class MyThread implements Runnable {
		public void run() {
			final boolean loginresult = CasClient.getInstance().login(name,
					pswords,
					getResources().getString(R.string.LOGIN_SECURITY_CHECK_LOCAL));
			if(loginresult)
			Log.i("tag1", "1q");
			if (loginresult) {

				String msg1 = CasClient.getInstance().doGet(
						getResources().getString(R.string.USER_GETIMF_LOCAL));
				try {
					jsonObject = (new JSONObject(msg1)).getJSONObject("data");
					number1 = jsonObject.getString("number");
					role1 = jsonObject.getString("role");
					roleNum1 = jsonObject.getString("roleNum");
					name1 = jsonObject.getString("name");
					userName1 = jsonObject.getString("userName");
					id1 = jsonObject.getString("id");
					image1 = jsonObject.getString("image");
					sex1 = jsonObject.getString("sex");
					userRole1 = jsonObject.getString("userRole");
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				informations = new Informations(number1, role1, roleNum1,
						name1, userName1, id1, image1, sex1, userRole1);

				Message msg = Message.obtain();
				msg.obj = informations;
				msg.what = 1;
				handler.sendMessage(msg);
			} else {
				Message msg = Message.obtain();
				msg.what = 2;
				handler.sendMessage(msg);
			}
		}

	}

}
