package com.labServer.manager;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.labServer.dao.LabModifyDaoImpl;
import com.labServer.model.LabModify;

public class LabModifyManagerImpl implements LabModifyManager {

	LabModifyDaoImpl labModifyDaoImpl = new LabModifyDaoImpl();

	/**
	 * 获取校准值表所有数据（现用）
	 *
	 *
	 */
	public Map<String, LabModify> resultSetToMapFromModify() {
		ResultSet rs = labModifyDaoImpl.findLabModify();

		Map<String, LabModify> modMap = new HashMap<String, LabModify>();
		ResultSetMetaData md;
		if (rs == null)
			return Collections.EMPTY_MAP;
		try {
			md = rs.getMetaData();
			int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
			// 得到结果集(rs)的结构信息，比如字段数、字段名等
			while (rs.next()) {
				LabModify lm = new LabModify();
				for (int i = 0; i <= columnCount; i++) {
					lm.setInputProbeNumber(rs.getObject("inputProbeNumber").toString());
					lm.setDisProbeNumber(rs.getObject("disProbeNumber").toString());
					lm.setModifyTemp(rs.getDouble("modifyTemp"));
					lm.setModifyHum(rs.getDouble("modifyHum"));
				}
				modMap.put(lm.getInputProbeNumber(), lm);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return modMap;
	}

}
