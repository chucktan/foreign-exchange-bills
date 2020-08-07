package com.foreign.exchange.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * 浮点型运算公共类
 * @author
 * @create 2020-08-07-9:42
 */
public class OptionUtils {

    public  static  final ObjectMapper mapper = new ObjectMapper();

    /**
     * 乘
     * @param d1
     * @param d2
     * @return
     */
    public  static double multi(double d1,double d2){
        BigDecimal b1 = new BigDecimal(String.valueOf(d1));
        BigDecimal b2 = new BigDecimal(String.valueOf(d2));

        return b1.multiply(b2).doubleValue();
    }

    /**
     * 除
     * @param d1
     * @param d2
     * @return
     */
    public  static  double divide(double d1,double d2){
        BigDecimal b1 = new BigDecimal(String.valueOf(d1));
        BigDecimal b2 = new BigDecimal(String.valueOf(d2));

        return b1.divide(b2,6,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 加
     * @param d1
     * @param d2
     * @return
     */
    public static  double add(double d1,double d2){
        BigDecimal b1 = new BigDecimal(String.valueOf(d1));
        BigDecimal b2 = new BigDecimal(String.valueOf(d2));

        return  b1.add(b2).doubleValue();
    }

    /**
     * 减
     * @param d1
     * @param d2
     * @return
     */
    public static double substract(double d1,double d2){
        BigDecimal b1 = new BigDecimal(String.valueOf(d1));
        BigDecimal b2 = new BigDecimal(String.valueOf(d2));

        return  b1.subtract(b2).doubleValue();
    }

    public static String toJsonString(Object obj){
        try {
            return  mapper.writeValueAsString(obj);
        }catch(JsonProcessingException ex){
            throw  new  RuntimeException(ex);
        }
    }

    public static <T> T toJsonObject(String jsonStr,Class<T> type){
        try {
            return mapper.readValue(jsonStr,type);
        }catch (IOException ex){
            throw   new RuntimeException(ex);
        }
    }
}
