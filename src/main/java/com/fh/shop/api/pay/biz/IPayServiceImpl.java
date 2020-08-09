package com.fh.shop.api.pay.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import com.fh.shop.api.common.ResponseEnum;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.common.SystemConstant;
import com.fh.shop.api.config.WXConfig;
import com.fh.shop.api.order.mapper.IOrderMapper;
import com.fh.shop.api.order.po.Order;
import com.fh.shop.api.paylog.mapper.IPayLogMapper;
import com.fh.shop.api.paylog.po.PayLog;
import com.fh.shop.api.utils.BigDecimalUtil;
import com.fh.shop.api.utils.KeyUtil;
import com.fh.shop.api.utils.RedisUitl;
import com.github.wxpay.sdk.WXPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("payService")
public class IPayServiceImpl implements IPayService {

    @Autowired
    private IOrderMapper orderMapper;

    @Autowired
    private IPayLogMapper payLogMapper;

    @Override
    public ServerResponse createNative(Long memberId) {
        //获取会0对号的支付日志
        String payLogJSON = RedisUitl.get(KeyUtil.buildPayLogKey(memberId));
        PayLog payLog = JSONObject.parseObject(payLogJSON, PayLog.class);
        String orderId = payLog.getOrderId();
        //获取支付相关信息
        String outTradeNo = payLog.getOutTradeNo();
        BigDecimal payMoney = payLog.getPayMoney();
        //调微信接口做统1下单
        try {

            WXConfig config = new WXConfig();
            WXPay wxpay = new WXPay(config);

            Map <String, String> data = new HashMap <String, String>();
            data.put("body", "小熊TV--在线支付");
            int money = BigDecimalUtil.mul(payMoney.toString(), "100").intValue();
            data.put("out_trade_no", outTradeNo);
            data.put("device_info", "");
            data.put("total_fee", money + "");
            data.put("notify_url", "http://www.example.com/wxpay/notify");
            data.put("trade_type", "NATIVE");  // 此处指定为扫码支付
            Map <String, String> resp = wxpay.unifiedOrder(data);

            System.out.println(resp);

            String return_code = resp.get("return_code");
            String return_msg = resp.get("return_msg");
            if (!return_code.equals("SUCCESS")) {
                return ServerResponse.error(9999, return_msg);//微信平台自己返回的错误信息
            }
            String result_code = resp.get("result_code");
            String err_code_des = resp.get("err_code_des");
            if (!result_code.equals("SUCCESS")) {
                return ServerResponse.error(9999, err_code_des);//微信平台自己返回的错误信息
            }
            //证明 return_code和result_code都是 SUCCESS
            String code_url = resp.get("code_url");
            Map <String, String> resultMap = new HashMap <>();
            resultMap.put("codeUrl", code_url);
            resultMap.put("orderId", orderId);
            resultMap.put("totalPrice", money + "");
            return ServerResponse.success(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.error();
        }

    }

    @Override
    public ServerResponse queryStatus(Long memberId) {
        String payLogJson = RedisUitl.get(KeyUtil.buildPayLogKey(memberId));
        PayLog payLog = JSONObject.parseObject(payLogJson, PayLog.class);
        String orderId = payLog.getOrderId();
        String outTradeNo = payLog.getOutTradeNo();
        WXConfig config = new WXConfig();

        try {
            WXPay wxpay = new WXPay(config);
            Map <String, String> data = new HashMap <String, String>();
            data.put("out_trade_no", outTradeNo);
            Map <String, String> resp =  wxpay.orderQuery(data);
            System.out.println(resp);
            String return_code = resp.get("return_code");
            String return_msg = resp.get("return_msg");
            if (!return_code.equals("SUCCESS")) {
                return ServerResponse.error(99999, return_msg);
            }
            String result_code = resp.get("result_code");
            String err_code_des = resp.get("err_code_des");
            if (!result_code.equals("SUCCESS")) {
                return ServerResponse.error(99999, err_code_des);
            }
            String trade_state = resp.get("trade_state");
            if (trade_state.equals("SUCCESS")) {
                //到这里证明支付成功了
                String transaction_id = resp.get("transaction_id");
                //更新状态
                Order order = new Order();
                order.setId(orderId);
                order.setPayTime(new Date());
                order.setStatus(SystemConstant.payStatus.PA_SUEEESS);
                orderMapper.updateById(order);
                //更新支付日志表
                PayLog payLogInfo = new PayLog();
                payLogInfo.setPayTime(new Date());
                payLogInfo.setPayStatus(SystemConstant.payStatus.PA_SUEEESS);
                payLogInfo.setTransactionsId(transaction_id);
                payLogMapper.updateById(payLogInfo);
                //删除redis中日志的标志位
                RedisUitl.del(KeyUtil.buildPayLogKey(memberId));
                //返回客户端
                return ServerResponse.success();
            } else {
                return ServerResponse.error(ResponseEnum.PA_ZHIFU_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.error();
        }

    }
}

