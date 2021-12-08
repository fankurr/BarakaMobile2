package com.baraka.barakamobile.ui.usermanaje;

public class WorkerLogViewModel {
    private String idLogin;
    private String idCompLogin;
    private String userLogin;
    private String datetimeLogin;
    private String datetimeLogout;

    private String idUser;
    private String compUser;
    private String nameUser;
    private String emailUser;
    private String addrUser;
    private String lvlUser;
    private String phoneUser;
    private String loginUser;
    private String imgUser;

    public WorkerLogViewModel(String idLogin,
                              String idCompLogin,
                              String userLogin,
                              String nameUser,
                              String emailUser,
                              String datetimeLogin,
                              String datetimeLogout){
        this.idLogin = idLogin;
        this.idCompLogin = idCompLogin;
        this.userLogin = userLogin;
        this.nameUser = nameUser;
        this.emailUser = emailUser;
        this.datetimeLogin = datetimeLogin;
        this.datetimeLogout = datetimeLogout;
    }

    public String getIdLogin() {
        return idLogin;
    }

    public void setIdLogin(String idLogin) {
        this.idLogin = idLogin;
    }

    public String getIdCompLogin() {
        return idCompLogin;
    }

    public void setIdCompLogin(String idCompLogin) {
        this.idCompLogin = idCompLogin;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getDatetimeLogin() {
        return datetimeLogin;
    }

    public void setDatetimeLogin(String datetimeLogin) {
        this.datetimeLogin = datetimeLogin;
    }

    public String getDatetimeLogout() {
        return datetimeLogout;
    }

    public void setDatetimeLogout(String datetimeLogout) {
        this.datetimeLogout = datetimeLogout;
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

}
