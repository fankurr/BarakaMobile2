package com.baraka.barakamobile.ui.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrdctViewModel {

    private String idPrdct;
    private String idCompPrdct;
    private String idCatPrdct;
    private String namePrdct;
    private String codePrdct;
    private String nameSplrPrdct;
    private String unitPrice;
    private String unitPrdct;
    private String stockPrdct;
    private String lastUpdate;
    private String qtyUpdate;
    private String updateBy;
    private String descPrdct;
    private String imgPrdct;

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

    private String idSplr;
    private String idCompSplr;
    private String nameSplr;
    private String addrSplr;
    private String phoneSplr;
    private String emailSplr;
    private String descSplr;
    private String imgSplr;

    public PrdctViewModel(String idPrdct,
                          String namePrdct,
                          String codePrdct,
                          String nameSplrPrdct,
                          String unitPrice,
                          String unitPrdct,
                          String stockPrdct,
                          String lastUpdate,
                          String qtyUpdate,
                          String updateBy,
                          String descPrdct,
                          String imgPrdct,
                          String idCat,
                          String idCompCat,
                          String nameCat,
                          String descCat,
                          String imgCat,
                          String idSplr,
                          String idCompSplr,
                          String nameSplr,
                          String addrSplr,
                          String phoneSplr,
                          String emailSplr,
                          String descSplr,
                          String imgSplr) {
        this.idPrdct = idPrdct;
        this.idCompPrdct = idCompPrdct;
        this.namePrdct = namePrdct;
        this.idCatPrdct = idCatPrdct;
        this.codePrdct = codePrdct;
        this.nameSplrPrdct = nameSplrPrdct;
        this.unitPrice = unitPrice;
        this.unitPrdct = unitPrdct;
        this.stockPrdct = stockPrdct;
        this.lastUpdate = lastUpdate;
        this.qtyUpdate = qtyUpdate;
        this.updateBy = updateBy;
        this.descPrdct = descPrdct;
        this.imgPrdct = imgPrdct;
        this.idCat = idCat;
        this.idCompCat = idCompCat;
        this.nameCat = nameCat;
        this.descCat = descCat;
        this.imgCat = imgCat;
        this.idComp = idComp;
        this.nameComp = nameComp;
        this.addrComp = addrComp;
        this.phoneComp = phoneComp;
        this.emailComp = emailComp;
        this.logoComp = logoComp;
        this.idSplr = idSplr;
        this.idCompSplr = idCompSplr;
        this.nameSplr = nameSplr;
        this.addrSplr = addrSplr;
        this.phoneSplr = phoneSplr;
        this.emailSplr = emailSplr;
        this.descSplr = descSplr;
        this.imgSplr = imgSplr;
    }

    public String getIdPrdct() {
        return idPrdct;
    }

    public void setIdPrdct(String idPrdct) {
        this.idPrdct = idPrdct;
    }

    public String getIdCompPrdct() {
        return idCompPrdct;
    }

    public void setIdCompPrdct(String idCompPrdct) {
        this.idCompPrdct = idCompPrdct;
    }

    public String getIdCatPrdct() {
        return idCatPrdct;
    }

    public void setIdCatPrdct(String idCatPrdct) {
        this.idCatPrdct = idCatPrdct;
    }

    public String getNamePrdct() {
        return namePrdct;
    }

    public void setNamePrdct(String namePrdct) {
        this.namePrdct = namePrdct;
    }

    public String getCodePrdct() {
        return codePrdct;
    }

    public void setCodePrdct(String codePrdct) {
        this.codePrdct = codePrdct;
    }

    public String getNameSplrPrdct() {
        return nameSplrPrdct;
    }

    public void setNameSplrPrdct(String nameSplrPrdct) {
        this.nameSplrPrdct = nameSplrPrdct;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getUnitPrdct() {
        return unitPrdct;
    }

    public void setUnitPrdct(String unitPrdct) {
        this.unitPrdct = unitPrdct;
    }

    public String getStockPrdct() {
        return stockPrdct;
    }

    public void setStockPrdct(String stockPrdct) {
        this.stockPrdct = stockPrdct;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getQtyUpdate() {
        return qtyUpdate;
    }

    public void setQtyUpdate(String qtyUpdate) {
        this.qtyUpdate = qtyUpdate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getDescPrdct() {
        return descPrdct;
    }

    public void setDescPrdct(String descPrdct) {
        this.descPrdct = descPrdct;
    }

    public String getImgPrdct() {
        return imgPrdct;
    }

    public void setImgPrdct(String imgPrdct) {
        this.imgPrdct = imgPrdct;
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

    public String getIdSplr() {
        return idSplr;
    }

    public void setIdSplr(String idSplr) {
        this.idSplr = idSplr;
    }

    public String getIdCompSplr() {
        return idCompSplr;
    }

    public void setIdCompSplr(String idCompSplr) {
        this.idCompSplr = idCompSplr;
    }

    public String getNameSplr() {
        return nameSplr;
    }

    public void setNameSplr(String nameSplr) {
        this.nameSplr = nameSplr;
    }

    public String getAddrSplr() {
        return addrSplr;
    }

    public void setAddrSplr(String addrSplr) {
        this.addrSplr = addrSplr;
    }

    public String getPhoneSplr() {
        return phoneSplr;
    }

    public void setPhoneSplr(String phoneSplr) {
        this.phoneSplr = phoneSplr;
    }

    public String getEmailSplr() {
        return emailSplr;
    }

    public void setEmailSplr(String emailSplr) {
        this.emailSplr = emailSplr;
    }

    public String getDescSplr() {
        return descSplr;
    }

    public void setDescSplr(String descSplr) {
        this.descSplr = descSplr;
    }

    public String getImgSplr() {
        return imgSplr;
    }

    public void setImgSplr(String imgSplr) {
        this.imgSplr = imgSplr;
    }
}
