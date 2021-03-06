package com.weight.baseadapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextAdapter mAdapter;
    private List<String> mData;
    private RecyclerView mRvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRvData = findViewById(R.id.rv_data);
        mRvData.setLayoutManager(new LinearLayoutManager(this));
        mData = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            mData.add("胡歌：" + i);
        }
        mAdapter = new TextAdapter(this, mData);

        View headView = LayoutInflater.from(this).inflate(R.layout.item_text_view, mRvData, false);
        AppCompatTextView atvContent = headView.findViewById(R.id.atv_content);
        atvContent.setText("我的headView");
        mAdapter.addHeadView(headView);

        View footView = LayoutInflater.from(this).inflate(R.layout.item_text_view, mRvData, false);
        AppCompatTextView atvFootContent = footView.findViewById(R.id.atv_content);
        atvFootContent.setText("我的FootView");
        mAdapter.addFootView(footView);

        mRvData.setAdapter(mAdapter);

        mRvData.postDelayed(mRunnable, 3000);

    }

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mAdapter.removeFootView();
            View footView1 = getLayoutInflater().inflate(R.layout.item_text_view, mRvData, false);
            AppCompatTextView atvFootContent1 = footView1.findViewById(R.id.atv_content);
            atvFootContent1.setText("我的FootView------");
            mAdapter.addFootView(footView1);

            mData.clear();
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRvData.removeCallbacks(mRunnable);
    }
}