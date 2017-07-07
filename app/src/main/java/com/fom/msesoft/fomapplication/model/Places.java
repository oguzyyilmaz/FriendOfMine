package com.fom.msesoft.fomapplication.model;

import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created by oguz on 30.06.2016.
 */
@Accessors(chain = true)
public class Places {

    @Getter
    private Long id;

    @Getter
    @Setter
    private String uniqueId;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String type;


}
