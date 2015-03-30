package com.e1858.building.bean;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class CaseBean implements Serializable {
	public String		remark;		//	String	备注
	public String		createTime;	//	String	上传时间
	public List<String>	pictureUrls;	//	String数组	图片链接数组
	public String		ID;			//	string	ID

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public List<String> getPictureUrls() {
		return pictureUrls;
	}

	public void setPictureUrls(List<String> pictureUrls) {
		this.pictureUrls = pictureUrls;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

}
