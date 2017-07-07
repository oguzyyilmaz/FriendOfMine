package com.fom.msesoft.fomapplication.fragment;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.activity.MainActivity;
import com.fom.msesoft.fomapplication.adapter.DegreeViewAdapter;
import com.fom.msesoft.fomapplication.adapter.OnLoadMoreListener;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.fom.msesoft.fomapplication.repository.PersonRepository;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterPreferences;
import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.degree_fragment)
public class DegreeFragment extends Fragment {

    DegreeViewAdapter mAdapter;

    boolean listSearchLogic = true;
    int degree = 2, skip = 0, width, height;
    private GridLayoutManager lLayout;
    boolean isDegreeEnd = false;

    boolean logicRefresh = true;
    List<CustomPerson> itemsData = new ArrayList<>();
    List<CustomPerson> freshItemsData = new ArrayList<>();

    @ViewById(R.id.progress_bar)
    ProgressBar progressBar;

    @ViewById(R.id.filter_layout)
    RelativeLayout filterLayout;

    @ViewById(R.id.filter_image)
    ImageView filterImage;

    @RestService
    PersonRepository personRepository;

    @ViewById(R.id.recyclerView)
    RecyclerView recyclerView;

    @ViewById(R.id.searchButton)
    ImageView searchButton;

    @ViewById(R.id.degree_search)
    EditText searchTxt;

    boolean filterCheck=true;

    @TargetApi(Build.VERSION_CODES.M)
    @AfterViews
    void execute() {

        WindowManager wm = (WindowManager) (getActivity().getSystemService(Context.WINDOW_SERVICE));
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        Picasso.with(getActivity())
                .load(R.drawable.degree_search_button)
                .resize(width / 15, width / 15)
                .into(searchButton);

        listAll();
        searchTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });
    }
    @Click(R.id.filter_image)
    void filterImageClick () {

        if(filterCheck) {
            filterLayout.setVisibility(View.VISIBLE);
            filterCheck=false;
        }
            else if(!filterCheck) {
            filterLayout.setVisibility(View.GONE);
            filterCheck = true;
        }
        }
    @AfterTextChange(R.id.degree_search)
    void afterSearch(){

        UiThreadExecutor.cancelAll("cancellable_task");
        searchBackPost();
    }


    @UiThread(delay = 1000,id="cancellable_task")
    void searchBackPost(){
        search();
    }

    @Click(R.id.searchButton)
    void search() {
        itemsData.clear();
        if (searchTxt.getText().toString().matches("")) {
            listSearchLogic = true;
            execute();
        } else {
            listSearch(searchTxt.getText().toString());
            listSearchLogic = false;
        }
    }

    @Background
    void listSearch(String like) {
        preSearchExecute();

        String token = ((MainActivity) getActivity()).getToken();

        CustomPerson[] persons = personRepository.findDegreeFriend(token, like, 2, 0);

        for (int i = 0; i < persons.length; i++) {
            itemsData.add(persons[i]);
        }
        postSearchExecute();
    }

    @UiThread
    void preSearchExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @UiThread
    void postSearchExecute() {

        mAdapter.notifyDataSetChanged();
        mAdapter.setLoaded();
        progressBar.setVisibility(View.GONE);
    }

    @Background
    void listRefresh(List<CustomPerson> itemsData, String like, int degree, int skip) {
        preRefreshExecute();
        CustomPerson[] persons = personRepository.findDegreeFriend(((MainActivity) getActivity()).getToken(), like, degree, skip);
        freshItemsData.clear();
        for (int i = 0; i < persons.length; i++) {

            freshItemsData.add(persons[i]);
        }


        if (freshItemsData.size() < 12) {
            isDegreeEnd = true;
        } else {
            isDegreeEnd = false;
        }
        if (persons.length <= 0)
            logicRefresh = false;
        else
            logicRefresh = true;
        postRefreshExecute();

    }

    @Background
    void listAll() {
        preExecute();

        String token = ((MainActivity) getActivity()).getToken();

        CustomPerson[] persons = personRepository.findDegreeFriend(token, "", 2, 0);

        for (int i = 0; i < persons.length; i++) {
            itemsData.add(persons[i]);
        }
        postExecute(itemsData);

    }

    @UiThread
    void preExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }


    @UiThread
    void preRefreshExecute() {
        itemsData.add(null);
        mAdapter.notifyItemInserted(itemsData.size() - 1);


    }


    @UiThread
    void postRefreshExecute() {
        itemsData.remove(itemsData.size() - 1);
        mAdapter.notifyItemRemoved(itemsData.size());

        itemsData.addAll(freshItemsData);

        mAdapter.notifyDataSetChanged();
        mAdapter.setLoaded();
    }

    @UiThread
    void postExecute(final List<CustomPerson> itemsData) {

        lLayout = new GridLayoutManager(getActivity(), 3);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(lLayout);

        progressBar.setVisibility(View.GONE);


        mAdapter = new DegreeViewAdapter(getActivity(), itemsData, recyclerView, ((MainActivity) getActivity()).getToken());

        recyclerView.setAdapter(mAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (listSearchLogic && logicRefresh) {
                    skip += 12;
                    if (!isDegreeEnd) {
                        listRefresh(itemsData, "", degree, skip);
                    } else {
                        degree += 1;
                        skip = 0;
                        listRefresh(itemsData, "", degree, skip);
                    }
                }
            }

        });




    }

}