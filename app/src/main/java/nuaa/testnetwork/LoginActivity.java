package nuaa.testnetwork;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;



import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nuaa.adapter.UserAdapter;
import nuaa.util.NetworkUtils;

/**
 * Created by Administrator on 2017/11/2.
 */

public class LoginActivity extends AppCompatActivity {
    private ListView list;
    private UserAdapter adapter;
    private List<Map<String, Object>> allValues = new ArrayList<>();
    private Handler handler;//Handler主要用于异步消息的处理：当发出一个消息之后，首先进入一个消息队列，发送消息的函数即刻返回，而另外一个部分在消息队列中逐一将消息取出，然后对消息进行处理，也就是发送消息和接收消息不是同步的处理

    private Button loginBtn;
    private TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = (Button)findViewById(R.id.xuehao_sign_in_button);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(in);
            }
        });

        signup = (TextView)findViewById(R.id.sign);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(in);
            }
        });

//        list = (ListView)findViewById(R.id.login_form);


//        handler = new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                if(msg.what==0) {
//                    adapter = new UserAdapter(LoginActivity.this, allValues);
//                    list.setAdapter(adapter);
//                }
//            }
//        };

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("信息提示");
        dialog.setMessage("数据正在加载，请稍候....");
        dialog.show();

        Thread t = new Thread() {
            @Override
            public void run() {
                loadData();
                handler.sendEmptyMessage(0);
                dialog.dismiss();//新线程执行完之后让progressdialog自动消失
            }
        };
        t.start();

    }


    public void loadData() {
        try {
            JSONArray arr = NetworkUtils.getJSONArrayByURL(NetworkUtils.QUERY_STU_URL);
            for(int i=0;i<arr.length();i++) {
                JSONObject obj = arr.getJSONObject(i);
                Map<String, Object> map = new HashMap<>();
                map.put("id", obj.getInt("id"));
                map.put("pwd", obj.getString("pwd"));
                allValues.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
