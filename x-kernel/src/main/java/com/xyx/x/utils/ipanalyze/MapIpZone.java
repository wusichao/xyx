package com.xyx.x.utils.ipanalyze;

import java.util.List;

public class MapIpZone {

	private static final int SIZE_BARREL = 1 << 16;
	
	private IpSegLowBit[][] barrels = new IpSegLowBit[SIZE_BARREL][];

	private static final int MASK_CNET = 0xFF00;

	private static final int MASK_PART = 0xFF;
	
	public void initMap(List<IpSegLowBit> list) {
		for (int i=0; i<SIZE_BARREL; i++) {
			barrels[i] = null;
		}
		for (IpSegLowBit record: list) {
			IpSegLowBit[] tmp = barrels[record.getBnet()];
			IpSegLowBit[] result = null;
			if (tmp == null) {
				result = new IpSegLowBit[1];
				result[0] = record;
			} else {
				result = new IpSegLowBit[tmp.length + 1];
				
				for (int i=0; i<tmp.length; i++) {
					result[i] = tmp[i];
				}
				
				result[result.length-1] = record; 
			}
			
			barrels[record.getBnet()] = result;
			
		}
		for (int i=0; i<SIZE_BARREL; i++) {
			if (barrels[i] == null) {
				continue;
			} else {
				insertSortIpsegments(barrels[i]);
			}
		}
	}
	
	public int findCityIdWithJudge(int bitHigh, int bitLow) {
		int cityId = -1;
		if (!verifyBitHigh(bitHigh)) {
			return cityId;
		}
		if ((bitLow&MASK_PART) == 0 ) {
			cityId = findCityIdByCNet(bitHigh, bitLow);
		} else {
			cityId = findCityIdByIp(bitHigh, bitLow);
		}
		return cityId;
	}
	
	private int findCityIdByCNet(int bitHigh, int bitLow) {
		int ip = findCityIdByIp(bitHigh, bitLow);
		if (ip == -1) {
			int low = 0;
			int high = barrels[bitHigh].length - 1;
			int mid;
			while (low <= high) {
				mid = (low + high) >> 1;
				if (bitLow > barrels[bitHigh][mid].getSegEnd()) {
					low = mid + 1;
				} else if (bitLow < (barrels[bitHigh][mid].getSegStart() & MASK_CNET)) {
					high = mid - 1;
				} else {
					return barrels[bitHigh][mid].getIdCity();
				}
			}
		}
		return ip;
	}
	
	private int findCityIdByIp(int bitHigh, int bitLow) {
		int low = 0;
		int high = barrels[bitHigh].length - 1;
		int mid;
		while (low <= high) {
			mid = (low + high) >> 1;
			if (bitLow > barrels[bitHigh][mid].getSegEnd()) {
				low = mid + 1;
			} else if (bitLow < barrels[bitHigh][mid].getSegStart()) {
				high = mid - 1;
			} else {
				return barrels[bitHigh][mid].getIdCity();
			}
		}
		return -1;
	}
	
	public IpZone citySeekWithJudge(int bitHigh, int bitLow) {
		IpZone region = new IpZone();
		region.setCityRecognized(false);
		if (!verifyBitHigh(bitHigh)) {
			return region;
		}
		if ((bitLow&MASK_PART) == 0 ) {
			region = citySeekByCNet(bitHigh, bitLow);
		} else {
			region = citySeek(bitHigh, bitLow);
		}
		return region;
	}
	
	private IpZone citySeek(int bitHigh, int bitLow) {
		IpZone region = new IpZone();
		region.setCityRecognized(false);
		int low = 0;
		int high = barrels[bitHigh].length - 1;
		int mid;
		while (low <= high) {
			mid = (low + high) >> 1;
			if (bitLow > barrels[bitHigh][mid].getSegEnd()) {
				low = mid + 1;
			} else if (bitLow < barrels[bitHigh][mid].getSegStart()) {
				high = mid - 1;
			} else {
				region.setCityRecognized(true);
				region.setCityId(barrels[bitHigh][mid].getIdCity());
				return region;
			}
		}
		return region;
	}
	
	private IpZone citySeekByCNet(int bitHigh, int bitLow) {
		IpZone region = citySeek(bitHigh, bitLow);
		if (!region.isCityRecognized()) {
			int low = 0;
			int high = barrels[bitHigh].length - 1;
			int mid;
			while (low <= high) {
				mid = (low + high) >> 1;
				if (bitLow > barrels[bitHigh][mid].getSegEnd()) {
					low = mid + 1;
				} else if (bitLow < (barrels[bitHigh][mid].getSegStart() & MASK_CNET)) {
					high = mid - 1;
				} else {
					region.setCityRecognized(true);
					region.setCityId(barrels[bitHigh][mid].getIdCity());
					return region;
				}
			}
		}
		return region;
	}
	
	private boolean verifyBitHigh(int bitHigh) {
		if (bitHigh >= SIZE_BARREL || barrels[bitHigh] == null) {
			return false;
		} else {
			return true;
		}
	}
	
	private void insertSortIpsegments(IpSegLowBit[] array) {
		for (int i=1; i<array.length; i++) {
			int j=i;
			IpSegLowBit tmp = array[j];
			for (; j>0; j--) {
				if (array[j-1].getSegStart() <= tmp.getSegStart()) {
					break;
				} else {
					array[j] = array[j-1];
				}
			}
			array[j] = tmp;
		}
	}
}
