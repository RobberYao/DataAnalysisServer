package com.labServer.manager;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.labServer.dao.LabDisprobeNumberDao;
import com.labServer.model.LabDisprobeNumber;

public class LabDisprobeNumberManagerImpl implements LabDisprobeNumberManager {

	LabDisprobeNumberDao labDisprobeNumberDao;

	public Map<String, LabDisprobeNumber> resultSetToListFromDisProbe() {

		ResultSet rs = labDisprobeNumberDao.findLabDisprobeNumber();

		Map<String, LabDisprobeNumber> disMap = new HashMap<String, LabDisprobeNumber>();
		ResultSetMetaData md;
		if (rs == null) {
			return Collections.EMPTY_MAP;
		}
		try {
			md = rs.getMetaData();
			// 得到结果集(rs)的结构信息，比如字段数、字段名等
			int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
			while (rs.next()) {
				LabDisprobeNumber ld = new LabDisprobeNumber();
				for (int i = 1; i <= columnCount; i++) {
					ld.setInputProbeNumber(rs.getObject("inputProbeNumber").toString());
					ld.setDisplayProbeNumber(rs.getObject("displayProbeNumber").toString());
					ld.setTab_InputName(rs.getObject("tab_InputName").toString());
					ld.setTab_DisplayName(rs.getObject("tab_DisplayName").toString());
				}
				disMap.put(ld.getInputProbeNumber(), ld);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return disMap;
	}
}
