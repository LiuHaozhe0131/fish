package com.example.myporject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends AppCompatActivity implements IFishListener {

    private TextView tvCount;
    RecyclerView goldfish;
    FishAdapter goldFishAdapter;

    private boolean hasCheck = false;//当前是否有翻面
    private Integer integer = -1;//当前是否有翻面
    private int ID = -1;//当前是否有翻面
    private int COUNT = 0;
    List<FishData> goldFishBeanList = new ArrayList<>();
    String sql_count;
    private int num = 8;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        tvCount = findViewById(R.id.tv_count);
        goldfish = findViewById(R.id.goldfish);

        goldFishAdapter = new FishAdapter(PlayActivity.this,this);
        num = getIntent().getIntExtra("num",8);
        if (num == 8){//简单
            goldfish.setLayoutManager(new GridLayoutManager(getApplicationContext(),4));
        }else if (num ==12){//中等
            goldfish.setLayoutManager(new GridLayoutManager(getApplicationContext(),4));
        }else {//困难
            goldfish.setLayoutManager(new GridLayoutManager(getApplicationContext(),4));
        }
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(getApplicationContext(),"inter");
        int current = sharedPreferencesUtils.getInt("integer",-1);
        goldfish.addItemDecoration(new MyItemDecoration(10));
        goldfish.setAdapter(goldFishAdapter);
        if (Utils.goldFishBeanList.size() > 0){
            if (current!= -2){
                integer = current;
                ID = sharedPreferencesUtils.getInt("id",0);
            }
            goldFishBeanList = Utils.goldFishBeanList;
            int n = 0;
            for (int i = 0; i < goldFishBeanList.size(); i++) {
                if (goldFishBeanList.get(i).isCheck()){
                    ++n;
                }
            }
            if (n%2 == 0){
                hasCheck = false;
            }else {
                hasCheck = true;
            }
            COUNT = sharedPreferencesUtils.getInt("count",0);
            tvCount.setText(getString(R.string.count)+COUNT);
        }else {
            tvCount.setText(getString(R.string.count)+COUNT);
            goldFishBeanList = Utils.goldfidhListCopy(num);
        }

        goldFishAdapter.setNewData(goldFishBeanList);
        goldFishAdapter.notifyDataSetChanged();





        String hasSql = getIntent().getStringExtra("has");
        if (TextUtils.isEmpty(hasSql)){
            for (int i = 0; i <goldFishBeanList.size() ; i++) {
                History history = new History();
                history.setDiff(COUNT+"");
                history.setType("goldFish");
                history.setId(goldFishBeanList.get(i).getId());
                history.setPic(goldFishBeanList.get(i).getPic());
                history.setCheck(goldFishBeanList.get(i).isCheck()+"");
                HostoryDBUtils.getInstance(getApplicationContext()).save(history);
            }
        }

    }

    private boolean canCheck = false;//设置图片每隔1秒才可以点击
    @Override
    public void OnFishClick(FishData goldFishBean) {

        if (!canCheck){
            canCheck = true;
            checkImage(goldFishBean);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    canCheck =false;
                }
            },1000);
        }

    }


    private void checkImage(FishData goldFishBean){
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(getApplicationContext(),"inter");

        ++COUNT;
        sharedPreferencesUtils.putInt("count",COUNT);
        tvCount.setText(getString(R.string.count)+COUNT);
        //如果当前首次点击，或者点击不是同一张图片
        //首次点击
        if (!hasCheck && integer != goldFishBean.getPic()){
            //保存当前选择图片id
            hasCheck = true;
            ID = goldFishBean.getId();
            sharedPreferencesUtils.putInt("id",ID);
            integer = goldFishBean.getPic();
            sharedPreferencesUtils.putInt("integer",integer);
            changeGoldFish(goldFishBean);
            return;
        }
        //如果两张图片id一样，翻转并旋转，否则翻转展示不一致之后，还原覆盖
        if ((integer+"").equals(goldFishBean.getPic()+"")){
            integer = -1;
            hasCheck = false;
            sharedPreferencesUtils.putInt("integer",integer);
            sharedPreferencesUtils.putInt("id",ID);
            Toast.makeText(getApplicationContext(),getString(R.string.great),Toast.LENGTH_SHORT).show();
            changeGoldFish1(goldFishBean);
            return;
        }

        notGoldFish(goldFishBean);

    }
    private void changeGoldFish(FishData goldFishBean){
        for (int i = 0; i < goldFishBeanList.size(); i++) {

            if (goldFishBean.getId() == goldFishBeanList.get(i).getId()){
                goldFishBeanList.get(i).setCheck(true);
            }
        }
        goldFishAdapter.setNewData(goldFishBeanList);
        goldFishAdapter.notifyDataSetChanged();
        changSql(goldFishBean);
    }

    private void changSql(FishData goldFishBean){
        List<History> historyList = HostoryDBUtils.getInstance(getApplicationContext()).load("goldFish");
        if (historyList.size() ==0){
            return;
        }
        for (int i = 0; i < historyList.size(); i++) {
            if (goldFishBean.getId() == historyList.get(i).getId()){
                historyList.get(i).setCheck("true");
                historyList.get(i).setDiff(COUNT+"");
                HostoryDBUtils.getInstance(getApplicationContext()).change(getApplicationContext(),historyList.get(i));
            }
        }
    }
    private void changeGoldFish1(FishData goldFishBean){
        for (int i = 0; i < goldFishBeanList.size(); i++) {
            if (goldFishBean.getId() == goldFishBeanList.get(i).getId()){
                goldFishBeanList.get(i).setCheck(true);
            }
        }

        goldFishAdapter.setNewData(goldFishBeanList);
        goldFishAdapter.notifyDataSetChanged();
        changSql(goldFishBean);
        int  has = 0;
        for (int i = 0; i < goldFishBeanList.size(); i++) {
            //如果全部翻完
            if (goldFishBeanList.get(i).isCheck()){
                ++has;
            }
        }
        Log.d("shuliang",has+"");
        //点击完
        if (has == goldFishBeanList.size()){
            showTips();
        }
    }

    //完成提示
    private void showTips(){
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(getApplicationContext(),"user");
        int name = sharedPreferencesUtils.getInt("u",0);
        int fen = sharedPreferencesUtils.getInt("fen",0);

        if (name ==0){
            showNormalDialog();
            return;
        }
        int user_count = fen;
        if ( COUNT < user_count){
            showNormalDialog();
            return;
        }
        if (user_count > COUNT){
            showMorelDialog();
            return;
        }

        showMorelDialog();

    }

    private void inputData(){
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(getApplicationContext(),"user");
        sharedPreferencesUtils.putInt("u",10);
        sharedPreferencesUtils.putInt("fen",COUNT);
        delete();
    }

    //赢了提示
    private void showNormalDialog(){

        final AlertDialog.Builder normalDialog =  new AlertDialog.Builder(PlayActivity.this);
        normalDialog.setTitle(getString(R.string.hint));
        normalDialog.setMessage(getString(R.string.win) +","+getString(R.string.count)+COUNT  );
        normalDialog.setPositiveButton(getString(R.string.ok),  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do

                inputData();
            }
        });
        normalDialog.setNegativeButton(getString(R.string.cancel),  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do
                inputData();
            }
        });
        // 显示
        normalDialog.show();
    }

    //赢了且比以前分数更高
    private void showMorelDialog(){

        final AlertDialog.Builder normalDialog =  new AlertDialog.Builder(PlayActivity.this);
        normalDialog.setTitle(getString(R.string.hint) );
        normalDialog.setMessage(getString(R.string.win) +","+getString(R.string.count)+COUNT  );
        normalDialog.setPositiveButton(getString(R.string.ok),  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do
                inputData();
            }
        });
        normalDialog.setNegativeButton(getString(R.string.cancel),  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do
                inputData();

            }
        });
        // 显示
        normalDialog.show();
    }
    private void notGoldFish(FishData goldFishBean){
        for (int i = 0; i < goldFishBeanList.size(); i++) {
            if (goldFishBean.getId() == goldFishBeanList.get(i).getId()){
                goldFishBeanList.get(i).setCheck(true);
            }
        }
        goldFishAdapter.setNewData(goldFishBeanList);
        goldFishAdapter.notifyDataSetChanged();
        goldfish.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < goldFishBeanList.size(); i++) {
                    if (goldFishBean.getId() == goldFishBeanList.get(i).getId()){
                        goldFishBeanList.get(i).setCheck(false);
                    }
                }
                goldfish.setEnabled(true);
                goldFishAdapter.setNewData(goldFishBeanList);
                goldFishAdapter.notifyDataSetChanged();
            }
        },800);

    }

    private void delete(){
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(getApplicationContext(),"inter");
        sharedPreferencesUtils.putInt("integer",-2);
        sharedPreferencesUtils.putInt("count",0);
        List<History> historyList = HostoryDBUtils.getInstance(getApplicationContext()).load("goldFish");
        if (historyList.size() > 0){
            for (int i = 0; i < historyList.size(); i++) {
                HostoryDBUtils.getInstance(getApplicationContext()).delete(getApplicationContext(),historyList.get(i).getUid()+"");
            }
        }
        finish();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.goldFishBeanList.clear();
    }
}

