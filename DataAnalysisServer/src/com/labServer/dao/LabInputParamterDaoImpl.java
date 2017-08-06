package com.labServer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.fleety.util.pool.db.DbConnPool.DbHandle;
import com.fleety.util.pool.db.DbConnPool.StatementHandle;
import com.labServer.model.LabInputParamter;

import server.db.DbServer;

public class LabInputParamterDaoImpl implements LabInputParamterDao {

	public void addLabInputParamter(LabInputParamter labInputParamter) {
		DbHandle con = DbServer.getSingleInstance().getConn();
		StatementHandle stmt;
		try {
			stmt = con.prepareStatement(
					"insert into ? (INPUTPROBENUMBER,CREATEDON,INPUTTEMPERATURE,INPUTHUMIDITY) values(?,?,?,?)");
			stmt.setString(1, labInputParamter.getInputTableName());
			stmt.setString(2, labInputParamter.getInputProbeNumber());
			stmt.setString(3, labInputParamter.getCreatedOn());
			stmt.setDouble(4, labInputParamter.getInputTemperature());
			stmt.setDouble(5, labInputParamter.getInputHumidity());
			stmt.executeUpdate();
			con.commit();
			DbServer.getSingleInstance().releaseConn(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Double findAVGInputTemperature(LabInputParamter labInputParamter, String inputTable) {
		DbHandle con = DbServer.getSingleInstance().getConn();
		StatementHandle stmt;
		Double avgTemp = 0.0;
		try {
			stmt = con.prepareStatement(
					"select AVG(INPUTTEMPERATURE) from ? where createdOn >  DATE_SUB(?, INTERVAL 5 SECOND )");
			stmt.setString(1, labInputParamter.getInputTableName());
			stmt.setString(2, labInputParamter.getCreatedOn());
			ResultSet sets = stmt.executeQuery();
			if (sets.next()) {
				avgTemp = Double.parseDouble(sets.getString(1));
			}
			DbServer.getSingleInstance().releaseConn(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return avgTemp;
	}

	public void addListItemsToSumInput(List<LabInputParamter> list) {
		DbHandle con = DbServer.getSingleInstance().getConn();
		StatementHandle stmt;
		try {
			String sql = "insert into lab_inputparamter (INPUTPROBENUMBER,CREATEDON,INPUTTEMPERATURE,INPUTHUMIDITY) VALUES(?,?,?,?)";
			stmt = con.prepareStatement(sql);
			con.setAutoCommit(false);
			for (int i = 0; i < list.size(); i++) {
				stmt.setString(1, list.get(i).getInputProbeNumber());
				stmt.setString(2, list.get(i).getCreatedOn());
				stmt.setDouble(3, list.get(i).getInputTemperature());
				stmt.setDouble(4, list.get(i).getInputHumidity());
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
