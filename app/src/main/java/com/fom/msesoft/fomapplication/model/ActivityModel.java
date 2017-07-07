package com.fom.msesoft.fomapplication.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


@Accessors(chain = true)
public class ActivityModel {

    @Getter
    private Long id;

    @Getter
    @Setter
    private String uniqueId;

    @Getter
    @Setter
    private String personUniqueId;

    @Getter
    @Setter
    private String activityArea;
    @Getter
    @Setter
    private String activityName;

    @Getter
    @Setter
    private String activityDate;

    @Getter
    @Setter
    private String activityPhoto;

    @Getter
    @Setter
    private String activityPlaces;

    @Getter
    @Setter
    private String activityDescription;

    @Getter
    @Setter
    private List<String> activityJoinPerson;

    @Getter
    @Setter
    private String photoId;

    @Getter
    @Setter
    private List<String> joinList;

}
