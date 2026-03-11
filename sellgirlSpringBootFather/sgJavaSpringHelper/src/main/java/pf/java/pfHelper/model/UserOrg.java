//package pf.java.pfHelper.model;
//
//import com.alibaba.fastjson.annotation.JSONField;
//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//
//import pf.java.pfHelper.PFEnumClassConvert;
//import pf.java.pfHelper.PFEnumClassDeconvert;
//import pf.java.pfHelper.PFEnumClassDeserialiaer;
//import pf.java.pfHelper.PFEnumClassSerialiaer;
//
//public class UserOrg {
//
//    public String Org ;
//    public String OrgName;
//    @JSONField(name = "UserType", serializeUsing = PFEnumClassConvert.class,deserializeUsing=PFEnumClassDeconvert.class)
//    @JsonSerialize(using = PFEnumClassSerialiaer.class)
//    @JsonDeserialize(using = PFEnumClassDeserialiaer.class)
//    public UserTypeClass UserType ;
//			
//}
