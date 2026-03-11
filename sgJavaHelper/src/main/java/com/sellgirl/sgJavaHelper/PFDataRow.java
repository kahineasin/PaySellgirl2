package com.sellgirl.sgJavaHelper;

import java.sql.Blob;
import java.sql.Date;
import java.util.List;

import com.sellgirl.sgJavaHelper.config.SGDataHelper;

public class PFDataRow {

    List<PFDataColumn> col;

    public  PFDataRow(List<PFDataColumn> c) {
        col = c;
    }
    
    /**
     * @功能描述 返回指定DataColumn类型数据列对象
     * @作者 叶小钗
     */
    public PFDataColumn getColumnObject(String colName) {
        for (PFDataColumn c : col) {
            if (c.getKey().toUpperCase().equals(colName.toUpperCase())) {
                try {
                    return c;
                } catch (Exception e) {
                    System.out.println("错误描述：" + e.toString());
                }
            }
        }
        return null;
    }
 
    /**
     * @功能描述 返回指定Object类型数据列对象
     * @作者 叶小钗
     */
    public Object getColumn(String colName) {
        for (PFDataColumn c : col) {
            if (c.getKey().toUpperCase().equals(colName.toUpperCase())) {
                try {
                    return c.getValue();
                } catch (Exception e) {
                    System.out.println("错误描述：" + e.toString());
                }
            }
        }
        return null;
    }
 
    /**
     * @功能描述 返回指定int类型数据列对象
     * @作者 叶小钗
     */
    public int getIntColumn(String colName) {
        for (PFDataColumn c : col) {
            if (c.getKey().toUpperCase().equals(colName.toUpperCase())) {
                try {
                    return Integer.parseInt(c.getValue().toString());
                } catch (Exception e) {
                    System.out.println("错误描述：" + e.toString());
                }
            }
        }
        return 0;
    }
 
    /**
     * @功能描述 返回指定String类型数据列对象
     * @作者 叶小钗
     */
    public String getStringColumn(String colName) {
        for (PFDataColumn c : col) {
            if (c.getKey().toUpperCase().equals(colName.toUpperCase())) {
                try {
                    return SGDataHelper.ObjectToString(c.getValue());
                } catch (Exception e) {
                    System.out.println("错误描述：" + e.toString());
                }
 
            }
        }
        return null;
    }
 
    /**
     * @功能描述 返回指定String类型数据列对象
     * @作者 叶小钗
     */
    public String eval(String colName) {
        for (PFDataColumn c : col) {
            if (c.getKey().toUpperCase().equals(colName.toUpperCase())) {
                try {
                    return c.getValue() + "";// 此方法将屏蔽错误！！！
                } catch (Exception e) {
                    System.out.println("错误描述：" + e.toString());
                }
            }
        }
        return null;
    }
 
    /**
     * @功能描述 返回指定Date类型数据列对象
     * @作者 叶小钗
     */
    public Date getDateColumn(String colName) {
        for (PFDataColumn c : col) {
            if (c.getKey().toUpperCase().equals(colName.toUpperCase())) {
                try {
                    return Date.valueOf(c.getValue().toString());
                } catch (Exception e) {
                    System.out.println("错误描述：" + e.toString());
                }
            }
        }
        return null;
    }
 
    /**
     * @功能描述 返回指定Blob类型数据列对象
     * @作者 叶小钗
     */
    public Blob getBlobColumn(String colName) {
        for (PFDataColumn c : col) {
            if (c.getKey().toUpperCase().equals(colName.toUpperCase())) {
                try {
                    return (Blob) c.getValue();
                } catch (Exception e) {
                    System.out.println("错误描述：" + e.toString());
                }
            }
        }
        return null;
    }
 
    /**
     * @功能描述 返回指定Blob类型数据列对象
     * @作者 叶小钗
     */
    public float getFloatColumn(String colName) {
        for (PFDataColumn c : col) {
            if (c.getKey().toUpperCase().equals(colName.toUpperCase())) {
                try {
                    return Float.parseFloat(c.getValue().toString());
                } catch (Exception e) {
                    System.out.println("错误描述：" + e.toString());
                }
            }
        }
        return 0;
    }
 
 
    public List<PFDataColumn> getCol() {
        return col;
    }
 
    public void setCol(List<PFDataColumn> col) {
        this.col = col;
    }
    public void set(String columnName,Object value) {
    	PFDataColumn col=getColumnObject(columnName);
    	if(col!=null) {
    		col.setValue(value);
    	}
    }
}
