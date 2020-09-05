package com.chensenling.dao.role;

import com.chensenling.pojo.Role;

import java.sql.Connection;
import java.util.List;

public interface RoleDao {

    // 获取角色列表
    public List<Role> getRoleList(Connection connection);

}
