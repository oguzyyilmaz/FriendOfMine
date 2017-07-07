package com.fom.msesoft.fomapplication.repository;


import com.fom.msesoft.fomapplication.config.Config;
import com.fom.msesoft.fomapplication.model.Places;

import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * Created by oguz on 17.07.2016.
 */
@Rest(rootUrl = Config.ROOT_URL+"places",converters = { MappingJackson2HttpMessageConverter.class })
public interface PlacesRepository {

    @Get("/personWorkSearch?uniqueId={uniqueId}")
    Places personWorkSearch (@Path String uniqueId);
}
