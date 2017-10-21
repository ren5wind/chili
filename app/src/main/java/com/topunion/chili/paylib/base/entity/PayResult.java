package com.topunion.chili.paylib.base.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LinYi.
 * 后台返回支付结果的实体
 */
public class PayResult implements Serializable {
    @SerializedName("descr")
    private String descr;//描述信息，如你已成功参与了xx件商品共xx人次夺宝，xx元已存入到您的余额中。
    @SerializedName("snatchList")
    private List<Goods> mGoodsList = new ArrayList<>();//购买的商品列表

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public List<Goods> getGoodsList() {
        return mGoodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.mGoodsList = goodsList;
    }
}
