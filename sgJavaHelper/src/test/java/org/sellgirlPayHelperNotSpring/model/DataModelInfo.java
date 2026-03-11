package org.sellgirlPayHelperNotSpring.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.UUID;

public class DataModelInfo {

    @JsonProperty
    public UUID Id;

    @JsonProperty
    public String DatamodelQueryName;

    @JsonProperty
    public String QueryConfig;

    @JsonProperty
    public String QueryType;

    @JsonProperty
    public String Description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty
    public Date Time;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty
    public Date UpdateTime;

    @JsonProperty
    public String UserId;

    @JsonProperty
    public Boolean Enable;

    @JsonProperty
    public UUID DatabaseId;

    @JsonProperty
    public UUID GroupId;

    @JsonProperty
    public Boolean Cache;

    @JsonProperty
    public Integer CacheInterval;

    @JsonProperty
    public String CacheResult;

    @JsonProperty
    public Integer DatamodelQueryId;

//    @JsonProperty
//    public Integer ReqAvgUseMs;
//
//    @JsonProperty
//    public Boolean UserEnableCache;
//
//    @JsonProperty
//    public Integer Visits;

}
