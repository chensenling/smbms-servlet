package com.chensenling.dao.user;

import com.chensenling.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {

    //得到登录的用户
    public User getLoginUser(Connection connection, String userCode) throws SQLException;

    //修改用户密码
    public int updatePwd(Connection connection,int id,String password) throws SQLException;

    // 根据用户名或角色名查询用户总数
    public int getUserCount(Connection connection,String username,int userrole) throws SQLException;

    // 得到用户列表
    public List<User> getUserList(Connection connection,String username,int userRole, int currentPageNo,int pageSize) throws SQLException;

    // 根据uid 用户编号查询用户
    public User getUserByUid(Connection connection,int uid) throws SQLException;

    // 根据表单数据更新用户
    public int updateUserByForm(Connection connection,User newuser) throws SQLException;

    //删除用户
    public int delUserByUid(Connection connection,int uid) throws SQLException;

    // 添加用户
    public int addUser(Connection connection,User user) throws SQLException;
    // 按userCode在数据库里面查
    public User queryUserByUserCode(Connection connection,String userCode) throws SQLException;
}
