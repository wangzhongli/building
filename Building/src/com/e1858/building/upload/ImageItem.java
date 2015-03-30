package com.e1858.building.upload;

import java.io.Serializable;

/**
 * 一个图片对象
 * 
 * @author Administrator
 */
@SuppressWarnings("serial")
public class ImageItem implements Serializable {
	public String	imageId;
	public String	thumbnailPath;
	public String	imagePath;
	public boolean	isSelected	= false;

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
