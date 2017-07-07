package com.fom.msesoft.fomapplication.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.squareup.picasso.Picasso;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by oguz on 04.08.2016.
 */


public class ProfilePhotoHolder extends RecyclerView.ViewHolder{

    @Setter
    private OnUploadPhotoListener onUploadPhotoListener;

    @Setter
    private OnDeletePhotoListener onDeletePhotoListener;

    @Setter
    @Getter
    private CustomPerson person;

    @Setter
    private int position;

    @Setter
    private String url;

    @Setter
    private List<String> itemList;

    @Setter
    private int itemlistSize;

    @Setter
    private int itemPosition;

    public ImageView personPhoto;

    Context mContext;

    Dialog dialog;


    public ProfilePhotoHolder(final View itemView, final Context context, OnUploadPhotoListener onUploadPhotoListener, final CustomPerson customPerson,OnDeletePhotoListener onDeletePhotoListener) {
        super(itemView);
        mContext = context;
        this.person = customPerson;
        setOnUploadPhotoListener(onUploadPhotoListener);
        setOnDeletePhotoListener(onDeletePhotoListener);
        this.personPhoto = (ImageView) itemView.findViewById(R.id.photo);
       itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(position);
               if (position == itemlistSize-1) {
                    ProfilePhotoHolder.this.onUploadPhotoListener.onUploadPhotoClick();
                  }
                else {

                    dialog = new Dialog(itemView.getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogEnterAnimation;
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setContentView(R.layout.profile_photos_dialog);
                    // set the custom dialog components - text, image and button
                    WindowManager wm = (WindowManager) (itemView.getContext()).getSystemService(Context.WINDOW_SERVICE);
                    Display display = wm.getDefaultDisplay();
                    DisplayMetrics metrics = new DisplayMetrics();
                    display.getMetrics(metrics);
                    int width = metrics.widthPixels;
                    int height = metrics.heightPixels;

                   final ImageView photosView = (ImageView) dialog.findViewById(R.id.photos_dialog);
                   Picasso.with(itemView.getContext())
                           .load(url)
                           .centerCrop()
                           .resize(width*9/10,width*9/10)
                            .into(photosView);


                   Button addPhotoButton = (Button) dialog.findViewById(R.id.add_photo);
                   Button deleteButton = (Button) dialog.findViewById(R.id.delete_photo);
                   deleteButton.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           System.out.println(itemPosition);
                           ProfilePhotoHolder.this.onDeletePhotoListener.onDeletePhotoClick(itemList.get(position),position);
                           dialog.dismiss();
                       }
                   });
                   addPhotoButton.setVisibility(View.GONE);
                   dialog.show();
                }

            }
        });
    }

}
