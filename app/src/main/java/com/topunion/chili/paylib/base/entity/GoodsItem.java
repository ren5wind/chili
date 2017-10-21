package com.topunion.chili.paylib.base.entity;

/**
 * 对应购买物品的id和数量
 */
public class GoodsItem {

    Integer gid;//购买商品id
    Integer amount;//购买商品数量
    Double price; //价格

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getGid() {

        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }
}
