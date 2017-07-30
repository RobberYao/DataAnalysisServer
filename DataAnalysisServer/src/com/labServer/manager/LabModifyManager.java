package com.labServer.manager;

import java.util.List;
import java.util.Map;

import com.labServer.model.LabModify;

public interface LabModifyManager {

	List<Map<String, Double>> getLabModifyByInputProbNum(String inputProbreNumber);

	Map<String, LabModify> getSumLabModify();
}
