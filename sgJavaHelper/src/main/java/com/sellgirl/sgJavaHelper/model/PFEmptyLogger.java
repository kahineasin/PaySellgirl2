////package com.sellgirl.pfHelperNotSpring.model;
////
////import org.slf4j.Logger;
////import org.slf4j.Marker;
////
////import com.sellgirl.pfHelperNotSpring.PFDate;
////
/////**
//// * 此类无用,因为 org.slf4j.LoggerFactory.getLogger() 如果报错,就会整个程序报错, catch异常也没用
//// * @author 1011712002
//// *
//// */
////public class PFEmptyLogger implements Logger{
////
////	String classStr;
////	public PFEmptyLogger(Class<?> cls) {
////		classStr=cls.getName();
////	}
////	private void printHeader() {
////		System.out.println("PFEmptyLogger:");
////		System.out.println(classStr+" "+PFDate.Now().toString());
////	}
////	@Override
////	public String getName() {
////		return this.getName();
////	}
////
////	@Override
////	public boolean isTraceEnabled() {
////		// TODO Auto-generated method stub
////		return true;
////	}
////
////	@Override
////	public void trace(String msg) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void trace(String format, Object arg) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void trace(String format, Object arg1, Object arg2) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void trace(String format, Object... arguments) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void trace(String msg, Throwable t) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public boolean isTraceEnabled(Marker marker) {
////		// TODO Auto-generated method stub
////		return true;
////	}
////
////	@Override
////	public void trace(Marker marker, String msg) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void trace(Marker marker, String format, Object arg) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void trace(Marker marker, String format, Object arg1, Object arg2) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void trace(Marker marker, String format, Object... argArray) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void trace(Marker marker, String msg, Throwable t) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public boolean isDebugEnabled() {
////		// TODO Auto-generated method stub
////		return true;
////	}
////
////	@Override
////	public void debug(String msg) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void debug(String format, Object arg) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void debug(String format, Object arg1, Object arg2) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void debug(String format, Object... arguments) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void debug(String msg, Throwable t) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public boolean isDebugEnabled(Marker marker) {
////		// TODO Auto-generated method stub
////		return true;
////	}
////
////	@Override
////	public void debug(Marker marker, String msg) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void debug(Marker marker, String format, Object arg) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void debug(Marker marker, String format, Object arg1, Object arg2) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void debug(Marker marker, String format, Object... arguments) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void debug(Marker marker, String msg, Throwable t) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public boolean isInfoEnabled() {
////		return true;
////	}
////
////	@Override
////	public void info(String msg) {
////		this.printHeader();
////		System.out.print(msg);
////	}
////
////	@Override
////	public void info(String format, Object arg) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void info(String format, Object arg1, Object arg2) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void info(String format, Object... arguments) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void info(String msg, Throwable t) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public boolean isInfoEnabled(Marker marker) {
////		// TODO Auto-generated method stub
////		return true;
////	}
////
////	@Override
////	public void info(Marker marker, String msg) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void info(Marker marker, String format, Object arg) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void info(Marker marker, String format, Object arg1, Object arg2) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void info(Marker marker, String format, Object... arguments) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void info(Marker marker, String msg, Throwable t) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public boolean isWarnEnabled() {
////		// TODO Auto-generated method stub
////		return true;
////	}
////
////	@Override
////	public void warn(String msg) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void warn(String format, Object arg) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void warn(String format, Object... arguments) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void warn(String format, Object arg1, Object arg2) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void warn(String msg, Throwable t) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public boolean isWarnEnabled(Marker marker) {
////		// TODO Auto-generated method stub
////		return true;
////	}
////
////	@Override
////	public void warn(Marker marker, String msg) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void warn(Marker marker, String format, Object arg) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void warn(Marker marker, String format, Object arg1, Object arg2) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void warn(Marker marker, String format, Object... arguments) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void warn(Marker marker, String msg, Throwable t) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public boolean isErrorEnabled() {
////		// TODO Auto-generated method stub
////		return true;
////	}
////
////	@Override
////	public void error(String msg) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void error(String format, Object arg) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void error(String format, Object arg1, Object arg2) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void error(String format, Object... arguments) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void error(String msg, Throwable t) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public boolean isErrorEnabled(Marker marker) {
////		// TODO Auto-generated method stub
////		return true;
////	}
////
////	@Override
////	public void error(Marker marker, String msg) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void error(Marker marker, String format, Object arg) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void error(Marker marker, String format, Object arg1, Object arg2) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void error(Marker marker, String format, Object... arguments) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	@Override
////	public void error(Marker marker, String msg, Throwable t) {
////		// TODO Auto-generated method stub
////		
////	}
////
////}
//package com;
//
//


