package com.fom.msesoft.fomapplication.adapter;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.extras.CircleTransform;
import com.fom.msesoft.fomapplication.model.CustomFriendRelationship;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.fom.msesoft.fomapplication.model.FriendRelationship;
import com.fom.msesoft.fomapplication.model.Image;
import com.squareup.picasso.Picasso;


public class DialogWayPagerAdapter extends PagerAdapter {

    CustomFriendRelationship[] friendRelationships;
    Context mContext;
    CustomPerson person;
    LayoutInflater mLayoutInflater;

    Dialog dialog;

    int position,width,height;

    public DialogWayPagerAdapter(Context context, CustomFriendRelationship[] friendRelationships, CustomPerson person) {
        mContext = context;
        this.friendRelationships=friendRelationships;
        this.person=person;

        WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return friendRelationships.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        // this.position = position;

        if(!friendRelationships[position].getEndNode().getEmail().equals(person.getEmail())) {
            final View itemView = mLayoutInflater.inflate(R.layout.dialog_way_item1, container, false);


            TextView tv1=(TextView)itemView.findViewById(R.id.dialog_way_text1);
            TextView tv2=(TextView)itemView.findViewById(R.id.dialog_way_text2);
            ImageView firstDegree = (ImageView) itemView.findViewById(R.id.first_degree);
            ImageView secondDegree = (ImageView) itemView.findViewById(R.id.second_degree);
            ImageView topArrow = (ImageView) itemView.findViewById(R.id.item1_top_arrow);
            ImageView bottomArrow = (ImageView) itemView.findViewById(R.id.item1_bottom_arrow);
            ImageView centerArrow = (ImageView) itemView.findViewById(R.id.item1_center_arrow);

            topArrow.getLayoutParams().height=height/10;
            bottomArrow.getLayoutParams().height=height/10;
            centerArrow.getLayoutParams().width=width/4;

            tv1.setText(friendRelationships[position].getStartNode().getFirstName()+" "+friendRelationships[position].getStartNode().getLastName());
            tv2.setText(friendRelationships[position].getEndNode().getFirstName()+" "+friendRelationships[position].getEndNode().getLastName());


            Picasso.with(itemView.getContext())
                    .load(friendRelationships[position].getStartNode().getPhoto())
                    .resize(height/10,height/10)
                    .transform(new CircleTransform())
                    .into(firstDegree);
            Picasso.with(itemView.getContext())
                    .load(friendRelationships[position].getEndNode().getPhoto())
                    .resize(height/10,height/10)
                    .transform(new CircleTransform())
                    .into(secondDegree);
            firstDegree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                            .load(friendRelationships[position].getStartNode().getPhoto())
                            .resize(300,300)
                            .into(photosView);


                    Button addPhotoButton = (Button) dialog.findViewById(R.id.add_photo);
                    Button deleteButton = (Button) dialog.findViewById(R.id.delete_photo);
                    addPhotoButton.setVisibility(View.GONE);
                    deleteButton.setVisibility(View.GONE);
                    RelativeLayout footer = (RelativeLayout)dialog.findViewById(R.id.footer);
                    footer.setVisibility(View.GONE);
                    dialog.show();
                }
            });
            secondDegree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                            .load(friendRelationships[position].getEndNode().getPhoto())
                            .resize(300,300)
                            .into(photosView);


                    Button addPhotoButton = (Button) dialog.findViewById(R.id.add_photo);
                    Button deleteButton = (Button) dialog.findViewById(R.id.delete_photo);
                    addPhotoButton.setVisibility(View.GONE);
                    deleteButton.setVisibility(View.GONE);
                    RelativeLayout footer = (RelativeLayout)dialog.findViewById(R.id.footer);
                    footer.setVisibility(View.GONE);
                    dialog.show();
                }
            });
            container.addView(itemView);

            return itemView;
        }else {
            final View itemView = mLayoutInflater.inflate(R.layout.dialog_way_item2, container, false);
            ImageView topArrow=(ImageView) itemView.findViewById(R.id.item2_top_arrow);
            ImageView bottomArrow=(ImageView) itemView.findViewById(R.id.item2_bottom_arrow);
            TextView firstFriend = (TextView)itemView.findViewById(R.id.first_friend_text);
            ImageView firstFriendView = (ImageView)itemView.findViewById(R.id.first_friend);

            firstFriend.setText(friendRelationships[position].getStartNode().getFirstName()+" "+friendRelationships[position].getStartNode().getLastName());
            topArrow.getLayoutParams().height=height/10;
            topArrow.getLayoutParams().width=width/10;
            bottomArrow.getLayoutParams().width=width/10;
            bottomArrow.getLayoutParams().height=height/10;

            Picasso.with(itemView.getContext())
                    .load(friendRelationships[position].getStartNode().getPhoto())
                    .resize(height/10,height/10)
                    .transform(new CircleTransform())
                    .into(firstFriendView);
            container.addView(itemView);



            firstFriendView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                    //
                    final ImageView photosView = (ImageView) dialog.findViewById(R.id.photos_dialog);
                    Picasso.with(itemView.getContext())
                            .load(friendRelationships[position].getStartNode().getPhoto())
                            //.resize(300,300)
                            .into(photosView);


                    Button addPhotoButton = (Button) dialog.findViewById(R.id.add_photo);
                    Button deleteButton = (Button) dialog.findViewById(R.id.delete_photo);
                    addPhotoButton.setVisibility(View.GONE);
                    deleteButton.setVisibility(View.GONE);
                    RelativeLayout footer = (RelativeLayout)dialog.findViewById(R.id.footer);
                    footer.setVisibility(View.GONE);
                    dialog.show();

                }
            });
            return itemView;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}