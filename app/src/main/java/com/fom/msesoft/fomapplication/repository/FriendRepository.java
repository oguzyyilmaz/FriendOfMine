package com.fom.msesoft.fomapplication.repository;

import com.fom.msesoft.fomapplication.config.Config;
import com.fom.msesoft.fomapplication.model.CustomFriendRelationship;
import com.fom.msesoft.fomapplication.model.FriendRelationship;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * Created by oguz on 20.07.2016.
 */
@Rest(rootUrl = Config.ROOT_URL+"friendRelationShip",converters = { MappingJackson2HttpMessageConverter.class })
public interface FriendRepository {

    @Post("/saveFriend?token={token}&uniqueId={uniqueId}")
    void saveFriend(@Path String token,@Path String uniqueId);

    @Get("/friendWay?startNode={startNode}&endNode={endNode}")
    CustomFriendRelationship[] friendWay(@Path String startNode,@Path String endNode);
}
