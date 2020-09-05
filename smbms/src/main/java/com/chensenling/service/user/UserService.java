package com.chensenling.service.user;

import com.chensenling.pojo.Role;
import com.chensenling.pojo.User;

import java.sql.SQLException;
import java.util.List;

public interface UserService {

    //用户登录
    public User login(String  userCode,String  password);
    //根据用户id修改密码
    public boolean  updatePwd(int id,String pwd) throws SQLException;

    // 查询记录数
    public int getUserCount(String username,int userRole);

    //得到用户列表
    public List<User> getUserList(String username,int userRole,int currentPageNo,int pageSize);

    // 根据uid 用户编号 得到用户信息
    public User getUserByUid(int uid) throws SQLException;
    //得到角色列表
    public List<Role> getRoleList();
    //根据表单更新用户
    public boolean updateUserByForm(User user) throws SQLException;

    //删除用户
    public boolean delUserByUid(int uid) throws SQLException;

    // 添加用户
    public boolean addUser(User user) throws SQLException;
    // 按userCode在数据库里面查
    public User queryUserByUserCode(String userCode) throws SQLException;

}
