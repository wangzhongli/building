package com.e1858.building.receiver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.common.utils.JsonUtil;
import com.common.utils.StringUtils;
import com.e1858.building.Constant;
import com.e1858.building.R;
import com.e1858.building.bean.PayLoad;
import com.e1858.building.order.RobListActivity;
import com.e1858.building.persondata.NotCompleteActivity;
import com.e1858.building.persondata.NotReserveActivity;
import com.igexin.sdk.PushConsts;

/**
 * 推送
 * 
 * @author jiajia
 */
public class PushMessageService extends BroadcastReceiver {
	// Notification的标示ID
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Bundle bundle = intent.getExtras();
		Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));
		switch (bundle.getInt(PushConsts.CMD_ACTION)) {
		case PushConsts.GET_MSG_DATA:
			// 获取透传数据
			// String appid = bundle.getString("appid");
			byte[] payload = bundle.getByteArray("payload");
			String taskid = bundle.getString("taskid");
			String messageid = bundle.getString("messageid");
			if (payload != null) {
				String data = new String(payload);
				Log.d("GetuiSdkDemo", "Got Payload:" + data);
				displayNotification(intent, context, data);
			}
			break;
		}
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void displayNotification(Intent intent, Context context, String data) {
		//解析payload 得到title 和content
		try {
			if (!StringUtils.isEmpty(data)) {
				PayLoad payLoad = JsonUtil.fromJson(data, PayLoad.class);
				if (payLoad == null) {
					return;
				}
				NotificationManager nm = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
//				//创建通知  
				Notification notification = new Notification();
				notification.icon = R.drawable.launcher;
				notification.tickerText = context.getResources().getString(R.string.app_name);
				notification.when = System.currentTimeMillis();
				notification.defaults = Notification.DEFAULT_ALL;
				Bundle bundle = new Bundle();
				//重新定义该方法中的intent参数对象  
				//该Intent使得当用户点击该通知后发出这个Intent  
				switch (payLoad.getTypeID()) {
				case Constant.PUSH_TYPE.TYPE_ARRIVE:
					intent = new Intent(context, NotCompleteActivity.class);
//					bundle.putString(NoticeActivity.NOTICE_ID, payLoad.getID());
					intent.putExtras(bundle);
					break;
				case Constant.PUSH_TYPE.TYPE_PAIDAN:
				case Constant.PUSH_TYPE.TYPE_RELEASE:
					intent = new Intent(context, NotReserveActivity.class);
					intent.putExtras(bundle);
					break;
//				case Constant.PUSH_TYPE.COMPLAINT:
//					intent = new Intent(context, ComplaintDetailActivity.class);
//					bundle.putInt(Constant.DETAIL_ID, payLoad.getID());
//					intent.putExtras(bundle);
//					break;
//				case Constant.PUSH_TYPE.MAINTENANCE:
//					intent = new Intent(context, MaintenanceDetailActivity.class);
//					bundle.putInt(Constant.DETAIL_ID, payLoad.getID());
//					intent.putExtras(bundle);
//					break;
//		        case "打开连接":
//		        	intent = new Intent(Intent.ACTION_VIEW, Uri.parse("连接地址"));
//		        	break;
				default:
					intent = new Intent(context, RobListActivity.class);
					break;
				}
				//请注意，如果要以该Intent启动一个Activity，一定要设置 Intent.FLAG_ACTIVITY_NEW_TASK 标记  
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// PendingIntent.FLAG_UPDATE_CURRENT表示：如果描述的PendingIntent已存在，则改变已存在的PendingIntent的Extra数据为新的PendingIntent的Extra数据  
				PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				notification.setLatestEventInfo(context, context.getResources().getString(R.string.app_name),
						payLoad.getContent(), contentIntent);
				notification.flags = Notification.FLAG_AUTO_CANCEL;

//				Notification notification = new Notification.Builder(context).setSmallIcon(R.drawable.ic_launcher)
//						.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
//						.setTicker(context.getResources().getString(R.string.app_name))
//						.setWhen(System.currentTimeMillis()).setContentTitle(payLoad.getTitle())
//						.setContentText(payLoad.getContent()).setContentIntent(contentIntent).setAutoCancel(true)
//						.build();

				nm.notify(0x0, notification);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
