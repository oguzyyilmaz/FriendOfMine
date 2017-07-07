package com.fom.msesoft.fomapplication.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created by oguz on 10/31/16.
 */

@Accessors(chain = true)
public class ContactModel {


    @Getter
    @Setter
    private String phoneNumber;

    @Getter
    @Setter
    private String name;
}
