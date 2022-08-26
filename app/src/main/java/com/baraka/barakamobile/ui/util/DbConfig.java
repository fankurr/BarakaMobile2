package com.baraka.barakamobile.ui.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DbConfig {
    public static final String URL_PRDCT = "http://192.168.0.118/posbaraka/apiPrdct/";
    public static final String URL_CATE = "http://192.168.0.118/posbaraka/apiCat/";
    public static final String URL_SELL = "http://192.168.0.118/posbaraka/apiTx/";
    public static final String URL_SPLR = "http://192.168.0.118/posbaraka/apiSplr/";
    public static final String URL_WORKER = "http://192.168.0.118/posbaraka/apiUser/";
    public static final String URL_LOG = "http://192.168.0.118/posbaraka/apiLog/";
    public static final String URL_PAY = "http://192.168.0.118/posbaraka/apiPay/";
    public static final String URL_COMP = "http://192.168.0.118/posbaraka/apiComp/";
    public static final String URL_HOME = "http://192.168.0.118/posbaraka/apiHome/";
    public static final String URL_TX = "http://192.168.0.118/posbaraka/apiTx/";

    public static final String URL = "http://192.168.0.118/posbaraka/apiUser/";
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
