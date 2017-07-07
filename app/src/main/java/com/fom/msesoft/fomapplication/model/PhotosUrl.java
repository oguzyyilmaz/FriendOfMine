package com.fom.msesoft.fomapplication.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created by oguz on 03.08.2016.
 */
@Accessors(chain = true)
public class PhotosUrl {

    @Getter
    @Setter
    private String url;
}
