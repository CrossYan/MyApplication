package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private TextView tv1;
    private EditText edit;
    private EditText edit2;
    private Button btn_login;
    private Button btn_resetting;
    private Button btn_register;
    private Button btn_clear;
    private CheckBox check;
    private String getAccount;
    private String getPwd;
    private SharedPreferences helper;
    private SharedPreferences.Editor editor;
    private Handler mHander;
    private Runnable mRunnable;
    private boolean booaccount;
    private boolean boopwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_login = findViewById(R.id.button);
        btn_resetting = findViewById(R.id.button3);
        btn_register = findViewById(R.id.button4);
        btn_clear = findViewById(R.id.button7);

        edit = findViewById(R.id.editText);
        edit2 = findViewById(R.id.editText2);
        tv1 = findViewById(R.id.textView4);

        helper = getSharedPreferences("text", Context.MODE_PRIVATE);
        editor = helper.edit();

        InitConfig();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getAccount = edit.getText().toString();
                getPwd = edit2.getText().toString();

                booaccount = isSpecialChar(getAccount);
                boopwd = isSpecialChar(getPwd);
                Log.d("msg","reslut:" + booaccount);

                if (getAccount.equals("") || getPwd.equals("")) {

                    Toast.makeText(MainActivity.this, "账号或密码不能为空", Toast.LENGTH_LONG).show();

                }else if(booaccount || boopwd){
                    Toast.makeText(MainActivity.this, "账号包含非法字符", Toast.LENGTH_LONG).show();
                }else{

                    saveData(getAccount, getPwd);
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                }

            }
        });

        btn_resetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,ResettingActivity.class);
////                startActivityForResult(intent,1);

                String studentNumber = helper.getString("name", null);
                String passWord = helper.getString("pwd", null);
                if (studentNumber == null) {
                    Toast.makeText(MainActivity.this, "当前没有学生记录请添加！", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "学号：" + studentNumber + "\n" + "密码：" + passWord, Toast.LENGTH_LONG).show();
                }


            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
//                startActivity(intent);
//                finish();
                helper = getSharedPreferences("text", Context.MODE_PRIVATE);
                String name = helper.getString("name", "");
                String pwd = helper.getString("pwd", "");
                tv1.setText(getAccount);
//                edit2.setText(getPwd);
                Toast.makeText(MainActivity.this, "read success", Toast.LENGTH_LONG).show();
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit.setText("");
                edit2.setText("");
                helper = getSharedPreferences("text", Context.MODE_PRIVATE);
                editor.clear();
                editor.commit();
            }
        });

    }

    private void saveData(String account, String password) {//
        SharedPreferences helper = getSharedPreferences("text", Context.MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = helper.edit();
        editor.putString("name", account);
        editor.putString("pwd", password);
        editor.commit();
        super.onStop();
    }

    private void InitConfig() {
        helper = getSharedPreferences("text", Context.MODE_PRIVATE);
        edit.setText(helper.getString("name", ""));
        edit2.setText(helper.getString("pwd", ""));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 2) {

            String content = data.getStringExtra("data");
            tv1.setText(content);
        }

    }

    private static boolean checkAccountMark(String account){
        String all = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$";
        Pattern pattern = Pattern.compile(all);
        return pattern.matches(all,account);
    }

    public static boolean isSpecialChar(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

}
