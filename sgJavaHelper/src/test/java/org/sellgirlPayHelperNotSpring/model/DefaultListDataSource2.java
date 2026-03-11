package org.sellgirlPayHelperNotSpring.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Optional;

public class DefaultListDataSource2<T extends List<?>> {

    public  DefaultListDataSource2(){}

    public  DefaultListDataSource2(T dataSource,Integer recordCount){
        this.DataSource=dataSource;
        this.RecordCount=recordCount;
    }

    @JsonProperty
    private T DataSource;

    public void setDataSource(T dataSource) {
		this.DataSource = dataSource;
	}

	@JsonProperty
    private Integer RecordCount;

    public void setRecordCount(Integer recordCount) {
		RecordCount = recordCount;
	}

	public T getDataSource() {
        return DataSource;
    }

    public Integer getRecordCount() {
        return Optional.ofNullable(RecordCount).orElse(0);
    }
}
