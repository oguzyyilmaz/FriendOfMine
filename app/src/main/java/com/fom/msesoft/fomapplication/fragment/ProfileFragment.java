package com.fom.msesoft.fomapplication.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fom.msesoft.fomapplication.activity.FriendListActivity_;
import com.fom.msesoft.fomapplication.activity.LoginActivity_;
import com.fom.msesoft.fomapplication.activity.MainActivity;
import com.fom.msesoft.fomapplication.config.Config;
import com.fom.msesoft.fomapplication.extras.CircleTransform;
import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.adapter.ProfilePagerAdapter;
import com.fom.msesoft.fomapplication.extras.Preferences_;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.fom.msesoft.fomapplication.model.Image;
import com.fom.msesoft.fomapplication.model.Places;
import com.fom.msesoft.fomapplication.repository.PersonRepository;
import com.fom.msesoft.fomapplication.repository.PlacesRepository;
import com.jauker.widget.BadgeView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@EFragment(R.layout.profile_fragment)
public class ProfileFragment extends Fragment {

    @Getter
    @Setter
    private CustomPerson customPerson;


    @Pref
    Preferences_ preferences;

    String token;
    String encoded;

    @RestService
    PersonRepository personRepository;

    @RestService
    PlacesRepository placesRepository;

    @ViewById(R.id.profilePicture)
    ImageView profilePicture;

    @ViewById(R.id.friendCountView)
    ImageView friendCountView;

    @ViewById(R.id.friendCount)
    TextView friendCount;


    @ViewById(R.id.friendCount)
    TextView friendNumber;

    @ViewById(R.id.profileName)
    TextView profileName;

    @ViewById(R.id.profilePager)
    ViewPager viewPager;

    @ViewById(R.id.profileTab)
    TabLayout tabLayout;

    @ViewById(R.id.workName)
    TextView work;

    @ViewById(R.id.upload_progress)
    ProgressBar uploadProgress;

    @ViewById(R.id.popular)
    ImageView popular;

    @ViewById(R.id.popular_text)
    TextView popularText;

    @ViewById(R.id.profileBannerButton)
    Button profileBannerButton;
    BadgeView badgeView ;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReciever, new IntentFilter(Config.REQUEST_FINISH_RECIEVED));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReciever);
    }



    @Click(R.id.profileBannerButton)
    void bannerButton() {
        PopupMenu popup = new PopupMenu(getActivity(), profileBannerButton);

        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        Object menuHelper;
        Class[] argTypes;
        try {
            Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
            fMenuHelper.setAccessible(true);
            menuHelper = fMenuHelper.get(popup);
            argTypes = new Class[]{boolean.class};
            menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
        } catch (Exception e) {


            return;
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.logout) {
                    preferences.clear();
                    Intent i = new Intent(getActivity(), LoginActivity_.class);
                    startActivity(i);
                    getActivity().finish();
                }
                return true;
            }
        });

        popup.show();


    }


    Dialog dialog;

    ImageView imageView;

    View myView;



    @AfterViews
    void profileView() {

        token = ((MainActivity) getActivity()).getToken();
        uploadProgress.setVisibility(View.GONE);
        profileConnection();

        TextView textView;
        textView = new TextView(getContext());
        imageView = new ImageView(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        myView = factory.inflate(R.layout.badge_layout, null);
        imageView = (ImageView) myView.findViewById(R.id.badge_view);

        imageView.setImageResource(R.drawable.profiele_tab_notify);

        textView = (TextView) myView.findViewById(R.id.badge_count);
        textView.setText("");

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.profile_tab_pics2));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.profile_setting));
        tabLayout.addTab(tabLayout.newTab().setCustomView(myView));
