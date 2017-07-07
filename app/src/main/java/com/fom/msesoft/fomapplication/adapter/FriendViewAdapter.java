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
import com.fom.msesoft.fomapplication.model.ContactModel;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by oguz on 04.08.2016.
 */
public class FriendViewAdapter extends RecyclerView.Adapter<FriendViewHolders> {

    private List<CustomPerson> itemList;
    private Context context;
    private List<ContactModel> contactModels;
    private String name;
    int position;


    public FriendViewAdapter(Context context, List<CustomPerson> itemList, List<ContactModel> contactModels) {
        this.itemList = itemList;
        this.context = context;
        this.contactModels = contactModels;
    }


    @Override
    public FriendViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_listrow, null);

        return new FriendViewHolders(view);
    }

    @Override
    public void onBindViewHolder(FriendViewHolders holder, int position) {
        this.position = position;
        holder.setPerson(itemList.get(position));
        holder.setContext(context);
        WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        holder.txtLayout.getLayoutParams().height = height/8;
        Picasso.with(context)
                .load(itemList.get(position).getPhoto())
                .resize(width / 15 * 3, width / 15 * 3)
                .centerCrop()
                .transform(new CircleTransform())
                .into(holder.personPhoto);
        if (((itemList.get(position).getFirstName()) !=null))
        holder.nameTxt.setText(itemList.get(position).getFirstName() + " " + itemList.get(position).getLastName());

        else {
            //incelenecek eksiklikler var
            search(itemList.get(position).getPhoneNumber());
            holder.nameTxt.setText(name);
        }


    }
    private void search (String phoneNumber) {
        for (int i = 0; i<contactModels.size(); i++) {

            if (phoneNumber.equals(contactModels.get(i).getPhoneNumber())) {
                name = contactModels.get(i).getName();
                System.out.println(contactModels.get(i).getName() + contactModels.get(i).getPhoneNumber());

            }
        }
    }


    @Override
    public int getItemCount() {
        return (null != itemList ? itemList.size() : 0);
    }

}