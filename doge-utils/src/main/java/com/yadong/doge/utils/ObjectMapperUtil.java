package com.yadong.doge.utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

public class ObjectMapperUtil {


    private static final ObjectMapper MAPPER = new ObjectMapper();


    public static String toJSON(Object object){
        try {
            if(object == null){
                throw new RuntimeException("传递的参数object为null,请认真检查");
            }
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            //应该将检查异常,转化为运行时异常.
            throw new RuntimeException("传递的对象不支持json转化/检查是否有get/set方法");
        }
    }


    //2.将任意的JSON串转化为对象  传递什么类型转化什么对象
    public static <T> T toObject(String json,Class<T> target){

        if(StringUtils.isEmpty(json) || target == null){
            throw new RuntimeException("传递的参数不能为null");
        }
        try {
            return MAPPER.readValue(json,target);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
//            throw new RuntimeException("json转化异常");
            return null;
        }
    }
}


