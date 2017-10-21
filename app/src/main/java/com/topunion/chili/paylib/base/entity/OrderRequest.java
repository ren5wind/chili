package com.topunion.chili.paylib.base.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 订单请求实体
 */
public class OrderRequest {
    @SerializedName("userId")
    Integer userId;//用户Id
    @SerializedName("snatchList")
    List<GoodsItem> goodsList = new ArrayList<>();//购买商品列表
    @SerializedName("payWays")
    Set<Integer> payWays = new HashSet<>();//支付方式
    @SerializedName("redPackIds")
    Set<Integer> redPackIds = new HashSet<>();//使用的红包id
    @SerializedName("thirdpartMoney")
    Integer thirdPartyMoney;//第三方支付金额
    @SerializedName("isAlipayWithH5")
    boolean isAlipayWithH5;//是否用html5支付，默认App

    public boolean isAlipayWithH5() {
        return isAlipayWithH5;
    }

    public void setIsAlipayWithH5(boolean isAlipayWithH5) {
        this.isAlipayWithH5 = isAlipayWithH5;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<GoodsItem> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsItem> goodsList) {
        this.goodsList = goodsList;
    }

    public Set<Integer> getPayWays() {
        return payWays;
    }

    public void setPayWays(Set<Integer> payWays) {
        this.payWays = payWays;
    }

    public Set<Integer> getRedPackIds() {
        return redPackIds;
    }

    public void setRedPackIds(Set<Integer> redPackIds) {
        this.redPackIds = redPackIds;
    }

    public Integer getThirdPartyMoney() {
        return thirdPartyMoney;
    }

    public void setThirdPartyMoney(Integer thirdPartyMoney) {
        this.thirdPartyMoney = thirdPartyMoney;
    }
}
