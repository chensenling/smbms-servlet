package com.chensenling.service.bill;


import com.chensenling.dao.BaseDao;
import com.chensenling.dao.bill.BillDao;
import com.chensenling.dao.bill.BillDaoImpl;
import com.chensenling.pojo.Bill;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BillServiceImpl implements BillService {
	
	private BillDao billDao;
	public BillServiceImpl(){
		billDao = new BillDaoImpl();
	}

	public boolean add(Bill bill) {

		boolean flag = false;
		Connection connection = null;
		try {
			connection = BaseDao.getConnection();
			// 關閉自動提交
			connection.setAutoCommit(false);//开启JDBC事务管理
			if(billDao.add(connection,bill) > 0)
				flag = true;
			//提交
			connection.commit();
		} catch (Exception e) {

			e.printStackTrace();
			try {
//				//回滾
				connection.rollback();
			} catch (SQLException e1) {

				e1.printStackTrace();
			}
		}finally{
			//在service层进行connection连接的关闭
			BaseDao.closeResource(connection, null, null);
		}
		return flag;
	}


	public List<Bill> getBillList(Bill bill) {

		Connection connection = null;
		List<Bill> billList = null;
		System.out.println("query productName ---- > " + bill.getProductName());
		System.out.println("query providerId ---- > " + bill.getProviderID());
		System.out.println("query isPayment ---- > " + bill.getIsPayment());
		
		try {
			connection = BaseDao.getConnection();
			billList = billDao.getBillList(connection, bill);
		} catch (Exception e) {

			e.printStackTrace();
		}finally{
			BaseDao.closeResource(connection, null, null);
		}
		return billList;
	}


	public boolean deleteBillById(String delId) {

		Connection connection = null;
		boolean flag = false;
		try {
			connection = BaseDao.getConnection();
			if(billDao.deleteBillById(connection, delId) > 0)
				flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		}finally{
			BaseDao.closeResource(connection, null, null);
		}
		return flag;
	}


	public Bill getBillById(String id) {

		Bill bill = null;
		Connection connection = null;
		try{
			connection = BaseDao.getConnection();
			bill = billDao.getBillById(connection, id);
		}catch (Exception e) {

			e.printStackTrace();
			bill = null;
		}finally{
			BaseDao.closeResource(connection, null, null);
		}
		return bill;
	}


	public boolean modify(Bill bill) {

		Connection connection = null;
		boolean flag = false;
		try {
			connection = BaseDao.getConnection();
			if(billDao.modify(connection,bill) > 0)
				flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		}finally{
			BaseDao.closeResource(connection, null, null);
		}
		return flag;
	}

}
