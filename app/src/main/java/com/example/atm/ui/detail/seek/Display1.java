package com.example.atm.ui.detail.seek;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.atm.MainActivity.acc;

import com.example.atm.ui.detail.Remind;
import com.example.atm.ui.detail.changeMoney.ChangePassword;
import com.example.atm.ui.detail.function;
import com.example.atm.MainActivity;
import com.example.atm.R;
import com.example.atm.database.Information;
import com.example.atm.util.Fruit;
import com.example.atm.util.FruitAdapter;
import com.example.atm.util.MyAppCompatActivity;
import com.example.atm.util.OperateSql;

public class Display1 extends MyAppCompatActivity {
    private List<Fruit> fruitList = new ArrayList<>();
    FruitAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display1);
//        设置隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        adapter = new FruitAdapter(Display1.this, R.layout.fruit_item, fruitList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter) ;
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("交易明细查询");
        Button return1 = (Button)findViewById(R.id.返回);
        Button back_card = (Button) findViewById(R.id.退卡);
//      ListView 数据获取

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Message message = handler.obtainMessage();
                    List<String> record = new ArrayList<>();
                    HashMap<Integer, List<String>> map = new OperateSql().getRecord(acc);
                    if(map != null){
                        for(int i=0;i<map.size();i++){
                            record = map.get(new Integer(i));
                            Fruit apple = new Fruit(record.get(0), record.get(1),record.get(2));
                            System.out.println("apple:" + apple);

                            System.out.println("map:" + record);
                            fruitList.add(apple);
                            message.what = 0x11;
                        }
                    }
//                  发消息通知主线程更新UI
                    handler.sendMessage(message);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();


//       返回
        return1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Display1.this, function.class);
                startActivity(intent);
                finish();
            }
        });
//       退卡
        back_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Display1.this, MainActivity.class);
                startActivity(intent1);
                finish();
                Toast.makeText(Display1.this, "欢迎下次使用", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x11:
//                    通知Adapter更新
                      adapter.notifyDataSetChanged();
                    break;
                case 0x12:
//                    String ss = (String) msg.obj;
//                    tv_data.setText(ss);
                    break;
            }

        }
    };
}
