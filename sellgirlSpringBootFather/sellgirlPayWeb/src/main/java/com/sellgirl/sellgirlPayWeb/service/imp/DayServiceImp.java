package com.sellgirl.sellgirlPayWeb.service.imp;

import com.sellgirl.sellgirlPayDao.DayDAO;
//import com.sellgirl.sellgirlPayDao.TestDAO;
//import com.sellgirl.sellgirlPayDomain.HyzlChange;
import com.sellgirl.sellgirlPayDomain.SalesDay;
//import com.sellgirl.sellgirlPayDomain.Test;
//import com.sellgirl.sellgirlPayDomain.UsersModify;
import com.sellgirl.sellgirlPayWeb.service.DayService;
//import com.sellgirl.sellgirlPayWeb.service.DemoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DayServiceImp implements DayService {
	
	@Autowired private DayDAO _dayDAO;
	
	@Override
	public List<SalesDay> getSalesDay(SalesDay vo) {
		// TODO Auto-generated method stub
		return _dayDAO.getSalesDay(vo);
	}
}
