package com.fom.msesoft.fomapplication.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.adapter.FriendViewAdapter;
import com.fom.msesoft.fomapplication.fragment.ProfileFragment_;
import com.fom.msesoft.fomapplication.model.ContactModel;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.fom.msesoft.fomapplication.repository.PersonRepository;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_friend_list)
@Fullscreen
public class FriendListActivity extends AppCompatActivity {

    public String token;

    List<ContactModel> contactModels = new ArrayList<>();
    List<String> cList = new ArrayList<>();


    @RestService
    PersonRepository personRepository;

    @ViewById(R.id.recyclerView)
    RecyclerView recyclerView;

    @ViewById(R.id.friend_progress_bar)
    ProgressBar progressBar;

    @AfterViews
    void excute(){
        contactListAll();
        listAll();
    }

    @ViewById(R.id.friendlist_back)
    ImageView back;

    ProgressDialog progressDialog;

    private GridLayoutManager lLayout;

    @Click(R.id.friendlist_back)
    void backButton () {
        Intent i = new Intent(this, MainActivity_.class);
        startActivity(i);
    }
    @Background
    void contactListAll() {

        ContentResolver contentResolver = FriendListActivity.this.getContentResolver();
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
                        String person_phoneName = person_cursor.getString(person_cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        if (!cList.contains(person_phoneNumber.replace("+90","0").replace(" ",""))) {
                            cList.add(person_phoneNumber.replace("+90","0").replace(" ",""));
                            ContactModel contactModel = new ContactModel();
                            contactModel.setName(person_phoneName)
                                    .setPhoneNumber(person_phoneNumber.replace("+90","0").replace(" ",""));
                            contactModels.add(contactModel);
                        }
                        //contactModels.add(person_phoneNumber.replace("+90","0")); // ismini ve telefon numarasını list içine at
                    }
                    person_cursor.close();
                }
            }
        }


    }
    @Background
    void listAll(){
        preExecute();
        List<CustomPerson> itemsData=new ArrayList<>();


        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        CustomPerson[] persons = personRepository.findByFirstDegreeFriend(token);

        for(int i = 0 ;i<persons.length;i++){
            itemsData.add(persons[i]);
        }

        postExecute(itemsData);

    }

    @UiThread
    void preExecute(){
        progressBar.setVisibility(View.VISIBLE);

    }

    @UiThread
    void postExecute(final List<CustomPerson> itemsData){

        progressBar.setVisibility(View.GONE);

        lLayout = new GridLayoutManager(this,1);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(lLayout);

        final FriendViewAdapter mAdapter = new FriendViewAdapter(this,itemsData,contactModels);

        recyclerView.setAdapter(mAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }
}
