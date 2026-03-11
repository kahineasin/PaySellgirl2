package org.sellgirlPayHelperNotSpring.model;

import java.util.List;

//public class HealthOrdersApiResult extends AbstractApiResult<HealthOrdersResult2> {
//}
//class HealthOrdersResult2
//{
//    //public IList<DayOrdersCreate> data { get; set; }//暂不使用这个属性
//    public Integer total ;
//}

public class HealthOrdersApiResult<T>
{
    //public IList<DayOrdersCreate> data { get; set; }//暂不使用这个属性
   // public List<t_orders> data;//暂不使用这个属性
	public List<T> data;//暂不使用这个属性
    public Integer total ;
}