package com.labServer.manager;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.fleety.util.pool.db.DbConnPool.DbHandle;
import com.fleety.util.pool.db.DbConnPool.StatementHandle;
import com.labServer.model.LabDisplayParamter;
import com.labServer.model.LabModify;

import server.db.DbServer;

public class LabDisplayParamterManagerImpl implements LabDisplayParamterManager {

	/**
	 * 新增显示数据(暂不用)
	 * 
	 * @param labInputParamter
	 */
	/*
	 * public void addLabDiaplayParamter(LabDisplayParamter labDisplayParamter,
	 * List<Map<String, Double>> modifys) { SqlSession sqlSession =
	 * MyBatisUtil.getSqlSession(); LabDisplayParamterMapper
	 * labDisplayParamterMapper =
	 * sqlSession.getMapper(LabDisplayParamterMapper.class);
	 * calParamterByModify(labDisplayParamter, modifys);// 计算校准值
	 * labDisplayParamterMapper.insertLabDisplayParamter(labDisplayParamter);
	 * sqlSession.commit(); sqlSession.close();
	 * 
	 * }
	 */

	/**
	 * 新增显示数据,动态表名(暂不用)
	 * 
	 * @param labInputParamter
	 */
	/*
	 * public void addLabDiaplayParamter(LabDisplayParamter labDisplayParamter,
	 * List<Map<String, Double>> modifys, String displayTable) { SqlSession
	 * sqlSession = MyBatisUtil.getSqlSession(); LabDisplayParamterMapper
	 * labDisplayParamterMapper =
	 * sqlSession.getMapper(LabDisplayParamterMapper.class);
	 * calParamterByModify(labDisplayParamter, modifys);// 计算校准值
	 * labDisplayParamterMapper.insertLabDisplayParamterByDisplayTable(
	 * labDisplayParamter, displayTable); sqlSession.commit();
	 * sqlSession.close();
	 * 
	 * }
	 */

	/**
	 * 传入显示探头参数对象、校准值，并计算。
	 * 
	 * @param labDisplayParamter
	 * @param modify
	 * @return
	 */
	public LabDisplayParamter calParamterByModify(LabDisplayParamter labDisplayParamter,
			Map<String, LabModify> modifys) {
		// 遍历校准 并对温湿进行赋值（暂无光照）
		String inputNum = labDisplayParamter.getInputProbeNumber();
		labDisplayParamter
				.setDisTemperature(modifys.get(inputNum).getModifyTemp() + labDisplayParamter.getDisTemperature());
		labDisplayParamter.setDisHumidity(modifys.get(inputNum).getModifyHum() + labDisplayParamter.getDisHumidity());
		return labDisplayParamter;

	}

	/**
	 * 插入显示数据汇总表
	 * 
	 */
	public void addListItemsToSumDisplay(List<LabDisplayParamter> list) {
		DbHandle con = DbServer.getSingleInstance().getConn();
		StatementHandle stmt;
		try {
			String sql = "insert into lab_displayparamter (INPUTPROBENUMBER,DISPROBENUMBER,CREATEDON,DISTEMPERATURE,DISHUMIDITY)VALUES(?,?,?,?,?)";
			stmt = con.prepareStatement(sql);
			con.setAutoCommit(false);
			for (int i = 0; i < list.size(); i++) {
				stmt.setString(1, list.get(i).getInputProbeNumber());
				stmt.setString(2, list.get(i).getDisProbeNumber());
				stmt.setString(3, list.get(i).getCreatedOn());
				stmt.setDouble(4, list.get(i).getDisTemperature());
				stmt.setDouble(5, list.get(i).getDisHumidity());
				stmt.addBatch();
			}
			stmt.executeBatch();
			con.commit();
			DbServer.getSingleInstance().releaseConn(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 插入显示数据分表
	 * 
	 * 
	 */
	public void addListItemsToDiffDisplay(List<LabDisplayParamter> list) {
		DbHandle con = DbServer.getSingleInstance().getConn();
		StatementHandle stmt;
		try {
			String sql = "insert into ? (INPUTPROBENUMBER,DISPROBENUMBER,CREATEDON,DISTEMPERATURE,DISHUMIDITY)VALUES(?,?,?,?,?)";
			stmt = con.prepareStatement(sql);
			con.setAutoCommit(false);
			for (int i = 0; i < list.size(); i++) {
				stmt.setString(1, list.get(i).getDisplayTableName());
				stmt.setString(2, list.get(i).getInputProbeNumber());
				stmt.setString(3, list.get(i).getDisProbeNumber());
				stmt.setString(4, list.get(i).getCreatedOn());
				stmt.setDouble(5, list.get(i).getDisTemperature());
				stmt.setDouble(6, list.get(i).getDisHumidity());
				stmt.addBatch();
			}
			stmt.executeBatch();
			con.commit();
			DbServer.getSingleInstance().releaseConn(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
