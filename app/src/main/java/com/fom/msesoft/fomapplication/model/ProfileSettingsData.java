package com.fom.msesoft.fomapplication.model;

import com.squareup.picasso.Picasso;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by oguz on 15.07.2016.
 */
public class ProfileSettingsData {

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private int imageUrl;


    public ProfileSettingsData(String title, int imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }
}
