package com.chensenling.servlet.provider;

import com.alibaba.fastjson.JSONArray;
import com.chensenling.pojo.Provider;
import com.chensenling.pojo.User;
import com.chensenling.service.provider.ProviderService;
import com.chensenling.service.provider.ProviderServiceImpl;
import com.chensenling.util.Constants;
import com.chensenling.util.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProviderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");

        if (method != null && method.equals("query")) {
            this.query(req, resp);
        } else if (method != null && method.equals("add")) {
            try {
                this.addProvider(req, resp);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        } else if (method != null && method.equals("pcexist")) {
            try {
                this.addProvider(req, resp);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        } else if (method != null && method.equals("delprovider")) {
            try {
                this.delprovider(req, resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else if (method != null && method.equals("modify")) {
            try {
                this.modify(req, resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else if (method != null && method.equals("view")) {
            try {
                this.view(req, resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else if(method!=null&& method.equals("modifyexe")){
            try {
                this.modifyexe(req,resp);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }


    // 列表查询,分页展示
    public void query(HttpServletRequest req, HttpServletResponse resp) {

        String queryProCode = req.getParameter("queryProCode");
        String queryProName = req.getParameter("queryProName");
        String pageIndex = req.getParameter("pageIndex");


        // 第一次走这个请求一定是第一页，页面大小固定
        int pageSize = 5;// 页大小
        int currentPageNo = 1;// 第一次的页码

        if (pageIndex != null) {
            currentPageNo = Integer.parseInt(pageIndex);// 拿到当前页码
        }

        ProviderService providerService = new ProviderServiceImpl();

        // sql开始索引 = （当前页-1）*size
        //获取用户总数(分页 ： 上一页 ，下一页)
        int totalCount = providerService.getTotalCount(queryProCode, queryProName);

        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        //总页数
        int totalPageCount = pageSupport.getTotalPageCount();
        // 控制首页和尾页
        // 首页：页码1，当页码<1时，没有上一页
        // 如果页面小于1，就显示第一页的东西
        if (currentPageNo < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {
            // 当前页码大于最后一页，没有后一页
            currentPageNo = totalPageCount;
        }


        List<Provider> list = providerService.queryProviderByCodeAndName(queryProCode, queryProName, currentPageNo, pageSize);

        req.setAttribute("providerList", list);
        req.setAttribute("totalPageCount", totalPageCount); // 总页数
        req.setAttribute("currentPageNo", currentPageNo); //当前页码
        req.setAttribute("totalCount", totalCount);//总记录数


        // 返回前端
        try {
            req.getRequestDispatcher("providerlist.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 添加供应商

    public void addProvider(HttpServletRequest req, HttpServletResponse resp) throws ParseException, SQLException, ServletException, IOException {

        System.out.println("into addProvider");
        //获取前端，表单数据
        String proCode = req.getParameter("proCode");
        String proName = req.getParameter("proName");
        String proContact = req.getParameter("proContact");
        String proPhone = req.getParameter("proPhone");
        String proAddress = req.getParameter("proAddress");
        String proFax = req.getParameter("proFax");
        String proDesc = req.getParameter("proDesc");


        // 封装参数pro
        Provider provider = new Provider();
        provider.setProCode(proCode);
        provider.setProName(proName);
        provider.setProDesc(proDesc);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProAddress(proAddress);
        provider.setProFax(proFax);

        User loginuser = (User) req.getSession().getAttribute(Constants.USER_SESSION);
        provider.setCreatedBy(loginuser.getId());

        provider.setCreationDate(new Date());


        //调用业务层
        boolean flag = false;
        ProviderService providerservic = new ProviderServiceImpl();
        flag = providerservic.addPro(provider);


        if (flag) {
            resp.sendRedirect(req.getContextPath() + "/jsp/provider.do?method=query");
        } else {
            resp.sendRedirect(req.getContextPath() + "/provideradd.jsp");
        }

    }

    public void pcexist(HttpServletRequest req, HttpServletResponse resp) throws ParseException, SQLException, ServletException, IOException {
        //获取参数
        String proCode = req.getParameter("proCode");
        ProviderService providerService = new ProviderServiceImpl();
        Provider provider = providerService.queryProviderByProCode(proCode);

        //万能的map
        HashMap<String, String> resultMap = new HashMap<String, String>();

        if (provider == null) {
            resultMap.put("proCode", "noexist");

        } else {
            resultMap.put("proCode", "exist");
        }


        // 写回前端
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(resultMap));
        writer.flush();
        writer.close();

    }

    // 查看供应商信息
    public void view(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        // 获取前端参数
        String pid =req.getParameter("proid");


        Provider provider=null;
        if(pid==null || pid.equals("")){
            req.setAttribute("message","选项问题");
            req.getRequestDispatcher("error.jsp").forward(req,resp);
        }else{

            provider=new ProviderServiceImpl().getProviderByPid(Integer.parseInt(pid));
            System.out.println(provider);
            req.setAttribute("provider",provider);
            req.getRequestDispatcher("providerview.jsp").forward(req,resp);
        }
    }

    // 修改供应商信息
    public void modify(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        //獲取前端id
       String pid= req.getParameter("proid");

       ProviderService providerService= new ProviderServiceImpl();
       Provider provider =providerService.getProviderByPid(Integer.parseInt(pid));
       if(provider!=null){
           req.setAttribute("provider",provider);
           req.getRequestDispatcher("providermodify.jsp").forward(req,resp);
       }

    }
    //進行修改
    public void modifyexe(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        //獲取表單數據
        String pid =req.getParameter("proId");
        String proCode= req.getParameter("proCode");
        String proName= req.getParameter("proName");
        String proContact= req.getParameter("proContact");
        String proPhone= req.getParameter("proPhone");
        String proAddress= req.getParameter("proAddress");
        String proFax= req.getParameter("proFax");
        String proDesc= req.getParameter("proDesc");


        //封裝Provider
        Provider provider=new Provider();

        provider.setId(Integer.parseInt(pid));
        provider.setProCode(proCode);
        provider.setProName(proName);
        provider.setProDesc(proDesc);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProAddress(proAddress);
        provider.setProFax(proFax);

        User loginuser=(User)req.getSession().getAttribute(Constants.USER_SESSION);
        provider.setModifyBy(loginuser.getId());
        provider.setModifyDate(new Date());


        // 調用業務
        ProviderService providerService=new ProviderServiceImpl();
       boolean flag= providerService.updateProviderByForm(provider);

       if(flag){
           resp.sendRedirect(req.getContextPath()+"/jsp/provider.do?method=query");
       }else{
           resp.sendRedirect("providermodify.jsp");
       }


    }

    // 删除供应商
    public void delprovider(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        // 獲取前端數據
        String proid=req.getParameter("proid");
        // 調用業務層
        ProviderService providerService=new ProviderServiceImpl();
        boolean flag=providerService.delProviderByPid(Integer.parseInt(proid));

        // 萬能的 map
        HashMap<String,String > resultMap=new HashMap<String, String>();

        if (flag){
            resultMap.put("delResult","true");

        }else{
            resultMap.put("delResult","false");
        }
        if(proid==null || proid.equals("")){
            resultMap.put("delResult","notexist");
        }

        // 轉換為json轉回前端
        resp.setContentType("application/json");
        PrintWriter writer=resp.getWriter();
        writer.write(JSONArray.toJSONString(resultMap));

        writer.flush();
        writer.close();
    }


}
