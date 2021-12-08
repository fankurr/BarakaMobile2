package com.baraka.barakamobile.ui.sell;

public class TxViewModel {
    private int idTx;
    private int idCompTx;
    private int idPrdctTx;
    private String qtyTx;
    private String datetimeTx;

    private int idComp;
    private String nameComp;
    private String addrComp;
    private String phoneComp;
    private String emailComp;
    private String logoComp;

    private int idPrdct;
    private String idCompPrdct;
    private String catPrdct;
    private String namePrdct;
    private String codePrdct;
    private String nameSplr;
    private String unitPrice;
    private String unitPrdct;
    private String stockPrdct;
    private String lastUpdate;
    private String qtyUpdate;
    private String updateBy;
    private String descPrdct;
    private String imgPrdct;

    public TxViewModel(int idTx,
                       int idCompTx,
                       String nameComp,
                       int idPrdctTx,
                       String namePrdct,
                       String qtyTx,
                       String unitPrdct,
                       String datetimeTx) {
        this.idTx = idTx;
        this.idCompTx = idCompTx;
        this.idPrdctTx = idPrdctTx;
        this.qtyTx = qtyTx;
        this.unitPrdct = unitPrdct;
        this.datetimeTx = datetimeTx;
        this.nameComp = nameComp;
        this.namePrdct = namePrdct;
    }

    public int getIdTx() {
        return idTx;
    }

    public void setIdTx(int idTx) {
        this.idTx = idTx;
    }

    public int getIdCompTx() {
        return idCompTx;
    }

    public void setIdCompTx(int idCompTx) {
        this.idCompTx = idCompTx;
    }

    public int getIdPrdctTx() {
        return idPrdctTx;
    }

    public void setIdPrdctTx(int idPrdctTx) {
        this.idPrdctTx = idPrdctTx;
    }

    public String getQtyTx() {
        return qtyTx;
    }

    public void setQtyTx(String qtyTx) {
        this.qtyTx = qtyTx;
    }

    public String getDatetimeTx() {
        return datetimeTx;
    }

    public void setDatetimeTx(String datetimeTx) {
        this.datetimeTx = datetimeTx;
    }

    public int getIdComp() {
        return idComp;
    }

    public void setIdComp(int idComp) {
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

    public int getIdPrdct() {
        return idPrdct;
    }

    public void setIdPrdct(int idPrdct) {
        this.idPrdct = idPrdct;
    }

    public String getIdCompPrdct() {
        return idCompPrdct;
    }

    public void setIdCompPrdct(String idCompPrdct) {
        this.idCompPrdct = idCompPrdct;
    }

    public String getCatPrdct() {
        return catPrdct;
    }

    public void setCatPrdct(String catPrdct) {
        this.catPrdct = catPrdct;
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

    public String getNameSplr() {
        return nameSplr;
    }

    public void setNameSplr(String nameSplr) {
        this.nameSplr = nameSplr;
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
}