/*
        tabLayout.getTabAt(2).getCustomView().getLayoutParams().height = myView.getHeight()/2;
        tabLayout.getTabAt(2).getCustomView().getLayoutParams().width = tabLayout.getTabAt(2).getCustomView().getLayoutParams().width/2;
*/
        badgeView=new BadgeView(getActivity());
        badgeView.setTargetView(textView);
        badgeView.setBadgeMargin(7,0,0,5);


        final ProfilePagerAdapter adapter = new ProfilePagerAdapter
                (((MainActivity) getActivity()).getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    tab.setIcon(R.drawable.profile_tab_pics2);
                } else if (tab.getPosition() == 1) {
                    tab.setIcon(R.drawable.profile_setting2);
                } else if (tab.getPosition() == 2) {
                    imageView.setImageResource(R.drawable.profile_tab_notify2);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    tab.setIcon(R.drawable.profile_tab_pics);
                } else if (tab.getPosition() == 1) {
                    tab.setIcon(R.drawable.profile_setting);
                } else if (tab.getPosition() == 2) {
                    imageView.setImageResource(R.drawable.profiele_tab_notify);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Background
    void profileConnection() {
        setCustomPerson(personRepository.findByToken(((MainActivity) getActivity()).getToken()));
        if (customPerson != null) {
            int friendSize = personRepository.findByFriendSize(token);
            //   List<CustomPerson> firstDegreeFriend = Arrays.asList(personRepository.findByFirstDegreeFriend(token));
            Places places = placesRepository.personWorkSearch(customPerson.getUniqueId());
            profileNumber(friendSize, customPerson, places);

            lastUpdatePhoto();
        } else {
            Intent i = new Intent(getActivity(), LoginActivity_.class);
            startActivity(i);
            getActivity().finish();
        }

    }

    @UiThread
    void lastUpdatePhoto() {
        if (customPerson.getPhoto() != null) {
            Picasso.with(getActivity())
                    .load(customPerson.getPhoto().toString())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(R.drawable.add_photo)
                    .transform(new CircleTransform())
                    .into(profilePicture);
        } else {
            Picasso.with(getActivity())
                    .load(R.drawable.add_photo)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(R.drawable.add_photo)
                    .transform(new CircleTransform())
                    .into(profilePicture);
        }
    }

    @UiThread
    void profileNumber(int firstDegreeFriend, CustomPerson customPerson, Places places) {
        friendNumber.setText(String.valueOf(firstDegreeFriend).toString());
        if (customPerson.getPhoto() != null) {
            Picasso.with(getActivity())
                    .load(customPerson.getPhoto().toString())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .transform(new CircleTransform())
                    .into(profilePicture);
        } else {
            Picasso.with(getActivity())
                    .load(R.drawable.add_photo)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .transform(new CircleTransform())
                    .into(profilePicture);
        }
        profileName.setText(customPerson.getFirstName() + " " + customPerson.getLastName());

        if (places != null) {
            work.setText(places.getType() + ", " + places.getName());

        } else {
            work.setText("No Work");
        }
        viewPager.setOffscreenPageLimit(3);
    }

    @Click(R.id.friendCount)
    void friendList() {

        Intent i = new Intent(getActivity(), FriendListActivity_.class);
        i.putExtra("token", token);
        startActivity(i);

    }

    @Click(R.id.checkButton)
    void CheckButton() {


    }

    @Click(R.id.profilePicture)
    void changeProfilePhoto() {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogEnterAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.profile_photos_dialog);
        ImageView photosDialog = (ImageView) dialog.findViewById(R.id.photos_dialog);
        WindowManager wm = (WindowManager) (getContext()).getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        if (getCustomPerson().getPhoto() != null) {
            Picasso.with(getContext())
                    .load(getCustomPerson().getPhoto())
                    .resize(width * 9 / 10, width * 9 / 10)
                    .centerCrop()
                    .into(photosDialog);
        } else {
            Picasso.with(getContext())
                    .load(R.drawable.add_photo)
                    .resize(width * 9 / 10, width * 9 / 10)
                    .centerCrop()
                    .into(photosDialog);
        }
        Button deletePhoto = (Button) dialog.findViewById(R.id.delete_photo);
        Button addPhoto = (Button) dialog.findViewById(R.id.add_photo);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePhoto();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void updatePhoto() {
        Intent selectImageIntent = new Intent();
        selectImageIntent.setType("image/*");
        selectImageIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(selectImageIntent, 29);
        profileConnection();
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

            uploadProgress.setVisibility(View.VISIBLE);
            uploading();

        }
    }

    @UiThread
    void progressUploading() {
        uploadProgress.setVisibility(View.VISIBLE);
    }

    @Background
    void uploading() {
        progressUploading();
        Image image = new Image()
                .setBase64String(encoded)
                .setToken(token);
        personRepository.uploadProfilePhoto(image);
        encoded = null;

        refreshProfile();
    }

    @UiThread
    void refreshProfile() {
        uploadProgress.setVisibility(View.GONE);
        profileConnection();

    }

    BroadcastReceiver mReciever = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case Config.REQUEST_FINISH_RECIEVED:
                    badgeView.setText(((MainActivity)getActivity()).getBadgeTxt());
                    break;
                default:
                    // do nothing
            }
        }
    };

}