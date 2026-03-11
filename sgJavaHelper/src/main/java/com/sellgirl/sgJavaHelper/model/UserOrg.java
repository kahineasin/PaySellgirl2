package com.sellgirl.sgJavaHelper.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sellgirl.sgJavaHelper.PFEnumClassConvert;
import com.sellgirl.sgJavaHelper.PFEnumClassDeconvert;
import com.sellgirl.sgJavaHelper.PFEnumClassDeserialiaer;
import com.sellgirl.sgJavaHelper.PFEnumClassSerialiaer;

public class UserOrg {

    public String Org ;
    public String OrgName;
    @JSONField(name = "UserType", serializeUsing = PFEnumClassConvert.class,deserializeUsing=PFEnumClassDeconvert.class)
    @JsonSerialize(using = PFEnumClassSerialiaer.class)
    @JsonDeserialize(using = PFEnumClassDeserialiaer.class)
    public UserTypeClass UserType ;
			
}
