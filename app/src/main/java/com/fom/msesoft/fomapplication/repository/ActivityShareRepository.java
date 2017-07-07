package com.fom.msesoft.fomapplication.repository;

import com.fom.msesoft.fomapplication.config.Config;
import com.fom.msesoft.fomapplication.model.ActivityModelCustom;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * Created by oguz on 9/5/16.
 */
@Rest(rootUrl = Config.ROOT_URL+"activity",converters = { MappingJackson2HttpMessageConverter.class })
public interface ActivityShareRepository {
    @Post("/share?token={token}")
    void share(@Body ActivityModelCustom activityModelCustom,@Path String token);


    @Get("/allActivity?token={token}")
    ActivityModelCustom[] allActivity(@Path String token);
}
