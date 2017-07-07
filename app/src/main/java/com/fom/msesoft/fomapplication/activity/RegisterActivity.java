package com.fom.msesoft.fomapplication.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.model.Person;
import com.fom.msesoft.fomapplication.model.Register;
import com.fom.msesoft.fomapplication.repository.PersonRepository;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.ArrayList;
import java.util.List;


@EActivity(R.layout.activity_register)
public class RegisterActivity extends AppCompatActivity {

    @RestService
    PersonRepository personRepository;

    @ViewById(R.id.phone_number)
    EditText phoneNumberTxt;
    @ViewById(R.id.first_name)
    EditText firstNameTxt;
    @ViewById(R.id.last_name)
    EditText lastNameTxt;
    @ViewById(R.id.email)
    EditText emailTxt;
    @ViewById(R.id.password)
    EditText passwordTxt;

    @ViewById(R.id.register_progress)
    ProgressBar registerProgressbar;

    @ViewById(R.id.register_button)
    Button registerButton;

    List<String> contactList = new ArrayList<>();
    Person person = new Person();

    @Click(R.id.register_button)
    void registerButtonClick () {
        person.setPhoneNumber(phoneNumberTxt.getText().toString())
                .setFirstName(firstNameTxt.getText().toString())
                .setLastName(lastNameTxt.getText().toString())
                .setEmail(emailTxt.getText().toString())
                .setPassword(passwordTxt.getText().toString());
        //register.setContactPhoneList();
        contactListConnection();
    }

    @UiThread
    void progressThread () {
        registerProgressbar.setVisibility(View.VISIBLE);
        registerButton.setText(" ");
    }

    @Background
    void contactListConnection () {
        progressThread();
        ContentResolver contentResolver = RegisterActivity.this.getContentResolver();
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
                        if (!contactList.contains(person_phoneNumber.replace("+90","0")))
                        contactList.add(person_phoneNumber.replace("+90","0")); // ismini ve telefon numarasını list içine at
                    }
                    person_cursor.close();
                }
            }
        }
        contactListAdd();
    }

    @UiThread
    void contactListAdd() {
        Register register = new Register();
        register.setContactPhoneList(contactList);
        register.setPerson(person);
        register(register);
    }
    @Background
    void register(Register register) {
        personRepository.registerPerson(register);
        postRegister();
    }
    void postRegister () {

        Intent intent = new Intent(this,LoginActivity_.class);
        startActivity(intent);
        RegisterActivity.this.finish();
    }
}
