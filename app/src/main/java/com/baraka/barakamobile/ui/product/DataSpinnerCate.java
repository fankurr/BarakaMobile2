package com.baraka.barakamobile.ui.product;

public class DataSpinnerCate {
    private String idCat;
    private String idCompCat;
    private String nameCat;

    public DataSpinnerCate(){

    }

    public DataSpinnerCate(String idCat, String idCompCat, String nameCat){
        this.idCat = idCat;
        this.idCompCat = idCompCat;
        this.nameCat = nameCat;
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
}
