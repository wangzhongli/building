package com.e1858.building.upload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.upload.ImageGridAdapter.TextCallback;

public class ImageGridActivity extends BaseActivity {
	public static final String	EXTRA_IMAGE_LIST	= "imagelist";

	List<ImageItem>				dataList;
	GridView					gridView;
	ImageGridAdapter			adapter;
	AlbumHelper					helper;
	Button						bt;
	public static final int		WHAT				= 0x111;
	Handler						mHandler			= new Handler() {
														@Override
														public void handleMessage(Message msg) {
															switch (msg.what) {
															case WHAT:
																showToast("最多选择9张图片");
																break;

															default:
																break;
															}
														}
													};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.personinfo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_accept) {
			selectPic();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_grid);
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		dataList = (List<ImageItem>) getIntent().getSerializableExtra(EXTRA_IMAGE_LIST);
		initView();
	}

	public void selectPic() {
		ArrayList<String> list = new ArrayList<String>();
		Collection<String> c = adapter.map.values();
		Iterator<String> it = c.iterator();
		for (; it.hasNext();) {
			list.add(it.next());
		}

		switch (BitmapUtil.img_type) {
		case UploadPicActivity.IMGS_GOODS:
			for (int i = 0; i < list.size(); i++) {
				if (BitmapUtil.imgs_goods.size() > 9) {
					showToast("最多选择9张图片");
				} else {
					BitmapUtil.imgs_goods.add(list.get(i));
				}
			}
			break;
		case UploadPicActivity.IMGS_ORDER:
			for (int i = 0; i < list.size(); i++) {
				if (BitmapUtil.imgs_order.size() > 9) {
					showToast("最多选择9张图片");
				} else {
					BitmapUtil.imgs_order.add(list.get(i));
				}
			}
			break;
		case UploadPicActivity.IMGS_WULIU:
			for (int i = 0; i < list.size(); i++) {
				if (BitmapUtil.imgs_wuliu.size() > 9) {
					showToast("最多选择9张图片");
				} else {
					BitmapUtil.imgs_wuliu.add(list.get(i));
				}
			}
			break;

		default:
			break;
		}
		if (list.size() > 9) {
			showToast("最多选择9张图片");
		} else {
//			startActivity(UploadPicActivity.class);
			finish();
		}
	}

	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new ImageGridAdapter(ImageGridActivity.this, dataList, mHandler);
		gridView.setAdapter(adapter);
		adapter.setTextCallback(new TextCallback() {
			public void onListen(int count) {
//				showToast("您已经选择" + count + "张图片");
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				adapter.notifyDataSetChanged();
			}
		});

	}
}
