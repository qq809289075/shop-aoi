package com.fh.shop.api.job;

import com.fh.shop.api.product.biz.IProductService;
import com.fh.shop.api.product.po.Product;
import com.fh.shop.api.utils.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class JobSrockLess {

    @Autowired
    private MailUtil mailUtil;

    @Resource(name="productService")
    private IProductService productService;

    @Scheduled(cron = "0 15 10 15 * ?")
    public void checkStock(){
        List <Product> srockLess =  productService.findSrockLess();

        StringBuffer productHtml = new StringBuffer();
        productHtml.append("<table bgcolor=\"#ffc0cb\" cellspacing=\"0\" cellpadding=\"0\" width=\"500px\" border=\"1\">\n" +
                "   <thead>\n" +
                "       <tr>\n" +
                "           <th>商品</th>\n" +
                "           <th>价格</th>\n" +
                "           <th>库存</th>\n" +
                "       </tr>\n" +
                "\n" +
                "   </thead>\n" +
                "    <tbody>");

        for (Product product : srockLess) {
            productHtml.append(" <tr>\n" +
                    "           <td>"+product.getName()+"</td>\n" +
                    "           <td>"+product.getPrice().toString()+"</td>\n" +
                    "           <td>"+product.getStock()+"</td>\n" +
                    "    </tr>");


      }
        productHtml.append(" </tbody>\n" +
                "</table>");
        String tableHtml = productHtml.toString();
        mailUtil.sendMail("809289075@qq.com","商品库存不足报告",tableHtml);

    }
}
