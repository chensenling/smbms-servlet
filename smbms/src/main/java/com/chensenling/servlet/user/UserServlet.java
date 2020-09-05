package com.chensenling.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.chensenling.pojo.Role;
import com.chensenling.pojo.User;
import com.chensenling.service.role.RoleServiceImpl;
import com.chensenling.service.user.UserService;
import com.chensenling.service.user.UserServiceImpl;
import com.chensenling.util.Constants;
import com.chensenling.util.PageSupport;
import com.mysql.jdbc.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

//实现servlet复用
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method.equals("savepwd") && method!=null){
            this.updatePwd(req,resp);

        }else if(method.equals("pwdmodify") && method!=null){
            this.pwdmodify(req,resp);
        }else if(method.equals("query") && method!= null){
        this.query(req,resp);
        }else if(method.equals("view") && method!= null){
            try {
                this.view(req,resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else if(method.equals("modify") && method!= null){
            try {
                this.modify(req,resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else if(method.equals("deluser") && method!= null){
            try {
                this.deluser(req,resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else if(method.equals("getrolelist") && method!= null){
            this.getrolelist(req,resp);
        }else if(method.equals("modifyexe") && method!= null){
            try {
                this.modifyexe(req,resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else if(method.equals("add") && method!=null){
            try {
                this.addUser(req,resp);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else if(method.equals("ucexist") && method!=null){
            try {
                this.ucexist(req,resp);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       doGet(req,resp);
    }


    //修改密码,成功移除session
    public void updatePwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从session中拿id
        User obj =(User)req.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = req.getParameter("newpassword");
        boolean flag=false;
        if(obj!=null && !StringUtils.isNullOrEmpty(newpassword)){
            UserService userService=new UserServiceImpl();
            try {
                flag = userService.updatePwd(obj.getId(),newpassword);
                if(flag){
                    req.setAttribute("message","修改密码成功，请退出，使用新密码登录");
                    //密码修改成功，移除当前的session
                    req.getSession().removeAttribute(Constants.USER_SESSION);
//                    req.getRequestDispatcher("error.jsp").forward(req,resp);

                }else{
                    req.setAttribute("message","密码修改失败");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }else{
            req.setAttribute("message","新密码有问题");
        }
        req.getRequestDispatcher("pwdmodify.jsp").forward(req,resp);
    }

    //验证旧密码,采用获取session中的用户解决，减轻数据库压力。
    public void pwdmodify(HttpServletRequest req, HttpServletResponse resp){
       String oldpassword= req.getParameter("oldpassword");
       User obj =(User)req.getSession().getAttribute(Constants.USER_SESSION);

       //万能的Map，利用其 key -value 解决多种情况的参数传递
        HashMap<String,String> resultMap =new HashMap<String, String>();

        if(obj==null){
            //session过期
            resultMap.put("result","sessionerror");

        }else if(StringUtils.isNullOrEmpty(oldpassword)){
            //旧密码为空
            resultMap.put("result","error");

        }else {
            if(obj.getUserPassword().equals(oldpassword) &&obj!=null){
            resultMap.put("result","true");
        }else{
                resultMap.put("result","false");

        }
        }

        // 前端判断方式，用json传递 {key:value}
        try {
            //以json格式传回前端
            resp.setContentType("application/json");
            //使用打印流将json字符串打印到前端
            PrintWriter writer =resp.getWriter();
            //使用阿里巴巴的fastjson将流转为json格式字符串、
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // 查询 --用户管理
    public void query(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {

        // 从前端获取数据
        String queryUserName= req.getParameter("queryname");
//        System.out.println(queryUserName+"<-用户名 ");
        String temp = req.getParameter("queryUserRole");

        String pageIndex = req.getParameter("pageIndex");


        int queryUserRole=0;//查询的角色

        // 获取用户列表
        UserServiceImpl userService = new UserServiceImpl();
        // 第一次走这个请求一定是第一页，页面大小固定
        int pageSize=5;// 页大小
        int currentPageNo=1;// 第一次的页码


        if(queryUserName == null){
            queryUserName="";// 置空
        }

        if(temp!=null && !temp.equals("")){
            queryUserRole = Integer.parseInt(temp);// 拿到角色号
        }
        System.out.println("servlet-->"+queryUserRole);
        if(pageIndex!=null ){
            currentPageNo =Integer.parseInt(pageIndex);// 拿到当前页码
        }

        // sql开始索引 = （当前页-1）*size
        //获取用户总数(分页 ： 上一页 ，下一页)
        int totalCount = userService.getUserCount(queryUserName,queryUserRole);

        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        //总页数
        int totalPageCount = pageSupport.getTotalPageCount();
        // 控制首页和尾页
        // 首页：页码1，当页码<1时，没有上一页
        // 如果页面小于1，就显示第一页的东西
        if(currentPageNo < 1){
            currentPageNo=1;
        }else if(currentPageNo > totalPageCount){
            // 当前页码大于最后一页，没有后一页
            currentPageNo=totalPageCount;
        }

        //获取用户页面展示
        // 获取到用户列表
        List<User> userlist=userService.getUserList(queryUserName,queryUserRole,currentPageNo,pageSize);
        req.setAttribute("userList",userlist); // 加入request域，与前端共享

        // 获取到角色列表
        RoleServiceImpl roleService =new RoleServiceImpl();
        List<Role> roleList= roleService.getRoleList();

        req.setAttribute("roleList",roleList);// 角色列表
        req.setAttribute("queryUserRole",queryUserRole);//查询角色号
        req.setAttribute("totalPageCount",totalPageCount); // 总页数
        req.setAttribute("currentPageNo",currentPageNo); //当前页码
        req.setAttribute("totalCount",totalCount);//总记录数


        // 返回前端
        try {
            req.getRequestDispatcher("userlist.jsp").forward(req,resp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 用户的查看 √
    public void view(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {

        // 获取前端参数
        String uid =req.getParameter("uid");
        String msg =req.getParameter("message");
        req.setAttribute("modifyuser",msg);

        User userview=null;
        if(uid==null || uid.equals("")){
            req.setAttribute("message","选项问题");
            req.getRequestDispatcher("error.jsp").forward(req,resp);
        }else{
            System.out.println(Integer.parseInt(uid));
            userview=new UserServiceImpl().getUserByUid(Integer.parseInt(uid));

            req.setAttribute("user",userview);
            req.getRequestDispatcher("userview.jsp").forward(req,resp);
        }

    }

    //用户的修改页面数据获取
    public void modify(HttpServletRequest req, HttpServletResponse resp) throws ServletException, SQLException, IOException {

        // 获取前端参数
        String uid =req.getParameter("uid");

        User userview=null;
        if(uid==null || uid.equals("")){
            req.setAttribute("message","选项问题");
            req.getRequestDispatcher("error").forward(req,resp);
        }else{
            System.out.println(Integer.parseInt(uid));
            userview=new UserServiceImpl().getUserByUid(Integer.parseInt(uid));

            req.setAttribute("user",userview);

            req.getRequestDispatcher("usermodify.jsp").forward(req,resp);
        }

    }
    // 得到角色列表
    public void getrolelist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Role> roleList=null;

        roleList =new UserServiceImpl().getRoleList();
        System.out.println(roleList.get(0).getRoleName());

        // 设置前端接受的格式
        resp.setContentType("application/json");

        PrintWriter writer=resp.getWriter();
        // 转换为json格式,写入resp打印流
        writer.write(JSONArray.toJSONString(roleList));

        writer.flush();
        writer.close();




    }
    // 执行修改操作
    public void modifyexe(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        System.out.println("start modifyexe");
        //获取到表单的数据
       String uid = req.getParameter("uid");
        String userName = req.getParameter("userName");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");
        // 封装数据
        User user=new User();
        user.setId(Integer.parseInt(uid));
        user.setUserName(userName);
        user.setGender(Integer.parseInt(gender));
        user.setBirthday(Date.valueOf(birthday));
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.parseInt(userRole));
        user.setModifyBy(Integer.parseInt(uid));
        user.setModifyDate(new java.util.Date());
        //
        UserService userService =new UserServiceImpl();
        boolean flag = userService.updateUserByForm(user);
        String msg =null;
        if(flag){
            msg="用户更新成功";
            resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
        }else{
            msg="更新失败";
            resp.sendRedirect("usermodify.jsp");
        }

    }

    // 用户的删除
    public void deluser(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {

        // 拿到uid
        String uid =req.getParameter("uid");
        boolean flag=true;
        UserService userService=new UserServiceImpl();
        flag=userService.delUserByUid(Integer.parseInt(uid));

        // 万能的map
        HashMap<String ,String> map =new HashMap<String, String>();

        if(flag){
            map.put("delResult","true");
        }else{
            map.put("delResult","false");
        }
        if(uid==null){
            map.put("delResult","notexist");
        }

        //前端ajax判断
        //设置resp
        resp.setContentType("application/json");

        PrintWriter writer=resp.getWriter();
        writer.write(JSONArray.toJSONString(map));

        writer.flush();
        writer.close();


    }

    //用户添加
    public void addUser(HttpServletRequest req, HttpServletResponse resp) throws ParseException, SQLException, ServletException, IOException {

        System.out.println("into addUser");
        //获取前端，表单数据
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        // 封装参数user
        User user =new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setGender(Integer.parseInt(gender));
        user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.parseInt(userRole));
       User loginuser= (User)req.getSession().getAttribute(Constants.USER_SESSION);

        user.setCreatedBy(loginuser.getId());
        user.setCreationDate(new java.util.Date());
        user.setModifyBy(loginuser.getId());
        user.setModifyDate(new java.util.Date());

        //调用业务层
        boolean flag=false;
        UserService userService=new UserServiceImpl();
        flag = userService.addUser(user);
        System.out.println("addUser:"+flag);

        if (flag){
            resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
        }else{
            resp.sendRedirect(req.getContextPath()+"/useradd.jsp");
        }

    }

    public void ucexist(HttpServletRequest req, HttpServletResponse resp) throws ParseException, SQLException, ServletException, IOException {
        //获取参数
        String userCode=req.getParameter("userCode");
        UserService userService=new UserServiceImpl();
        User user =userService.queryUserByUserCode(userCode);

        //万能的map
        HashMap<String,String> resultMap=new HashMap<String,String>();

        if(user==null){
            resultMap.put("userCode","noexist");

        }else {
            resultMap.put("userCode","exist");
        }


        // 写回前端
        resp.setContentType("application/json");
        PrintWriter writer =resp.getWriter();
        writer.write(JSONArray.toJSONString(resultMap));
        writer.flush();
        writer.close();

    }



}
