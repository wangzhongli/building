package com.e1858.building.order;

import java.sql.SQLException;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.e1858.building.AppManager;
import com.e1858.building.Constant;
import com.e1858.building.R;
import com.e1858.building.base.BaseActivity;
import com.e1858.building.bean.RobOrder;
import com.e1858.building.bean.ServiceTypeBean;
import com.e1858.building.entity.EntityOpenHelper;
import com.e1858.building.entity.RobOrderEntity;
import com.e1858.building.entity.ServiceTypeEntity;
import com.e1858.building.holder.RobOrderHolder;
import com.e1858.building.httppackage.GetRobOrdersRequest;
import com.e1858.building.httppackage.GetRobOrdersResponse;
import com.e1858.building.httppackage.GetServiceTypesRequest;
import com.e1858.building.httppackage.GetServiceTypesResponse;
import com.e1858.building.net.HttpPacketClient;
import com.e1858.building.net.HttpPacketClient.ResponseHandler;
import com.e1858.building.persondata.PersonCenterActivity;
import com.e1858.building.utils.DbOpenUtils;
import com.e1858.building.utils.ResponseUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hg.android.entitycache.EntityCacheHelper;
import com.hg.android.ormlite.extra.OrmLiteIteratorAdapterExt;
import com.hg.android.utils.HGUtils;
import com.hg.android.widget.CityDBManager;
import com.igexin.sdk.PushManager;
import com.j256.ormlite.stmt.QueryBuilder;

/**
 * 抢单王列表
 * 
 * @author momo
 *         2014-12-27下午3:52:47
 */
public class RobListActivity extends BaseActivity {
	private LinearLayout			select_service_time;
	private LinearLayout			select_service_area;
	private LinearLayout			select_service_type;
	private PopupWindow				pop;
	String[]						types				= null;
	String[]						times				= { "全部", "今天", "明天", "后天" };
	private ImageView				img_tab_area;
	private ImageView				img_tab_type;
	private ImageView				img_tab_now;
	private RobAdapter				adapter;
	private PullToRefreshListView	roborder_list;
	public List<RobOrder>			robOrders;
	//1.今天2.明天3.后天
//	public int						time				= 1;
	public int						serviceType			= 0;
	public String					serviceArea			= "";

	public int						gType				= -1;
	public static final int			TYPE_SERVICETYPE	= 1;
	public static final int			TYPE_SERVICEAREA	= 2;
	public static final int			TYPE_TIME			= 3;
	private TextView				service_type_tv;
	private TextView				service_area_tv;
	private TextView				time_tv;
	private ListView				pop_listView;
	private List<ServiceTypeBean>	serviceTypeList;

	CityDBManager					cityDBManager;
	private List<String>			districts;
	ArrayAdapter<String>			pop_adapter			= null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(true);
		//初始化个推
		PushManager.getInstance().initialize(this.getApplicationContext());
		setContentView(R.layout.activity_roblist);
		initView();
		loadRobOrders(0);
		serviceTypeList = DbOpenUtils.readAllServiceTypes(mContext);
		if (!HGUtils.isListEmpty(serviceTypeList)) {
			types = new String[serviceTypeList.size() + 1];
			types[0] = "全部";
			for (int i = 1; i < serviceTypeList.size() + 1; i++) {
				types[i] = serviceTypeList.get(i - 1).getName();
			}
		} else {
			types = new String[1];
			types[0] = "全部";
			loadServiceTypes(false);
		}

		cityDBManager = CityDBManager.sharedInstance(mContext);
		cityDBManager.openDatabase();
//		//定位的城市
//		String city_location = (String) getParamFromSp(Constant.CITY_LOCATION, "郑州市");
//		//用户选择的城市。默认的是定位的城市
//		String city = (String) getParamFromSp(Constant.CITY, city_location);
//		String province_location = (String) getParamFromSp(Constant.PROVINCE_LOCATION, "河南省");
//		String province = (String) getParamFromSp(Constant.PROVINCE, province_location);
//		districts = cityDBManager.districtsForCityName(city, province);
	}

	@Override
	public void onResume() {
		//定位的城市
		String city_location = getParamFromSp(Constant.CITY_LOCATION, "郑州市");
		//用户选择的城市。默认的是定位的城市
		String city = getParamFromSp(Constant.CITY, city_location);
		String province_location = getParamFromSp(Constant.PROVINCE_LOCATION, "河南省");
		String province = getParamFromSp(Constant.PROVINCE, province_location);
		districts = cityDBManager.districtsForCityName(city, province);
		districts.add(0, "全部");
		super.onResume();
	}

	public void loadRobOrders(final int offset) {
		ResponseHandler<GetRobOrdersResponse> responseHandler = new ResponseHandler<GetRobOrdersResponse>() {
			@Override
			public void onStart() {

			}

			@Override
			public void onFinish(GetRobOrdersResponse response, String error) {
				roborder_list.onRefreshComplete();
				closeProgressDialog();
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					EntityCacheHelper.sharedInstance().saveCacheEntities(RobOrderEntity.class, response.getRobOrders(),
							offset, null);
				}
			}
		};
		String province_location = getParamFromSp(Constant.PROVINCE_LOCATION, "河南省");
		String city_location = getParamFromSp(Constant.CITY_LOCATION, "郑州市");
		String province = getParamFromSp(Constant.PROVINCE, province_location);
		String city = getParamFromSp(Constant.CITY, city_location);
		String latitude = getParamFromSp(Constant.LATITUDE, "0");
		String longitude = getParamFromSp(Constant.LONGITUDE, "0");
		String provinceCode = CityDBManager.sharedInstance(mContext).codeForProvince(province);
		String cityCode = CityDBManager.sharedInstance(mContext).codeForCity(province, city);
		String disCode = CityDBManager.sharedInstance(mContext).codeForDistrict(province, city, serviceArea);

		GetRobOrdersRequest request = new GetRobOrdersRequest();
		request.setCount(Constant.FetchCount);
		request.setOffset(offset);
		request.setCity(cityCode);
		request.setProvince(provinceCode);
		request.setLatitude(latitude);
		request.setLongitude(longitude);
		request.setServiceType(serviceType);
		request.setServiceArea(disCode);
