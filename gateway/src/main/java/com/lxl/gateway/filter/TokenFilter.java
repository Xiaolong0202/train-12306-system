package com.lxl.gateway.filter;

import com.lxl.gateway.domain.RequestWhiteList;
import com.lxl.gateway.utils.MemberTokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @Author LiuXiaolong
 * @Description train-12306-system
 * @DateTime 2023/9/21  22:49
 **/
@Component
public class TokenFilter implements GlobalFilter {

    Logger log = LoggerFactory.getLogger(TokenFilter.class);

    private static final String TOKEN = "token";


    @Autowired
    private RequestWhiteList requestWhiteList;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String requestPath = exchange.getRequest().getURI().getPath();
        log.info("requestPath = " + requestPath);
        //先判断请求路径是否在白名单当中
        if (requestPath.contains("/admin")){
            log.info("{} 在请求白名单中，不需要验证token",requestPath);
            return chain.filter(exchange);
        }
        for (String s : requestWhiteList.getWhiteList()) {
            String afterReplace = s.replace("/**", "");
            if (s.equals(requestPath)||requestPath.startsWith(afterReplace)){
                log.info("{} 在请求白名单中，不需要验证token",requestPath);
                return chain.filter(exchange);
            }
        }
        List<String> list = exchange.getRequest().getHeaders().get(TOKEN);
        if (CollectionUtils.isEmpty(list)){
            log.error("token校验失败--token 为空 ");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }else {
            String token = list.get(0);
            if (!MemberTokenUtils.verify(token)){
                log.error("token校验失败--token 非法");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }
        return chain.filter(exchange);
    }

    public static void main(String[] args) {
        System.out.println("\"/member/**\".replace(\"/**\",\"\") = " +
                "/member/**".replace("/**", ""));
    }
}
