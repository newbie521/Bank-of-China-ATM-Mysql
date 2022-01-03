package com.example.atm.ui.detail;

import static com.example.atm.MainActivity.acc;
import static com.example.atm.MainActivity.bal;
import static com.example.atm.MainActivity.avi_bal;
import static com.example.atm.ui.detail.transfer.Transfer.acc1;
import static com.example.atm.ui.detail.transfer.Transfer.bal1;
import static com.example.atm.MainActivity.avi_money;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.SQLException;
import android.net.Network;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.atm.MainActivity;
import com.example.atm.R;
import com.example.atm.database.Acc;
import com.example.atm.database.Information;
import com.example.atm.ui.detail.changeMoney.ChangePassword;
import com.example.atm.ui.detail.getmoney.GetMoney;
import com.example.atm.ui.detail.seek.Display;
import com.example.atm.ui.detail.transfer.Transfer;
import com.example.atm.util.Fruit;
import com.example.atm.util.OperateSql;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Remind extends AppCompatActivity {
    private String flag =  "0";
    public static List<Fruit> database_tmp = new ArrayList<>();
    private TextView textView;
    private Button return1;
    private Button back_card;
    private Button display;
    private Button print;
    private String data;
    private List<String> record = new ArrayList<>();

    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remind);
//        设置隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        textView = (TextView) findViewById(R.id.textview1);
        return1 = (Button) findViewById(R.id.返回);
        back_card = (Button) findViewById(R.id.退卡);
        display = (Button) findViewById(R.id.显示余额);
        print = (Button) findViewById(R.id.打印凭条);
//        接收数据
        try{
            getData() ;
        }catch(ParseException e){
        }

//        返回
        return1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(data.equals("0")){
                    Intent intent1 = new Intent(Remind.this, ChangePassword.class);
                    startActivity(intent1);
                }else if(data.equals("1")){
                    Intent intent1 = new Intent(Remind.this, function.class);
                    startActivity(intent1);
                }else if(data.equals("2")&&flag.equals("0")){
                    Intent intent1 = new Intent(Remind.this, GetMoney.class);
                    startActivity(intent1);
                }else if(data.equals("2")&&flag.equals("1")){
                    Intent intent1 = new Intent(Remind.this, function.class);
                    startActivity(intent1);
                }else if(data.equals("3")){
                    Intent intent1 = new Intent(Remind.this, function.class);
                    startActivity(intent1);
                }else if(data.equals("4")&&flag.equals("0")){
                    Intent intent1 = new Intent(Remind.this, Transfer.class);
                    startActivity(intent1);
                }else if(data.equals("4")&&flag.equals("1")){
                    Intent intent1 = new Intent(Remind.this, function.class);
                    startActivity(intent1);
                }
                finish();
            }
        });
//        退卡
        back_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Remind.this, MainActivity.class);
                startActivity(intent1);
                finish();
                Toast.makeText(Remind.this, "欢迎下次使用", Toast.LENGTH_SHORT).show();
            }
        });
//        显示余额
        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Remind.this, Display.class);
                startActivity(intent1);
                finish();
            }
        });
