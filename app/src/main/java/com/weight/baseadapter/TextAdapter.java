package com.weight.baseadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.weight.adapter.BaseAdapter;

import java.util.List;

/**
 * 作者: 胡庆岭
 * 创建时间: 2021/7/4 20:09
 * 更新时间: 2021/7/4 20:09
 * 描述:
 */
public class TextAdapter extends BaseAdapter<String> {


    public TextAdapter(@NonNull Context context, @NonNull List<String> data) {
        super(context, data);
    }

    @Override
    public TextAdapter.ViewHolder onCreateViewHolderChild(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_text_view, parent, false));
    }

    @Override
    protected void onBindViewHolderChild(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder textHolder = (ViewHolder) holder;
            textHolder.mAtvContent.setText(mData.get(position));
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private final AppCompatTextView mAtvContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mAtvContent = itemView.findViewById(R.id.atv_content);
        }
    }


}
