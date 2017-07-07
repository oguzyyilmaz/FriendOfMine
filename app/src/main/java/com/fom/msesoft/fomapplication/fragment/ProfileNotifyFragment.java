package com.fom.msesoft.fomapplication.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.activity.MainActivity;
import com.fom.msesoft.fomapplication.adapter.NotifyViewAdapter;
import com.fom.msesoft.fomapplication.adapter.OnNotifyListener;
import com.fom.msesoft.fomapplication.config.Config;
import com.fom.msesoft.fomapplication.gcm.GcmBroadcastReceiver;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.fom.msesoft.fomapplication.repository.PersonRepository;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.profile_notify_fragment)
public class ProfileNotifyFragment extends Fragment implements OnNotifyListener {

    @RestService
    PersonRepository personRepository;

    List<CustomPerson> itemsData;

    @ViewById(R.id.notifyRecyclerView)
    RecyclerView recyclerView;

    @ViewById(R.id.notify_progress)
    ProgressBar notifyProgress;

    private LinearLayoutManager lLayout;

    NotifyViewAdapter adapter;

    GcmBroadcastReceiver GCM;

    String token;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReciever, new IntentFilter(Config.REQUEST_RECIEVED));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReciever);
    }

    @AfterViews
    void afterViews() {
        token = ((MainActivity) getActivity()).getToken();
        listAll();
    }

    @Background
    void listAll() {
        preExecute();
        itemsData = new ArrayList<CustomPerson>();
        for (CustomPerson customPerson : personRepository.notifies(token)) {
            itemsData.add(customPerson);
        }
        postExecute(itemsData);

    }
    @UiThread
    void preExecute() {
        notifyProgress.setVisibility(View.VISIBLE);
    }

    @UiThread
    void postExecute(List<CustomPerson> itemsData) {
        notifyProgress.setVisibility(View.GONE);
        lLayout = new LinearLayoutManager(getActivity());

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(lLayout);

        adapter = new NotifyViewAdapter(getActivity(), itemsData, token);
        adapter.setOnNotifyListener(this);
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        notifyProgress.setVisibility(View.GONE);

        ((MainActivity)getActivity()).setBadgeTxt(String.valueOf(adapter.getItemCount()).toString());
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(Config.REQUEST_FINISH_RECIEVED));
    }

    @Override
    public void deleteNotify(int position) {
        itemsData.remove(position);
        adapter.notifyDataSetChanged();
        ((MainActivity)getActivity()).setBadgeTxt(String.valueOf(adapter.getItemCount()).toString());
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(Config.REQUEST_FINISH_RECIEVED));
    }

    @Override
    public void catchNotify() {
        listAll();
    }


    @UiThread
    public void refreshList(){
        listAll();
    }

    BroadcastReceiver mReciever = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case Config.REQUEST_RECIEVED:
                    refreshList();
                    ((MainActivity)getActivity()).setBadgeTxt(String.valueOf(adapter.getItemCount()).toString());
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(Config.REQUEST_FINISH_RECIEVED));
                    break;
                default:
                    // do nothing
            }
        }
    };
}
