package com.fom.msesoft.fomapplication.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.config.Config;
import com.fom.msesoft.fomapplication.model.CustomPerson;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import lombok.Setter;

public class ProfileSettingHolder extends RecyclerView.ViewHolder {

    @Setter
    CustomPerson person;

    @Setter
    String token;

    @Setter
    Context context;

    Dialog dialog;

    String editTxt;

    RestTemplate restTemplate;

    public ImageView settingPhoto, settingSet;

    public TextView value,type;

    RelativeLayout txtLayout;

    public ProgressBar progressBar;

    public ProfileSettingHolder(final View itemView) {
        super(itemView);
        restTemplate=new RestTemplate();
        restTemplate.getMessageConverters().clear();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        value = (TextView) itemView.findViewById(R.id.nameTxt);
        settingPhoto = (ImageView) itemView.findViewById(R.id.settingPhoto);
        txtLayout = (RelativeLayout) itemView.findViewById(R.id.settingLayout);
        settingSet = (ImageView) itemView.findViewById(R.id.settingSet);
        type = (TextView) itemView.findViewById(R.id.type);
        progressBar = (ProgressBar) itemView.findViewById(R.id.profileSettingProgress);

        settingSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(itemView.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogEnterAnimation;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.profile_setting_dialog);
                TextView typeEdit = (TextView) dialog.findViewById(R.id.dialogEditType);
                typeEdit.setText(type.getText().toString());
                final EditText edit = (EditText)dialog.findViewById(R.id.profile_setting_edit);
                edit.setText(value.getText().toString());
                ImageButton editButton = (ImageButton) dialog.findViewById(R.id.profileDialogButton);



                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    editTxt=edit.getText().toString();

                    new GetCustomPerson().execute();

                        dialog.dismiss();

                    }
                });
                        dialog.show();
            }
        });

    }
    private class UpdatePerson extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            HttpEntity<CustomPerson> requestEntity = new HttpEntity<CustomPerson>(person);
            person=restTemplate.exchange(Config.ROOT_URL +"person".concat("/updatePerson?token="+token), HttpMethod.POST, requestEntity, CustomPerson.class).getBody();

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            value.setText(editTxt);

        }

    }

    private class GetCustomPerson extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }
        @Override
        protected Void doInBackground(Void... voids) {
            HashMap<String, Object> urlVariables = new HashMap<String, Object>();
            urlVariables.put("token", token);
            person=restTemplate.exchange(Config.ROOT_URL +"person".concat("/findByToken?token={token}"), HttpMethod.GET, null, CustomPerson.class, urlVariables).getBody();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            switch (type.getText().toString()){
                case "Name and Lastname":
                    person.setFirstName(editTxt);
                    break;
                case "Business":
                    person.setOccupation(editTxt);
                    break;
                case "School":
                    person.setSchool(editTxt);
                    break;
                case "Hobby":
                    person.setHoby(editTxt);
                    break;
                case "Gender":
                    person.setGender(editTxt);
                    break;
                default:
                    person=person;
                    break;

            }
            new UpdatePerson().execute();
        }
    }

}
