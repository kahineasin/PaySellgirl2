
package com.sellgirl.sellgirlPayWeb.oAuth.model;

//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlType;


/**
 * <p>ActionReturnInfo complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ActionReturnInfo">
 *   &lt;complexContent>
 *     &lt;extension base="{http://right.perfect99.com/AMSWeb/}BaseReturnInfo">
 *       &lt;sequence>
 *         &lt;element name="Actions" type="{http://right.perfect99.com/AMSWeb/}ArrayOfActionInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(name = "ActionReturnInfo", propOrder = {
//    "actions"
//})
public class ActionReturnInfo
    extends BaseReturnInfoDto
{

//    @XmlElement(name = "Actions")
    protected ArrayOfActionInfo actions;

    /**
     * 获取actions属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfActionInfo }
     *     
     */
    public ArrayOfActionInfo getActions() {
        return actions;
    }

    /**
     * 设置actions属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfActionInfo }
     *     
     */
    public void setActions(ArrayOfActionInfo value) {
        this.actions = value;
    }

}
