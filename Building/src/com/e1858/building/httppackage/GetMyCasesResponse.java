package com.e1858.building.httppackage;

import java.util.List;

import com.e1858.building.bean.CaseBean;
import com.e1858.building.bean.PacketResp;

@SuppressWarnings("serial")
public class GetMyCasesResponse extends PacketResp {

	public List<CaseBean>	cases;	//	Case数组	案例数组

	public List<CaseBean> getCases() {
		return cases;
	}

	public void setCases(List<CaseBean> cases) {
		this.cases = cases;
	}

}
