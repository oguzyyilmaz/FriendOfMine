package com.fom.msesoft.fomapplication.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.adapter.ContactViewAdapter;
import com.fom.msesoft.fomapplication.repository.PersonRepository;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.ArrayList;

@EActivity(R.layout.activity_multiselect)
public class ContactViewActivity extends AppCompatActivity implements ContactViewAdapter.ViewHolder.ClickListener {


    private ContactViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<String> mArrayList = new ArrayList<String>();
    private ArrayList<String> mArrayListName = new ArrayList<>();
    private ArrayList<String> mUsedList = new ArrayList<>();
    private ArrayList<String> mArrayListNameSearch = new ArrayList<>();
    @ViewById(R.id.my_recycler_view)
    RecyclerView mRecyclerView;


    @RestService
    PersonRepository personRepository;

    @AfterViews
    void afterViews() {
        conctactBackground();

    }

    @Extra("token")
    String token;

    @Extra("isNewLogin")
    boolean isNewLogin;

    @ViewById(R.id.contactSearch)
    EditText contactSearchTxt;

    @ViewById(R.id.contactBannerTxt)
    TextView contactBannerTxt;

    @Click(R.id.contactSearchImg)
    void searchVisible(){
        contactSearchTxt.setVisibility(View.VISIBLE);
        contactBannerTxt.setVisibility(View.GONE);
    }


    @AfterTextChange(R.id.contactSearch)
    void contactSearch(){
        mArrayListNameSearch.clear();
        for (String name:mArrayListName){
            if (name.contains(contactSearchTxt.getText().toString()))
            mArrayListNameSearch.add(name);
        }
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(ContactViewActivity.this, 3));
        mAdapter = new ContactViewAdapter(ContactViewActivity.this, mArrayListNameSearch, ContactViewActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onItemClicked(int position) {

        toggleSelection(position);
    }

    @Override
    public boolean onItemLongClicked(int position) {
        toggleSelection(position);

        return true;
    }

    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);

    }

    @Click(R.id.send_sms)
    void sendSMS() {
        if (!mArrayList.isEmpty()) {

            for (int i = 0; i < mAdapter.getSelectedItemCount(); i++) {

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(mArrayList.get(mAdapter.getSelectedItems().get(i)), null, "Friend Of Mine", null, null);

            }
        }
        Intent i = new Intent(this,MainActivity_.class);
        i.putExtra("token",token);
        i.putExtra("isNewLogin",isNewLogin);
        startActivity(i);
        finish();
    }

    @Click(R.id.next_main_bttn)
    void nextMain () {
        Intent i = new Intent(this,MainActivity_.class);
        i.putExtra("token",token);
        i.putExtra("isNewLogin",isNewLogin);
        startActivity(i);
        finish();
    }

    @Background
    void getUseList() {
        for (String number : personRepository.findContact(mArrayList)) {
            mUsedList.add(number);
        }
        usedListPostExecute();
    }

    @UiThread
    void usedListPostExecute() {
        for (int i = 0; i < mArrayList.size(); i++) {
            for (int j = 0; j < mUsedList.size(); j++) {
                if (mArrayList.get(i).equals(mUsedList.get(j))) {
                    mArrayList.remove(i);
                    mArrayListName.remove(i);
                }
            }
        }
        progressBar.setVisibility(View.GONE);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(ContactViewActivity.this, 3));
        mAdapter = new ContactViewAdapter(ContactViewActivity.this, mArrayListName, ContactViewActivity.this);
        mRecyclerView.setAdapter(mAdapter);



    }

    @ViewById(R.id.all_select)
    Button allSelect;
    boolean allSelectLogic=true;
    @Click(R.id.all_select)
    void allSelect(){
        if(allSelectLogic){
            allSelect.setText("Hepsini Sil");
            allSelectLogic=false;
            for(int i=0;i<mAdapter.getItemCount();i++)
                mAdapter.toggleSelection(i);
        }
        else {
            allSelect.setText("Hepsini Seç");
            allSelectLogic=true;
            mAdapter.clearSelection();

        }

    }

    @ViewById(R.id.contactProgress)
    ProgressBar progressBar;

    @UiThread
    void contactPostExecute() {
        getUseList();

    }
    @UiThread
    void contactPreExecute(){
        progressBar.setVisibility(View.VISIBLE);
    }

    @Background
    void conctactBackground() {
        contactPreExecute();
        ContentResolver contentResolver = ContactViewActivity.this.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)); // id ye göre eşleşme yapılacak
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)); // telefonda kayıtlı olan ismi
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    // telefon numarasına sahip ise if içine gir.
                    Cursor person_cursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (person_cursor.moveToNext()) {

                        String person_phoneNumber = person_cursor.getString(person_cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String person_name = person_cursor.getString(person_cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                        if (!mArrayList.contains((person_phoneNumber.replace("+90", "0")).replace(" ",""))) {
                            mArrayList.add((person_phoneNumber.replace("+90", "0")).replace(" ",""));
                            mArrayListName.add(person_name);
                        }// ismini ve telefon numarasını list içine at
                    }
                    person_cursor.close();
                }
            }
        }
        contactPostExecute();
    }
}