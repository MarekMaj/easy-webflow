//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.12.10 at 02:03:57 AM CET 
//


package easywebflow.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for stateData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="stateData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="viewId" type="{}stringType" minOccurs="0"/>
 *         &lt;element name="secured" type="{}securedType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "stateData", propOrder = {
    "viewId",
    "secured"
})
public class StateData {

    protected String viewId;
    protected SecuredType secured;

    /**
     * Gets the value of the viewId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getViewId() {
        return viewId;
    }

    /**
     * Sets the value of the viewId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setViewId(String value) {
        this.viewId = value;
    }

    /**
     * Gets the value of the secured property.
     * 
     * @return
     *     possible object is
     *     {@link SecuredType }
     *     
     */
    public SecuredType getSecured() {
        return secured;
    }

    /**
     * Sets the value of the secured property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecuredType }
     *     
     */
    public void setSecured(SecuredType value) {
        this.secured = value;
    }

}
