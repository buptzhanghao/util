package com.sankuai.xm.search.dashboard.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhanghao
 * @version 1.0
 * @created 16/1/11
 */
public class GeoHash {

    private static final List<Integer> EMPTY_LIST = new ArrayList<>();
    private static final String EMPTY_GEO_HASH = "";

    private static final BiMap<Integer, Character> BASE32 = HashBiMap.create();
//    "0123456789bcdefghjkmnpqrstuvwxyz"; （去掉a, i, l, o）

    static {
        for ( int i = 0 ; i != 10 ; i ++ ) {
            BASE32.put(i, (char)('0' + i));
        }

        char base = 'b';
        for ( int i = 10; i != 32 ; i ++ ) {
            if ( base == 'i' ) {
                base = (char)(base + 1);
            }
            if ( base == 'l' ) {
                base = (char)(base + 1);
            }
            if ( base == 'o' ) {
                base = (char)(base + 1);
            }
            BASE32.put(i, base);
            base = (char)(base + 1);
        }
    }

    /**
     * 将经纬度转化为GEO HASH
     */
    public static String encodeGeoHash(double latitude, double longitude, int precision) {

        StringBuilder geohash = new StringBuilder();

        boolean isEven = true;
        int i = 0;
        double[] lat = new double[2];
        double[] lon = new double[2];
        double   mid;
        char bits[] = {16,8,4,2,1};
        int bit = 0, ch = 0;
        lat[0] = -90.0; lat[1] = 90.0;
        lon[0] = -180.0; lon[1] = 180.0;

        while (i < precision) {
            if (isEven) {
                mid = (lon[0] + lon[1]) / 2;
                if (longitude > mid) {
                    ch |= bits[bit];
                    lon[0] = mid;
                } else {
                    lon[1] = mid;
                }
            } else {
                mid = (lat[0] + lat[1]) / 2;
                if (latitude > mid) {
                    ch |= bits[bit];
                    lat[0] = mid;
                } else {
                    lat[1] = mid;
                }
            }
            isEven = !isEven;
            if (bit < 4) {
                bit++;
            }
            else {
                geohash.append(BASE32.get(ch));
                i++;
                bit = 0;
                ch = 0;
            }
        }
        return geohash.toString();
    }

    /**
     * 将二进制坐标点转化为GEO HASH值
     */
    public static String encodeGeoHash(List<Integer> latList, List<Integer> lngList) {
        if ( (latList.size() + lngList.size()) % 5 != 0) {
            return EMPTY_GEO_HASH;
        }

        StringBuilder geohash = new StringBuilder();
        boolean isEven = false;
        int precision = (latList.size() + lngList.size()) / 5;
        int i = 0;

        int base = 0 ;
        int len = 0;
        int a = 0 ;
        int b = 0;
        while ( i < precision ) {

            if (isEven) {
                base = (base << 1) + latList.get(a++);
            } else {
                base = (base << 1) + lngList.get(b++);
            }

            if ( 4 == len) {
                geohash.append(BASE32.get(base));
                base = 0;
                len = 0;
                i++;
            } else {
                len++;
            }

            isEven = !isEven;
        }
        return geohash.toString();
    }

    /**
     * 将 GEO HASH 字符串转化为经度纬度
     */
    public static Map<String, Double[]> decodeGeohash(String geohash) {
        Map<String, List<Integer>> map = decodeGeohashToBinary(geohash);

        return getLatAndLng(map.get("lat"), map.get("lng"));
    }

    /**
     * 将 GEO HASH 字符串转化为二进制坐标
     */
    public static Map<String, List<Integer>> decodeGeohashToBinary(String geohash) {
        int precision = geohash.length();
        boolean isEven = false;
        List<Integer> latList = new ArrayList<>();
        List<Integer> lngList = new ArrayList<>();
        char bits[] = {16,8,4,2,1};

        int i = 0 ;
        while ( i < precision ) {
            int x = BASE32.inverse().get(geohash.charAt(i));

            int bit = 0;
            while ( bit < 5 ) {
                if ( isEven ) {
                    latList.add( (x & bits[bit]) == bits[bit] ? 1 : 0);
                } else {
                    lngList.add( (x & bits[bit]) == bits[bit] ? 1 : 0);
                }
                bit++;
                isEven = !isEven;
            }
            i++;
        }

        Map<String, List<Integer>> map = new HashMap<>();
        map.put("lat", latList);
        map.put("lng", lngList);
        return map;
    }

