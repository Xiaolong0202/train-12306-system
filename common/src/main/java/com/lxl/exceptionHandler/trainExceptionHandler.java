package com.lxl.exceptionHandler;

import com.lxl.exception.BusinessException;
import com.lxl.exception.exceptionEnum.BussinessExceptionEnum;
import com.lxl.resp.CommonResp;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/19  22:36
 **/
@ControllerAdvice
public class trainExceptionHandler {

        @ResponseBody
        @ExceptionHandler(BusinessException.class)
        public CommonResp<?> handler(BusinessException e){
            CommonResp<String> commonResp = new CommonResp<>();
            commonResp.setSuccess(false);
            commonResp.setMessage("出现全局异常");
            commonResp.setContent(e.getExceptionEnum().desc);
            return commonResp;
        }

}
