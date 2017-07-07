package com.fom.msesoft.fomapplication.model;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class FriendRelationship {


    private Long id;

    @Getter
    @Setter
    private String friendType;

    @Getter
    @Setter
    private Person startNode;

    @Getter
    @Setter
    private Person endNode;


}
