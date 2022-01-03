package com.example.atm.ui.detail.changeMoney;

import static com.example.atm.MainActivity.acc;
import static com.example.atm.MainActivity.pas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.atm.ui.detail.Remind;
import com.example.atm.ui.detail.function;
import com.example.atm.ui.detail.transfer.Transfer;
import com.example.atm.util.MyAppCompatActivity;
import com.example.atm.util.MyButton;
import com.example.atm.R;

import com.example.atm.util.OperateSql;

public class ChangePassword extends MyAppCompatActivity {

private String password1 = "";

    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        //        设置隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView textView = (TextView) findViewById(R.id.textview);
        EditText editText = (EditText) findViewById(R.id.password);
        MyButton firm = findViewById(R.id.确认);
        MyButton clear = findViewById(R.id.清除);
        MyButton return1 = findViewById(R.id.返回);

//      确认
        firm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().equals("")||editText.getText().toString().equals(pas)){
//                    使用LitePal更新数据
                    if(password1.equals("")){
                        textView.setText("请再次输入新密码");
                        password1 = editText.getText().toString();
                        editText.setText("");
                    }else if(password1.equals(editText.getText().toString())){

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Message message = handler.obtainMessage();
                                    new OperateSql().updatePassword(acc, editText.getText().toString());
                                    password1 = "";
//                                  转到提示成功界面
                                    Intent intent = new Intent(ChangePassword.this, Remind.class);
                                    intent.putExtra("data", "1");
                                    startActivity(intent);
                                    finish();

//                                  发消息通知主线程更新UI
                                    handler.sendMessage(message);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }else if(!password1.equals(editText.getText().toString())){
//                       转到提示错误界面
                        Intent intent = new Intent(ChangePassword.this, Remind.class);
                        intent.putExtra("data", "0");
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
//      清除
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText(null);
            }
        });
//      返回
        return1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangePassword.this, function.class);
                intent.putExtra("data", "0");
                startActivity(intent);
                finish();
            }
        });

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x11:
//                    Toast.makeText(ChangePassword.this, "用户账号不存在", Toast.LENGTH_SHORT).show();
                    break;
                case 0x12:
//                    String ss = (String) msg.obj;
//                    tv_data.setText(ss);
                    break;
            }

        }
    };
}
