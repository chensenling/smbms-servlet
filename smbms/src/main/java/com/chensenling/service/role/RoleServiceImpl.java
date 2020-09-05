package com.chensenling.service.role;

import com.chensenling.dao.BaseDao;
import com.chensenling.dao.role.RoleDao;
import com.chensenling.dao.role.RoleDaoImpl;
import com.chensenling.pojo.Role;

import java.sql.Connection;
import java.util.List;

public class RoleServiceImpl implements  RoleService {
    private RoleDao roleDao = null;

    public RoleServiceImpl() {
        roleDao = new RoleDaoImpl();
    }

    public List<Role> getRoleList() {
        Connection connection = null;
        List<Role> roleList = null;
        connection = BaseDao.getConnection();

        roleList = roleDao.getRoleList(connection);

        //释放 connection
        BaseDao.closeResource(connection, null, null);
        return roleList;
    }


}


