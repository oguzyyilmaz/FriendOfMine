package com.fom.msesoft.fomapplication.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


/**
 * Created by oguz on 18.06.2016.
 */

@Accessors(chain = true)
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    private Long id;

    @Getter
    @Setter
    private String uniqueId;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String phoneNumber;

    @Getter
    @Setter
    private String password;

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

    @Setter
    @Getter
    private String deviceID;

    @Getter
    @Setter
    private String token;

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
    private boolean active;
}
