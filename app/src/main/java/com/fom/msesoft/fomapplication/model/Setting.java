package com.fom.msesoft.fomapplication.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created by kerse on 05.09.2016.
 */
@Accessors(chain = true)
public class Setting {

    @Getter
    @Setter
    private String settingType;

    @Getter
    @Setter
    private String settingValue;
}
