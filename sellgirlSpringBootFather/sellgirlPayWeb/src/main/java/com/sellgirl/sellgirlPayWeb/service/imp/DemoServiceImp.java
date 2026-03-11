package com.sellgirl.sellgirlPayWeb.service.imp;

import com.sellgirl.sellgirlPayDao.TestDAO;
import com.sellgirl.sellgirlPayDomain.HyzlChange;
import com.sellgirl.sellgirlPayDomain.Test;
import com.sellgirl.sellgirlPayDomain.UsersModify;
import com.sellgirl.sellgirlPayWeb.service.DemoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoServiceImp implements DemoService {
	
	@Autowired private TestDAO testDAO;
	
    @Override
    public List<Test> testDemo1(Test vo) {
    	//return null;
        List<Test> test = testDAO.getTest(vo);
        return test;
    }

    @Override
    public List<UsersModify> getUsersModify(UsersModify vo) {
    	//return null;
        List<UsersModify> test = testDAO.getUsersModify(vo);
        return test;
    }

    @Override
    public int addHyzlChange(HyzlChange vo) {
//    	return -1;
       return testDAO.addHyzlChange(vo);
    }
}
