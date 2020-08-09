package com.fh.shop.api.cart.biz;

import com.alibaba.fastjson.JSONObject;
import com.fh.shop.api.cart.vo.Cart;
import com.fh.shop.api.cart.vo.CartItem;
import com.fh.shop.api.common.ResponseEnum;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.common.SystemConstant;
import com.fh.shop.api.product.mapper.IProductMapper;
import com.fh.shop.api.product.po.Product;
import com.fh.shop.api.utils.BigDecimalUtil;
import com.fh.shop.api.utils.KeyUtil;
import com.fh.shop.api.utils.RedisUitl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service("cartService")
public class ICartServiceImpl implements ICartService {

    @Autowired
    private IProductMapper productMapper;

    @Override
    public ServerResponse addItem(Long memberId, Long goodsId, int num) {
        //判断商品是否存在
        Product product = productMapper.selectById(goodsId);
        if (product == null) {
            return ServerResponse.error(ResponseEnum.CART_IS_NOT_NULL);

        }
        //判断商品是否热销
        if (product.getIsHot() == SystemConstant.CART_MEMBER) {
            return ServerResponse.error(ResponseEnum.CART_IS_ISHOT);
        }
        //如果会员已经有了对应的购物车
        String cartKey = KeyUtil.buildMemberKey(memberId);
        String cartJson = RedisUitl.get(cartKey);
        if (!StringUtils.isEmpty(cartJson)) {

            //直接向购物放入商品
            Cart cart = JSONObject.parseObject(cartJson, Cart.class);
            List <CartItem> cartItemList = cart.getCartItemList();
            CartItem cartItem = null;
            for (CartItem item : cartItemList) {
                if (item.getGoodsId().longValue() == goodsId.longValue()) {
                    cartItem = item;
                    break;
                }
            }
            if (cartItem != null) {

                //如果商品存在这添加商品更新数量 小计【 总计 和 总个数 】
                cartItem.setNum(cartItem.getNum() + num);
                int num1 = cartItem.getNum();
                if (num1 <= 0) {
                    //删除整个商品
                    cartItemList.remove(cartItem);

                } else {

                    BigDecimal subPrice = BigDecimalUtil.mul(num1 + "", cartItem.getPrice().toString());
                    cartItem.setSudPrice(subPrice);
                }

                updateCart(memberId, cart);
            } else {

                //如果商品不存在这添加商品更新购物车【 总计 和 总个数 】
                //构建商品
                buildCartItem(num, product, cart);
                updateCart(memberId, cart);
            }

        } else {

            if (num <= 0) {
                return ServerResponse.error(ResponseEnum.CART_ADD_ERER_NO);
            }
            //如果会员没有对应的购物车
            //创建购物车
            Cart cart = new Cart();
            buildCartItem(num, product, cart);
            updateCart(memberId, cart);
        }

        return ServerResponse.success();
    }


    @Override
    public ServerResponse findItemList(Long memberId) {

        String cartKey = KeyUtil.buildMemberKey(memberId);
        String cartJson = RedisUitl.get(cartKey);
        Cart cart = JSONObject.parseObject(cartJson, Cart.class);

        return ServerResponse.success(cart);
    }

    @Override
    public ServerResponse findItemNum(Long memberId) {
        String cartKey = KeyUtil.buildMemberKey(memberId);
        String cartJson = RedisUitl.get(cartKey);
        Cart cart = JSONObject.parseObject(cartJson, Cart.class);
        if(cart==null){
            return ServerResponse.success(0);
        }
        Integer totalNum = cart.getTotalNum();
        return ServerResponse.success(totalNum);

    }

    @Override
    public ServerResponse deleteCartItem(Long memberId, Long goodsId) {
       //获取购物车
        String cartJson = RedisUitl.get(KeyUtil.buildMemberKey(memberId));
        Cart cart = JSONObject.parseObject(cartJson, Cart.class);
       //删除购物车中的商品
        List <CartItem> cartItemList = cart.getCartItemList();
      //边遍历  边删除
        Iterator <CartItem> iterator =  cartItemList.iterator();
     while (iterator.hasNext()){
         CartItem next = iterator.next();
         if(next.getGoodsId().longValue()==goodsId.longValue()){
             iterator.remove();
             break;
         }
     }
     //更新购物车
        updateCart(memberId ,cart);

        return ServerResponse.success();
    }

    @Override
    public ServerResponse deleteBatchItems(Long memberId, String ids) {
        if(StringUtils.isEmpty(ids)){
            return ServerResponse.error(ResponseEnum.ITEM_ADD_IDS_NULL);
        }
       String[] idsArr = ids.split(",");
       /*  ArrayList <Long> idsList = new ArrayList <>();
        for (String s : idsArr) {
            idsList.add(Long.parseLong(s));
        }*/
        List <Long> idList = Arrays.stream(idsArr).map(x -> Long.parseLong(x)).collect(Collectors.toList());
        //获取购物车
        String cartJson = RedisUitl.get(KeyUtil.buildMemberKey(memberId));
        Cart cart = JSONObject.parseObject(cartJson, Cart.class);
        //删除购物车中的商品
        List <CartItem> cartItemList = cart.getCartItemList();
        //边遍历  边删除
        Iterator <CartItem> iterator =  cartItemList.iterator();
        for (Long id : idList) {
            while (iterator.hasNext()){
                CartItem next = iterator.next();
                if(next.getGoodsId().longValue()==id.longValue()){
                    iterator.remove();
                    break;
                }
            }
        }
        //更新购物车
        updateCart(memberId ,cart);

        return ServerResponse.success();
    }


    private void buildCartItem(int num, Product product, Cart cart) {
        //构建商品
        CartItem cartItemInfo = new CartItem();
        cartItemInfo.setGoodsId(product.getId());
        cartItemInfo.setPrice(product.getPrice());
        cartItemInfo.setImgUrl(product.getFilePath());
        cartItemInfo.setGoodsName(product.getName());
        cartItemInfo.setNum(num);
        BigDecimal subPrice = BigDecimalUtil.mul(num + "", product.getPrice().toString());
        cartItemInfo.setSudPrice(subPrice);
        //加入购物车
        cart.getCartItemList().add(cartItemInfo);
    }


    private void updateCart(Long memberId, Cart cart) {

        List <CartItem> cartItemList = cart.getCartItemList();

        int totalCount = 0;

        BigDecimal totalPrice = new BigDecimal(0);

        String cartKey = KeyUtil.buildMemberKey(memberId);
        if (cartItemList.size() <= 0) {
            //删除购物车
            RedisUitl.del(cartKey);
            return;
        }

        //更新购物车
        for (CartItem item : cartItemList) {
            totalCount += item.getNum();
            totalPrice = BigDecimalUtil.add(totalPrice.toString(), item.getSudPrice().toString());

        }

        cart.setTotalPrice(totalPrice);
        cart.setTotalNum(totalCount);
        //最终往redis里更新
        String cartNewJson = JSONObject.toJSONString(cart);

        RedisUitl.set(cartKey, cartNewJson);
    }
}