    /**
     * 将二进制坐标转化为经度纬度
     */
    public static Map<String, Double[]> getLatAndLng(List<Integer> latList, List<Integer> lngList) {
        Map<String, Double[]> map = new HashMap<>();
        Double[] x1;
        Double[] x2;

        x1 = getLat(latList);
        x2 = getLng(lngList);

        map.put("lat", x1);
        map.put("lng", x2);

        return map;
    }

    /**
     * 将二进制坐标转化为纬度
     */
    public static Double[] getLat(List<Integer> latList) {
        Double[] lat = new Double[2];
        lat[0] = -90.0; lat[1] = 90.0;

        for ( Integer i : latList ) {
            double mid = (lat[0] + lat[1]) / 2;
            if ( i == 1 ) {
                lat[0] = mid;
            } else if ( i == 0 ) {
                lat[1] = mid;
            }
        }

        return lat;
    }

    /**
     * 将二进制坐标转化为经度
     */
    public static Double[] getLng(List<Integer> lngList) {
        Double[] lon = new Double[2];
        lon[0] = -180.0; lon[1] = 180.0;

        for ( Integer i : lngList ) {
            double mid = (lon[0] + lon[1]) / 2;
            if ( i == 1 ) {
                lon[0] = mid;
            } else if ( i == 0 ) {
                lon[1] = mid;
            }
        }

        return lon;
    }

    /**
     * 获取给定的GEO HASH 值附近的8个坐标， 加上自己共9个坐标
     * ===================
     *   0  ||  1  ||  2
     * ===================
     *   3  ||  4  ||  5
     * ===================
     *   6  ||  7  ||  8
     * ===================
     */
    public static String[] getNearGeoHash(String geohash) {
        String[] strArr = new String[9];
        Map<String, List<Integer>> map = decodeGeohashToBinary(geohash);

        List<Integer> latList = map.get("lat");
        List<Integer> lngList = map.get("lng");

        List<Integer> latUList = genUpList(latList);
        List<Integer> latDList = genDownList(latList);
        List<Integer> lngUList = genUpList(lngList);
        List<Integer> lngDList = genDownList(lngList);

        List<List<Integer>> xList = new ArrayList<>();
        xList.add(latDList);
        xList.add(latList);
        xList.add(latUList);

        List<List<Integer>> yList = new ArrayList<>();
        yList.add(lngUList);
        yList.add(lngList);
        yList.add(lngDList);

        for ( int i = 0 ; i != 3 ; i ++ ) {
            for ( int j = 0 ; j != 3 ; j ++ ) {
                if ( xList.get(i) != EMPTY_LIST && yList.get(j) != EMPTY_LIST ) {
                    strArr[ i * 3 + j] = encodeGeoHash(xList.get(i), yList.get(j));
                } else {
                    strArr[ i * 3 + j] = EMPTY_GEO_HASH;
                }
            }
        }

        return strArr;
    }

    private static List<Integer> genUpList(List<Integer> lList) {
        List<Integer> list = Arrays.asList(new Integer[lList
                .size()]);
        Collections.copy(list, lList);
        int i = list.size() - 1;
        for ( ; i != -1 ; i -- ) {
            int x = list.get(i);
            if ( x == 1 ) {
                list.set(i, 0);
                break;
            } else {
                list.set(i, 1);
            }
        }

        if ( i == -1 ) {
            return EMPTY_LIST;
        } else {
            return list;
        }
    }

    private static List<Integer> genDownList(List<Integer> lList) {
        List<Integer> list = Arrays.asList(new Integer[lList
                .size()]);
        Collections.copy(list, lList);
        int i = list.size() - 1;
        for ( ; i != -1 ; i -- ) {
            int x = list.get(i);
            if ( x == 0 ) {
                list.set(i, 1);
                break;
            } else {
                list.set(i, 0);
            }
        }

        if ( i == -1 ) {
            return EMPTY_LIST;
        } else {
            return list;
        }
    }


    public static void main(String[] args) {
        System.out.println(encodeGeoHash(39.928167, 116.389550, 5));

        Map<String, Double[]> map = decodeGeohash("wx4g0");

        for ( Double d : map.get("lat") ) {
            System.out.println(d);
        }

        for ( Double d : map.get("lng") ) {
            System.out.println(d);
        }

        getNearGeoHash("wx57p");

        String[] arr = getNearGeoHash("wx4g0");

        for ( int i = 0 ; i != 9 ; i ++ ) {
            System.out.print(arr[i] + ", ");
            if ( i % 3 == 2 ) {
                System.out.println();
            }
        }
    }
}
