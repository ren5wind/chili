package com.topunion.chili.paylib.base.entity;

/**
 * Created by LinYi.
 * <p/>获取支付结果返回的list中的实体
 */
public class Snatch {

    private String goodsName;// 商品名称
    private Integer goodsTimesId;//参与期号id
    private String goodsTimesName;//参与期号
    private Integer buyTimes;//本期参与人次
    private Integer[] nums;// 夺宝号码数组，最多显示前6个号码

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

    public Integer[] getNums() {
        return nums;
    }

    public void setNums(Integer[] nums) {
        this.nums = nums;
    }
}
