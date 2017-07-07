package com.fom.msesoft.fomapplication.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created by oguz on 9/6/16.
 */
@Accessors(chain = true)
public class ActivityModelCustom {

    @Getter
    private Long id;

    @Getter
    @Setter
    private String uniqueId;

    @Getter
    @Setter
    private CustomPerson person;
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
    private List<CustomPerson> activityJoinPerson;

    @Getter
    @Setter
    private String photoId;

    @Getter
    @Setter
    private Image image;

    @Getter
    @Setter
    private List<String> joinList;

}
