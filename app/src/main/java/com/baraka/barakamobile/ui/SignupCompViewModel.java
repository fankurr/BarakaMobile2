package com.baraka.barakamobile.ui;

public class SignupCompViewModel {

    private String idComp;
    private String nameComp;
    private String codeComp;
    private String addrComp;
    private String phoneComp;
    private String emailComp;
    private String logoComp;

    public SignupCompViewModel(String idComp,
                               String nameComp,
                               String codeComp,
                               String addrComp,
                               String phoneComp,
                               String emailComp,
                               String logoComp){
        this.idComp = idComp;
        this.nameComp = nameComp;
        this.codeComp = codeComp;
        this.addrComp = addrComp;
        this.phoneComp = phoneComp;
        this.emailComp = emailComp;
        this.logoComp = logoComp;
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

    public String getCodeComp() {
        return codeComp;
    }

    public void setCodeComp(String codeComp) {
        this.codeComp = codeComp;
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
