package com.zichan.dao.springJdbc.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

public class MapObjHelper {

	/**
	 * 如果确认Map<String, Object>中的值全部为String类型，可把map转换成Map<String, String>
	 */
	public static Map<String, String> mapObjToString(Map<String, Object> map) {
		Map<String, String> returnMap = new HashMap<String, String>();
		Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			String value = "";
			if (null != entry.getValue()) {
				value = String.valueOf(entry.getValue());
			}
			returnMap.put(entry.getKey(), value);
		}
		return returnMap;
	}

	/**
	 * 当map中只有一对key：value时，取得value值
	 */
	public static Object getMapValue(Map<String, Object> map) {
		Object result = null;
		Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			result = entry.getValue();
		}
		return result;
	}

	/**
	 * 放入此map中的数据会根据key进行排序并按照顺序输出 已实现线程同步机制
	 */
	public static Map<String, Object> getMapSortedByKey() {
		Map<String, Object> map = Collections.synchronizedMap(new TreeMap<String, Object>(new Comparator<String>() {
			public int compare(String obj1, String obj2) {
				return obj2.compareTo(obj1);
			}
		}));
		return map;
	}

	/**
	 * trim 所有指定的key的值
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> trimValueByKey(Map<K, V> map, K... ks) {
		try {
			for (K k : ks) {
				if (map.containsKey(k)) {
					try {
						map.put(k, (V) StringUtils.trim((String) map.get(k)));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	
	/**
	 * 根据参数映射关系，生成一个新映射的map
	 * @param name_name2Map
	 * @param name_valueMap
	 * @return name2_valueMap
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> transMap(Map<K, V> name_name2Map, Map<K, V> name_valueMap,boolean...serializeNulls) {
		Map<K, V> name2_valueMap = new HashMap<K, V>();
		try {
			for (K name : name_name2Map.keySet()) {
				V name2 = name_name2Map.get(name);
				V value = name_valueMap.get(name);
				try{
					if (name_valueMap.containsKey(name)) {
						name2_valueMap.put((K) name2, value);
					}else{
						if(ArrayUtils.isNotEmpty(serializeNulls)&&true == serializeNulls[0]){
							name2_valueMap.put((K) name2, null);
						}
					}
				}catch(Exception e){
					name2_valueMap.put((K) String.valueOf(name2), value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name2_valueMap;
	}
	
	/**
	 * 数据清洗list
	 * @param K_VList
	 * @param VS
	 * @return
	 */
	public static <K, V>List<Map<K, V>> transAndFlushListMap(List<Map<K,V>>K_VList,List<V[]>VS){
		if(ObjectUtils.isEmpty(K_VList)){
			return K_VList;
		}
		List<Map<K,V>>K2_VList = new ArrayList<Map<K,V>>();
		for(Map<K,V> K_VMap:K_VList){
			K2_VList.add(transAndFlushMap(K_VMap, VS));
		}
		return K2_VList;
	}
	
	/**
	 * 数据清洗map
	 * @param K_VMap
	 * @param VS
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> transAndFlushMap(Map<K, V> K_VMap, List<V[]> VS) {
		if (ObjectUtils.isEmpty(K_VMap)) {
			return K_VMap;
		}
		Map<K, V> K2_VMap = new HashMap<>();
		for (V[] vs : VS) {
			if (K_VMap.containsKey((K) vs[0])) {
				V v = K_VMap.get((K) vs[0]);
				if (v == null) {
					v = vs[2];
				} else {
					if (!ObjectUtils.isEmpty(vs[3])) {
						try {
							String[] invokeArgs = ((String) vs[3]).split("#");
							v = (V) InvokeMethod.invoke(invokeArgs[0] + "#" + invokeArgs[1],
									new Class[] { Object.class, Object.class }, invokeArgs[2], v);
						} catch (Exception e) {
							v = vs[2];
						}
					}
					K2_VMap.put((K) vs[1], v);
				}
			} else {
				K2_VMap.put((K) vs[1], vs[2]);
			}
		}
		return K2_VMap;
	}
	
	
	
	
}
