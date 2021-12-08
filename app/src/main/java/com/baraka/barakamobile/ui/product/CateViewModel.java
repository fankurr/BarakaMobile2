package com.baraka.barakamobile.ui.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CateViewModel {

    private String idCat;
    private String idCompCat;
    private String nameCat;
    private String descCat;
    private String imgCat;

    private String idComp;
    private String nameComp;
    private String addrComp;
    private String phoneComp;
    private String emailComp;
    private String logoComp;

    public CateViewModel(String idCat, String idCompCat, String nameCat, String descCat, String imgCat){
        this.idCat = idCat;
        this.idCompCat = idCompCat;
        this.nameCat = nameCat;
        this.descCat = descCat;
        this.imgCat = imgCat;
    }

    public String getIdCat() {
        return idCat;
    }

    public void setIdCat(String idCat) {
        this.idCat = idCat;
    }

    public String getIdCompCat() {
        return idCompCat;
    }

    public void setIdCompCat(String idCompCat) {
        this.idCompCat = idCompCat;
    }

    public String getNameCat() {
        return nameCat;
    }

    public void setNameCat(String nameCat) {
        this.nameCat = nameCat;
    }

    public String getDescCat() {
        return descCat;
    }

    public void setDescCat(String descCat) {
        this.descCat = descCat;
    }

    public String getImgCat() {
        return imgCat;
    }

    public void setImgCat(String imgCat) {
        this.imgCat = imgCat;
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