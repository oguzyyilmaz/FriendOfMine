package com.fom.msesoft.fomapplication.extras;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface Preferences {

    @DefaultString("")
    String email();

    @DefaultString("")
    String password();

    @DefaultBoolean(false)
    boolean isLogin();

    long lastUpdated();


}
