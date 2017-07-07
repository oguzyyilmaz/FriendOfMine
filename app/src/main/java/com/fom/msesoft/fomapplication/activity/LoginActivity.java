package com.fom.msesoft.fomapplication.activity;

import android.app.Activity;
import android.content.Intent;

import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.UiThread;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.extras.Preferences_;
import com.fom.msesoft.fomapplication.model.Person;
import com.fom.msesoft.fomapplication.model.Token;
import com.fom.msesoft.fomapplication.repository.PersonRepository;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;

import java.io.IOException;

import io.fabric.sdk.android.Fabric;


@EActivity(R.layout.activity_login)
@Fullscreen
public class LoginActivity extends AppCompatActivity {
    public Person person;
    private Boolean saveLogin = false;

    public Token token = null;

    @RestService
    PersonRepository personRepository;

    @ViewById(R.id.progress_bar_login)
    ProgressBar progressBar;

    @ViewById(R.id.input_layout_email)
    TextInputLayout inputLayoutEmail;

    @ViewById(R.id.input_email)
    EditText inputEmail;

    @ViewById(R.id.input_login_password)
    EditText inputLoginPassword;

    @ViewById(R.id.register)
    Button registerBtn;

    @Pref
    Preferences_ preferences;

    @AfterViews
    void afterViews() {
        saveLogin = preferences.isLogin().get();
        if (saveLogin) {

            Intent intent = new Intent(this, MainActivity_.class);
            startActivity(intent);
            finish();
        }

    }

    @Click(R.id.register)
    void registerButtonClick() {
        Intent i = new Intent(this, RegisterActivity_.class);
        startActivity(i);
        LoginActivity.this.finish();
    }

    @AfterTextChange(R.id.input_email)
    void onTextChancedInputEmail() {
        validateEmail();
    }

    @ViewById(R.id.input_layout_login_password)
    TextInputLayout inputLayoutLoginPassword;

    @ViewById(R.id.input_login_password)
    EditText password;

    @ViewById(R.id.btn_signin)
    Button sign;

    @Click(R.id.btn_signin)
    void btn_signin() {
        sign(inputEmail.getText().toString(), password.getText().toString());
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @FocusChange(R.id.input_email)
    void changeEmail(View v) {
        if (!inputEmail.hasFocus()) {
            hideKeyboard(v);
        }
    }

    @FocusChange(R.id.input_login_password)
    void changePassword(View v) {
        if (!inputLoginPassword.hasFocus()) {
            hideKeyboard(v);
        }
    }

    @Background
    public void sign(String email, String password) {
        preExecute();
        token = personRepository.signIn(email, password);
        chechSign(token);
    }

    @UiThread
    void preExecute() {
        sign.setText("");

        progressBar.setVisibility(View.VISIBLE);
    }


    @UiThread
    void chechSign(Token token) {
        if (token != null) {
            if (token.isNew()) {
                Intent intent = new Intent(this, ContactViewActivity_.class);
                intent.putExtra("token", token.getToken());
                intent.putExtra("isNewLogin", true);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(this, MainActivity_.class);
                intent.putExtra("token", token.getToken());
                intent.putExtra("isNewLogin", true);
                startActivity(intent);
                finish();
            }
            this.finish();
            registerGCM();

            preferences.email().put(inputEmail.getText().toString());
            preferences.password().put(password.getText().toString());
            preferences.isLogin().put(true);

            progressBar.setVisibility(View.GONE);

        } else {
            Toast.makeText(this, "Wrong E-mail or Password", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            sign.setText("Sing in");
        }
    }

    @Background
    void registerGCM() {
        GoogleCloudMessaging gcm = null;//Google Cloud referansı
        String PROJECT_ID = "369106327986";
        String regid = null;


        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(this);//GCM objesi oluşturduk ve gcm referansına bağladık
            }
            regid = gcm.register(PROJECT_ID);//gcm objesine PROJECT_ID mizi göndererek regid değerimizi aldık.Bu değerimizi hem sunucularımıza göndereceğiz Hemde Androidde saklıyacağız

        } catch (IOException e) {
            e.printStackTrace();
        }

        personRepository.registerGCM(token.getToken(), regid);


    }


    @AfterTextChange(R.id.input_email)
    void onTextChancedInputName() {
        validateEmail();
    }


    @AfterTextChange(R.id.input_login_password)
    void onTextChancedInputPassword() {
        validatePassword();
    }

    private void submitForm() {


        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
    }


    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (password.getText().toString().trim().isEmpty()) {
            inputLayoutLoginPassword.setError(getString(R.string.err_msg_password));
            requestFocus(password);
            return false;
        } else {
            inputLayoutLoginPassword.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
    }

}

