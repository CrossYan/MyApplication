package com.example.myapplication;

import android.app.AlertDialog;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
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
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertDummyContactWrapper();

    }

    private void insertDummyContactWrapper() {
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showMessageOKCancel("You need to allow access to Memory",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        buttonClick();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    buttonClick();
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "WRITE_EXTERNAL_STORAGE Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    /*
     * Button点击
     */

    private void buttonClick() {

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
                Log.d("msg", "reslut:" + booaccount);

                if (getAccount.equals("") || getPwd.equals("")) {

                    Toast.makeText(MainActivity.this, "账号或密码不能为空", Toast.LENGTH_LONG).show();

                } else if (booaccount || boopwd) {
                    Toast.makeText(MainActivity.this, "账号包含非法字符", Toast.LENGTH_LONG).show();
                } else {

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
                helper = getSharedPreferences("text", Context.MODE_PRIVATE);
                String name = helper.getString("name", "");
                String pwd = helper.getString("pwd", "");
                tv1.setText(getAccount);
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
        SharedPreferences helper = getSharedPreferences("text", Context.MODE_PRIVATE);
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

    public static boolean isSpecialChar(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

}
