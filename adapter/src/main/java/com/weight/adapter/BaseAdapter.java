package com.weight.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * 作者: 胡庆岭
 * 创建时间: 2021/7/4 9:58
 * 更新时间: 2021/7/4 9:58
 * 描述:
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected final Context mContext;
    protected final List<T> mData;
    private static final int HEAD_VIEW_TYPE = 100;
    private static final int FOOT_VIEW_TYPE = 101;
    private static final int EMPTY_VIEW_TYPE = 102;
    private View mHeadView;
    private View mFootView;
    //默认要不要展示NotDataView
    private boolean isShowDefaultEmptyView= true;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public BaseAdapter(@NonNull Context context, @NonNull List<T> data) {
        this.mContext = context;
        this.mData = data;
    }

    protected boolean isShowDefaultEmptyView() {
        return isShowDefaultEmptyView;
    }

    public void addHeadView(@NonNull View view) {
        this.mHeadView = view;
    }

    public void addFootView(@NonNull View view) {
        this.mFootView = view;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case BaseAdapter.HEAD_VIEW_TYPE:
                viewHolder = new RecyclerView.ViewHolder(mHeadView) {
                };
                break;
            case BaseAdapter.FOOT_VIEW_TYPE:
                viewHolder = new RecyclerView.ViewHolder(mFootView) {
                };
                break;
            case BaseAdapter.EMPTY_VIEW_TYPE:
                viewHolder = new EmptyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_empty_view, parent, false));
                break;
            default:
                viewHolder = onCreateViewHolderChild(parent, viewType);
                break;
        }
        return viewHolder;
    }

    public abstract RecyclerView.ViewHolder onCreateViewHolderChild(@NonNull ViewGroup parent, int viewType);

    protected abstract void onBindViewHolderChild(@NonNull RecyclerView.ViewHolder holder, int position);

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EmptyViewHolder) {
            EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
            if (!isShowDefaultEmptyView) {
                holder.itemView.setVisibility(View.GONE);
                isShowDefaultEmptyView = true;
            } else {
                holder.itemView.setVisibility(View.VISIBLE);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onClickEmptyView(v);
                    }
                }
            });

        } else {
            if (mHeadView!=null){
                position--;
            }
            clickItemViewHolder(holder,position);
            onBindViewHolderChild(holder, position);

        }
    }

    protected  void clickItemViewHolder(RecyclerView.ViewHolder holder, int position){
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onClickItem(v, position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onLongClickItem(v, position);
                }
                return false;
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        int type = super.getItemViewType(position);
        if (mHeadView != null && position == 0) {
            type = BaseAdapter.HEAD_VIEW_TYPE;
        }
        if (BaseUtils.isEmptyList(mData)) {
            type = BaseAdapter.EMPTY_VIEW_TYPE;
        }
        if (mFootView != null && position == getItemCount() - 1) {
            type = BaseAdapter.FOOT_VIEW_TYPE;
        }
        return type;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (mHeadView != null) {
            count++;
        }
        count = BaseUtils.isEmptyList(mData) ? count + 1 : BaseUtils.getListSize(mData);
        if (mFootView != null) {
            count++;
        }
        Log.w("getItemCount--", count + "--");
        return count;
    }


    public static class EmptyViewHolder extends RecyclerView.ViewHolder {

        private final AppCompatImageView mAivEmpty;
        private final AppCompatTextView mAtvEmpty;

        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
            mAivEmpty = itemView.findViewById(R.id.aiv_empty);
            mAtvEmpty = itemView.findViewById(R.id.atv_empty);
        }
    }

    public interface OnItemClickListener {
        default void onClickItem(@NonNull View view, int position) {
        }

        default void onLongClickItem(@NonNull View view, int position) {
        }

        default void onClickEmptyView(@NonNull View view) {
        }
    }
}
