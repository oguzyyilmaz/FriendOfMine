package com.fom.msesoft.fomapplication.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


@Accessors(chain = true)
public class CustomPerson {

    @Getter
    @Setter
    private String uniqueId;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String phoneNumber;

    @Setter
    @Getter
    private String firstName;

    @Setter
    @Getter
    private String lastName;

    @Getter
    @Setter
    private String gender;

    @Setter
    @Getter
    private String hoby;

    @Setter
    @Getter
    private String photo;

    @Getter
    @Setter
    private String occupation;

    @Getter
    @Setter
    private String school;

    @Getter
    @Setter
    private List<String> photoList = new ArrayList<>();

    @Getter
    @Setter
    private float popular;

    @Getter
    @Setter
    private ArrayList<String> contactFriend = new ArrayList<>();

    @Getter
    @Setter
    private boolean active;

}
