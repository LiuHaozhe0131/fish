package com.example.myporject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Button play;
    List<String> stringList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play = findViewById(R.id.play);

        //开始游戏
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.goldFishBeanList.clear();
                List<History> historyList = HostoryDBUtils.getInstance(getApplicationContext()).load("goldFish");
                if (historyList.size() > 0) {
                    for (int i = 0; i < historyList.size(); i++) {
                        FishData goldFishBean = new FishData();
                        goldFishBean.setId(historyList.get(i).getId());
                        goldFishBean.setPic(historyList.get(i).getPic());
                        if ("true".equals(historyList.get(i).getCheck())) {
                            goldFishBean.setCheck(true);
                        } else {
                            goldFishBean.setCheck(false);
                        }
                        Utils.goldFishBeanList.add(goldFishBean);
                    }
                    Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                    intent.putExtra("num", 8);
                    intent.putExtra("has", "has");
                    intent.putExtra("count", historyList.get(0).getDiff());
                    startActivity(intent);
                    return;
                }
                Intent intent = new Intent(MainActivity.this,ModeActivity.class);
                intent.putExtra("detail","goldfish");
                startActivity(intent);
            }
        });

        getGoldFish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.change:
                String sta= getResources().getConfiguration().locale.getLanguage();
                LangugeUtils.translateText(MainActivity.this,sta);
                break;
        }
        return true;
    }

    /**
     * 获取金鱼图片
     */
    public void getGoldFish(){
        final OkHttpClient client = new OkHttpClient();
        final Request request=new Request.Builder()
                .get()
                .tag(this)
                .url(Utils.base_goldfish)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String data  = response.body().string();
                        Fish goldFishBean = NetUtils.getInstance().fromJson(data, Fish.class);
                        if (goldFishBean !=null && goldFishBean.getPictureset()!=null){
                            for (int i = 0; i < goldFishBean.getPictureset().size(); i++) {
                                stringList.add(Utils.goldfish+goldFishBean.getPictureset().get(i));
                            }
                        }
                    } else {
                        throw new IOException("Unexpected code " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}