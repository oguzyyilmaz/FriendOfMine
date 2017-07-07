package com.fom.msesoft.fomapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.extras.CircleTransform;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.squareup.picasso.Picasso;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by oguz on 04.08.2016.
 */
public class NotifyViewAdapter extends RecyclerView.Adapter<NotifyViewHolder> {


    @Setter
    @Getter
    private OnNotifyListener onNotifyListener;

    private List<CustomPerson> itemList;
    private Context context;

    int position;
    String token;


    public NotifyViewAdapter(Context context, List<CustomPerson> itemList, String token) {
        this.itemList = itemList;
        this.context = context;
        this.token=token;

    }


    @Override
    public NotifyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_list_row, null);

        return new NotifyViewHolder(view,onNotifyListener);
    }

    @Override
    public void onBindViewHolder(NotifyViewHolder holder, int position) {
        this.position = position;
        holder.setPerson(itemList.get(position));
        holder.setToken(token);
        holder.setPosition(position);
        WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        holder.txtLayout.getLayoutParams().height=height/12;
        Picasso.with(context)
                .load(itemList.get(position).getPhoto())
                .resize(width/6,width/6)
                .centerCrop()
                .transform(new CircleTransform())
                .into(holder.personPhoto);
        holder.notify.setText(itemList.get(position).getFirstName()+" "+itemList.get(position).getLastName()+"Arkadaşlık isteği..");
    }

    @Override
    public int getItemCount() {
        return (null != itemList ? itemList.size() : 0);
    }


}