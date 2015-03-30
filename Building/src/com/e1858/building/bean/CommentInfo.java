package com.e1858.building.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CommentInfo implements Serializable {
	public String	commentTime;
	public String	commentDec;
	public String	headPortrait;
	public String	name;
	public int		star;

	public String getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

	public String getCommentDec() {
		return commentDec;
	}

	public void setCommentDec(String commentDec) {
		this.commentDec = commentDec;
	}

	public String getHeadPortrait() {
		return headPortrait;
	}

	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

}
