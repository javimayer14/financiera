package com.financial.exchange.market.utils;

public class StringCustomUtils {
    
    public static String trimString(String param) {
        String trimmedParam = param;
        if (param != null) {
            trimmedParam = param.trim();
        }
        return trimmedParam;
    }
}
