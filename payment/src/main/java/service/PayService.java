package service;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.lxl.common.domain.ConfirmOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author LiuXiaolong
 * @Description react-demo
 * @DateTime 2024/3/6  16:18
 **/
@Service
@Slf4j
public class PayService {

    @Value("${alipay.returnUrl}")
    private String returnUrl;
    public Object pay(ConfirmOrder confirmOrder) throws Exception {
        AlipayTradePagePayResponse response = Factory.Payment.Page().pay("车次"+confirmOrder.getTrainCode(),confirmOrder.getId().toString(),confirmOrder.getSumPrice().toString(), returnUrl);
        return response.body;
    }
}
