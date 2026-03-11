package com.sellgirl.sellgirlPayDao;


import org.apache.ibatis.annotations.Mapper;
//import org.springframework.stereotype.Repository;

import com.sellgirl.sellgirlPayDomain.HyzlChange;
import com.sellgirl.sellgirlPayDomain.Test;
import com.sellgirl.sellgirlPayDomain.UsersModify;

import java.util.List;
@Mapper
//@Repository
public interface TestDAO  {
    /**
     * 测试
     * @param vo
     * @return
     */
    List<Test> getTest(Test vo);
    List<UsersModify> getUsersModify(UsersModify vo);
    /**
     * 测试
     * @param vo
     * @return
     */
    List<HyzlChange> getHyzlChange(HyzlChange vo);
    int addHyzlChange(HyzlChange vo);

}