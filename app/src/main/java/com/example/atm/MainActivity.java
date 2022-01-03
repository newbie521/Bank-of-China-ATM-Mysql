package com.example.atm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.atm.ui.login.login;
import com.example.atm.util.MyAppCompatActivity;
import com.example.atm.util.OperateSql;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

public class MainActivity extends MyAppCompatActivity {

    public static String  acc;
    public static String pas;
    public static int bal;
    public static int avi_bal;//    当日可用金额
    public static final int avi_money = 10000;
    private String pass_temp=null;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x11:
                    Toast.makeText(MainActivity.this, "用户账号不存在", Toast.LENGTH_SHORT).show();
//                    String s = (String) msg.obj;
//                    tv_data.setText(s);
                    break;
                case 0x12:
//                    String ss = (String) msg.obj;
//                    tv_data.setText(ss);
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

//        设置隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.waiting);
        Button button = (Button) findViewById(R.id.button); // 插卡
        EditText account = (EditText) findViewById(R.id.account); // 卡号
        Button warn = (Button) findViewById(R.id.用户须知); //用户须知

//        初始化数据库
        Button addData= (Button) findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // 创建数据库
//                LitePal.getDatabase();
//                // list表
//
//                DataSupport.deleteAll(Information.class);
//                DataSupport.deleteAll(Acc.class);
//
//                Acc database = new Acc();
//                database.setAccount("08192977");
//                database.setPassword("123456");
//                database.setBalance(50000);
//                database.setAvi_balance(10000);
//                database.save();
//
//
//                Acc database1 = new Acc();
//                database1.setAccount("08192978");
//                database1.setPassword("123456");
//                database1.setBalance(60000);
//                database1.setAvi_balance(10000);
//                database1.save();
//
//                Acc database2 = new Acc();
//                database2.setAccount("08192979");
//                database2.setPassword("123456");
//                database2.setBalance(70000);
//                database2.setAvi_balance(10000);
//                database2.save();
////
//                Information database3 = new Information();
//                database3.setDate("2000");
//                database3.setExchange("00");
//                database3.setType("dsa");
//                database3.save();

            }
        });
//        插入卡
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (account.getText().equals("")) {

                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Message message = handler.obtainMessage();
                                pass_temp = new OperateSql().getPassword(account.getText().toString());
                                if (pass_temp != null) {
                                    acc = account.getText().toString();
                                    pas = pass_temp;
                                    bal = new OperateSql().getBalance(acc);
                                    avi_bal = new OperateSql().getAvi_Balance(acc);
//                                  加载中
//                                  转到输入密码login界面
                                    Intent intent = new Intent(MainActivity.this, login.class);
                                    startActivity(intent);
                                } else {
                                    message.what = 0x11;
                                }
                                // 发消息通知主线程更新UI
                                handler.sendMessage(message);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
//                    List<Acc> some = DataSupport.findAll(Acc.class);
//                    System.out.println(some);
//                    for(Acc book:some) {
//                        if(account.getText().toString().equals(book.getAccount())){
//                            acc = book.getAccount();
//                            pas = book.getPassword();
//                            bal = book.getBalance();
//                            avi_bal = book.getAvi_balance();
//
//                            Log.d("MainActivity", "account " + book.getAccount());
//                            Log.d("MainActivity", "password " + book.getPassword());
//                            Log.d("MainActivity", "balance " + book.getBalance());
//
////                          加载中
////                           转到输入密码login界面
//                            Intent intent = new Intent(MainActivity.this, login.class);
////                           intent.putExtra("password",pas);
//                            startActivity(intent);
//
//                            break;
//                        }else {
//                            Toast.makeText(MainActivity.this, "用户账号不存在", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                }

//            }
        });
//        用户须知
        warn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, Warn.class);
                Intent intent = new Intent(MainActivity.this, Warn.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        EditText account = (EditText) findViewById(R.id.account); // 卡号
        account.setText("");
    }

}