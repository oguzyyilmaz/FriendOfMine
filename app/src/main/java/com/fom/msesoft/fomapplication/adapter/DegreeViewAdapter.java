package com.fom.msesoft.fomapplication.adapter;

import android.content.Context;
import android.graphics.PixelFormat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.MaskTransformation;

public class DegreeViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<CustomPerson> itemList;
    private Context context;

    private String token;
    private int position;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private OnLoadMoreListener mOnLoadMoreListener;

    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    RecyclerView recyclerView;

    public DegreeViewAdapter(Context context, List<CustomPerson> itemList, RecyclerView recyclerView, String token) {
        this.itemList = itemList;
        this.context = context;
        this.token = token;
        final GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = gridLayoutManager.getItemCount();
                lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }
    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.degree_listrow, null);
            return new DegreeViewHolders(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_item, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        this.position = position;
        if (holder instanceof DegreeViewHolders) {
            DegreeViewHolders userViewHolder = (DegreeViewHolders) holder;
            userViewHolder.setPerson(itemList.get(position));
            userViewHolder.setToken(token);
            WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;

            display.getMetrics(metrics);
            float density  = context.getResources().getDisplayMetrics().density;
            float dpWidth  = metrics.widthPixels / density;
            int dp =  width/ (int) dpWidth;
            dp*=28;
            width-=dp;

            Picasso.with(context)
                    .load(itemList.get(position).getPhoto())
                    .resize(width/3,width/3)
                    .transform(new MaskTransformation(context, R.drawable.rectangle_soft))
                    .centerCrop()
                    .into(userViewHolder.personPhoto);

        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }


    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    public void setLoaded() {
        isLoading = false;
    }
}
