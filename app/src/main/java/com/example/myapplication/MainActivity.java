package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private TextView tv1;
    private EditText edit;
    private EditText edit2;
    private String getAccount;
    private String getPwd;
    private SharedPreferences helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_login = findViewById(R.id.button);
        Button btn_resetting = findViewById(R.id.button3);
        Button btn_register = findViewById(R.id.button4);
        edit = findViewById(R.id.editText);
        edit2 = findViewById(R.id.editText2);
        tv1 = findViewById(R.id.textView4);

        getAccount = edit.getText().toString().trim();
        getPwd = edit2.getText().toString().trim();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                helper = getSharedPreferences("text", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = helper.edit();
                editor.putString("name",getAccount);
                editor.putString("pwd",getPwd);
                editor.apply();
//                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
//                startActivity(intent);
//                finish();
                Toast.makeText(MainActivity.this,"write success", Toast.LENGTH_LONG).show();

            }
        });

        btn_resetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ResettingActivity.class);
                startActivityForResult(intent,1);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
//                startActivity(intent);
//                finish();
                helper = getSharedPreferences("text", Context.MODE_PRIVATE);
                String name = helper.getString("name","");
                String pwd = helper.getString("pwd","");
                tv1.setText(getAccount);
//                edit2.setText(getPwd);
                Toast.makeText(MainActivity.this,"read success", Toast.LENGTH_LONG).show();
            }
        });

    }

//     private void saveData(String account,String password){//
//        SharedPreferences helper = getSharedPreferences("text", Context.MODE_WORLD_WRITEABLE);
//        SharedPreferences.Editor editor = helper.edit();
//        editor.putString("name",account);
//        editor.putString("pwd",password);
//        editor.commit();
//        super.onStop();
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 2){

            String content = data.getStringExtra("data");
            tv1.setText(content);
        }

    }

}
