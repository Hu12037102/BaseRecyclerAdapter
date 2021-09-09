package com.weight.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
    //默认要不要展示NotDataView
    private boolean isShowDefaultEmptyView = true;
    private boolean mHasHeadView;
    private boolean mHasFootView;
    private ConstraintLayout mClFootParent;
    private ConstraintLayout mClHeadParent;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public BaseAdapter(@NonNull Context context, @NonNull List<T> data) {
        this.mContext = context;
        this.mData = data;
        initHeadFootParent();
    }

    protected void initHeadFootParent() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mClHeadParent = new ConstraintLayout(mContext);
        mClHeadParent.setLayoutParams(params);
        mClFootParent = new ConstraintLayout(mContext);
        mClFootParent.setLayoutParams(params);
    }

    protected boolean isShowDefaultEmptyView() {
        return isShowDefaultEmptyView;
    }

    public void addHeadView(@NonNull View view) {
        //  removeAllView(mClHeadParent);
        mClHeadParent.addView(view);
        mHasHeadView = true;
    }

    public void removeHeadView() {
        removeAllView(mClHeadParent);
        mHasHeadView = false;
    }

    public void addFootView(@NonNull View view) {
        // removeAllView(mClFootParent);
        mClFootParent.addView(view);
        mHasFootView = true;
    }

    public void removeFootView() {
        removeAllView(mClFootParent);
        mHasFootView = false;
    }

    private void removeAllView(ViewGroup viewGroup) {
        if (viewGroup != null && viewGroup.getChildCount() > 0) {
            viewGroup.removeAllViews();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.w("BaseAdapter--", "onCreateViewHolder:" + parent + "--");
        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case BaseAdapter.HEAD_VIEW_TYPE:
                viewHolder = new RecyclerView.ViewHolder(mClHeadParent) {
                };
                break;
            case BaseAdapter.FOOT_VIEW_TYPE:
                viewHolder = new RecyclerView.ViewHolder(mClFootParent) {
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
        Log.w("BaseAdapter--", "onBindViewHolder:" + holder + "--" + position);
        if (holder instanceof EmptyViewHolder) {
            EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
            if (!isShowDefaultEmptyView) {
                holder.itemView.setVisibility(View.GONE);
                isShowDefaultEmptyView = true;
            } else {
                holder.itemView.setVisibility(View.VISIBLE);
            }
            holder.itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onClickEmptyView(v);
                }
            });

        } else if (getItemViewType(position) == 0) {
            if (mHasHeadView) {
                position--;
            }
            clickItemViewHolder(holder, position);
            onBindViewHolderChild(holder, position);

        }
    }

    protected void clickItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClickItem(v, position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onLongClickItem(v, position);
                }
                return false;
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        int type;
        if (mHasHeadView && position == 0) {
            type = BaseAdapter.HEAD_VIEW_TYPE;
        } else if (mHasFootView && position == getItemCount() - 1) {
            type = BaseAdapter.FOOT_VIEW_TYPE;
        } else if (BaseUtils.isEmptyList(mData)) {
            type = BaseAdapter.EMPTY_VIEW_TYPE;
        } else {
            type = super.getItemViewType(position);
        }
        Log.w("BaseAdapter--", "getItemViewType:" + type + "--" + position);
        return type;
    }

    @Override
    public long getItemId(int position) {
        Log.w("BaseAdapter--", "getItemId:" + position + "--");
        return position;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (mHasHeadView) {
            count++;
        }
        count = BaseUtils.isEmptyList(mData) ? count + 1 : count + BaseUtils.getListSize(mData);
        if (mHasFootView) {
            count++;
        }
        Log.w("BaseAdapter--", "getItemCount:" + count + "--");
        return count;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        Log.w("BaseAdapter--", "onViewAttachedToWindow:" + holder + "--");
        View view = holder.itemView;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        int itemType = holder.getItemViewType();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
            params.setFullSpan((mHasHeadView && itemType == BaseAdapter.HEAD_VIEW_TYPE) || (mHasFootView && itemType == BaseAdapter.FOOT_VIEW_TYPE));
            view.setLayoutParams(params);
        } /*else if (layoutParams instanceof GridLayoutManager.LayoutParams) {
            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
            GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) layoutParams;
            params.width = (mHasHeadView && itemType == BaseAdapter.HEAD_VIEW_TYPE) || (mHasFootView && itemType == BaseAdapter.FOOT_VIEW_TYPE) ? metrics.widthPixels : ViewGroup.LayoutParams.MATCH_PARENT;
            view.setLayoutParams(params);
        }*/
    }

    private boolean isHeadViewType(int itemType) {
        return itemType == BaseAdapter.HEAD_VIEW_TYPE;
    }

    private boolean isFootViewType(int itemType) {
        return itemType == BaseAdapter.FOOT_VIEW_TYPE;
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        Log.w("BaseAdapter--", "onViewDetachedFromWindow:" + holder + "--");
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull RecyclerView.ViewHolder holder) {
        Log.w("BaseAdapter--", "onFailedToRecycleView:" + holder + "--");
        return super.onFailedToRecycleView(holder);

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    Log.w("getSpanSize--", type + "--"+gridLayoutManager.getSpanCount());
                    return isFootViewType(type) || isHeadViewType(type) ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
        Log.w("BaseAdapter--", "onAttachedToRecyclerView:" + recyclerView + "--");
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Log.w("BaseAdapter--", "onDetachedFromRecyclerView:" + recyclerView + "--");
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        Log.w("BaseAdapter--", "onViewRecycled:" + holder + "--");
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
