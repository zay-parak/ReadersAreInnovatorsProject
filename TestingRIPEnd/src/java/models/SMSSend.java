/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "smsreq")
@XmlAccessorType(XmlAccessType.FIELD)
public class SMSSend {
    
    @XmlElement( name = "datetime")
    private String date;
    @XmlElement( name = "user")
    private String user;
    @XmlElement( name = "pass")
    private String password;
    @XmlElement( name = "msisdn")
    private String msisdn ;
    @XmlElement( name = "message")
    private String message;

    public SMSSend() {
    }

    public SMSSend(String date, String user, String password, String phoneNumber, String message) {
        this.date = date;
        this.user = user;
        this.password = password;
        this.msisdn = phoneNumber;
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

   

   

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SMSRequest{" + "date=" + date + ", user=" + user + ", password=" + password + ", phoneNumber=" + msisdn + ", message=" + message + '}';
    }
    
    
}
