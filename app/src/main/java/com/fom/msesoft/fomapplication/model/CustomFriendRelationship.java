package com.fom.msesoft.fomapplication.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created by oguz on 8/11/16.
 */
@Accessors(chain = true)
public class CustomFriendRelationship {
    @Getter
    @Setter
    private String friendType;

    @Getter
    @Setter
    private CustomPerson startNode;

    @Getter
    @Setter
    private CustomPerson endNode;
}
