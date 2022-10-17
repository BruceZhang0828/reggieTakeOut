package com.itzhy.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itzhy.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhy
 * @description
 * @date 2022/10/16 10:59
 **/
@WebFilter(filterName = "loginCheckFilter")
@Slf4j
public class LoginCheckFilter implements Filter {
    // 路径匹配器，通配符匹配
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 1 获取本次请求uri
        String requestURI = request.getRequestURI();
        log.info("拦截到的请求，{}", requestURI);
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
        };
        // 2 判断本次请求是否需要处理
        boolean check = check(requestURI, urls);
        if (check) {
            // 3 如果不需要处理，直接放行
            log.info("本次请求{}不需要拦截", requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        // 4 如果登录直接放行
        if (request.getSession().getAttribute("employee") != null) {
            log.info("用户已经登录，用户id为：{}", request.getSession().getAttribute("employee"));
            filterChain.doFilter(request, response);
            return;
        }
        // 5如果没有登录 通过response对象给前台写回输入流
        log.info("用户未登录！");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * @Description: 检查本次是否需要放行
     * @Author: zhy
     * @Date: 2022/10/16 17:11
     * @Param: [requestURI, urls]
     * @return: boolean
     */
    private boolean check(String requestURI, String[] urls) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
