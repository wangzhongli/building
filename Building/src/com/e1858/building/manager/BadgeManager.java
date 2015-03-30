package com.e1858.building.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.utils.SharedPreferencesUtils;
import com.e1858.building.Constant;
import com.e1858.building.R;

public class BadgeManager {
	OnSharedPreferenceChangeListener	onSharedPreferenceChangeListener;
	TextView							badgeView;
	public Context						mContext;

	public BadgeManager(Context mContext) {
		this.mContext = mContext;
	}

	@SuppressLint("NewApi")
	public void register(final MenuItem menuItem, final Object menuOwner) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			return;
		}
		Activity activity = null;
		if (Activity.class.isInstance(menuOwner)) {
			activity = (Activity) menuOwner;
		} else if (Fragment.class.isInstance(menuOwner)) {
			activity = ((Fragment) menuOwner).getActivity();
		}

		menuItem.setActionView(R.layout.menu_badge);
		menuItem.getActionView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Activity.class.isInstance(menuOwner)) {
					((Activity) menuOwner).onOptionsItemSelected(menuItem);
				} else if (Fragment.class.isInstance(menuOwner)) {
					((Fragment) menuOwner).onOptionsItemSelected(menuItem);
				}
			}
		});
		ImageView imageView = (ImageView) menuItem.getActionView().findViewById(R.id.menu_badge_icon);
		imageView.setImageDrawable(menuItem.getIcon());
		badgeView = (TextView) menuItem.getActionView().findViewById(R.id.menu_badge_text);
		badgeView.setBackgroundResource(R.drawable.notice_icon);
		badgeView.setTextColor(activity.getResources().getColor(R.color.white));
		SharedPreferencesUtils.getSharedPreferences(mContext).registerOnSharedPreferenceChangeListener(
				getSharedPreferenceChangeListener());
		updateBadgeView();
	}

	public void unregister() {
		SharedPreferencesUtils.getSharedPreferences(mContext).unregisterOnSharedPreferenceChangeListener(
				getSharedPreferenceChangeListener());
	}

	OnSharedPreferenceChangeListener getSharedPreferenceChangeListener() {
		if (onSharedPreferenceChangeListener == null) {
			onSharedPreferenceChangeListener = new OnSharedPreferenceChangeListener() {

				@Override
				public void onSharedPreferenceChanged(SharedPreferences pref, String key) {
					if (Constant.NOTICE_NUM.equals(key)) {
						updateBadgeView();
					}
				}
			};
		}
		return onSharedPreferenceChangeListener;
	}

	void updateBadgeView() {
		if (badgeView == null) {
			return;
		}
		int count = SharedPreferencesUtils.getSpParam(mContext, Constant.NOTICE_NUM, 0);

		if (count > 0) {
			badgeView.setText("");
			badgeView.setVisibility(View.VISIBLE);
		} else {
			badgeView.setVisibility(View.GONE);
		}
	}
}
