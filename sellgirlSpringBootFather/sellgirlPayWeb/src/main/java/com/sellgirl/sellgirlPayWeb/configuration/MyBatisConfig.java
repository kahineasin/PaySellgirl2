package com.sellgirl.sellgirlPayWeb.configuration;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.sellgirl.sellgirlPayWeb.configuration.jdbc.*;
import com.sellgirl.sellgirlPayWeb.dataSource.DatabaseType;
import com.sellgirl.sellgirlPayWeb.dataSource.DynamicDataSource;

import com.sellgirl.sgJavaHelper.sql.ISGJdbc;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

//import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
//import java.util.List;
import java.util.Map;
import java.util.Properties;

@Configuration // 该注解类似于spring配置文件
public class MyBatisConfig {

    @Autowired
    private Environment env;
//    @Autowired
//    private Jdbc jdbc;
    @Autowired
    private DbSellerJdbc dbSellerJdbc;
    @Autowired
    private DayJdbc dayJdbc;
    
    private Map<DatabaseType,DataSource> _dataSources=null;

    /**
     * @Primary 该注解表示在同一个接口有多个实现类可以注入的时候，默认选择哪一个，而不是让@Autowire注解报错
     * @Qualifier 根据名称进行注入，通常是在具有相同的多个类型的实例的一个注入（例如有多个DataSource类型的实例）
     */
    @Bean
    @Primary
    public DynamicDataSource dataSource() {
    	
        Map<Object, Object> targetDataSources = new HashMap<>();
        
        Map<DatabaseType,DataSource> dataSources=GetDataSources();
  
        Iterator<DatabaseType> iter = dataSources.keySet().iterator();
        while(iter.hasNext()){
        	DatabaseType key=iter.next();
        	DataSource value = dataSources.get(key);
        	targetDataSources.put(key, value);
        }   

        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSources);// 该方法是AbstractRoutingDataSource的方法
        dataSource.setDefaultTargetDataSource(dataSources.get(DatabaseType.mytestdb));// 默认的dataSource设置为myTestDbDataSource

        return dataSource;
    }

    /**
     * 根据数据源创建SqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception{
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(this.dataSource());
        fb.setTypeAliasesPackage(env.getProperty("mybatis.typeAliasesPackage"));
        fb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(env.getProperty("mybatis.mapperLocations")));
        return fb.getObject();
    }
    
    /**
     * 配置事务管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DynamicDataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }
    
    private DataSource GetDataSourceByJdbc(ISGJdbc jdbc) throws Exception {
        Properties props = new Properties();
        props.put("driverClassName",jdbc.getDriverClassName());
        props.put("url", jdbc.getUrl());
        props.put("username", jdbc.getUsername());
        props.put("password", jdbc.getPassword());
        return DruidDataSourceFactory.createDataSource(props);
    }
    private Map<DatabaseType,DataSource> GetDataSources() {
    	if(_dataSources==null) {
            _dataSources= new HashMap<DatabaseType,DataSource>();
            try {
				_dataSources.put(DatabaseType.mytestdb, GetDataSourceByJdbc(dbSellerJdbc));
				_dataSources.put(DatabaseType.dayDb, GetDataSourceByJdbc(dayJdbc));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
        return _dataSources;
    }
}
