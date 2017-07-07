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
import com.fom.msesoft.fomapplication.model.Setting;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
     * Created by oguz on 04.08.2016.
     */
    public class ProfileSettingAdapter extends RecyclerView.Adapter<ProfileSettingHolder> {

        private List<Setting> itemList;
        private Context context;
        private String token;
        int position;


        public ProfileSettingAdapter(Context context, List<Setting> itemList, String token) {
            this.itemList = itemList;
            this.context = context;
            this.token = token;
        }


        @Override
        public ProfileSettingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_setting_listrow, null);

            return new ProfileSettingHolder(view);
        }

        @Override
        public void onBindViewHolder(ProfileSettingHolder holder, int position) {
            this.position = position;
            String settingType = null;

            holder.setContext(context);
            int photo;
            WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;

            holder.txtLayout.getLayoutParams().height = height/12;
            switch (itemList.get(position).getSettingType()){
                case "name":
                    settingType="Name and Lastname";
                    photo= R.drawable.name;
                    break;
                case "business":
                    settingType="Business";
                    photo= R.drawable.business;
                    break;
                case "school":
                    settingType="School";
                    photo= R.drawable.school;
                    break;
                case "hobby":
                    settingType="Hobby";
                    photo= R.drawable.hobby;
                    break;
                case "gender":
                    settingType="Gender";
                    photo= R.drawable.gender;
                    break;
                default:
                    photo= R.drawable.placeholder;
            }
            holder.setToken(token);
            holder.type.setText(settingType);
            holder.value.setText(itemList.get(position).getSettingValue());
            Picasso.with(context)
                    .load(photo)
                    .into(holder.settingPhoto);
        }

        @Override
        public int getItemCount() {
            return (null != itemList ? itemList.size() : 0);
        }


}
