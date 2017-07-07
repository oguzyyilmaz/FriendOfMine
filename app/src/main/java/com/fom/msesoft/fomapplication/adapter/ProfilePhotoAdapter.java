package com.fom.msesoft.fomapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.activity.MainActivity;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.squareup.picasso.Picasso;

import org.androidannotations.rest.spring.annotations.Get;

import java.util.List;

import jp.wasabeef.picasso.transformations.MaskTransformation;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by oguz on 04.08.2016.
 */
public class ProfilePhotoAdapter extends RecyclerView.Adapter<ProfilePhotoHolder> {

    private List<String> itemList;
    private Context context;

    int position;

    CustomPerson customPerson;

    @Setter
    @Getter
    private OnUploadPhotoListener onUploadPhotoListener;

    @Setter
    @Getter
    private OnDeletePhotoListener onDeletePhotoListener;


    public ProfilePhotoAdapter(Context context, List<String> itemList,CustomPerson customPerson) {
        this.itemList = itemList;
        this.context = context;
        this.customPerson = customPerson;
        System.out.println(itemList.size());

    }


    @Override
    public ProfilePhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_photo_listrow, null);
        ProfilePhotoHolder viewHolder = new ProfilePhotoHolder(view,context,onUploadPhotoListener, customPerson,onDeletePhotoListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProfilePhotoHolder holder, int position) {
        this.position = position;

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
        if(position!=itemList.size()-1){
        Picasso.with(context)
                .load(itemList.get(position))
                .centerCrop()
                .transform(new MaskTransformation(context, R.drawable.rectangle_3))
                .resize(width/3,width/3)
                .into(holder.personPhoto);
    }
    else {
        Picasso.with(context)
                .load(R.drawable.add_photo)
                .resize(width/3,width/3)
                .transform(new MaskTransformation(context, R.drawable.rectangle_3))
                .centerCrop()
                .into(holder.personPhoto);

    }
        holder.setUrl(itemList.get(position));
        holder.setItemPosition(position);
        holder.setPosition(position);
        holder.setItemlistSize(itemList.size());
        holder.setItemList(itemList);
        System.out.println(position);
    }


    @Override
    public int getItemCount() {
        return (null != itemList ? itemList.size() : 0);
    }

}