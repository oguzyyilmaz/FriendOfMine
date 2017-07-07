package com.fom.msesoft.fomapplication.repository;


import com.fom.msesoft.fomapplication.config.Config;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.fom.msesoft.fomapplication.model.Image;
import com.fom.msesoft.fomapplication.model.Person;
import com.fom.msesoft.fomapplication.model.Register;
import com.fom.msesoft.fomapplication.model.Token;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;


@Rest(rootUrl = Config.ROOT_URL+"person",converters = { MappingJackson2HttpMessageConverter.class })
public interface PersonRepository {

    @Get("/regGCM?token={token}&regId={regId}")
    void registerGCM(@Path String token,@Path String regId);

    @Get("/signIn?email={email}&password={password}")
    Token signIn (@Path String email, @Path String password);

    @Get("/findByFirstDegreeFriend?token={token}")
    CustomPerson[] findByFirstDegreeFriend(@Path String token);

    @Get("/workNotFriend?uniqueId={uniqueId}")
    CustomPerson[] workNotFriend(@Path String uniqueId);

    @Post("/signUp")
    Person insert(@Body Person person);

    @Post("/findContact")
    String[] findContact(@Body List<String> listNumber);

    @Get("/friendDegree?token={token}&like={like}&degree={degree}&skip={skip}")
    CustomPerson[] findDegreeFriend(@Path String token,@Path String like, @Path int degree , @Path int skip);

    @Get("/findByToken?token={token}")
    CustomPerson findByToken(@Path String token);

    @Get("/findByUniqueId?uniqueId={uniqueId}")
    CustomPerson findPersonByUniqueId(@Path String uniqueId);

    @Post("/uploadPhoto")
    void uploadPhoto(@Body Image image);

    @Get("/media?token={token}")
    CustomPerson loadingPhoto(@Path String token);

    @Post("/uploadProfilePhoto")
    void uploadProfilePhoto(@Body Image image);

    @Get("/deletePhoto?token={token}&photoId={photoId}&index={index}")
    void deletePhoto(@Path String token,@Path String photoId,@Path int index);


    @Get("/notifies?token={token}")
    CustomPerson[] notifies(@Path String token);

    @Post("/registerPerson")
    void registerPerson(@Body Register register);

    @Get("/friendSize?token={token}")
    Integer findByFriendSize(@Path String token);
}

