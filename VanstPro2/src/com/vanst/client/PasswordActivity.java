package com.vanst.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends Activity {
	EditText oldEditText;
	EditText newEditText;
	EditText new2EditText;
	Button button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password);
		oldEditText = (EditText)findViewById(R.id.password_old);
		newEditText = (EditText)findViewById(R.id.password_new);
		new2EditText = (EditText)findViewById(R.id.password_new2);
		oldEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		newEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		new2EditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		button = (Button)findViewById(R.id.password_button);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				SharedPreferences pref=getSharedPreferences("config", MODE_PRIVATE);
				String password = pref.getString("password", "vanst");
				if(oldEditText.getText().toString().length() == 0){
					Toast.makeText(PasswordActivity.this, "ԭ���벻��Ϊ��", Toast.LENGTH_SHORT).show();
				}else if(newEditText.getText().toString().length() == 0 || new2EditText.getText().toString().length() ==0){
					Toast.makeText(PasswordActivity.this, "�����벻��Ϊ��", Toast.LENGTH_SHORT).show();
				}else if(newEditText.getText().toString().length() < 5 || newEditText.getText().toString().length()>10){
					Toast.makeText(PasswordActivity.this, "����Ӧ����5~10λ", Toast.LENGTH_SHORT).show();
				}else if(!newEditText.getText().toString().equals(new2EditText.getText().toString())){
					Toast.makeText(PasswordActivity.this, "�����������벻һ��", Toast.LENGTH_SHORT).show();
				}else if(!password.equals(oldEditText.getText().toString())){
					Toast.makeText(PasswordActivity.this, "ԭ�������", Toast.LENGTH_SHORT).show();
				}else{
					SharedPreferences.Editor editor=pref.edit();
					editor.putString("password", newEditText.getText().toString());
					editor.commit();
					AlertDialog.Builder builder = new AlertDialog.Builder(PasswordActivity.this);
					builder.setTitle("�޸�����ɹ�");
					builder.setIcon(android.R.drawable.ic_dialog_info);
					builder.setPositiveButton("ȷ��", null);
					builder.show();
					Toast.makeText(PasswordActivity.this, "�޸�����ɹ���", Toast.LENGTH_SHORT).show();
					Connect.isLogin = false;
					oldEditText.setText("");
					newEditText.setText("");
					new2EditText.setText("");
				}
			}
		});
	}
}
