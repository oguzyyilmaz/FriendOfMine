package com.fom.msesoft.fomapplication.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.adapter.DialogPagerAdapter;
import com.fom.msesoft.fomapplication.adapter.MainPagerAdapter;
import com.fom.msesoft.fomapplication.config.Config;
import com.fom.msesoft.fomapplication.extras.CircleTransform;
import com.fom.msesoft.fomapplication.extras.Preferences_;
import com.fom.msesoft.fomapplication.extras.Rotate3dAnimation;
import com.fom.msesoft.fomapplication.gcm.GCMNotificationIntentService;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.fom.msesoft.fomapplication.model.Token;
import com.fom.msesoft.fomapplication.repository.FriendRepository;
import com.fom.msesoft.fomapplication.repository.PersonRepository;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@EActivity(R.layout.activity_main)
@Fullscreen
public class MainActivity extends AppCompatActivity {


    @ViewById(R.id.pager)
    ViewPager viewPager;

    @ViewById(R.id.tab_layout)
    TabLayout tabLayout;

    @Extra("token")
    @Getter
    @Setter
    String token;

    @Extra("isNewLogin")
    boolean isNewLogin;

    @RestService
    PersonRepository personRepository;

    @RestService
    FriendRepository friendRepository;

    @Pref
    Preferences_ preferences;

    @Setter
    @Getter
    String badgeTxt;

    String isNotify = "", uniqueId;
    Dialog dialog;
    int width, height;

    boolean infoClick = true;
    TextView infoEmail;
    TextView infoName;

    RelativeLayout infoLayout;

    CustomPerson person;

    ImageView infoClickView;

    Button button;

    Handler handler;


    @AfterViews
    void afterViews() {
        if (isNewLogin) {
            afterView();
        } else {
            sign();
        }

    }

    void afterView() {
        if(getIntent().getStringExtra("notification") != null)
            isNotify = getIntent().getStringExtra("notification");



        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.main_find_blue));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.profile_button_inducator));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.main_feed_icon));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final MainPagerAdapter adapter = new MainPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(3);
        if (isNotify.equals("notify")) {
            uniqueId = getIntent().getStringExtra("uniqueId");
            notifyDialog();
        }else if(isNotify.equals("activity")){
            viewPager.setCurrentItem(2);
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    tab.setIcon(R.drawable.main_find_blue);
                } else if (tab.getPosition() == 1) {
                    tab.setIcon(R.drawable.profile_button);
                } else if (tab.getPosition() == 2) {
                    tab.setIcon(R.drawable.main_feed_icon2);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    tab.setIcon(R.drawable.main_find_blue_indacotor);
                } else if (tab.getPosition() == 1) {
                    tab.setIcon(R.drawable.profile_button_inducator);
                } else if (tab.getPosition() == 2) {
                    tab.setIcon(R.drawable.main_feed_icon);
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Background
    public void sign() {

        token = personRepository.signIn(preferences.email().get(), preferences.password().get()).getToken();
        postExecute();
    }

    @UiThread
    void postExecute() {
        afterView();
    }



    @Background
    void notifyDialog() {
        person = personRepository.findPersonByUniqueId(uniqueId);
        showDialog();
    }

    @UiThread
    void showDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogEnterAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.degree_dialog);
        // set the custom dialog components - text, image and button
        WindowManager wm = (WindowManager) (this).getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;
        friendWay();
        infoLayout = (RelativeLayout) dialog.findViewById(R.id.infoLayout);
        infoLayout.setVisibility(View.GONE);
        infoLayout.getLayoutParams().height = height;
        infoLayout.getLayoutParams().width = width;

        infoEmail = (TextView) dialog.findViewById(R.id.infoEmail);

        infoName = (TextView) dialog.findViewById(R.id.infoName);

        ImageView infoImage = (ImageView) dialog.findViewById(R.id.infoImage);
        ImageView infoImageBack = (ImageView) dialog.findViewById(R.id.infoİmageBack);

        infoImage.getLayoutParams().width = width / 4 - 10;
        infoImage.getLayoutParams().height = width / 4 - 10;

/*
                infoImage.getLayoutParams().width=width/2;
                infoImage.getLayoutParams().height=width/2;
*/
        infoImageBack.getLayoutParams().width = width / 4;
        infoImageBack.getLayoutParams().height = width / 4;


        ViewPager viewPager = (ViewPager) dialog.findViewById(R.id.dialog_pager);
        viewPager.getLayoutParams().height = height / 2;
        viewPager.getLayoutParams().width = width;


        List<String> photos = new ArrayList<String>();
        photos.add(person.getPhoto());
        photos.addAll(person.getPhotoList());
        DialogPagerAdapter dialogPagerAdapter = new DialogPagerAdapter(this, photos);
        viewPager.setAdapter(dialogPagerAdapter);

        infoEmail.setText(person.getEmail());
        infoName.setText(person.getFirstName() + " " + person.getLastName());

        Picasso.with(this)
                .load(person.getPhoto())
                .resize(500, 500)
                .centerCrop()
                .transform(new CircleTransform())
                .into(infoImage);

        final TextView text = (TextView) dialog.findViewById(R.id.dialogText);
        text.setText(person.getFirstName() + " " + person.getLastName());
              /*  final ImageView image = (ImageView) dialog.findViewById(R.id.dialogImg);
                Picasso.with(itemView.getContext())
                        .load(person.getPhoto())
                        .resize(width,height/2)
                        .into(image);*/


        button = (Button) dialog.findViewById(R.id.addFriend);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                addFriend();
                Toast.makeText(MainActivity.this, "İstek gönderildi", Toast.LENGTH_LONG).show();
            }

        });
        final RelativeLayout animLayout = (RelativeLayout) dialog.findViewById(R.id.animLayout);
        final RelativeLayout animLayout2 = (RelativeLayout) dialog.findViewById(R.id.animLayout2);

        animLayout.getLayoutParams().height = height / 10 * 9;
        animLayout.getLayoutParams().width = (width / 8) * 7;

        final TextView dialogTxt = (TextView) dialog.findViewById(R.id.dialogText);
        infoClickView = (ImageView) dialog.findViewById(R.id.infoClickView);
        infoClickView.setImageResource(R.drawable.ic_info_white_48dp);

        handler = new Handler();
        infoClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (infoClick == true) {

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //  image.setVisibility(View.GONE);
                            dialogTxt.setVisibility(View.GONE);
                            infoLayout.setVisibility(View.VISIBLE);
                            Picasso.with(MainActivity.this)
                                    .load(person.getPhoto())
                                    .transform(new CircleTransform())
                                    .into(infoClickView);
                            infoClick = false;
                        }
                    }, 300);

                    applyRotation(animLayout);
                    applyRotation(animLayout2);


                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialogTxt.setVisibility(View.VISIBLE);
                            infoLayout.setVisibility(View.GONE);
                            infoClickView.setImageResource(R.drawable.ic_info_white_48dp);
                            infoClick = true;
                        }
                    }, 300);
                    applyRotation(animLayout);
                    applyRotation(animLayout2);
                }
            }
        });

        dialog.show();
    }

    @Background
    void friendWay() {
        friendRepository.friendWay(token, person.getUniqueId());
    }

    private void applyRotation(RelativeLayout relativeLayout) {
        final Rotate3dAnimation rotation = new Rotate3dAnimation(relativeLayout, 360);
        rotation.applyPropertiesInRotation();
        relativeLayout.startAnimation(rotation);
    }

    @Background
    void addFriend() {
        friendRepository.saveFriend(token, uniqueId);
    }
}