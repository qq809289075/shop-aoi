package com.fh.shop.api.order.biz;

import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.order.param.OrderParam;

public interface IOrderService {

    public ServerResponse genOrderConfirm(Long memberId);

    ServerResponse genOrder(OrderParam orderParam);

    void createOrder(OrderParam orderParam);

    ServerResponse getResult(Long memberId);
}
