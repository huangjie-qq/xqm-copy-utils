package com.xqm.utils;

import com.xqm.annotation.Copy;
import com.xqm.annotation.Exclude;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author：小球某
 * 2021/09/23/22:57
 * Bean转换工具类
 */
public class CopyUtils {

    /**
     * 单个Bean转换
     * @param source      来源对象
     * @param targetClass 目标对象class
     * @param <T>
     * @return
     */
    public static <T> T copy(Object source,Class<T> targetClass){
        if(targetClass==null)
            throw new RuntimeException("目标对象class 不能为空");
        if(source==null)
            return null;
        T targe =null;
        try {
            targe = targetClass.newInstance();
            Class<?> sourceClass = source.getClass();
            Field[] fields = targetClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (handleExclude(field))
                    continue;
                String mappingField=handleCopy(field);
                try {
                    Field sourceField = sourceClass.getDeclaredField(mappingField);
                    sourceField.setAccessible(true);
                    field.set(targe,sourceField.get(source));
                }catch (Exception e){}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return targe;
    }


    /**
     * 多个Bean转换
     * @param sourceList  来源对象列表
     * @param targetClass 目标对象class
     * @param <T>
     * @return
     */
    public static <T> List<T> copy(List sourceList,Class<T> targetClass){
        if(sourceList==null||sourceList.size()==0)
            return null;
        List<T> targetList = new ArrayList<>();
        sourceList.forEach(source->{targetList.add(copy(source,targetClass));});
        return targetList;
    }


    /**
     * map集合转Bean
     * @param map          来源Map
     * @param targetClass  目标对象class
     * @param <T>
     * @return
     */
    public static <T> T toBean(Map<String,Object> map,Class<T> targetClass){
        if(targetClass==null)
            throw new RuntimeException("目标对象class 不能为空");
        if(map==null||map.size()==0)
            return null;
        T targe =null;
        try {
            targe=targetClass.newInstance();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                try {
                    Field field = targetClass.getDeclaredField(entry.getKey());
                    field.setAccessible(true);
                    field.set(targe,entry.getValue());
                } catch (NoSuchFieldException e) {}
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return targe;
    }

    /**
     * Bean转Map
     * @param target  来源对象
     * @param
     * @return
     */
    public static  Map<String,Object> toMap(Object target){
        if(target==null)
            throw new RuntimeException("转换的Bean 不能为空");
        Class<?> targetClass = target.getClass();
        Map<String,Object> map=new HashMap<>();
        Field[] fields = targetClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(),field.get(target));
            } catch (IllegalAccessException e) {}
        }
        return map;
    }

    /**
     * 处理Exclude注解
     * @param field
     * @return true 跳过字段映射  false 处理字段映射
     */
    private static boolean handleExclude(Field field){
        Exclude exclude = field.getAnnotation(Exclude.class);
        if(exclude!=null&& exclude.value()==true)
            return true;
        return false;
    }


    /**
     * 处理Copy注解
     * @param field
     * @return 返回映射内容 没有则返回FieldName
     */
    private static String handleCopy(Field field){
        Copy copy = field.getAnnotation(Copy.class);
        String mappingField=null;
        if(copy!=null&&!copy.value().trim().equals("")){
            mappingField = copy.value();
        }
        return mappingField==null?field.getName():mappingField;
    }
}
