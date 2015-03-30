package com.e1858.building.upload;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.utils.MLog;
import com.common.utils.StringUtils;
import com.common.utils.ToastUtil;
import com.e1858.building.Constant;
import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.bean.PhotoType;
import com.e1858.building.httppackage.ServiceDoneUpPicRequest;
import com.e1858.building.httppackage.ServiceDoneUpPicResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.net.UploadJson;
import com.e1858.building.utils.ResponseUtils;
import com.hg.android.app.LocalPictureBrowser;
import com.hg.android.utils.HGUtils;
import com.hg.android.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UploadPicActivity extends BaseActivity {

	private GridView				goods_gridView;
	private GridAdapter				goods_adapter;
	private GridAdapter				order_adapter;
	private GridAdapter				wuliu_adapter;
	protected DisplayImageOptions	options;
	private GridView				order_gridView;
	private GridView				wuliu_gridView;
	public static final int			REQUESTCODE_PICKPHOTO	= 110;
	public static final int			REQUESTCODE_TAKEPHOTO	= 111;

	private String					path					= "";

	public static final int			IMGS_GOODS				= 0x111;
	public static final int			IMGS_ORDER				= 0x112;
	public static final int			IMGS_WULIU				= 0x113;
	public static final int			WHAT_UPLOAD				= 0x1;
	public static final String		ACTIVITY_FROM			= "activity_from";
	public static final String		ORDER_ID				= "orderID";
	public static final String		TYPES					= "types";
	public static final String		FLAG					= "flag";
	int[]							types					= null;
	List<String>					urls					= null;
	private String					from;
	private String					orderId;
	private int						flag					= -1;
	private BroadcastReceiver		receiver;

	private int						pic_type				= 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_pic);
		initView();
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.hg_add_picture)
				.showImageForEmptyUri(R.drawable.hg_add_picture).showImageOnFail(R.drawable.hg_add_picture)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		if (getIntent() != null) {
			from = getIntent().getStringExtra(ACTIVITY_FROM);
			orderId = getIntent().getStringExtra(ORDER_ID);
			types = (int[]) getIntent().getSerializableExtra(TYPES);
			flag = (Integer) getIntent().getSerializableExtra(FLAG);
		}
		if ("OrderCompleteActivity".equals(from)) {
			TextView tv_first = (TextView) this.findViewById(R.id.tv_first);
			TextView tv_second = (TextView) this.findViewById(R.id.tv_second);
			this.findViewById(R.id.ll_three_layout).setVisibility(View.GONE);
			if (flag == Constant.UPDATEPICFLAG.FLAG_COMPLETE) {
				this.findViewById(R.id.ll_two_layout).setVisibility(View.GONE);
				tv_first.setText("服务完成拍照");
			} else {

				tv_first.setText("买家签单拍照");
				tv_second.setText("商品拍照");
			}
		}
		if (receiver == null) {
			receiver = new BroadcastReceiver() {

				@Override
				public void onReceive(Context arg0, Intent intent) {
					if (intent.getAction().equals(LocalPictureBrowser.BroadcastAction_Delete)) {
						int index = intent.getIntExtra(LocalPictureBrowser.ImtentKey_Index, -1);
						removeImageAtIndex(pic_type, index);
					}
				}
			};
			IntentFilter filter = new IntentFilter(LocalPictureBrowser.BroadcastAction_Delete);
			mContext.registerReceiver(receiver, filter);
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		goods_adapter.notifyDataSetChanged();
		order_adapter.notifyDataSetChanged();
		wuliu_adapter.notifyDataSetChanged();
		MLog.e(TAG, "onResume");
	}

	private void removeImageAtIndex(int pic_type, int index) {
		switch (pic_type) {
		case IMGS_GOODS:
			BitmapUtil.imgs_goods.remove(index);
			goods_adapter.notifyDataSetChanged();
			break;
		case IMGS_ORDER:
			BitmapUtil.imgs_order.remove(index);
			order_adapter.notifyDataSetChanged();
			break;
		case IMGS_WULIU:
			BitmapUtil.imgs_wuliu.remove(index);
			wuliu_adapter.notifyDataSetChanged();
			break;

		default:
			break;
		}

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		MLog.e(TAG, "onRestart");

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		BitmapUtil.imgs_goods.clear();
		BitmapUtil.imgs_order.clear();
		BitmapUtil.imgs_wuliu.clear();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.personinfo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_accept) {
			uploadPicture();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 上传图片
	 * void
	 */
	private void uploadPicture() {
		if (HGUtils.isListEmpty(BitmapUtil.imgs_goods) && HGUtils.isListEmpty(BitmapUtil.imgs_order)
				&& HGUtils.isListEmpty(BitmapUtil.imgs_wuliu)) {
			ToastUtil.show(getActivity(), "没有选择照片");
			return;
		}
		UploadImagesTask task = new UploadImagesTask();
		task.execute();
	}

	//////////////////刘浩歌 2015.01.15 7:04//////////////////////////////////////

	class UploadImagesTask extends AsyncTask<Void, Void, Boolean> {

		List<PhotoType>	photoTypes;
		ProgressDialog	dialog	= new ProgressDialog(getActivity());

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("正在上传图片");
			dialog.show();
			dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

				@Override
				public void onCancel(DialogInterface arg0) {
					UploadImagesTask.this.cancel(true);
					ToastUtil.show(getActivity(), "图片上传已取消");
				}
			});
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			List<String> imageUrls_goods = uploadImages(BitmapUtil.imgs_goods);
			if (isCancelled() || imageUrls_goods.size() < BitmapUtil.imgs_goods.size()) {
				MLog.i(TAG, "imgs_goods" + imageUrls_goods.size() + "     " + BitmapUtil.imgs_goods.size());
				return false;
			}
			List<String> imageUrls_order = uploadImages(BitmapUtil.imgs_order);
			if (isCancelled() || imageUrls_order.size() < BitmapUtil.imgs_order.size()) {
				MLog.i(TAG, "imgs_goods");
				return false;
			}
			List<String> imageUrls_wuliu = uploadImages(BitmapUtil.imgs_wuliu);
			if (isCancelled() || imageUrls_wuliu.size() < BitmapUtil.imgs_wuliu.size()) {
				MLog.i(TAG, "imgs_goods");
				return false;
			}
			photoTypes = new ArrayList<PhotoType>();
			int index = 0;
			if (types.length > index) {
				PhotoType type = new PhotoType();
				type.setType(types[index++]);
				type.setUrls(imageUrls_goods);
				photoTypes.add(type);
			}
			if (types.length > index) {
				PhotoType type = new PhotoType();
				type.setType(types[index++]);
				type.setUrls(imageUrls_order);
				photoTypes.add(type);
			}
			if (types.length > index) {
				PhotoType type = new PhotoType();
				type.setType(types[index++]);
				type.setUrls(imageUrls_wuliu);
				photoTypes.add(type);
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			dialog.dismiss();
			MLog.i(TAG, "result_" + result);
			if (result) {
				submitTypes(photoTypes);
			}
		}

		String uploadImage(String file) {
			if (file.startsWith("file://")) {
				file = file.substring("file://".length());
			}
			String destJPG = new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg").getAbsolutePath();
			MLog.i(TAG, "destJPG  " + destJPG);
			if (!ImageUtils.scaleImage(file, destJPG, Constant.PICTURE_MIN_SIDE_LENGTH, Constant.PICTURE_MAX_SIZE)) {
				MLog.i(TAG, "destJPG  " + destJPG);
				return null;
			}
			if (isCancelled()) {
				MLog.i(TAG, "uploadImage  +  isCancelled");
				return null;
			}
			UploadJson uploadJson = HttpPacketClient.syncUpdaloadFile(destJPG);
			MLog.i(TAG, "uploadImage  +  uploadJson" + uploadJson.getUrl());
			return uploadJson.getUrl();
		}

		List<String> uploadImages(List<String> files) {
			List<String> imageUrls = new ArrayList<String>();
			for (String file : files) {
				if (isCancelled()) {
					return imageUrls;
				}
				String url = uploadImage(file);
				MLog.i(TAG, "url  " + url);
				if (!TextUtils.isEmpty(url)) {
					imageUrls.add(url);
				}
			}
			return imageUrls;
		}
	}

	void submitTypes(List<PhotoType> photoTypes) {
		MLog.i(TAG, "" + photoTypes.get(0).getUrls().size());
		ResponseHandler<ServiceDoneUpPicResponse> responseHandler = new ResponseHandler<ServiceDoneUpPicResponse>() {
			@Override
			public void onStart() {
				showProgressDialog(getActivity(), "正在提交");
			}

			@Override
			public void onFinish(ServiceDoneUpPicResponse response, String error) {
				hideProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					ToastUtil.show(mContext, "提交服务器成功！");
					finish();
				}
			}
		};
		ServiceDoneUpPicRequest request = new ServiceDoneUpPicRequest();
		request.setOrderID(orderId);
		request.setFlag(flag);
		request.setPhotoTypes(photoTypes);
		HttpPacketClient.postPacketAsynchronous(request, ServiceDoneUpPicResponse.class, responseHandler, true);
	}

	////////////////////////////////////////////////////////

	public void initView() {

		goods_gridView = (GridView) findViewById(R.id.goods_gridView);
		goods_gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		goods_adapter = new GridAdapter(this, BitmapUtil.imgs_goods);
		goods_gridView.setAdapter(goods_adapter);
		goods_gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if (position == BitmapUtil.imgs_goods.size()) {
					clickOnPhoto();
					BitmapUtil.selectedSize = BitmapUtil.imgs_goods.size();
					BitmapUtil.img_type = IMGS_GOODS;
				} else {//大图显示
					Bundle bundle = new Bundle();
					String[] urls = StringUtils.listToStringArray(BitmapUtil.imgs_goods);
					bundle.putSerializable(LocalPictureBrowser.ImtentKey_Urls, (Serializable) urls);
					bundle.putSerializable(LocalPictureBrowser.ImtentKey_Index, position);
					pic_type = IMGS_GOODS;
					startActivity(LocalPictureBrowser.class, bundle);
				}
			}
		});
		order_gridView = (GridView) findViewById(R.id.order_gridView);
		order_gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		order_adapter = new GridAdapter(this, BitmapUtil.imgs_order);
		order_gridView.setAdapter(order_adapter);
		order_gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if (position == BitmapUtil.imgs_order.size()) {
					clickOnPhoto();
					BitmapUtil.img_type = IMGS_ORDER;
					BitmapUtil.selectedSize = BitmapUtil.imgs_order.size();
				} else {
					Bundle bundle = new Bundle();
					String[] urls = StringUtils.listToStringArray(BitmapUtil.imgs_order);
					bundle.putSerializable(LocalPictureBrowser.ImtentKey_Urls, (Serializable) urls);
					bundle.putSerializable(LocalPictureBrowser.ImtentKey_Index, position);
					pic_type = IMGS_ORDER;
					startActivity(LocalPictureBrowser.class, bundle);
				}
			}
		});
		wuliu_gridView = (GridView) findViewById(R.id.wuliu_gridView);
		wuliu_gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		wuliu_adapter = new GridAdapter(this, BitmapUtil.imgs_wuliu);
		wuliu_gridView.setAdapter(wuliu_adapter);
		wuliu_gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if (position == BitmapUtil.imgs_wuliu.size()) {
					clickOnPhoto();
					BitmapUtil.img_type = IMGS_WULIU;
					BitmapUtil.selectedSize = BitmapUtil.imgs_wuliu.size();
				} else {
					Bundle bundle = new Bundle();
					String[] urls = StringUtils.listToStringArray(BitmapUtil.imgs_wuliu);
					bundle.putSerializable(LocalPictureBrowser.ImtentKey_Urls, (Serializable) urls);
					bundle.putSerializable(LocalPictureBrowser.ImtentKey_Index, position);
					pic_type = IMGS_WULIU;
					startActivity(LocalPictureBrowser.class, bundle);
				}
			}
		});
	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater	inflater;	// 视图容器
		private List<String>	imgs;

		public GridAdapter(Context context, List<String> imgs) {
			inflater = LayoutInflater.from(context);
			this.imgs = imgs;
		}

		@Override
		public int getCount() {
			return (imgs.size() + 1) > 9 ? 9 : (imgs.size() + 1);
		}

		@Override
		public Object getItem(int arg0) {

			return null;
		}

		@Override
		public long getItemId(int arg0) {

			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.gridview_item, parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.item_gridview_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == imgs.size()) {
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				} else {
					ImageLoader.getInstance().displayImage("", holder.image, options);
				}
			} else {
				ImageLoader.getInstance().displayImage(imgs.get(position), holder.image, options);
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView	image;
		}

	}

	private void clickOnPhoto() {
		new AlertDialog.Builder(getActivity()).setTitle("上传照片")
				.setItems(R.array.pick_photo_items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if (arg1 == 0) {
							openCamera();
						} else if (arg1 == 1) {
							openAblum();
						}
					}
				}).setCancelable(true).show();
	}

	private void openCamera() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
		path = file.getPath();
		Uri imageUri = Uri.fromFile(file);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(openCameraIntent, REQUESTCODE_TAKEPHOTO);
	}

	private void openAblum() {
		Intent intent = new Intent(UploadPicActivity.this, SelectPicActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUESTCODE_PICKPHOTO:

			break;
		case REQUESTCODE_TAKEPHOTO://打开相机
			if (resultCode == RESULT_OK) {
				if (!path.startsWith("file:")) {
					path = "file://" + path;
				}
				switch (BitmapUtil.img_type) {
				case UploadPicActivity.IMGS_GOODS:
					if (BitmapUtil.imgs_goods.size() < 9) {
						BitmapUtil.imgs_goods.add(path);
					} else {
						showToast("最多选择9张图片");
					}
					break;
				case UploadPicActivity.IMGS_ORDER:
					if (BitmapUtil.imgs_order.size() < 9) {
						BitmapUtil.imgs_order.add(path);
					} else {
						showToast("最多选择9张图片");
					}
					break;
				case UploadPicActivity.IMGS_WULIU:
					if (BitmapUtil.imgs_wuliu.size() < 9) {
						BitmapUtil.imgs_wuliu.add(path);
					} else {
						showToast("最多选择9张图片");
					}
					break;

				default:
					break;
				}
			} else {
				showToast("添加图片失败");
			}
		}
	}

}
