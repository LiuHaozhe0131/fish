package com.example.myporject;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ModeActivity extends AppCompatActivity {

    private Button easy,medium,hard;
    String detail = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);
        easy = findViewById(R.id.easy);
        medium = findViewById(R.id.medium);
        hard = findViewById(R.id.hard);
        detail = getIntent().getStringExtra("detail");
        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("goldfish".equals(detail)){
                    Intent intent = new Intent(ModeActivity.this,PlayActivity.class);
                    intent.putExtra("num",8);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        });
        //中等
       medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("goldfish".equals(detail)){
                    Intent intent = new Intent(ModeActivity.this,PlayActivity.class);
                    intent.putExtra("num",12);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        });
        //困难
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModeActivity.this,PlayActivity.class);
                intent.putExtra("num",18);
                startActivity(intent);
                finish();
            }
        });
    }
}
