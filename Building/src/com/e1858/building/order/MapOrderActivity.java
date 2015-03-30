package com.e1858.building.order;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.amap.api.cloud.model.AMapCloudException;
import com.amap.api.cloud.model.CloudItem;
import com.amap.api.cloud.model.CloudItemDetail;
import com.amap.api.cloud.model.LatLonPoint;
import com.amap.api.cloud.search.CloudResult;
import com.amap.api.cloud.search.CloudSearch;
import com.amap.api.cloud.search.CloudSearch.OnCloudSearchListener;
import com.amap.api.cloud.search.CloudSearch.SearchBound;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.common.utils.MLog;
import com.common.utils.StringUtils;
import com.common.utils.ToastUtil;
import com.e1858.building.Constant;
import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.httppackage.PostOrderInfoRequest;
import com.e1858.building.httppackage.PostOrderInfoResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.persondata.NotReserveActivity;
import com.e1858.building.persondata.PersonCenterActivity;

public class MapOrderActivity extends BaseActivity implements LocationSource, AMapLocationListener,
		OnInfoWindowClickListener, InfoWindowAdapter, OnCloudSearchListener, OnMarkerClickListener, OnMapClickListener {
	private MapView						mapView;
	private AMap						aMap;
	private OnLocationChangedListener	mListener;
	private LocationManagerProxy		mAMapLocationManager;
	private LatLng						latlng;

	public static final LatLng			ZHENGZHOU	= new LatLng(34.7466, 113.625367);	// 郑州市经纬度
	private String						city;
	private String						latitude;
	private String						longitude;
	private CloudSearch					mCloudSearch;
	private String						mTableID	= "54bf1073e4b0c9ec6f36ad94";		// 用户tableid，从官网下载测试数据后在云图中新建地图并导入，获取相应的tableid
	private String						mKeyWord	= "";								// 搜索关键字
	private CloudSearch.Query			mQuery;
	private PoiOverlay					mPoiCloudOverlay;
	private List<CloudItem>				mCloudItems;
	private ArrayList<CloudItem>		items		= new ArrayList<CloudItem>();
	private LatLonPoint					mCenterPoint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写

		city = (String) getParamFromSp(Constant.CITY, "郑州市");
		latitude = (String) getParamFromSp(Constant.LATITUDE, "0");
		longitude = (String) getParamFromSp(Constant.LONGITUDE, "0");
		latlng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

		init();
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
			aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
			aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
			aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
			mCloudSearch = new CloudSearch(this);
			mCloudSearch.setOnCloudSearchListener(this);
			aMap.setOnMapClickListener(this);
		}
	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		//设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种 
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
//		aMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
		aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
	}

	/**
	 * 对marker标注点点击响应事件
	 */
	@Override
	public boolean onMarkerClick(final Marker marker) {
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.to_center) {
			if (application.verifyLoggedInAndGoToLogin(getActivity())) {
				startActivity(PersonCenterActivity.class);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.to_center, menu);
		return true;
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	private Marker	marker; // 定位雷达小图标

	public void addLocationMarker() {
		marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).period(50));
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.BLUE);// 设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
		myLocationStyle.strokeWidth(0.1f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation.getAMapException().getErrorCode() == 0) {
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
				closeProgressDialog();
				double latitude = amapLocation.getLatitude();
				double longitude = amapLocation.getLongitude();

				mCenterPoint = new LatLonPoint(latitude, longitude);
				if (null != marker) {
					marker.setPosition(new LatLng(latitude, longitude));// 定位雷达小图标
					float bearing = aMap.getCameraPosition().bearing;
					aMap.setMyLocationRotateAngle(bearing);// 设置小蓝点旋转角度
				}
//				aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
				searchByBound(mCenterPoint);
				deactivate();
			}
		}

	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
			mAMapLocationManager.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 3 * 1000, 10, this);
			openProgressDialog("定位中,请稍后...");
			if (!application.isNetworkAvailable()) {
				closeProgressDialog();
				showToast(R.string.network_fail);
			}
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		String tile = marker.getTitle();
		CloudItem c = (CloudItem) marker.getObject();
		String order_id = c.getCustomfield().get("OrderID");
		Bundle bundle = new Bundle();
		bundle.putString(OrderInfoActivity.ROBORDERID, order_id);
		startActivity(OrderInfoActivity.class, bundle);
	}

	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
		render(marker, infoWindow);
		return infoWindow;
	}

	/**
	 * 自定义infowinfow窗口
	 */
	public void render(final Marker marker, View view) {
		TextView titleUi = ((TextView) view.findViewById(R.id.map_window_title));
		TextView map_window_snippet = ((TextView) view.findViewById(R.id.map_window_snippet));
		TextView map_window_rob = ((TextView) view.findViewById(R.id.map_window_rob));
		TextView map_window_snippet_dis = ((TextView) view.findViewById(R.id.map_window_snippet_dis));
		CloudItem c = (CloudItem) marker.getObject();
		int distance = c.getDistance();
		MLog.e(TAG, "距离现在地点" + distance + "米");
		final String order_id = c.getCustomfield().get("OrderID");
		map_window_snippet_dis.setText(String.format("距离当前位置%d米", distance));
		titleUi.setText(c.getSnippet());
		map_window_snippet.setText(c.getCustomfield().get("ServiceTypeName") + " "
				+ c.getCustomfield().get("ServiceTime"));
		map_window_rob.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (application.verifyLoggedInAndGoToLogin(getActivity()) && application.isPassAuth(getActivity())) {
					robOrder(order_id, marker);
				}
			}
		});
	}

	@Override
	public void onCloudItemDetailSearched(CloudItemDetail arg0, int arg1) {

	}

	public void searchByBound(LatLonPoint mCenterPoint) {
		openProgressDialog("搜索抢单中，请稍后...");
		items.clear();
		aMap.clear();
		addLocationMarker();
		SearchBound bound = new SearchBound(new LatLonPoint(mCenterPoint.getLatitude(), mCenterPoint.getLongitude()),
				5000);
		try {
//			mQuery = new CloudSearch.Query(mTableID, "Status = 1", bound);
			mQuery = new CloudSearch.Query(mTableID, mKeyWord, bound);
			mQuery.setPageSize(500);
			CloudSearch.Sortingrules sorting = new CloudSearch.Sortingrules("id", false);
			mQuery.setSortingrules(sorting);
			mCloudSearch.searchCloudAsyn(mQuery);// 异步搜索
		}
		catch (AMapCloudException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onCloudSearched(CloudResult result, int rCode) {
		closeProgressDialog();

		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {
				if (result.getQuery().equals(mQuery)) {
					mCloudItems = result.getClouds();
					MLog.e(TAG, "高德地图返回结果——————" + mCloudItems.size());
//					showToast(String.format("您附近有%d个单子", mCloudItems.size()));
					if (mCloudItems != null && mCloudItems.size() > 0) {
						mPoiCloudOverlay = new PoiOverlay(aMap, mCloudItems);
//						mPoiCloudOverlay.removeFromMap();
						mPoiCloudOverlay.addToMap();
						mPoiCloudOverlay.zoomToSpan();
						if (mQuery.getBound().getShape().equals(SearchBound.BOUND_SHAPE)) {// 圆形
							aMap.addCircle(new CircleOptions()
									.center(new LatLng(mCenterPoint.getLatitude(), mCenterPoint.getLongitude()))
									.radius(5000).strokeColor(Color.BLUE).fillColor(Color.argb(50, 1, 1, 1))
									.strokeWidth(2));
							mPoiCloudOverlay.zoomToSpan();
							aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCenterPoint.getLatitude(),
									mCenterPoint.getLongitude()), 13));

						} else if ((mQuery.getBound().getShape().equals(SearchBound.LOCAL_SHAPE))) {
							mPoiCloudOverlay.zoomToSpan();
						}

					} else {
						showToast(R.string.no_result);
					}
				}
			} else {
				showToast(R.string.no_result);
			}
		} else {
			showToast(R.string.no_result);
		}
	}

	public void robOrder(final String orderID, final Marker marker) {
		ResponseHandler<PostOrderInfoResponse> responseHandler = new ResponseHandler<PostOrderInfoResponse>() {
			@Override
			public void onStart() {
				openProgressDialog("请稍后...");
			}

			@Override
			public void onFinish(PostOrderInfoResponse response, String error) {
				closeProgressDialog();
				if (response != null && response.isSuccess() && StringUtils.isEmpty(response.getError())) {
					//把marker从地图上去掉
					marker.remove();
					ToastUtil.showAtCenterShort(mContext, "恭喜您，抢单成功！");
					Intent intent = new Intent(mContext, NotReserveActivity.class);
					mContext.startActivity(intent);
				} else {
					ToastUtil.showAtCenterShort(mContext, response.getError());
				}
			}
		};
		PostOrderInfoRequest request = new PostOrderInfoRequest();
		request.setOrderID(orderID);
		HttpPacketClient.postPacketAsynchronous(request, PostOrderInfoResponse.class, responseHandler, true);
	}

	@Override
	public void onMapClick(LatLng latlng) {

	}
}
