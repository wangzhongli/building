package com.e1858.building.bean;

import java.io.Serializable;

/**
 * 日期: 2015年1月7日 上午8:50:37
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
@SuppressWarnings("serial")
public class ServiceTypeBean implements Serializable {
	public int		ID;
	public String	name;

	public int getID() {
		return ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServiceTypeBean other = (ServiceTypeBean) obj;
		if (ID != other.ID)
			return false;
		return true;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}
