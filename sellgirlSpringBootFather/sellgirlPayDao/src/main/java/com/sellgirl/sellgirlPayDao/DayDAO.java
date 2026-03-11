package com.sellgirl.sellgirlPayDao;


import org.apache.ibatis.annotations.Mapper;
//import org.springframework.stereotype.Repository;

//import com.sellgirl.sellgirlPayDomain.HyzlChange;
import com.sellgirl.sellgirlPayDomain.SalesDay;
//import com.sellgirl.sellgirlPayDomain.Test;
//import com.sellgirl.sellgirlPayDomain.UsersModify;

import java.util.List;

@Mapper
public interface DayDAO  {
    /**
     * 测试
     * @param vo
     * @return
     */
    List<SalesDay> getSalesDay(SalesDay vo);

}