package com.e1858.building.httppackage;

import java.util.List;

import com.e1858.building.bean.CommentInfo;
import com.e1858.building.bean.PacketResp;
import com.e1858.building.net.HttpDefine;

@SuppressWarnings("serial")
public class GetCommentInfoReponse extends PacketResp {
	private List<CommentInfo>	commentInfos;

	public GetCommentInfoReponse() {
		command = HttpDefine.GETCOMMENTINFO_RESP;
	}

	public List<CommentInfo> getCommentInfos() {
		return commentInfos;
	}

	public void setCommentInfos(List<CommentInfo> commentInfos) {
		this.commentInfos = commentInfos;
	}

}
