package com.baraka.barakamobile.ui.usermanaje;

public class WorkerViewModel {
    private String idUser;
    private String compUser;
    private String nameUser;
    private String emailUser;
    private String addrUser;
    private String lvlUser;
    private String phoneUser;
    private String loginUser;
    private String imgUser;

    private String idComp;
    private String nameComp;
    private String addrComp;
    private String phoneComp;
    private String emailComp;
    private String logoComp;

    public WorkerViewModel(String idUser,
                           String emailUser,
                           String nameUser,
                           String addrUser,
                           String lvlUser,
                           String phoneUser,
                           String loginUser,
                           String imgUser,
                           String compUser,
                           String nameComp){
        this.idUser = idUser;
        this.emailUser = emailUser;
        this.nameUser = nameUser;
        this.addrUser = addrUser;
        this.lvlUser = lvlUser;
        this.phoneUser = phoneUser;
        this.loginUser = loginUser;
        this.imgUser = imgUser;
        this.idComp = compUser;
        this.nameComp = nameComp;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getCompUser() {
        return compUser;
    }

    public void setCompUser(String compUser) {
        this.compUser = compUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
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

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public String getImgUser() {
        return imgUser;
    }

    public void setImgUser(String imgUser) {
        this.imgUser = imgUser;
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

    public String getLogoComp() {
        return logoComp;
    }

    public void setLogoComp(String logoComp) {
        this.logoComp = logoComp;
    }
}
