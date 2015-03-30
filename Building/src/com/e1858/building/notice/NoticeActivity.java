package com.e1858.building.notice;

import android.os.Bundle;
import android.widget.TextView;

import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.bean.Notice;
import com.e1858.building.httppackage.ReadNoticeRequest;
import com.e1858.building.httppackage.ReadNoticeResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.utils.ResponseUtils;

public class NoticeActivity extends BaseActivity {
	public static final String	NOTICE_ID	= "notice_id";
	private String				notice_id;
	private TextView			notice_time;
	private TextView			notice_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice);
		if (null != getIntent()) {
			notice_id = getIntent().getStringExtra(NOTICE_ID);
			loadNotice(notice_id);
		}
		intiView();
	}

	private void intiView() {
		notice_time = (TextView) this.findViewById(R.id.notice_time);
		notice_content = (TextView) this.findViewById(R.id.notice_content);
	}

	/**
	 * 绑定数据
	 * 
	 * @param notice
	 *            void
	 */
	private void bindDate(Notice notice) {
		notice_time.setText(notice.getSendTime());
		notice_content.setText(notice.getContent());
		setTitle(notice.getTitle());
	}

	/**
	 * 加载通知数据
	 * 
	 * @param notice_id
	 *            void
	 */
	private void loadNotice(String notice_id) {
		ResponseHandler<ReadNoticeResponse> responseHandler = new ResponseHandler<ReadNoticeResponse>() {
			@Override
			public void onStart() {
				openProgressDialog("请稍候...");
			}

			@Override
			public void onFinish(ReadNoticeResponse response, String error) {
				closeProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					Notice notice = response.getNotice();
					bindDate(notice);
				}
			}
		};

		ReadNoticeRequest request = new ReadNoticeRequest();
		request.setID(notice_id);
		HttpPacketClient.postPacketAsynchronous(request, ReadNoticeResponse.class, responseHandler, true);
	}
}
