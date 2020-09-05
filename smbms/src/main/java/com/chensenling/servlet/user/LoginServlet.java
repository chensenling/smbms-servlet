package com.chensenling.servlet.user;

import com.chensenling.pojo.User;
import com.chensenling.service.user.UserService;
import com.chensenling.service.user.UserServiceImpl;
import com.chensenling.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet  extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("LoginServlet --starty...");;

        //获取用户名和密码
        String userCode= req.getParameter("userCode");
        String userPassword =req.getParameter("userPassword");
        System.out.println(userCode+" "+userPassword);
        //和数据库中的密码进行对比，调用业务层
        UserService userService =new UserServiceImpl();
        User user = userService.login(userCode,userPassword);

        if(user!=null) {
            //将用户的信息放到session中
            req.getSession().setAttribute(Constants.USER_SESSION,user);

            //跳转到内部主页
            resp.sendRedirect("jsp/frame.jsp");


        }else{
            //查无此人，无法登录
            //转发回登录页面，提示用户名或密码错误

            req.setAttribute("error","用户名或密码不正确");
            req.getRequestDispatcher("login.jsp").forward(req,resp);

        }

    }
}
