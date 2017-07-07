package com.fom.msesoft.fomapplication.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.ProgressBar;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.activity.MainActivity;
import com.fom.msesoft.fomapplication.adapter.OnDeletePhotoListener;
import com.fom.msesoft.fomapplication.adapter.OnUploadPhotoListener;
import com.fom.msesoft.fomapplication.adapter.ProfilePhotoAdapter;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.fom.msesoft.fomapplication.model.Image;
import com.fom.msesoft.fomapplication.repository.PersonRepository;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@EFragment(R.layout.profile_photo_fragment)
public class ProfilePhotoFragment extends Fragment implements OnUploadPhotoListener,OnDeletePhotoListener {

    private static final String TAG = "RecyclerViewExample";

    private CustomPerson customPerson;
    private ProfilePhotoAdapter adapter;

    private GridLayoutManager lLayout;

    String encoded;

    @RestService
    PersonRepository personRepository;

    @ViewById(R.id.recyclerView)
    RecyclerView recyclerView;

    @ViewById(R.id.progress_bar)
    ProgressBar progressBar;


    String token;

    List<String> itemsData;

    @AfterViews
    void afterViews () {

        loadingPersonPhotos();

    }
    @Background
    void loadingPersonPhotos() {
        itemsData= new ArrayList<String>();
        token = ((MainActivity)getActivity()).getToken();
        customPerson = personRepository.findByToken(((MainActivity)getActivity()).getToken());
        
        if (customPerson.getPhotoList()!=null) {
            for (int i = 0; i < customPerson.getPhotoList().size(); i++) {
                itemsData.add(customPerson.getPhotoList().get(i));
            }
        }else {}
        itemsData.add("http://fomserver.cloudapp.net/fomPic/extra/addPhoto.png");
        postExecute(itemsData);
    }
    @UiThread
    void postExecute(List<String> itemsData) {
        lLayout = new GridLayoutManager(getActivity(),3);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(lLayout);

        progressBar.setVisibility(View.GONE);

        adapter = new ProfilePhotoAdapter(getActivity(),itemsData,customPerson);
        adapter.setOnUploadPhotoListener(this);
        adapter.setOnDeletePhotoListener(this);
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onUploadPhotoClick() {
        Intent selectImageIntent = new Intent();
        selectImageIntent.setType("image/*");
        selectImageIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(selectImageIntent,30);
        refreshPhoto();

    }
    @Background
    void refreshPhoto() {
        loadingPersonPhotos();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bitmap bitmap = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (bitmap != null) {
                bitmap.recycle();
            }
            InputStream stream;
            try {
                stream = getContext().getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(stream);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                byte[] byteArray = baos.toByteArray();
                encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                data = null;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            uploadingProgress();
        }
    }

    @UiThread
    void uploadingProgress () {
        progressBar.setVisibility(View.VISIBLE);
        uploading();
    }

    @Background
    void uploading () {
        Image image = new Image()
                .setBase64String(encoded)
                .setToken(((MainActivity)getActivity()).getToken());
        personRepository.uploadPhoto(image);
        encoded = null;
        loadingPersonPhotos();
        refreshProfile();
    }
    @UiThread
    void refreshProfile() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDeletePhotoClick(String photoId, int index) {
        deleteProgress(photoId,index);


    }
    @UiThread
    void deleteProgress (String photoId,int index) {
        progressBar.setVisibility(View.VISIBLE);
        deleteBackground(photoId,index);
    }
    @Background
    void deleteBackground(String photoId,int index) {
        personRepository.deletePhoto (token,photoId,index);
        itemsData.remove(index);
        postExecute(itemsData);
    }


}
