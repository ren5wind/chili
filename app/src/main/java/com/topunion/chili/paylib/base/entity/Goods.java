package com.topunion.chili.paylib.base.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LinYi.
 * <p/>获取支付结果返回的list中的实体
 */
public class Goods implements Serializable {
    @SerializedName("goodsName")
    private String goodsName;// 商品名称
    @SerializedName("goodsTimesId")
    private Integer goodsTimesId;//参与期号id
    @SerializedName("goodsTimesName")
    private String goodsTimesName;//参与期号
    @SerializedName("buyTimes")
    private Integer buyTimes;//本期参与人次
    @SerializedName("nums")
    private List<String> nums;// 夺宝号码数组，最多显示前6个号码

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getGoodsTimesId() {
        return goodsTimesId;
    }

    public void setGoodsTimesId(Integer goodsTimesId) {
        this.goodsTimesId = goodsTimesId;
    }

    public String getGoodsTimesName() {
        return goodsTimesName;
    }

    public void setGoodsTimesName(String goodsTimesName) {
        this.goodsTimesName = goodsTimesName;
    }

    public Integer getBuyTimes() {
        return buyTimes;
    }

    public void setBuyTimes(Integer buyTimes) {
        this.buyTimes = buyTimes;
    }

    public List<String> getNums() {
        return nums;
    }

    public void setNums(List<String> nums) {
        this.nums = nums;
    }
}
