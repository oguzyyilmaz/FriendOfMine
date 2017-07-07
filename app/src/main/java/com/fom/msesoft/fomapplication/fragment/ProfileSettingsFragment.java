package com.fom.msesoft.fomapplication.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.activity.MainActivity;
import com.fom.msesoft.fomapplication.activity.MainActivity_;
import com.fom.msesoft.fomapplication.adapter.FriendViewAdapter;
import com.fom.msesoft.fomapplication.adapter.ProfileSettingAdapter;
import com.fom.msesoft.fomapplication.extras.Preferences_;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.fom.msesoft.fomapplication.model.Places;
import com.fom.msesoft.fomapplication.model.Setting;
import com.fom.msesoft.fomapplication.repository.PersonRepository;
import com.fom.msesoft.fomapplication.repository.PlacesRepository;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.profile_settings_fragment)
public class ProfileSettingsFragment extends Fragment {

    @RestService
    PersonRepository personRepository;

    @ViewById(R.id.profileSettingRecycler)
    RecyclerView recyclerView;



    @AfterViews
    void excute(){
        listAll();
    }

    @ViewById(R.id.friendlist_back)
    ImageView back;


    private LinearLayoutManager lLayout;
    private String token;

    @Background
    void listAll(){

        List<Setting> itemsData=new ArrayList<>();
        String hobby,name,business,school,gender;

        token = ((MainActivity) getActivity()).getToken();
        CustomPerson person = personRepository.findByToken(token);

        if(person.getFirstName()==null||person.getLastName()==null){
            name="Belirtilmedi";
        }else {
            name=person.getFirstName()+" "+person.getLastName();
        }
        if(person.getHoby()==null){
            hobby="Belirtilmedi";
        }else {
            hobby=person.getHoby();
        }
        if(person.getOccupation()==null){
            business="Belirtilmedi";
        }else {
            business=person.getOccupation();
        }
        if(person.getSchool()==null){
            school="Belirtilmedi";
        }else {
            school=person.getSchool();
        }
        if(person.getGender()==null){
            gender="Belirtilmedi";
        }else {
            gender=person.getGender();
        }

        itemsData.add(new Setting().setSettingType("name").setSettingValue(name));
        itemsData.add(new Setting().setSettingType("business").setSettingValue(business));
        itemsData.add(new Setting().setSettingType("school").setSettingValue(school));
        itemsData.add(new Setting().setSettingType("hobby").setSettingValue(hobby));
        itemsData.add(new Setting().setSettingType("gender").setSettingValue(gender));

        postExecute(itemsData);

    }



    @UiThread
    void postExecute(final List<Setting> itemsData){



        lLayout = new LinearLayoutManager(getActivity());

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(lLayout);

        final ProfileSettingAdapter mAdapter = new ProfileSettingAdapter(getActivity(),itemsData,((MainActivity)getActivity()).getToken());

        recyclerView.setAdapter(mAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }

}

