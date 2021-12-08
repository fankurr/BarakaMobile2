package com.baraka.barakamobile.ui.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DbConfig {
    public static final String URL_PRDCT = "https://barakapos.000webhostapp.com/apiPrdct/";
    public static final String URL_CATE = "https://barakapos.000webhostapp.com/apiCat/";
    public static final String URL_SELL = "https://barakapos.000webhostapp.com/apiTx/";
    public static final String URL_SPLR = "https://barakapos.000webhostapp.com/apiSplr/";
    public static final String URL_WORKER = "https://barakapos.000webhostapp.com/apiUser/";
    public static final String URL_LOG = "https://barakapos.000webhostapp.com/apiLog/";
    public static final String URL_PAY = "https://barakapos.000webhostapp.com/apiPay/";
    public static final String URL_COMP = "https://barakapos.000webhostapp.com/apiComp/";

    public static final String URL = "https://barakapos.000webhostapp.com/apiUser/";
//    public static Retrofit retrofit;
//
//    public static Retrofit getApiClient(){
//        if (retrofit==null){
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return retrofit;
//    }
}
