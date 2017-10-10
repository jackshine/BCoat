package com.elastica.beatle.audit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.elastica.beatle.audit.dashboardframeworkdto.BrrServiceCount;

public class AuditDashBoardReportResourceDataSetup {

	AuditGoldenSetDataController3 controller = null;
	List<GoldenSetData> goldenSetDataList = null;

	public AuditDashBoardReportResourceDataSetup(String firewallType) throws Exception {
		this.controller = new AuditGoldenSetDataController3(AuditTestConstants.BE_BARRACUDA_CLI_DATA_SHEET);
		this.goldenSetDataList = controller.loadXlData();
	}

	public static final Comparator<Integer> brrComparator = new Comparator<Integer>() {

		@Override
		public int compare(Integer o1, Integer o2) {
			return o1.compareTo(o2);
		}

	};

	public int getExpectedServiceCount() {
		return goldenSetDataList.size();
	}

	public BrrServiceCount[] getBrrGroupByServiceCount() {

		// String typeoforder- asc/desc: if asc= botton, desc=top
		// String 5,10, 25,50

		List<BrrServiceCount> brrGroupByServiceCountList = new ArrayList<BrrServiceCount>();

		// get the brr of each service
		// put map (brr,service)
		// store all keys in the set by descending order
		// take out top 5 brr scores
		// check each item how many times exist in the map
		// put the map (brr->List<servicename>)
		// prepare BrrServiceCount object from the map and keep it into list
		// convert the list to arry
		// return the array

		return null;
	}

	public static List<Integer> getEntries(Map<Integer, String> brrServicesMap) throws Exception {
		List<Integer> sortedBrrList = new ArrayList<Integer>(brrServicesMap.size());
		sortedBrrList.addAll(brrServicesMap.keySet());
		// Collections.sort(sortedBrrList);
		Collections.sort(sortedBrrList, brrComparator);
		return sortedBrrList;

	}

	static <K, V extends Comparable<? super V>> List<Entry<K, V>> entriesSortedByValues(Map<K, V> map) {

		List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(map.entrySet());

		Collections.sort(sortedEntries, new Comparator<Entry<K, V>>() {
			@Override
			public int compare(Entry<K, V> e1, Entry<K, V> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}
		});

		return sortedEntries;
	}

}
