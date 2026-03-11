package com.sellgirl.sellgirlPayWeb.service;

import java.util.List;

import com.sellgirl.sellgirlPayDomain.HyzlChange;
import com.sellgirl.sellgirlPayDomain.Test;
import com.sellgirl.sellgirlPayDomain.UsersModify;

public interface DemoService {
    public List<Test> testDemo1(Test vo);
    /**
     * 测试
     * @param vo
     * @return
     */
   public List<UsersModify> getUsersModify(UsersModify vo);

    public int addHyzlChange(HyzlChange vo);
}
