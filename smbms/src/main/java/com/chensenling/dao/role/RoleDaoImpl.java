package com.chensenling.dao.role;

import com.chensenling.dao.BaseDao;
import com.chensenling.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl  implements  RoleDao{

    //获取角色列表
    public List<Role> getRoleList(Connection connection) {

        ResultSet resultSet =null;
        PreparedStatement pre=null;
        Role role= null;
        ArrayList<Role> roleList=new ArrayList<Role>();
        String sql = "SELECT * FROM smbms_role";
        try {
            resultSet = BaseDao.execute(connection, sql, null, resultSet, pre);

            while(resultSet.next()){
               role=new Role();
               role.setId(resultSet.getInt("id"));
               role.setRoleCode(resultSet.getString("roleCode"));
               role.setRoleName(resultSet.getString("roleName"));
               role.setCreatedBy(resultSet.getInt("createdBy"));
               role.setCreationDate(resultSet.getDate("creationDate"));
               role.setModifyBy(resultSet.getInt("modifyBy"));
               role.setModifyDate(resultSet.getDate("modifyDate"));

               roleList.add(role);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeResource(null,resultSet,pre);
        }
        System.out.println(role);
      return roleList;
    }
}
