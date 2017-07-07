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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.google.android.gms.common.stats.ConnectionEvent;
import com.squareup.picasso.Picasso;

import lombok.Setter;

public class  FriendViewHolders extends RecyclerView.ViewHolder {

    @Setter
    private CustomPerson person;

    @Setter
    Context context;

    Dialog dialog;

    public ImageView personPhoto;

    public  TextView nameTxt;

    RelativeLayout txtLayout;
    public FriendViewHolders(final View itemView) {
        super(itemView);
        nameTxt = (TextView)itemView.findViewById(R.id.nameTxt);
        personPhoto = (ImageView)itemView.findViewById(R.id.personPhoto);
        txtLayout=(RelativeLayout)itemView.findViewById(R.id.friendLayout);
        personPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(itemView.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogEnterAnimation;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.friend_diaolog);

                WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
                Display display = wm.getDefaultDisplay();
                DisplayMetrics metrics = new DisplayMetrics();
                display.getMetrics(metrics);
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;

                // set the custom dialog components - text, image and button
                ImageView image = (ImageView) dialog.findViewById(R.id.dialogImg);
                Picasso.with(itemView.getContext())
                        .load(person.getPhoto())
                        .resize(width/10*7,width/10*7)
                        .centerCrop()
                        .into(image);
                dialog.show();
            }
        });
    }
}
