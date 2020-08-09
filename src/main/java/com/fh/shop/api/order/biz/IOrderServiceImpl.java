package com.fh.shop.api.order.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fh.shop.api.cart.vo.Cart;
import com.fh.shop.api.cart.vo.CartItem;
import com.fh.shop.api.common.ResponseEnum;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.common.SystemConstant;
import com.fh.shop.api.config.MQConfig;
import com.fh.shop.api.exception.StockLessException;
import com.fh.shop.api.order.mapper.IOrderItemMapper;
import com.fh.shop.api.order.mapper.IOrderMapper;
import com.fh.shop.api.order.param.OrderParam;
import com.fh.shop.api.order.po.Order;
import com.fh.shop.api.order.po.OrderItem;
import com.fh.shop.api.order.vo.OrderConfirmVo;
import com.fh.shop.api.paylog.mapper.IPayLogMapper;
import com.fh.shop.api.paylog.po.PayLog;
import com.fh.shop.api.product.mapper.IProductMapper;
import com.fh.shop.api.recipient.biz.IRecipientService;
import com.fh.shop.api.recipient.mapper.IRecipientMapper;
import com.fh.shop.api.recipient.po.Recipient;
import com.fh.shop.api.utils.KeyUtil;
import com.fh.shop.api.utils.RedisUitl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("orderService")
public class IOrderServiceImpl implements IOrderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IProductMapper productMapper;

    @Autowired
    private IRecipientMapper recipientMapper;

    @Autowired
    private IOrderMapper orderMapper;

    @Autowired
    private IOrderItemMapper orderItemMapper;

    @Autowired
    private IPayLogMapper payLogMapper;


    @Resource(name = "recipientService")
    private IRecipientService recipientService;

    @Override
    public ServerResponse genOrderConfirm(Long memberId) {
        //获取收件人对号列表
        List <Recipient> recipientList = recipientService.findList(memberId);
        String cartJson = RedisUitl.get(KeyUtil.buildMemberKey(memberId));
        Cart cart = JSONObject.parseObject(cartJson, Cart.class);


        //组装还回的信息
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();
        orderConfirmVo.setCart(cart);
        orderConfirmVo.setRecipientList(recipientList);
        return ServerResponse.success(orderConfirmVo);
    }

    @Override
    public ServerResponse genOrder(OrderParam orderParam) {
        Long memberId = orderParam.getMemberId();
        //清楚之前的标志位
        RedisUitl.del(KeyUtil.buildOderKey(memberId));
        RedisUitl.del(KeyUtil.buildStockLessKey(memberId));
        RedisUitl.del(KeyUtil.buildOrderErrorKey(memberId));
        //将订单发送到消息队列中
        String orderFaramJson = JSONObject.toJSONString(orderParam);
        rabbitTemplate.convertAndSend(MQConfig.ORDEREXCHANGE, MQConfig.ORDER,orderFaramJson);
        return ServerResponse.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(OrderParam orderParam) {

        Long memberId = orderParam.getMemberId();
        String cartJson = RedisUitl.get(KeyUtil.buildMemberKey(memberId));
        Cart cart = JSONObject.parseObject(cartJson, Cart.class);
        List <CartItem> cartItemList = cart.getCartItemList();

        //减库存

        //update —t_product set  stock = stock -num where id = 1 and stock >=num
        //处理并发量
        for (CartItem cartItem : cartItemList) {
            Long goodsId = cartItem.getGoodsId();
            int num = cartItem.getNum();
            int rowCount = productMapper.updateStock(goodsId, num);
            if (rowCount == 0) {
                //没更新成功  库存不足
                //及回滚  及提示
                throw new StockLessException("stock less");
            }
        }
        //获取收件人信息
        Long recipientId = orderParam.getRecipientId();
        Recipient recipient = recipientMapper.selectById(recipientId);
        //插入订单表
        Order order = new Order();
        //手动设置Id 通过雪花算法生产唯1标识
        String orderId = IdWorker.getIdStr();
        order.setId(orderId);
        order.setCreateTime(new Date());
        order.setRecipientor(recipient.getRecipientor());
        order.setPhone(recipient.getPhone());
        order.setAddress(recipient.getAddress());
        order.setMemberId(memberId);
        order.setTotalPrice(cart.getTotalPrice());
        order.setRecipientId(recipientId);
        order.setPayType(orderParam.getPayType());
        order.setStatus(SystemConstant.OrderStatus.WAIT_PA);//未支付
        order.setTotalNum(cart.getTotalNum());
        orderMapper.insert(order);
        //插入订明细单表
        //批量插入
        //insert  into 表明（字段名1 ，字段名2），（字段名1 ，字段名2）

        List <OrderItem> OrderItemList = new ArrayList <>();
        for (CartItem cartItem : cartItemList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setProductId(cartItem.getGoodsId());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setProductName(cartItem.getGoodsName());
            orderItem.setImgUrl(cartItem.getImgUrl());
            orderItem.setMemberId(memberId);
            orderItem.setSubPrice(cartItem.getSudPrice());
            orderItem.setNum(cartItem.getNum());
            OrderItemList.add(orderItem);
        }
        //批量插入订单明细表
        orderItemMapper.batchInserts(OrderItemList);
        //插入订单日志表
        PayLog payLog = new PayLog();
        String outTradeNo = IdWorker.getIdStr();
        payLog.setOutTradeNo(outTradeNo);
        payLog.setPayMoney(cart.getTotalPrice());
        payLog.setMemberId(memberId);
        payLog.setCreateTime(new Date());
        payLog.setOrderId(orderId);
        payLog.setPayStatus(SystemConstant.payStatus.PA_WAIT);
        payLog.setPayType(orderParam.getPayType());
        payLogMapper.insert(payLog);
        //将支付日志存入redis中
        String payLogJson = JSONObject.toJSONString(payLog);
        RedisUitl.set(KeyUtil.buildPayLogKey(memberId),payLogJson);
        //删除购物车中的信息
        RedisUitl.del(KeyUtil.buildMemberKey(memberId));
        //提交订单
        RedisUitl.set(KeyUtil.buildOderKey(memberId),"ok");
    }

    @Override
    public ServerResponse getResult(Long memberId) {
        if(RedisUitl.exist(KeyUtil.buildStockLessKey(memberId))){
            //证明库存不足
         return ServerResponse.error(ResponseEnum.ORDER_STOCK_LESS);
        }
        if(RedisUitl.exist(KeyUtil.buildOderKey(memberId))){
            //下订单成功
            return ServerResponse.success();
        }
        if(RedisUitl.exist(KeyUtil.buildOrderErrorKey(memberId))){
            //下订单错误
            return ServerResponse.error(ResponseEnum.ORDER_ERROR);
        }
        return ServerResponse.error(ResponseEnum.ORDER_SUCCESS_PD);
    }
}
