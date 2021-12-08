package com.baraka.barakamobile.ui.payout;

public class PayoutViewModelList {

    private String idPay;
    private String idCompPay;
    private String valPay;
    private String descPay;
    private String datetimePay;
    private String signPay;

    private String idComp;
    private String nameComp;
    private String addrComp;
    private String phoneComp;
    private String emailComp;

    private String idUser;
    private String emailUser;
    private String nameUser;
    private String addrUser;
    private String lvlUser;
    private String phoneUser;

    public PayoutViewModelList(String idPay,
                               String valPay,
                               String descPay,
                               String datetimePay,
                               String nameUser){
        this.idPay = idPay;
        this.valPay = valPay;
        this.descPay = descPay;
        this.datetimePay = datetimePay;
        this.nameUser = nameUser;
    }

    public String getIdPay() {
        return idPay;
    }

    public void setIdPay(String idPay) {
        this.idPay = idPay;
    }

    public String getIdCompPay() {
        return idCompPay;
    }

    public void setIdCompPay(String idCompPay) {
        this.idCompPay = idCompPay;
    }

    public String getValPay() {
        return valPay;
    }

    public void setValPay(String valPay) {
        this.valPay = valPay;
    }

    public String getDescPay() {
        return descPay;
    }

    public void setDescPay(String descPay) {
        this.descPay = descPay;
    }

    public String getDatetimePay() {
        return datetimePay;
    }

    public void setDatetimePay(String datetimePay) {
        this.datetimePay = datetimePay;
    }

    public String getSignPay() {
        return signPay;
    }

    public void setSignPay(String signPay) {
        this.signPay = signPay;
    }

    public String getIdComp() {
        return idComp;
    }

    public void setIdComp(String idComp) {
        this.idComp = idComp;
    }

    public String getNameComp() {
        return nameComp;
    }

    public void setNameComp(String nameComp) {
        this.nameComp = nameComp;
    }

    public String getAddrComp() {
        return addrComp;
    }

    public void setAddrComp(String addrComp) {
        this.addrComp = addrComp;
    }

    public String getPhoneComp() {
        return phoneComp;
    }

    public void setPhoneComp(String phoneComp) {
        this.phoneComp = phoneComp;
    }

    public String getEmailComp() {
        return emailComp;
    }

    public void setEmailComp(String emailComp) {
        this.emailComp = emailComp;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getAddrUser() {
        return addrUser;
    }

    public void setAddrUser(String addrUser) {
        this.addrUser = addrUser;
    }

    public String getLvlUser() {
        return lvlUser;
    }

    public void setLvlUser(String lvlUser) {
        this.lvlUser = lvlUser;
    }

    public String getPhoneUser() {
        return phoneUser;
    }

    public void setPhoneUser(String phoneUser) {
        this.phoneUser = phoneUser;
    }
}