//        打印凭条
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Remind.this, Print.class);
                startActivity(intent1);
                finish();
            }
        });

    }
    private void getData()throws ParseException {
        Intent intent = getIntent();
        data = intent.getStringExtra("data");

        if (data.equals("0")){
            textView.setText("提示：操作失败");
        }else if(data.equals("1")){
            textView.setText("提示：操作成功");
        }
        else if(data.equals("2")){
//           取款
            String value = intent.getStringExtra("value");

//          每天可取款值为10000
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
//                        每日限额
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        if(new OperateSql().getLastDate(acc) != null){
                            int x = differentDays(new Date() ,df.parse(new OperateSql().getLastDate(acc))); // 与上次相差几天
                            if(x > 0){
                                avi_bal = 10000;
                                new OperateSql().updateAvi_Balance(acc, avi_bal);
                            }
                        }


                        Message message = handler.obtainMessage();
                        if(Integer.parseInt(value)>bal){
//                            余额不足
                            message.what = 0x11;

                        }else{
                          if(Integer.parseInt(value)>avi_bal){
                              if(Integer.parseInt(value)>avi_money){
//                                  金额超出额度：10000
                                  message.what = 0x12;

                              }else{
//                                  今日可取金额不足
                                  message.what = 0x13;
                              }
                          }else{
                              bal -= Integer.parseInt(value);
                              avi_bal -= Integer.parseInt(value);
                              new OperateSql().updateBalance(acc, bal);
                              new OperateSql().updateAvi_Balance(acc, avi_bal);

//                             获取时间
                              SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                              Date date = new Date();
                              record.add(acc);
                              record.add(formatter.format(date));
                              record.add(value);
                              record.add("取款");
                              new OperateSql().updateRecord(record);
                              record.clear();

                              database_tmp.add(new Fruit(formatter.format(date),value,"取款"));//记录此次操作
                              message.what = 0x14;
                              message.obj = "取款";
                              flag = "1";
                          }
                        }
                        // 发消息通知主线程更新UI
                        handler.sendMessage(message);

                    } catch (SQLException | ParseException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
//            Toast.makeText(Remind.this, Integer.toString( user.getBalance(acc)), Toast.LENGTH_SHORT).show();

//            List<Information> some = DataSupport.findAll(Information.class);
//            for(int i = some.size()-1;i>=0;i--){
//                if(acc.equals(some.get(i).getAccount())){
//                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                    int x = differentDays(new Date() ,df.parse(some.get(i).getDate())); // 与上次相差几天
////                    long time = (new Date().getTime() - df.parse(some.get(i).getDate()).getTime());
////                    String result = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
//                    if(x > 0){
//                        avi_bal = 10000;
//                        Acc database = new Acc();
//                        database.setAvi_balance(avi_bal);
//                        database.updateAll("account = ?", acc);
//                    }
//                    break;
//                }
//            }
        }
        else if(data.equals("3")){
//          存款
            String value = intent.getStringExtra("value");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Message message = handler.obtainMessage();
                        bal += Integer.parseInt(value);
                        new OperateSql().updateBalance(acc,bal);
                        Log.d("ChangePassword", "修改余额:"+acc);

                        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//获取时间
                        Date date = new Date();
                        record.add(acc);
                        record.add(formatter.format(date));
                        record.add(value);
                        record.add("存款");
                        new OperateSql().updateRecord(record);
                        record.clear();
                        database_tmp.add(new Fruit(formatter.format(date),value,"存款"));//记录此次操作

                        message.what = 0x14;
                        message.obj = "存款";
                        // 发消息通知主线程更新UI
                        handler.sendMessage(message);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        else if(data.equals("4")){
//          转账
            String value = intent.getStringExtra("value");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Message message = handler.obtainMessage();
                        if(Integer.parseInt(value)>bal){
//                           余额不足
                            message.what = 0x11;
                        }else{
//                            修改自己账户余额

                            bal -= Integer.parseInt(value);
                            avi_bal -= Integer.parseInt(value);
                            new OperateSql().updateBalance(acc, bal);
                            new OperateSql().updateAvi_Balance(acc, avi_bal);

//                            修改自己账户记录
                            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//获取时间
                            Date date = new Date();
                            record.add(acc);
                            record.add(formatter.format(date));
                            record.add(value);
                            record.add("转出");
                            new OperateSql().updateRecord(record);
                            record.clear();
                            database_tmp.add(new Fruit(formatter.format(date), value,"转出"));//记录此次操作

//                          修改转入账户余额
                            bal1 += Integer.parseInt(value); //新增金额
                            new OperateSql().updateBalance(acc1, bal1);

//                          修改转入账户记录
                            record.add(acc1);
                            record.add(formatter.format(date));
                            record.add(value);
                            record.add("转入");
                            new OperateSql().updateRecord(record);
                            record.clear();

                            flag = "1";
                            message.what = 0x14;
                            message.obj = "转账";
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


    public static int differentDays(Date beforeDate, Date currentDate) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(beforeDate);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(currentDate);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2)   //同一年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)    //闰年
                {
                    timeDistance += 366;
                } else    //不是闰年
                {
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1);
        } else    //不同年
        {
            System.out.println("判断day2 - day1 : " + (day2 - day1));
            return day2 - day1;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x11:
//                   余额不足
                    textView.setText("提示：余额不足");
                    Toast.makeText(Remind.this, "余额不足！", Toast.LENGTH_SHORT).show();
//                    String s = (String) msg.obj;
//                    tv_data.setText(s);
                    break;
                case 0x12:
                    Toast.makeText(Remind.this, "金额超出额度：10000", Toast.LENGTH_SHORT).show();
                    break;
                case 0x13:
                    Toast.makeText(Remind.this, "今日可取金额："+ avi_bal, Toast.LENGTH_SHORT).show();
                    break;
                case 0x14:
                    String s = (String) msg.obj;
                    if(s.equals("取款")){
                        textView.setText("提示：取款成功");
                    }else if(s.equals("存款")){
                        textView.setText("提示：存款成功");
                    }else if(s.equals("转账")){
                        textView.setText("提示：转账成功");
                    }
                    break;
            }
        }
    };

}
