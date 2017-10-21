package com.topunion.chili.paylib.base.interf;

import com.topunion.chili.paylib.base.entity.BasePayOrder;

/**
 * 第三方支付顶层接口
 * @param <T>
 */
public interface IPay<T extends BasePayOrder> {

    
    /**
     * 唤起第三方支付
     * @param t
     */
    void invoke(T t);

}
