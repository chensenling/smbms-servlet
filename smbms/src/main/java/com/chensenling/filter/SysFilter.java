package com.chensenling.filter;

import com.chensenling.pojo.User;
import com.chensenling.util.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SysFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        //根据session做判断
        HttpServletRequest req= (HttpServletRequest) servletRequest;
        HttpServletResponse resp=(HttpServletResponse) servletResponse;

        User user = (User) req.getSession().getAttribute(Constants.USER_SESSION);

        if(user==null){
            //已经被注销或者过期了,重定向到错误页
            resp.sendRedirect(req.getContextPath()+"/error.jsp");
        }else{
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }

    public void destroy() {

    }
}
