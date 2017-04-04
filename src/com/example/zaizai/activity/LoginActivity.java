package com.example.zaizai.activity;

import com.example.zaizai.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class LoginActivity extends Activity {
	
	Button cancleLogin, loginIn, loginHead;
	EditText accEdit, pwdEdit;
	TextView loginText;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		cancleLogin = (Button)findViewById(R.id.cancel_login);
		loginIn = (Button) findViewById(R.id.login_in);
		accEdit = (EditText)findViewById(R.id.accountEdittext);
		pwdEdit = (EditText)findViewById(R.id.pwdEdittext);
		
		
		//���õ�¼�¼�
		loginIn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				String account = accEdit.getText().toString();
				String password = pwdEdit.getText().toString();
				if( account.equals("С��") &&
						password.equals("ustb")){
					
					//�ش����ݣ���½�ɹ�
					Intent intent = new Intent();
					intent.putExtra("launch_return", 1);
					setResult(RESULT_OK,intent);
					finish();
				}
				else{
					Toast.makeText(getBaseContext(), "���������˻�������", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		cancleLogin.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				finish();
			}
		});
		
		
	}
}
