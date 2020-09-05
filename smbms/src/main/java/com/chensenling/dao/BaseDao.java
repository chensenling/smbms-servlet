package com.chensenling.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class BaseDao {
    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    //在类加载时，就读取db.properties进行初始化
    static{
        //读取db.properties
        //利用类加载器读取
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
        //创建一个properties对象
       Properties pro = new Properties();
        try {
            //load方法加载流
            pro.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //初始化 成员变量
        driver=pro.getProperty("driver");
        url=pro.getProperty("url");
        username=pro.getProperty("username");
        password=pro.getProperty("password");

    }

    //提供外部 得到connection
    public static Connection getConnection()  {
        Connection connection=null;
        try {
            // 注册驱动
            Class.forName(driver);
            // 获取连接
            connection=DriverManager.getConnection(url,username,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    //编写查询公共类
    public static ResultSet execute(Connection connection,String sql,Object[] params,ResultSet resultSet,PreparedStatement preparedStatement ) throws SQLException {
          preparedStatement = connection.prepareStatement(sql);
          if(params!=null){
              for(int i=0;i<params.length;i++) {
                  preparedStatement.setObject(i+1,params[i]);

              }
              resultSet= preparedStatement.executeQuery();
          }else{
              resultSet=preparedStatement.executeQuery();
          }

         return resultSet;
    }

    //编写增删该公共方法
    public static int execute(Connection connection,String sql,Object[] params,PreparedStatement preparedStatement ) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);
        for(int i=0;i<params.length;i++) {
            preparedStatement.setObject(i+1,params[i]);
            System.out.println(params[i]);
        }
        int updateRows= preparedStatement.executeUpdate();
        return updateRows;
    }

    // 释放资源
    public static Boolean closeResource(Connection connection,ResultSet resultSet,PreparedStatement preparedStatement){
        Boolean flag= true;
        if(connection!=null){
            try {
                connection.close();
                //GC回收
                connection =null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag=false;
            }
        }
        if(resultSet!=null){
            try {
                resultSet.close();
                //GC回收
                resultSet =null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag=false;
            }
        }
        if(preparedStatement!=null){
            try {
                preparedStatement.close();
                //GC回收
                preparedStatement =null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag=false;
            }
        }
        return flag;
    }
}
