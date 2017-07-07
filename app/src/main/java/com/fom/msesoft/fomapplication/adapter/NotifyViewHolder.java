package com.fom.msesoft.fomapplication.adapter;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
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
import com.fom.msesoft.fomapplication.config.Config;
import com.fom.msesoft.fomapplication.extras.CircleTransform;
import com.fom.msesoft.fomapplication.extras.Rotate3dAnimation;
import com.fom.msesoft.fomapplication.model.CustomFriendRelationship;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.fom.msesoft.fomapplication.model.Image;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.ViewById;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Setter;

public class NotifyViewHolder extends RecyclerView.ViewHolder {


    @Setter
    private CustomPerson person;

    @Setter
    private CustomPerson mePerson;

    @Setter
    private String token;

    @Setter
    private int position;

    TextView infoEmail;
    TextView infoName;

    private OnNotifyListener onNotifyListener;

    RelativeLayout txtLayout;

    RelativeLayout infoLayout;

    Handler handler;

    CustomFriendRelationship[] friendRelationships;

    Dialog dialog;

    Button button ;

    ImageView infoClickView;

    boolean infoClick=true;

    int width,height;

    public ImageView personPhoto;

    TextView notify;

    RestTemplate restTemplate;

    public NotifyViewHolder(final View itemView, OnNotifyListener onDeleteNotifyListener) {
        super(itemView);
        this.onNotifyListener = onDeleteNotifyListener;


        restTemplate=new RestTemplate();
        notify=(TextView) itemView.findViewById(R.id.notifyTxt);
        txtLayout=(RelativeLayout)itemView.findViewById(R.id.friend_click_layout);
        restTemplate.getMessageConverters().clear();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        personPhoto = (ImageView)itemView.findViewById(R.id.notifyImg);
        ImageView trueView = (ImageView)itemView.findViewById(R.id.true_check);
        trueView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddFriend().execute();
                Toast.makeText(itemView.getContext(),"Arkadaşsınız", Toast.LENGTH_LONG).show();
                NotifyViewHolder.this.onNotifyListener.deleteNotify(position);
                new deleteNotification().execute();
            }
        });
        ImageView falseView = (ImageView)itemView.findViewById(R.id.false_check);
        falseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotifyViewHolder.this.onNotifyListener.deleteNotify(position);
                new deleteNotification().execute();
            }
        });
        txtLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(itemView.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogEnterAnimation;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.degree_dialog);
                // set the custom dialog components - text, image and button
                WindowManager wm = (WindowManager) (itemView.getContext()).getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                DisplayMetrics metrics = new DisplayMetrics();
                display.getMetrics(metrics);
                width = metrics.widthPixels;
                height = metrics.heightPixels;
                new FriendWay().execute();
                infoLayout = (RelativeLayout)dialog.findViewById(R.id.infoLayout);
                infoLayout.setVisibility(View.GONE);
                infoLayout.getLayoutParams().height = height;
                infoLayout.getLayoutParams().width = width;

                infoEmail = (TextView) dialog.findViewById(R.id.infoEmail);

                infoName = (TextView) dialog.findViewById(R.id.infoName);

                ImageView infoImage = (ImageView) dialog.findViewById(R.id.infoImage);
                ImageView infoImageBack = (ImageView) dialog.findViewById(R.id.infoİmageBack);

                infoImage.getLayoutParams().width=width/4-10;
                infoImage.getLayoutParams().height=width/4-10;


                infoImage.getLayoutParams().width=width/2;
                infoImage.getLayoutParams().height=width/2;

                infoImageBack.getLayoutParams().width=width/4;
                infoImageBack.getLayoutParams().height=width/4;


                ViewPager viewPager = (ViewPager)dialog.findViewById(R.id.dialog_pager);
                viewPager.getLayoutParams().height=height/2;
                viewPager.getLayoutParams().width = width;





                List<String> photos=new ArrayList<String>();
                photos.add(person.getPhoto());
                photos.addAll(person.getPhotoList());
                DialogPagerAdapter dialogPagerAdapter = new DialogPagerAdapter(itemView.getContext(),photos);
                viewPager.setAdapter(dialogPagerAdapter);
