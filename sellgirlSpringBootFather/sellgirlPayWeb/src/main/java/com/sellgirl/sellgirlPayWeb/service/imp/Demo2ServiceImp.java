package com.sellgirl.sellgirlPayWeb.service.imp;

import com.sellgirl.sellgirlPayDao.TestDAO;
import com.sellgirl.sellgirlPayDomain.Test;
import com.sellgirl.sellgirlPayWeb.service.Demo2Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Demo2ServiceImp implements Demo2Service {
    @Autowired
    private TestDAO testDAO;//注入失败benjamin todo

    @Override
    public List<Test> testDemo2(Test vo) {
        return testDAO.getTest(vo);
    	//return null;
    }
}
