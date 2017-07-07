package com.fom.msesoft.fomapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.fom.msesoft.fomapplication.R;

public class LoadingViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;

    public LoadingViewHolder(View itemView) {
        super(itemView);
        progressBar = (ProgressBar) itemView.findViewById(R.id.loadListProgress);
    }
}