package com.e1858.building.splash;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;

import com.common.utils.SharedPreferencesUtils;
import com.e1858.building.Constant;
import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.order.RobListActivity;
import com.viewpagerindicator.CirclePageIndicator;

public class SplashActivity extends BaseActivity {
	private ViewPager			mViewPager;
	private CirclePageIndicator	indicator;
	private Button				next_btn;
	private MyAdapter			myAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar.hide();
		setContentView(R.layout.guide_page);
		initView();
	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mViewPager.setAdapter(myAdapter = new MyAdapter());
		indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(mViewPager);
		next_btn = (Button) findViewById(R.id.startBtn);
		MyOnPageChangeListener changeListener = new MyOnPageChangeListener();
		indicator.setOnPageChangeListener(changeListener);
		mViewPager.setCurrentItem(0);
		changeListener.onPageSelected(0);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	class MyAdapter extends PagerAdapter {
		int[]	images	= { R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3 };

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			ImageView view = (ImageView) object;
			container.removeView(view);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = new ImageView(SplashActivity.this);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			imageView.setImageResource(images[position]);
			container.addView(imageView);
			return imageView;
		}
	}

	class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int position) {
			if (position == myAdapter.getCount() - 1) {
				next_btn.setVisibility(View.VISIBLE);
			} else {
				next_btn.setVisibility(View.GONE);
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}

		@Override
		public void onPageScrollStateChanged(int arg0) {}
	}

	public void startbutton(View v) {
		PackageManager manager = this.getPackageManager();
		PackageInfo info;
		try {
			info = manager.getPackageInfo(this.getPackageName(), 0);
			int vercode = info.versionCode;
			SharedPreferencesUtils.setSpParam(this, Constant.VERSION_CODE, vercode);
		}
		catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		Intent intent = new Intent();
		intent.setClass(SplashActivity.this, RobListActivity.class);
		startActivity(intent);
		this.finish();
	}

}
