package com.lxl.common.exceptionHandler;

import com.lxl.common.exception.BusinessException;
import com.lxl.common.resp.CommonResp;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/19  22:36
 **/
@Slf4j
@ControllerAdvice
public class trainExceptionHandler {

    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    public CommonResp<?> handler(BusinessException e) {
        CommonResp<String> commonResp = new CommonResp<>();
        commonResp.setSuccess(false);
        commonResp.setMessage(e.getExceptionEnum().desc);
        return commonResp;
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResp<?> handleValid(MethodArgumentNotValidException e) {
        e.printStackTrace();
        CommonResp<String> commonResp = new CommonResp<>();
        commonResp.setSuccess(false);
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        StringBuilder builder = new StringBuilder("[");
        allErrors.forEach(error -> {
            builder.append(error.getDefaultMessage());
            builder.append('、');
        });
        builder.delete(builder.length() - 1, builder.length());
        builder.append(']');
        commonResp.setMessage(builder.toString());
        return commonResp;
    }



    @ResponseBody
    @ExceptionHandler(ClassNotFoundException.class)
    public CommonResp<?> handleClassNotFoundException(ClassNotFoundException e) {
        e.printStackTrace();
        return CommonResp.buildFailure(e, "未找到对应的类");
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public CommonResp<?> handleException(Exception e) {
        if (StringUtils.hasText(RootContext.getXID())){
            log.info("Seata全局事务ID{}",RootContext.getXID());
            log.info("异常{}是在seata的全局事务当中发生，将会将异常抛出",e.getMessage());
            throw new RuntimeException(e);
        }
        e.printStackTrace();
        return CommonResp.buildFailure(e.getMessage(), "本次响应失败，请联系网站管理员");
    }

}
