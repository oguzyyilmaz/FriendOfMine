package com.fom.msesoft.fomapplication.adapter;

import android.app.Dialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.config.Config;
import com.fom.msesoft.fomapplication.model.ActivityModel;
import com.fom.msesoft.fomapplication.model.ActivityModelCustom;
import com.fom.msesoft.fomapplication.model.ContactModel;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.squareup.picasso.Picasso;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by oguz on 9/5/16.
 */
public class ActivityHolder extends RecyclerView.ViewHolder {

    @Setter
    private ActivityModel activityModel;

    @Getter
    @Setter
    List<String> joinList = new ArrayList<>();

    @Setter
    Context context;

    Dialog dialog;

    public ImageView ownerPhoto;

    public TextView ownerName;

    public TextView ownerDescription;

    public TextView dateTxt;

    public TextView placesTxt;

    public TextView joinTxt;

    public TextView activityName;

    public TextView whereTxt;

    public ImageView activityPhoto;

    ProgressBar progressBar;

    RestTemplate restTemplate;

    @Getter
    @Setter
    List<ActivityModelCustom> activityModelCustoms = new ArrayList<>();

    @Getter
    @Setter
    Map<Integer,ArrayList<String>> activityJoinList;

    List<CustomPerson> joinPersonList=new ArrayList<>();

    @Getter
    @Setter
    private int howCounter;

    @Getter
    @Setter
    private String token;

    @Setter
    @Getter
    private String activityUniqueId;

    List<CustomPerson> customPersons = new ArrayList<CustomPerson>();

    RecyclerView recyclerView;
    public Button joinButton,whoGoingButton;


    public ActivityHolder(final View itemView) {
        super(itemView);
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().clear();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        ownerPhoto = (ImageView) itemView.findViewById(R.id.activity_owner_photo_view);
        ownerName = (TextView) itemView.findViewById(R.id.activity_owner_name_txt);
        whereTxt = (TextView) itemView.findViewById(R.id.where_txt);
        ownerDescription = (TextView) itemView.findViewById(R.id.description);
        dateTxt = (TextView) itemView.findViewById(R.id.activity_date_txt);
        placesTxt = (TextView) itemView.findViewById(R.id.activity_places_txt);
        activityName = (TextView) itemView.findViewById(R.id.activity_name);
        activityPhoto = (ImageView) itemView.findViewById(R.id.activity_photo);
        joinButton = (Button) itemView.findViewById(R.id.join_bttn);
        whoGoingButton = (Button) itemView.findViewById(R.id.who_going_btn);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (joinButton.getText().equals("KATIL")) {
                    joinButton.setBackgroundColor(Color.RED);
                    joinButton.setText("VAZGEÇ");
                    howCounter++;
                    whoGoingButton.setText(howCounter + " KİŞİ DAHA KATILIYOR");
                    new joinActivity().execute();
                } else {
                    joinButton.setBackgroundColor(Color.parseColor("#102E73"));
                    if (howCounter>0){
                        howCounter--;
                        whoGoingButton.setText(howCounter + " KİŞİ DAHA KATILIYOR");
                    }
                    if(howCounter==0) whoGoingButton.setText("İLK SEN KATIL");
                    joinButton.setText("KATIL");

                    new notJoinActivity().execute();

                }
            }
        });
        whoGoingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(itemView.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogEnterAnimation;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.setContentView(R.layout.activity_friend_dialog);
                recyclerView = (RecyclerView)dialog.findViewById(R.id.activity_recycler);
                progressBar = (ProgressBar)dialog.findViewById(R.id.activity_dialog_progress);
                new customPersonList().execute();
                dialog.show();

            }
        });
        activityPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(itemView.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogEnterAnimation;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.setContentView(R.layout.activity_photo);
                ImageView bigPhoto = (ImageView)dialog.findViewById(R.id.activity_big_photo);
                Picasso.with(context)
                        .load(activityModelCustoms.get(getAdapterPosition()).getPhotoId())
                        .centerCrop()
                        .resize(500,800)
                        .into(bigPhoto);
                dialog.show();
            }
        });
    }


    private class customPersonList extends AsyncTask<Void, Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpEntity<List<String>> requestEntity = new HttpEntity<List<String>>(joinList);
            CustomPerson [] customPersonArray = restTemplate.exchange(Config.ROOT_URL + "activity".concat("/joinList"), HttpMethod.POST, requestEntity,CustomPerson[].class).getBody();
            customPersons = Arrays.asList(customPersonArray);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            GridLayoutManager lLayout;
            lLayout = new GridLayoutManager(dialog.getContext(),1);

            recyclerView.setHasFixedSize(true);

            recyclerView.setLayoutManager(lLayout);

            List<ContactModel> contactModels = new ArrayList<>();

            FriendViewAdapter friendViewAdapter = new FriendViewAdapter(dialog.getContext(),customPersons,contactModels);

            recyclerView.setAdapter(friendViewAdapter);

            recyclerView.setItemAnimator(new DefaultItemAnimator());

            progressBar.setVisibility(View.GONE);
        }
    }

    private class joinActivity extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HashMap<String, Object> urlVariables = new HashMap<>();
            urlVariables.put("token", token);
            urlVariables.put("uniqueId", getActivityUniqueId());
            restTemplate.exchange(Config.ROOT_URL + "activity".concat("/join?token={token}&uniqueId={uniqueId}"), HttpMethod.GET, null, ((Class<Void>) null), urlVariables).getBody();

            return null;
        }
    }

    private class notJoinActivity extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            HashMap<String, Object> urlVariables = new HashMap<>();
            urlVariables.put("token", token);
            urlVariables.put("uniqueId", getActivityUniqueId());
            restTemplate.exchange(Config.ROOT_URL + "activity".concat("/notJoin?token={token}&uniqueId={uniqueId}"), HttpMethod.GET, null, ((Class<Void>) null), urlVariables).getBody();
            return null;
        }
    }

}
