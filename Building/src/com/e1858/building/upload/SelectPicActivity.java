package com.e1858.building.upload;

import java.io.Serializable;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;

public class SelectPicActivity extends BaseActivity {
	List<ImageBucket>			dataList;
	GridView					gridView;
	ImageBucketAdapter			adapter;							// 自定义的适配器
	AlbumHelper					helper;
	public static final String	EXTRA_IMAGE_LIST	= "imagelist";
	public static Bitmap		bimap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_bucket);
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		initData();
		initView();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		dataList = helper.getImagesBucketList(false);
		bimap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused);
	}

	/**
	 * 初始化view视图
	 */
	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		adapter = new ImageBucketAdapter(SelectPicActivity.this, dataList);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(SelectPicActivity.this, ImageGridActivity.class);
				intent.putExtra(SelectPicActivity.EXTRA_IMAGE_LIST, (Serializable) dataList.get(position).imageList);
				startActivity(intent);
				finish();
			}

		});
	}
}
