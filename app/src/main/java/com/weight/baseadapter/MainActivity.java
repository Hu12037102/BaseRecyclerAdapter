package com.weight.baseadapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
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
        StaggeredGridLayoutManager layoutManager =  new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRvData.setLayoutManager(layoutManager);
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

    //    mRvData.postDelayed(mRunnable, 3000);


    }

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            mData.remove(1);
            mData.remove(2);
            mData.remove(3);
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRvData.removeCallbacks(mRunnable);
    }
}