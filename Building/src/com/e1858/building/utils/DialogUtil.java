package com.e1858.building.utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.simonvt.numberpicker.NumberPicker;
import net.simonvt.numberpicker.NumberPicker.Formatter;
import net.simonvt.numberpicker.NumberPicker.OnValueChangeListener;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.common.utils.DateUtils;
import com.common.utils.StringUtils;
import com.e1858.building.R;
import com.e1858.building.utils.LocationManager.OnloacationListener;

public class DialogUtil {
	private static Dialog	dialog;
	static Formatter		formatter		= new Formatter() {

												@Override
												public String format(int value) {
													return String.format("%02d:00", value);
												}
											};

	static Formatter		formatter_date	= new Formatter() {

												@Override
												public String format(int value) {
													return String.format("%02d", value);
												}
											};

	public static void show2Btn(Context context, String title, OnClickListener upLinstener,
			OnClickListener downLinstener) {
		View view = View.inflate(context, R.layout.selete_pic_dialog, null);
		dialog = new Dialog(context);
		dialog.setTitle(title);
		dialog.setContentView(view);
		Button take_pic = (Button) view.findViewById(R.id.take_pic);
		Button choose_pic = (Button) view.findViewById(R.id.choose_pic);
		take_pic.setOnClickListener(upLinstener);
		choose_pic.setOnClickListener(downLinstener);
		dialog.show();
	}

	@SuppressLint("NewApi")
	public static void showWithEdit(final Context context, String title, String hint,
			final OnEditFinishedListener listener) {
		final AlertDialog.Builder builder = new Builder(context);
		builder.setTitle(title);
		View view = View.inflate(context, R.layout.dialog_edit_btn_layout, null);
		builder.setView(view);
		final EditText dialog_edittext = (EditText) view.findViewById(R.id.dialog_edittext);
		final Button dialog_button = (Button) view.findViewById(R.id.dialog_button);
		dialog_edittext.setHint(hint);
		dialog_button.setEnabled(false);
		final AlertDialog create = builder.create();
		create.show();
		dialog_edittext.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void afterTextChanged(Editable s) {
				if (!StringUtils.isEmpty(s.toString())) {
					dialog_button.setEnabled(true);
				} else {
					dialog_button.setEnabled(false);
				}
			}
		});
		dialog_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				create.dismiss();
				listener.onFinished(dialog_edittext.getText().toString());
			}
		});
	}

	public static void showWithEditWithCancel(final Context context, String title, final OnEditFinishedListener listener) {
		AlertDialog.Builder builder = new Builder(context);
		final StringBuffer sb = new StringBuffer();
		builder.setTitle(title);
		View view = View.inflate(context, R.layout.dialog_date_picker_layout, null);
		builder.setView(view);
		final Calendar calendar = Calendar.getInstance(Locale.CHINA);
		final int year = calendar.get(Calendar.YEAR);
		final int monthOfYear = calendar.get(Calendar.MONTH);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);

		String month = String.format("%02d", (monthOfYear + 1));
		String day = String.format("%02d", dayOfMonth);

		final NumberPicker picker_from = (NumberPicker) view.findViewById(R.id.time_picker_from);
		final NumberPicker date_picker_month = (NumberPicker) view.findViewById(R.id.date_picker_month);
		final NumberPicker date_picker_day = (NumberPicker) view.findViewById(R.id.date_picker_day);
		final TextView tv_year = (TextView) view.findViewById(R.id.tv_year);

		tv_year.setText(year + "-");

		picker_from.setFormatter(formatter);

		date_picker_day.setFormatter(formatter_date);
		date_picker_month.setFormatter(formatter_date);

		picker_from.setMinValue(1);
		picker_from.setMaxValue(24);

		date_picker_month.setMinValue(1);
		date_picker_month.setMaxValue(12);

		date_picker_day.setMinValue(1);
		int value = calendar.getActualMaximum(Calendar.DATE);
		date_picker_day.setMaxValue(value);

		picker_from.findViewById(R.id.np__numberpicker_input).setVisibility(View.GONE);
		date_picker_month.findViewById(R.id.np__numberpicker_input).setVisibility(View.GONE);
		date_picker_day.findViewById(R.id.np__numberpicker_input).setVisibility(View.GONE);

		//设置默认值
		date_picker_month.setValue(Integer.parseInt(month));
		date_picker_day.setValue(Integer.parseInt(day));
		picker_from.setValue(hour + 2);
		int cDay = 0;
		int cMonth = 0;
		if (hour + 2 > 24) {
			cDay = Integer.parseInt(day) + 1;
			date_picker_day.setValue(cDay);
		}
		if (cDay > calendar.getActualMaximum(Calendar.DATE)) {
			cMonth = Integer.parseInt(month) + 1;
			date_picker_month.setValue(cMonth);
		}
		if (cMonth > 12) {
			tv_year.setText((year + 1) + "-");
		}
		date_picker_month.setOnValueChangedListener(new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				calendar.set(year, newVal, -1);
				int value = calendar.getActualMaximum(Calendar.DATE);
				date_picker_day.setMaxValue(value);
			}
		});

		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (listener != null) {
					sb.append(year);
					sb.append("-");
					sb.append(String.format("%02d", date_picker_month.getValue()));
					sb.append("-");
					sb.append(String.format("%02d", date_picker_day.getValue()));
					sb.append(" ");
					sb.append(String.format("%02d:00", picker_from.getValue()));
					listener.onFinished(sb.toString());
				}
			}
		});
		builder.setNegativeButton("取消", null);
		builder.create().show();
	}

	public interface OnEditFinishedListener {
		public void onFinished(String text);
	}

	public static int getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		return cal.getActualMaximum(Calendar.DATE);

	}

	public static void dialogOpen(Context context, String title, String msg,
			DialogInterface.OnClickListener mOkOnClickListener) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(msg);
		builder.setTitle(title);
		builder.setPositiveButton("确认", mOkOnClickListener);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	public static void dialogOpen(Context context, String title, String msg, String ok,
			DialogInterface.OnClickListener mOkOnClickListener) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(msg);
		builder.setTitle(title);
		builder.setPositiveButton(ok, mOkOnClickListener);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	public static void dialogOpenSignAddress(Context context, String title,
			final OnSignAddFinishedListener mOnAddlistener) {
		final Map<String, String> map = new HashMap<String, String>();
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle(title);
		View view = View.inflate(context, R.layout.sign_address_layout, null);
		TextView sign_address_time = (TextView) view.findViewById(R.id.sign_address_time);
		final TextView sign_address_address = (TextView) view.findViewById(R.id.sign_address_address);
		sign_address_time.setText(DateUtils.getCurrentDate("yyyy-MM-dd HH:mm"));
		LocationManager.getInstanse(context).initLocation(new OnloacationListener() {

			@Override
			public void startLocation() {
				sign_address_address.setText("定位中，请稍后...");
			}

			@Override
			public void endLocation(AMapLocation aMapLocation) {
				sign_address_address.setText(aMapLocation.getAddress());
				map.put("longitude", aMapLocation.getLongitude() + "");
				map.put("latitude", aMapLocation.getLatitude() + "");
				map.put("address", aMapLocation.getAddress() + "");
			}

			@Override
			public void errorLocation(String error) {
				sign_address_address.setText(error);
			}
		});
		builder.setView(view);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mOnAddlistener.onFinished(map.get("longitude"), map.get("latitude"), map.get("address"));
			}

		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	public interface OnSignAddFinishedListener {
		public void onFinished(String longitude, String latitude, String address);
	}
}
