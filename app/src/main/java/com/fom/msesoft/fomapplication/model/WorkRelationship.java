package com.fom.msesoft.fomapplication.model;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class WorkRelationship {


    private Long id;

    @Getter
    @Setter
    private String workType;

    @Getter
    @Setter
    private Person startNode;

    @Getter
    @Setter
    private Places endNode;


}
