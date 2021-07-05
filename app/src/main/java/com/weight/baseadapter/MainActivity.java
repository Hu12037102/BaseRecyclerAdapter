package com.weight.baseadapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rvData = findViewById(R.id.rv_data);
        rvData.setLayoutManager(new LinearLayoutManager(this));
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            data.add("胡歌：" + i);
        }
        TextAdapter adapter = new TextAdapter(this, data);

        View headView = getLayoutInflater().inflate(R.layout.item_text_view, rvData, false);
        AppCompatTextView atvContent = headView.findViewById(R.id.atv_content);
        atvContent.setText("我的headView");
        adapter.addHeadView(headView);

        View footView = getLayoutInflater().inflate(R.layout.item_text_view, rvData, false);
        AppCompatTextView atvFootContent = footView.findViewById(R.id.atv_content);
        atvFootContent.setText("我的FootView");
        adapter.addFootView(footView);

        rvData.setAdapter(adapter);

        rvData.postDelayed(new Runnable() {
            @Override
            public void run() {
                View footView1 = getLayoutInflater().inflate(R.layout.item_text_view, rvData, false);
                AppCompatTextView atvFootContent1 = footView1.findViewById(R.id.atv_content);
                atvFootContent1.setText("我的FootView------");
             //   adapter.addFootView(footView1);

                data.clear();
                adapter.notifyDataSetChanged();
            }
        }, 2000);

    }
}