/*
                infoEmail.setText(person.getEmail());
                infoName.setText(person.getFirstName() + " " + person.getLastName());
*/
                Picasso.with(itemView.getContext())
                        .load(person.getPhoto())
                        .resize(500,500)
                        .centerCrop()
                        .transform(new CircleTransform())
                        .into(infoImage);

                final TextView text = (TextView) dialog.findViewById(R.id.dialogText);
                text.setText(person.getFirstName()+" "+person.getLastName());
              /*  final ImageView image = (ImageView) dialog.findViewById(R.id.dialogImg);
                Picasso.with(itemView.getContext())
                        .load(person.getPhoto())
                        .resize(width,height/2)
                        .into(image);*/


                button = (Button)dialog.findViewById(R.id.addFriend);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        new AddFriend().execute();
                        Toast.makeText(itemView.getContext(),"Arkadaş Eklendi", Toast.LENGTH_LONG).show();
                    }

                });
                final RelativeLayout animLayout = (RelativeLayout)dialog.findViewById(R.id.animLayout);
                final RelativeLayout animLayout2 = (RelativeLayout)dialog.findViewById(R.id.animLayout2);

                animLayout.getLayoutParams().height=height/10*9;
                animLayout.getLayoutParams().width=(width/8)*7;

                final TextView dialogTxt=(TextView)dialog.findViewById(R.id.dialogText);
                infoClickView = (ImageView) dialog.findViewById(R.id.infoClickView);
                infoClickView.setImageResource(R.drawable.ic_info_white_48dp);

                handler = new Handler();
                infoClickView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(infoClick == true){

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //  image.setVisibility(View.GONE);
                                    dialogTxt.setVisibility(View.GONE);
                                    infoLayout.setVisibility(View.VISIBLE);
                                    Picasso.with(itemView.getContext())
                                            .load(person.getPhoto())
                                            .transform(new CircleTransform())
                                            .into(infoClickView);
                                    infoClick = false;
                                }
                            }, 300);

                            applyRotation(animLayout);
                            applyRotation(animLayout2);


                        }
                        else {
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


        });

    }
    private void applyRotation(RelativeLayout relativeLayout)
    {
        final Rotate3dAnimation rotation = new Rotate3dAnimation(relativeLayout,360);
        rotation.applyPropertiesInRotation();
        relativeLayout.startAnimation(rotation);
    }



    private class deleteNotification extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            HashMap<String, Object> urlVariables = new HashMap<String, Object>();
            urlVariables.put("token", token);
            urlVariables.put("uniqueId", person.getUniqueId());
            restTemplate.exchange(Config.ROOT_URL + "friendRelationShip".concat("/deleteNotification?token={token}&uniqueId={uniqueId}"), HttpMethod.GET, null, ((Class<Void>) null), urlVariables);
            return null;
        }
    }


    private class AddFriend extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            HashMap<String, Object> urlVariables = new HashMap<String, Object>();
            urlVariables.put("token", token);
            urlVariables.put("uniqueId",person.getUniqueId());
            restTemplate.exchange(Config.ROOT_URL +"friendRelationShip".concat("/saveFriend?token={token}&uniqueId={uniqueId}"), HttpMethod.POST, null, ((Class<Void> ) null), urlVariables);
            return null;
        }

    }

    private class FriendWay extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            HashMap<String, Object> urlVariables = new HashMap<String, Object>();
            urlVariables.put("startNode", token);
            urlVariables.put("endNode",person.getUniqueId());
            urlVariables.put("length",3);
            friendRelationships=restTemplate.exchange(Config.ROOT_URL +"friendRelationShip".concat("/friendWay?startNode={startNode}&endNode={endNode}&length={length}"), HttpMethod.GET, null, CustomFriendRelationship[].class, urlVariables).getBody();
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ViewPager viewWayPager = (ViewPager)dialog.findViewById(R.id.dialog_way_pager);
//            viewWayPager.getLayoutParams().width = width;

            DialogWayPagerAdapter dialogWayPagerAdapter = new DialogWayPagerAdapter(itemView.getContext(),friendRelationships,person);
            viewWayPager.setAdapter(dialogWayPagerAdapter);
        }

    }
}
