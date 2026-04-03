package com.sellgirl.sellgirlPayService;

import com.sellgirl.sellgirlPayDao.DaoTest;

public class ServiceTest {
	public String showService() {
		DaoTest daoTest=new DaoTest();
		return daoTest.showDao()+ ":我是Service!"; 
	}
}
