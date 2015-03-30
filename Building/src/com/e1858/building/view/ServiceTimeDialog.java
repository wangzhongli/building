package com.e1858.building.view;

import java.util.ArrayList;
import java.util.List;

import net.simonvt.numberpicker.NumberPicker;
import net.simonvt.numberpicker.NumberPicker.Formatter;
import net.simonvt.numberpicker.NumberPicker.OnValueChangeListener;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.common.utils.StringUtils;
import com.e1858.building.R;

/**
 * 日期: 2015年1月7日 上午11:06:03
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class ServiceTimeDialog extends AlertDialog {

	static int[]	weekDayIds			= { R.id.textView_monday, R.id.textView_tuesday, R.id.textView_wednesday,
			R.id.textView_thursday, R.id.textView_friday, R.id.textView_saturday, R.id.textView_sunday };
	static String[]	weekDayFmtTexts		= { "Mon", "Tues", "Wed", "Thur", "Fri", "Sat", "Sun" };
	static String[]	weekDayShowTexts	= { "一", "二", "三", "四", "五", "六", "日" };
	NumberPicker	timePicker_begin;
	NumberPicker	timePicker_end;																			;
	List<TextView>	weekDays			= new ArrayList<TextView>();
	Formatter		formatter			= new Formatter() {

											@Override
											public String format(int value) {
												return String.format("%02d:00", value);
											}
										};

	/**
	 * @param context
	 */
	@SuppressLint("NewApi")
	public ServiceTimeDialog(final Context context, final OnFinishedListener listener) {
		super(context);
		View view = View.inflate(context, R.layout.dialog_service_time, null);
		setView(view);
		setTitle("服务时间");
		timePicker_begin = (NumberPicker) view.findViewById(R.id.timePicker_begin);
		timePicker_end = (NumberPicker) view.findViewById(R.id.timePicker_end);

		timePicker_begin.setMinValue(7);
		timePicker_begin.setMaxValue(20);

		timePicker_begin.setFormatter(formatter);
		timePicker_end.setFormatter(formatter);

		timePicker_end.setMinValue(7);
		timePicker_end.setMaxValue(20);
		timePicker_begin.findViewById(R.id.np__numberpicker_input).setVisibility(View.GONE);
		timePicker_end.findViewById(R.id.np__numberpicker_input).setVisibility(View.GONE);
		timePicker_begin.setOnValueChangedListener(new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				if (newVal > timePicker_end.getValue()) {
//					ToastUtil.show(context, "开始时间与结束时间冲突");
					picker.setValue(timePicker_end.getValue());
				}
			}
		});
		timePicker_end.setOnValueChangedListener(new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				if (newVal < timePicker_begin.getValue()) {
					picker.setValue(timePicker_begin.getValue());
				}
			}
		});

		for (int id : weekDayIds) {
			TextView textView = (TextView) view.findViewById(id);
			weekDays.add(textView);
			textView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					v.setSelected(!v.isSelected());
				}
			});
		}
		for (int i = 0; i < 5; i++) {
			weekDays.get(i).setSelected(true);
		}

		setButton(DialogInterface.BUTTON_POSITIVE, "确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (listener != null) {
					listener.onFinished(createTimeShowText(), createTextFmtText());
				}
			}
		});
		setButton(DialogInterface.BUTTON_NEGATIVE, "取消", (OnClickListener) null);
	}

	@SuppressLint("NewApi")
	String createTextFmtText() {
		List<String> selectedDays = new ArrayList<String>();
		for (int i = 0; i < weekDays.size(); i++) {
			TextView textView = weekDays.get(i);
			if (textView.isSelected()) {
				selectedDays.add(weekDayFmtTexts[i]);
			}
		}
		if (selectedDays.size() == 0) {
			return "";
		}

		String time = StringUtils.join(",", selectedDays) + "|" + formatter.format(timePicker_begin.getValue()) + ","
				+ formatter.format(timePicker_end.getValue());
		return time;
	}

	public static String fmtText2ShowText(String fmtText) {
		if (TextUtils.isEmpty(fmtText)) {
			return "";
		}

		boolean[] allDays = new boolean[weekDayFmtTexts.length];
		try {
			String[] seps = fmtText.split("\\|");
			String[] days = seps[0].split("\\,");
			String[] times = seps[1].split("\\,");
			for (int i = 0; i < allDays.length; i++) {
				allDays[i] = false;
				for (String day : days) {
					if (day.equals(weekDayFmtTexts[i])) {
						allDays[i] = true;
						break;
					}
				}
			}

			///
			int serialBegin = -1, serialEnd = -1, notIndex = -1;
			for (int i = 0; i < allDays.length; i++) {
				boolean isSelected = allDays[i];
				if (isSelected) {
					if (serialBegin == -1) {
						serialBegin = i;
					}
					serialEnd = i;
					if (notIndex > serialBegin && notIndex < serialEnd) {
						serialEnd = -1;
						break;
					}
				} else {
					notIndex = i;
				}
			}
			if (serialBegin == -1) {
				return "无";
			}
			StringBuilder weekText = new StringBuilder();
			if (serialEnd >= 0) {
				if (serialEnd > serialBegin) {
					weekText.append("周");
					weekText.append(weekDayShowTexts[serialBegin]);
					weekText.append("至周");
					weekText.append(weekDayShowTexts[serialEnd]);
				} else {
					weekText.append("周" + allDays[serialBegin]);
				}
			} else {
				weekText.append("周");
				for (int i = 0; i < allDays.length; i++) {
					boolean isSelected = allDays[i];
					if (isSelected) {
						weekText.append(weekDayShowTexts[i]);
						weekText.append(",");
					}
				}
				weekText.deleteCharAt(weekText.length() - 1);
			}

			String time = times[0] + "-" + times[1];
			return weekText.toString() + " " + time;
		}
		catch (Exception e) {
			return fmtText;
		}

	}

	String createTimeShowText() {
		return fmtText2ShowText(createTextFmtText());
	}

	public interface OnFinishedListener {
		public void onFinished(String showText, String fmtText);
	}
}
