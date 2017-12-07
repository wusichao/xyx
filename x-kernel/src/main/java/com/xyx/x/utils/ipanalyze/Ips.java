package com.xyx.x.utils.ipanalyze;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.xyx.x.utils.IpUtils;

public class Ips {
	static class ConstBit {
		public static final long MASK_BNET = 0xFFFF0000;
		public static final long ADDRESS_BNET = 0xFFFF;
		public static final int OFFSET_BNET_START = 0;
		public static final int OFFSET_BNET_END = 0xFFFF;
	}

	static class ConstChar {
		public static final char DOT = 46;
		public static final char NUMBER_BASE = 48;
	}

	static class ConstCode {
		public static final byte LINE_FEED = 10;
		public static final byte SPACE = 32;
		public static final byte COMMA = 44;
	}

	private MapIpZone mapOfIPRegion;

	public Ips() {
		super();
		init();
	}

	public int getCityIdOfIPV4(String ip) {
		if (!verifyIpv4(ip)) {
			return -1;
		}
		int highBit = getHighBitFromIpv4String(ip);
		int lowBit = getLowBitFromIpv4String(ip);
		return mapOfIPRegion.findCityIdWithJudge(highBit, lowBit);
	}

	private boolean verifyIpv4(String ip) {
		if (ip == null || ip.length() == 0) {
			return false;
		}
		int mask = 0x3;
		int stat = 0;
		int count = 0;
		int start = 0;
		for (int i = 0; i < 3; i++) {
			while (count < ip.length() && ip.charAt(count++) != ConstChar.DOT)
				;
			if (count == ip.length()) {
				break;
			}
			stat |= count - start - 1 << i * 2;
			start = count;
		}
		if (count == ip.length()) {
			return false;
		}
		for (int i = 0; i < 3; i++) {
			if ((stat & mask << i * 2) == 0) {
				return false;
			}
		}
		return true;
	}

	static class UtilIO {
		public static byte[] readFile(InputStream in) {
			ByteArrayOutputStream bos = null;
			byte[] ret = null;
			try {
				byte[] buf = new byte[10240];
				bos = new ByteArrayOutputStream();
				int len = 0;
				while ((len = in.read(buf)) != -1) {
					bos.write(buf, 0, len);
				}
				ret = bos.toByteArray();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (in != null) {
						in.close();
					}
					if (bos != null) {
						bos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return ret;
		}
	}

	private void init() {
		
		try {
			byte[] dat = UtilIO.readFile(this.getClass().getResourceAsStream("/ip.csv"));
			List<IpSeg> ipsegs = parseIpSegment(dat);
			List<IpSegLowBit> bnetSegs = bmergeIpSegment(ipsegs);
			mapOfIPRegion = new MapIpZone();
			mapOfIPRegion.initMap(bnetSegs);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private List<IpSeg> parseIpSegment(byte[] dat) throws NumberFormatException, UnsupportedEncodingException {
		List<IpSeg> result = new ArrayList<IpSeg>();
		int initIndex = 0;
		int endIndex = 0;
		int currentOffset = 0;
		int[] splits = new int[3];
		while (endIndex < dat.length) {
			while (++endIndex < dat.length && dat[endIndex] != ConstCode.LINE_FEED)
				;

			currentOffset = initIndex;

			for (int i = 0; i < 3; i++) {
				while (++currentOffset < endIndex && dat[currentOffset] != ConstCode.COMMA)
					;
				splits[i] = currentOffset;
			}

			IpSeg record = new IpSeg();
			record.setSegStart(IpUtils.ip2Long(new String(dat, initIndex, splits[0] - initIndex, "UTF-8")));
			record.setSegEnd(IpUtils.ip2Long(new String(dat, splits[0] + 1, splits[1] - splits[0] - 1, "UTF-8")));
//			record.setIdProvince(Integer.valueOf(new String(dat, splits[1] + 1, splits[2] - splits[1] - 1, "UTF-8")));
			record.setIdCity(Integer.valueOf(new String(dat, splits[1] + 1, endIndex - splits[1] - 1, "UTF-8")));

			endIndex++;
			initIndex = endIndex;

			result.add(record);
		}

		return result;
	}

	private List<IpSegLowBit> bmergeIpSegment(List<IpSeg> list) {
		List<IpSegLowBit> resultList = new ArrayList<IpSegLowBit>();

		Collections.sort(list, new ComparatorIPSegment());

		IpSeg origRecord = null;
		IpSegLowBit resultRecord = null;
		int bnetStart = 0;
		int bnetEnd = 0;
		int offsetStart = 0;
		int offsetEnd = 0;

		for (int i = 0; i < list.size(); i++) {
			origRecord = list.get(i);
			bnetStart = (int) ((origRecord.getSegStart() & ConstBit.MASK_BNET) >> 16);
			bnetEnd = (int) ((origRecord.getSegEnd() & ConstBit.MASK_BNET) >> 16);
			offsetStart = (int) (origRecord.getSegStart() & ConstBit.ADDRESS_BNET);
			offsetEnd = (int) (origRecord.getSegEnd() & ConstBit.ADDRESS_BNET);

			while (bnetStart <= bnetEnd) {
				resultRecord = new IpSegLowBit();
				resultRecord.setBnet(bnetStart);
				resultRecord.setIdCity(origRecord.getIdCity());
//				resultRecord.setIdProvince(origRecord.getIdProvince());
				resultRecord.setSegStart(offsetStart);
				if (bnetStart < bnetEnd) {
					resultRecord.setSegEnd(ConstBit.OFFSET_BNET_END);
				} else {
					resultRecord.setSegEnd(offsetEnd);
				}

				bnetStart++;
				offsetStart = ConstBit.OFFSET_BNET_START;
				resultList.add(resultRecord);
			}
		}

		return resultList;
	}

	class ComparatorIPSegment implements Comparator<IpSeg> {
		@Override
		public int compare(IpSeg o1, IpSeg o2) {
			if (o1.getSegStart() < o2.getSegStart()) {
				return -1;
			} else if (o1.getSegStart() > o2.getSegStart()) {
				return 1;
			} else {
				return 0;
			}
		}

	}

	private int getHighBitFromIpv4String(String ip) {
		int value = 0;
		int knot = 0;
		int offset = 0;
		for (int i = 0; i < 2; i++) {
			while (ip.charAt(offset) != ConstChar.DOT) {
				knot *= 10;
				knot += ip.charAt(offset) - ConstChar.NUMBER_BASE;
				offset++;
			}
			value <<= 8;
			value |= knot;
			knot = 0;
			offset++;
		}
		return value;
	}

	private int getLowBitFromIpv4String(String ip) {
		int value = 0;
		int knot = 0;
		int offset = 0;
		for (int i = 0; i < 2; i++) {
			while (ip.charAt(offset++) != ConstChar.DOT)
				;
		}
		for (int i = 0; i < 2; i++) {
			while (offset < ip.length() &&
					ip.charAt(offset) != ConstChar.DOT) {
				knot *= 10;
				knot += ip.charAt(offset) - ConstChar.NUMBER_BASE;
				offset++;
			}
			value <<= 8;
			value |= knot;
			knot = 0;
			offset++;
		}
		return value;
	}

	public IpZone citySeek(String ip) {
		if (!verifyIpv4(ip)) {
			IpZone region = new IpZone();
			region.setCityId(-1);
			region.setCityRecognized(false);
			return region;
		}
		int highBit = getHighBitFromIpv4String(ip);
		int lowBit = getLowBitFromIpv4String(ip);
		return mapOfIPRegion.citySeekWithJudge(highBit, lowBit);
	}
}
