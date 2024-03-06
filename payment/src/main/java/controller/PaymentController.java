package controller;

import com.lxl.common.domain.ConfirmOrder;
import com.lxl.common.enums.ConfirmOrderStatusTypeEnum;
import com.lxl.common.mapper.ConfirmOrderMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.PayService;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2024/3/6  17:52
 **/
@RequestMapping("/payment")
@RestController
public class PaymentController {

    @Autowired
    PayService payService;

    @Autowired
    ConfirmOrderMapper confirmOrderMapper;
    @PostMapping("/commit")
    public Object pay(ConfirmOrder confirmOrder) throws Exception {
        //设置订单状态为支付中
        confirmOrder.setStatus(ConfirmOrderStatusTypeEnum.PAY_PENDING.getCode());
        confirmOrderMapper.updateById(confirmOrder);
        return payService.pay(confirmOrder);
    }

    @PostMapping("/fallback")
    public void fallback(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String[] outTradeNos = parameterMap.get("out_trade_no");
        if (outTradeNos != null) {
            String confirmOrderId = outTradeNos[0];
            ConfirmOrder entity = new ConfirmOrder();
            entity.setId(Long.valueOf(confirmOrderId));
            //设置订单状态为支付成功
            entity.setStatus(ConfirmOrderStatusTypeEnum.PAY_SUCCESS.getCode());
            confirmOrderMapper.updateById(entity);
        }
    }

}
