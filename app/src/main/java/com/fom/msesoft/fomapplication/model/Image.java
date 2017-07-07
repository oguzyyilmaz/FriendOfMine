package com.fom.msesoft.fomapplication.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


/**
 * Created by oguz on 01.08.2016.
 */
@Accessors(chain = true)
public class Image {
    @Getter
    @Setter
    String base64String;

    @Setter
    @Getter
    String token;

}
