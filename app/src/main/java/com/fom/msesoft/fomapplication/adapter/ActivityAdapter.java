package com.fom.msesoft.fomapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.extras.CircleTransform;
import com.fom.msesoft.fomapplication.model.ActivityModelCustom;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oguz on 9/5/16.
 */
public class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder> {

    private List<ActivityModelCustom> activityCustomList = new ArrayList<>();
    private Context context;
    private int position;
    private String token;
    private CustomPerson customPerson;
    private int howCounter;

    public ActivityAdapter(Context context, List<ActivityModelCustom> activityModelList, String token, CustomPerson customPerson) {
        this.activityCustomList = activityModelList;
        this.context = context;
        this.token = token;
        this.customPerson = customPerson;
    }

    @Override
    public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listrow, null);
        return new ActivityHolder(view);
    }


    @Override
    public void onBindViewHolder(ActivityHolder holder, int position) {
        this.position = position;
        holder.setJoinList(activityCustomList.get(position).getJoinList());
        holder.setHowCounter(activityCustomList.get(position).getJoinList().size());
        if (activityCustomList.get(position).getJoinList().size() > 0 ) {
            holder.whoGoingButton.setText(holder.getHowCounter() + " Kişi Daha Katılıyor");
        }else
            holder.whoGoingButton.setText("İLK SEN KATIL");
        if (activityCustomList.get(position).getJoinList().contains(customPerson.getUniqueId())) {
            holder.joinButton.setBackgroundColor(Color.RED);
            holder.joinButton.setText("VAZGEÇ");
        }

        holder.setActivityModelCustoms(activityCustomList);
        holder.setToken(token);
        holder.setActivityUniqueId(activityCustomList.get(position).getUniqueId());
        holder.setContext(context);
        WindowManager wm = (WindowManager) (context).getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        Picasso.with(context)
                .load(activityCustomList.get(position).getPerson().getPhoto())
                .centerCrop()
                .transform(new CircleTransform())
                .resize(width * 3 / 10, width * 3 / 10)
                .into(holder.ownerPhoto);
        holder.ownerName.setText(activityCustomList.get(position).getPerson().getFirstName() + " " + activityCustomList.get(position).getPerson().getLastName());
        holder.activityName.setText(activityCustomList.get(position).getActivityName());
        holder.whereTxt.setText(activityCustomList.get(position).getActivityPlaces());
        //holder.ownerDescription.setText(activityCustomList.get(position).getActivityDescription());

        holder.dateTxt.setText(activityCustomList.get(position).getActivityDate());

        Picasso.with(context)
                .load(activityCustomList.get(position).getPhotoId())
                .into(holder.activityPhoto);
    }

    @Override
    public int getItemCount() {
        return (null != activityCustomList ? activityCustomList.size() : 0);
    }


}