//		request.setTime(time);
		HttpPacketClient.postPacketAsynchronous(request, GetRobOrdersResponse.class, responseHandler, true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.to_map) {
			startActivity(MapOrderActivity.class);
			return true;
		}
		if (item.getItemId() == R.id.to_center) {
			if (application.verifyLoggedInAndGoToLogin(getActivity())) {
				startActivity(PersonCenterActivity.class);
			}
			return true;
		}
		if (item.getItemId() == android.R.id.home) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.to_map, menu);
		return true;
	}

	private void initView() {
		select_service_time = (LinearLayout) this.findViewById(R.id.roborder_title_select_service_time);
		select_service_area = (LinearLayout) this.findViewById(R.id.roborder_title_select_service_area);
		select_service_type = (LinearLayout) this.findViewById(R.id.roborder_title_select_service_type);
		service_type_tv = (TextView) this.findViewById(R.id.roborder_title_select_service_type_tv);
		service_area_tv = (TextView) this.findViewById(R.id.roborder_title_select_service_area_tv);
		time_tv = (TextView) this.findViewById(R.id.roborder_title_select_time_tv);

		select_service_time.setOnClickListener(this);
		select_service_area.setOnClickListener(this);
		select_service_type.setOnClickListener(this);
		img_tab_now = (ImageView) this.findViewById(R.id.img_tab_now);
		img_tab_area = (ImageView) this.findViewById(R.id.img_tab_area);
		img_tab_type = (ImageView) this.findViewById(R.id.img_tab_type);

		roborder_list = (PullToRefreshListView) this.findViewById(R.id.pf_listView);
		roborder_list.setMode(Mode.BOTH);
		roborder_list.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadRobOrders(0);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadRobOrders(adapter == null ? 0 : adapter.getCount());
			}
		});
		roborder_list.getRefreshableView().setCacheColorHint(Color.TRANSPARENT);
		roborder_list.setAdapter(adapter = new RobAdapter(mContext));

		View empty = View.inflate(mContext, R.layout.empty_text, null);
		TextView tv = (TextView) empty.findViewById(R.id.empty_tv);
		tv.setText("没有抢单数据");
		roborder_list.setEmptyView(empty);
		adapter.load(getSupportLoaderManager(), EntityOpenHelper.class, RobOrderEntity.class);
	}

	@Override
	protected void onDestroy() {
		if (adapter != null) {
			adapter.unload();
		}
		super.onDestroy();
	}

	@SuppressLint("NewApi")
	private void showPopupWindow(View view) {
		View contentView = View.inflate(mContext, R.layout.popup_layout, null);
		pop = new PopupWindow(contentView);
		pop.setOutsideTouchable(false);
		pop.setWidth(diaplayWidth);
		pop.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
		pop.setHeight(diaplayHeight / 3);
		pop_listView = (ListView) contentView.findViewById(R.id.pop_list);
		pop_listView.setSelected(true);
		pop_listView.setSelector(R.drawable.list_item_bg);
		pop.setFocusable(true);
		pop_listView.setDividerHeight(1);
		if (pop.isShowing()) {
			pop.dismiss();
		} else {
			pop.showAsDropDown(view);
		}
		switch (gType) {
		case TYPE_SERVICETYPE:
			pop_adapter = new ArrayAdapter<String>(this, R.layout.item_select_pop, types);
			break;
		case TYPE_SERVICEAREA:
			pop_adapter = new ArrayAdapter<String>(this, R.layout.item_select_pop, districts);
			break;
		case TYPE_TIME:
			pop_adapter = new ArrayAdapter<String>(this, R.layout.item_select_pop, times);
			break;

		default:
			break;
		}
		pop_listView.setAdapter(pop_adapter);
		pop_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (gType) {
				case TYPE_SERVICETYPE:
					if (!HGUtils.isListEmpty(serviceTypeList)) {
						if (position == 0) {
							serviceType = 0;
						} else {
							serviceType = serviceTypeList.get(position - 1).getID();
						}
					} else {
						serviceType = 0;
					}
					if (types != null) {
						service_type_tv.setText(types[position]);
						if (position == 0) {
							service_type_tv.setText("服务分类");
						}
					}
					if (pop != null && pop.isShowing()) {
						pop.dismiss();
					}
					loadRobOrders(0);
					openProgressDialog("请稍等...");
					break;
				case TYPE_SERVICEAREA:
					if (position == 0) {
						serviceArea = "";
					} else {
						serviceArea = districts.get(position);
					}
					service_area_tv.setText(districts.get(position));
					if (position == 0) {
						service_area_tv.setText("服务区域");
					}
					if (pop != null && pop.isShowing()) {
						pop.dismiss();
					}
					loadRobOrders(0);
					openProgressDialog("请稍等...");
					break;
				case TYPE_TIME:
//					time = position;
					time_tv.setText(times[position]);
					if (position == 0) {
						time_tv.setText("服务日期");
					}
					if (pop != null && pop.isShowing()) {
						pop.dismiss();
					}
					loadRobOrders(0);
					openProgressDialog("请稍等...");
					break;

				default:
					break;
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.roborder_title_select_service_type://服务类型分类
			gType = TYPE_SERVICETYPE;
			img_tab_now.setVisibility(View.INVISIBLE);
			img_tab_area.setVisibility(View.INVISIBLE);
			img_tab_type.setVisibility(View.VISIBLE);
			showPopupWindow(select_service_type);
			break;
		case R.id.roborder_title_select_service_area://服务区域分类
			gType = TYPE_SERVICEAREA;
			img_tab_now.setVisibility(View.INVISIBLE);
			img_tab_area.setVisibility(View.VISIBLE);
			img_tab_type.setVisibility(View.INVISIBLE);
			showPopupWindow(select_service_area);
			break;
//		case R.id.roborder_title_select_service_time://时间分类
//			gType = TYPE_TIME;
//			img_tab_now.setVisibility(View.VISIBLE);
//			img_tab_area.setVisibility(View.INVISIBLE);
//			img_tab_type.setVisibility(View.INVISIBLE);
//			showPopupWindow(select_service_time);
//			break;

		default:
			break;
		}
	}

	private class RobAdapter extends OrmLiteIteratorAdapterExt<RobOrderEntity> {

		public RobAdapter(Context context) {
			super(context);
		}

		@Override
		public void buildQueryBuilder(QueryBuilder<RobOrderEntity, ?> queryBuilder) throws SQLException {}

		@Override
		public View newView(Context context, final RobOrderEntity entity, ViewGroup parent) {
			View convertView = View.inflate(mContext, R.layout.item_roborder_list, null);
			RobOrderHolder holder = new RobOrderHolder(mContext, convertView);
			convertView.setTag(holder);
			return convertView;
		}

		@Override
		public void bindView(View view, Context context, RobOrderEntity entity, int position) {
			final RobOrder robOrder = entity.getBean();
			RobOrderHolder holder = (RobOrderHolder) view.getTag();
			holder.bindDate(robOrder, RobListActivity.this);
		}

	}

	protected void loadServiceTypes(final boolean showProgress) {

		ResponseHandler<GetServiceTypesResponse> responseHandler = new ResponseHandler<GetServiceTypesResponse>() {
			@Override
			public void onStart() {
				if (showProgress) {
					beginEmptyProgress(null);
				}
			}

			@Override
			public void onFinish(GetServiceTypesResponse response, String error) {
				stopEmptyProgress(null);
				if (ResponseUtils.checkResponseAndToastError(response, error)) {
					EntityCacheHelper.sharedInstance().saveCacheEntities(ServiceTypeEntity.class,
							response.getServiceTypes(), 0);
					List<ServiceTypeBean> serviceTypes = response.getServiceTypes();

					if (!HGUtils.isListEmpty(serviceTypes)) {
						types = new String[serviceTypes.size() + 1];
						types[0] = "全部";
						for (int i = 1; i < serviceTypes.size() + 1; i++) {
							types[i] = serviceTypes.get(i - 1).getName();
						}
					} else {
						types = new String[1];
						types[0] = "全部";
					}
					if (pop_adapter != null) {
						pop_adapter.notifyDataSetChanged();
					}
				}
			}
		};

		GetServiceTypesRequest request = new GetServiceTypesRequest();
		HttpPacketClient.postPacketAsynchronous(request, GetServiceTypesResponse.class, responseHandler, true);
	}

	private long	exitTime	= 0l;

	@Override
	public void onBackPressed() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			showToast("再按一次返回键回到桌面");
			exitTime = System.currentTimeMillis();
		} else {
			AppManager.getAppManager().finishAllActivity();
		}
	}
}
