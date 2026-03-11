package com.sellgirl.sellgirlPayWeb.aop;

import com.sellgirl.sellgirlPayDao.DayDAO;
import com.sellgirl.sellgirlPayDao.TestDAO;
import com.sellgirl.sellgirlPayWeb.dataSource.DatabaseContextHolder;
import com.sellgirl.sellgirlPayWeb.dataSource.DatabaseType;
//import com.sellgirl.sellgirlPayWeb.service.Demo2Service;
//import com.sellgirl.sellgirlPayWeb.service.DemoService;
//import com.sellgirl.sellgirlPayWeb.service.imp.Demo2ServiceImp;
//import com.sellgirl.sellgirlPayWeb.service.imp.DemoServiceImp;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DataSourceAspect {

///**
// * 使用空方法定义切点表达式
// */
//		@Pointcut("execution(* com.sellgirl.*.service.*.*(..))")
//public void declareJointPointExpression() {
//        }
//
//@Before("declareJointPointExpression()")
//public void setDataSourceKey(JoinPoint point){
//        //根据连接点所属的类实例，动态切换数据源
//        if (point.getTarget() instanceof DemoService
//        || point.getTarget() instanceof DemoServiceImp) {
//        DatabaseContextHolder.setDatabaseType(DatabaseType.mytestdb);
//        } else if((point.getTarget() instanceof Demo2Service
//                || point.getTarget() instanceof Demo2ServiceImp)) {
//        DatabaseContextHolder.setDatabaseType(DatabaseType.mytestdb2);
//        }
//        }
	
     /**
      * 使用空方法定义切点表达式
      */
	@Pointcut("execution(* com.sellgirl.sellgirlPayDao.*.*(..))")
     public void declareJointPointExpression() {
     }
 
     /**
      * 使用定义切点表达式的方法进行切点表达式的引入
      */
     @Before("declareJointPointExpression()")
     public void setDataSourceKey(JoinPoint point) {
         // 连接点所属的类实例是ShopDao
       if (point.getTarget() instanceof TestDAO) {
    	   DatabaseContextHolder.setDatabaseType(DatabaseType.mytestdb);
       } else if (point.getTarget() instanceof DayDAO){
    	   DatabaseContextHolder.setDatabaseType(DatabaseType.dayDb);
       } else  {
    	   DatabaseContextHolder.setDatabaseType(DatabaseType.mytestdb2);
       }
       }
}