package com.fom.msesoft.fomapplication.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fom.msesoft.fomapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.MaskTransformation;


public class DialogPagerAdapter extends PagerAdapter {

    List<String> photo=new ArrayList<String>();
    Context mContext;
    LayoutInflater mLayoutInflater;

    public DialogPagerAdapter(Context context, List<String> photo) {
        mContext = context;
        this.photo=photo;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return photo.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.dialog_pager_item, container, false);

        WindowManager wm = (WindowManager) (itemView.getContext()).getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        ImageView imageView = (ImageView) itemView.findViewById(R.id.dialogImgPager);
        Picasso.with(mContext)
                .load(photo.get(position))
                .resize(height/15*13,height/15*13)
               // .transform(new MaskTransformation(itemView.getContext(),R.drawable.rectangle_2))
                .centerCrop()
                .into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}