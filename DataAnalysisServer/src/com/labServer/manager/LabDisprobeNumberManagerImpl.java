package com.labServer.manager;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.fleety.util.pool.db.DbConnPool.DbHandle;
import com.fleety.util.pool.db.DbConnPool.StatementHandle;
import com.labServer.mapping.LabDisprobeNumberMapper;
import com.labServer.model.LabDisprobeNumber;
import com.labServer.model.LabModify;
import com.labServer.util.MyBatisUtil;

import server.db.DbServer;

public class LabDisprobeNumberManagerImpl implements LabDisprobeNumberManager {

	/**
	 * 通过原探头号查找对应探头映射表实力
	 * 
	 * @param inputProbeNumber
	 * @return
	 */
/*	public LabDisprobeNumber getDisprobeNumberByInputProbNum(String inputProbeNumber) {
		DbHandle con = DbServer.getSingleInstance().getConn();
		StatementHandle stmt;
		
		String sql="select * from lab_disprobenumber where INPUTPROBENUMBER = ?";
		
		ResultSet sets=stmt.executeQuery(sql);
		
		if (sets.next()) {
			sets.getMetaData();
		}

		return labDisprobeNumber;
	}*/

	public Map<String, LabDisprobeNumber> getSumDisprobeNumber() {
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		LabDisprobeNumberMapper mapper = sqlSession.getMapper(LabDisprobeNumberMapper.class);
		List<LabDisprobeNumber> labDisprobeNumbers = mapper.getSumDisprobeNumber();
		Map<String, LabDisprobeNumber> LabDisprobeNumberMaps = new HashMap<String, LabDisprobeNumber>();
		sqlSession.commit();
		sqlSession.close();
		for (LabDisprobeNumber labDisprobeNumber : labDisprobeNumbers) {
			LabDisprobeNumberMaps.put(labDisprobeNumber.getInputProbeNumber(), labDisprobeNumber);
		}
		return LabDisprobeNumberMaps;
	}

}
