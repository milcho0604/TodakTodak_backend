package com.padaks.todaktodak.common.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;

@Component
public class DistanceCalculator {

    private static final BigDecimal EARTH_RADIUS_KM = BigDecimal.valueOf(6371); // 지구 반지름

    public static String calculateDistance(BigDecimal lat1, BigDecimal lon1, BigDecimal lat2, BigDecimal lon2) {
        BigDecimal lat1Rad = BigDecimal.valueOf(Math.toRadians(lat1.doubleValue()));
        BigDecimal lon1Rad = BigDecimal.valueOf(Math.toRadians(lon1.doubleValue()));
        BigDecimal lat2Rad = BigDecimal.valueOf(Math.toRadians(lat2.doubleValue()));
        BigDecimal lon2Rad = BigDecimal.valueOf(Math.toRadians(lon2.doubleValue()));

        BigDecimal deltaLat = lat2Rad.subtract(lat1Rad);
        BigDecimal deltaLon = lon2Rad.subtract(lon1Rad);

        // Haversine 공식
        BigDecimal a = BigDecimal.valueOf(Math.sin(deltaLat.divide(BigDecimal.valueOf(2), MathContext.DECIMAL128).doubleValue()))
                .multiply(BigDecimal.valueOf(Math.sin(deltaLat.divide(BigDecimal.valueOf(2), MathContext.DECIMAL128).doubleValue())))
                .add(BigDecimal.valueOf(Math.cos(lat1Rad.doubleValue()))
                        .multiply(BigDecimal.valueOf(Math.cos(lat2Rad.doubleValue())))
                        .multiply(BigDecimal.valueOf(Math.sin(deltaLon.divide(BigDecimal.valueOf(2), MathContext.DECIMAL128).doubleValue())))
                        .multiply(BigDecimal.valueOf(Math.sin(deltaLon.divide(BigDecimal.valueOf(2), MathContext.DECIMAL128).doubleValue()))));
        BigDecimal c = BigDecimal.valueOf(2).multiply(BigDecimal.valueOf(Math.atan2(Math.sqrt(a.doubleValue()), Math.sqrt(BigDecimal.ONE.subtract(a).doubleValue()))));

        // 지구 반지름을 곱하여 거리를 구함 (단위: km)
        BigDecimal distanceKm = EARTH_RADIUS_KM.multiply(c);
        BigDecimal distanceM = distanceKm.multiply(BigDecimal.valueOf(1000)); // 거리 단위: 미터(m)

        // 거리 반환
        if (distanceM.compareTo(BigDecimal.valueOf(1000)) < 0) {
            return String.format("%.2f m", distanceM); // 1km 미만일 때 미터 단위
        } else {
            return String.format("%.2f km", distanceKm); // 1km 이상일 때 킬로미터 단위
        }
    }
}
