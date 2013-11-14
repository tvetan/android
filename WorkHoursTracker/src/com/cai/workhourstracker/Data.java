//
//package com.cai.workhourstracker;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import com.cai.workhourstracker.helper.DatabaseHelper;
//import com.cai.workhourstracker.model.Composer;
//import com.cai.workhourstracker.model.Job;
//
//import android.os.SystemClock;
//import android.util.Pair;
//
//public class Data {
//	public static final String TAG = Data.class.getSimpleName();
//
//	public static List<Pair<String, List<Job>>> getAllData() {
//		List<Pair<String, List<Job>>> res = new ArrayList<Pair<String, List<Job>>>();
//
//		for (int i = 0; i < 5; i++) {
//			res.add(getOneSection(i));
//		}
//
//		return res;
//	}
//
//	public static List<Composer> getFlattenedData() {
//		List<Composer> res = new ArrayList<Composer>();
//
//		for (int i = 0; i < 5; i++) {
//			res.addAll(getOneSection(i).second);
//		}
//
//		return res;
//	}
//
//	public static Pair<Boolean, List<Composer>> getRows(int page) {
//		List<Composer> flattenedData = getFlattenedData();
//		if (page == 1) {
//			return new Pair<Boolean, List<Composer>>(true,
//					flattenedData.subList(0, 5));
//		} else {
//			SystemClock.sleep(2000); // simulate loading
//			return new Pair<Boolean, List<Composer>>(
//					page * 5 < flattenedData.size(), flattenedData.subList(
//							(page - 1) * 5,
//							Math.min(page * 5, flattenedData.size())));
//		}
//	}
//
//	public static Pair<String, List<Composer>> getOneSection(int index) {
//		String[] titles = { "Renaissance", "Baroque", "Classical", "Romantic",
//				"test","test11","122" };
//		
//		
//		Composer[][] composerss = {
//				{ new Composer("Thomas Tallis", "1510-1585"),
//						new Composer("Josquin Des Prez", "1440-1521"),
//						new Composer("Pierre de La Rue", "1460-1518"), },
//				{ new Composer("Johann Sebastian Bach", "1685-1750"),
//						new Composer("George Frideric Handel", "1685-1759"),
//						new Composer("Antonio Vivaldi", "1678-1741"),
//						new Composer("George Philipp Telemann", "1681-1767"), },
//				{ new Composer("Franz Joseph Haydn", "1732-1809"),
//						new Composer("Wolfgang Amadeus Mozart", "1756-1791"),
//						new Composer("Barbara of Portugal", "1711Ð1758"),
//						new Composer("Frederick the Great", "1712Ð1786"),
//						new Composer("John Stanley", "1712Ð1786"),
//						new Composer("Luise Adelgunda Gottsched", "1713Ð1762"),
//						new Composer("Johann Ludwig Krebs", "1713Ð1780"),
//						new Composer("Carl Philipp Emanuel Bach", "1714Ð1788"),
//						new Composer("Christoph Willibald Gluck", "1714Ð1787"),
//						new Composer("Gottfried August Homilius", "1714Ð1785"), },
//				{ new Composer("Ludwig van Beethoven", "1770-1827"),
//						new Composer("Fernando Sor", "1778-1839"),
//						new Composer("Johann Strauss I", "1804-1849"), },
//				{ new Composer("Thomas Tallis", "1510-1585"),
//						new Composer("Josquin Des Prez", "1440-1521"),
//						new Composer("Pierre de La Rue", "1460-1518"), }, };
//		return new Pair<String, List<Composer>>(titles[index],
//				Arrays.asList(composerss[index]));
//	}
//}
