package com.fom.msesoft.fomapplication.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.activity.MainActivity;
import com.fom.msesoft.fomapplication.adapter.ActivityAdapter;
import com.fom.msesoft.fomapplication.model.ActivityModelCustom;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.fom.msesoft.fomapplication.model.Image;
import com.fom.msesoft.fomapplication.repository.ActivityShareRepository;
import com.fom.msesoft.fomapplication.repository.PersonRepository;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@EFragment(R.layout.activity_fragment)
public class ActivityFragment extends Fragment {


    private LinearLayoutManager lLayout;

    @ViewById(R.id.share_btn)
    Button share;

    List<ActivityModelCustom> activityCustomList = new ArrayList<>();

    @ViewById(R.id.activity_recycler)
    RecyclerView recyclerView;

    Dialog shareDialog, dateDialog;

    private int year;
    private int month;
    private int day;
    DatePicker dpResult;

    Image image = new Image();

    ImageView addedPhoto;

    byte[] imageAsBytes;

    String encoded;

    List<ActivityModelCustom> activityModelCustoms = new ArrayList<>();

    String token;


    @RestService
    PersonRepository personRepository;

    @RestService
    ActivityShareRepository shareRepository;

    @Getter
    @Setter
    CustomPerson customPerson;

    @AfterViews
    void afterViews() {
        token = ((MainActivity) getActivity()).getToken();
        personConnection();

    }

    @Background
    void personConnection() {
        setCustomPerson(personRepository.findByToken(token));
        postUI();
    }
    @UiThread
    void postUI () {
        activityConnection();
    }
    @Background
    void activityConnection() {
        ActivityModelCustom[] amc =shareRepository.allActivity(token);
        activityModelCustoms = Arrays.asList(amc);
        postExecute();
    }
    @UiThread
    void postExecute() {


        lLayout = new LinearLayoutManager(getActivity());


        recyclerView.setLayoutManager(lLayout);

        ActivityAdapter activityAdapter = new ActivityAdapter(getActivity(), activityModelCustoms,token,getCustomPerson());

        recyclerView.setAdapter(activityAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    @Click(R.id.share_floating)
    void shareActivity() {
        final ActivityModelCustom activityModelCustom = new ActivityModelCustom();
        shareDialog = new Dialog(getContext());
        shareDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        shareDialog.getWindow().getAttributes().windowAnimations = R.style.DialogEnterAnimation;
        shareDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        shareDialog.setContentView(R.layout.share_diaolog);
        final EditText activityName = (EditText) shareDialog.findViewById(R.id.activity_name_txt);
        final EditText activityDescription = (EditText) shareDialog.findViewById(R.id.activity_description_txt);
        final TextView activityDate = (TextView) shareDialog.findViewById(R.id.activity_date_txt);
        final EditText activityPlaces = (EditText) shareDialog.findViewById(R.id.activity_places_txt);
        final RadioButton friendRadio = (RadioButton) shareDialog.findViewById(R.id.friend_radio);
        final RadioButton allRadio = (RadioButton) shareDialog.findViewById(R.id.all_radio);
        final Button addPhoto = (Button) shareDialog.findViewById(R.id.add_photo);
       // final ImageView addedPhoto = (ImageView) shareDialog.findViewById(R.id.added_photo);
        addedPhoto = (ImageView) shareDialog.findViewById(R.id.added_photo);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePhoto();

            }
        });
        Button shareDialogBtn = (Button) shareDialog.findViewById(R.id.share_btn);
        shareDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityModelCustom.setActivityName(activityName.getText().toString());
                activityModelCustom.setActivityDescription(activityDescription.getText().toString());
                activityModelCustom.setActivityDate(activityDate.getText().toString());
                activityModelCustom.setActivityPlaces(activityPlaces.getText().toString());
                activityModelCustom.setPerson(getCustomPerson());
                activityModelCustom.setImage(new Image().setBase64String(encoded));
                if (friendRadio.isChecked())
                    activityModelCustom.setActivityArea("friend");
                else if (allRadio.isChecked())
                    activityModelCustom.setActivityArea("all");
                else
                    activityModelCustom.setActivityArea("all");

                shareBackground(activityModelCustom);
                shareDialog.cancel();
            }
        });

        activityDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateDialog = new Dialog(getContext());
                dateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dateDialog.getWindow().getAttributes().windowAnimations = R.style.DialogEnterAnimation;
                dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dateDialog.setContentView(R.layout.date_dialog);
                Window window = dateDialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.BOTTOM;

                Button setDate = (Button) dateDialog.findViewById(R.id.set_date);
                setDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dpResult = (DatePicker) dateDialog.findViewById(R.id.datePicker);
                        year = dpResult.getYear();
                        month = dpResult.getMonth();
                        day = dpResult.getDayOfMonth();
                        activityDate.setText(day + " " + month + " " + " " + year);
                        dateDialog.cancel();
                    }
                });


                dateDialog.show();
            }
        });
        shareDialog.show();
    }
    public void updatePhoto() {
        Intent selectImageIntent = new Intent();
        selectImageIntent.setType("image/*");
        selectImageIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(selectImageIntent, 29);
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
            uploading();

        }
    }

    @Background
    void uploading() {
        imageAsBytes = Base64.decode(encoded.getBytes(), Base64.DEFAULT);
        storePhoto();
    }
    @UiThread
    void storePhoto() {
        WindowManager wm = (WindowManager) (getContext()).getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        addedPhoto.getLayoutParams().width=width*4/10;
        addedPhoto.getLayoutParams().height=height*4/10;
        addedPhoto.setImageBitmap(
                BitmapFactory.decodeByteArray(imageAsBytes,0,imageAsBytes.length)
        );
    }

    @Background
    void shareBackground(ActivityModelCustom activityModelCustom) {
        shareRepository.share(activityModelCustom,token);
    }

}