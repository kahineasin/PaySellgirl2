package org.sellgirlPayHelperNotSpring.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Optional;

public class DefaultListDataSource<T extends List<?>> {

    public  DefaultListDataSource(){}

    public  DefaultListDataSource(T dataSource,Integer recordCount){
        this.DataSource=dataSource;
        this.RecordCount=recordCount;
    }

    @JsonProperty
    private T DataSource;

    @JsonProperty
    private Integer RecordCount;

    public T getDataSource() {
        return DataSource;
    }

    public Integer getRecordCount() {
        return Optional.ofNullable(RecordCount).orElse(0);
    }
}
