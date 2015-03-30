package com.e1858.building.order;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.cloud.model.CloudItem;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.e1858.building.R;

public class PoiOverlay {
	private List<CloudItem>		mPois;
	private AMap				mAMap;
	private ArrayList<Marker>	mPoiMarks	= new ArrayList<Marker>();

	public PoiOverlay(AMap amap, List<CloudItem> pois) {
		mAMap = amap;
		mPois = pois;
	}

	public void addToMap() {
		for (int i = 0; i < mPois.size(); i++) {
			Marker marker = mAMap.addMarker(getMarkerOptions(i));
			marker.setObject(mPois.get(i));
			mPoiMarks.add(marker);
		}
	}

	public void removeFromMap() {
		for (Marker mark : mPoiMarks) {
			mark.remove();
		}
	}

	public void zoomToSpan() {
		if (mPois != null && mPois.size() > 0) {
			if (mAMap == null)
				return;
			LatLngBounds bounds = getLatLngBounds();
			mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 15));
		}
	}

	private LatLngBounds getLatLngBounds() {
		LatLngBounds.Builder b = LatLngBounds.builder();
		for (int i = 0; i < mPois.size(); i++) {
			b.include(new LatLng(mPois.get(i).getLatLonPoint().getLatitude(), mPois.get(i).getLatLonPoint()
					.getLongitude()));
		}
		return b.build();
	}

	private MarkerOptions getMarkerOptions(int index) {
		return new MarkerOptions()
				.position(
						new LatLng(mPois.get(index).getLatLonPoint().getLatitude(), mPois.get(index).getLatLonPoint()
								.getLongitude())).title(getTitle(index)).snippet(getSnippet(index)).perspective(true)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.location));
	}

	protected BitmapDescriptor getBitmapDescriptor(int index) {
		return null;
	}

	protected String getTitle(int index) {
		return mPois.get(index).getTitle();
	}

	protected String getSnippet(int index) {
		return mPois.get(index).getSnippet();
	}

	public int getPoiIndex(Marker marker) {
		for (int i = 0; i < mPoiMarks.size(); i++) {
			if (mPoiMarks.get(i).equals(marker)) {
				return i;
			}
		}
		return -1;
	}

	public CloudItem getPoiItem(int index) {
		if (index < 0 || index >= mPois.size()) {
			return null;
		}
		return mPois.get(index);
	}
}
