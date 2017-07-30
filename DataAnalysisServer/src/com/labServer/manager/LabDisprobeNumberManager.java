package com.labServer.manager;

import java.util.List;
import java.util.Map;

import com.labServer.model.LabDisprobeNumber;

public interface LabDisprobeNumberManager {

	//LabDisprobeNumber getDisprobeNumberByInputProbNum(String inputProbeNumber);

	Map<String, LabDisprobeNumber> getSumDisprobeNumber();
}
