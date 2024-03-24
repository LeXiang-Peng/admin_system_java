package com.plx.admin_system.utils;
import java.lang.reflect.Field;
import java.util.Map;
import java.lang.reflect.Modifier;
import java.util.Collection;

/**
 * @author plx
 */
public class ObjectIsNullUitl {
    public static boolean checkFieldAllNull(Object object) throws IllegalAccessException {
        for (Field f : object.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            if (Modifier.isFinal(f.getModifiers()) && Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            if (!isEmpty(f.get(object))) {
                return false;
            }
            f.setAccessible(false);
        }
        //父类public属性
        for (Field f : object.getClass().getFields()) {
            f.setAccessible(true);
            if (Modifier.isFinal(f.getModifiers()) && Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            if (!isEmpty(f.get(object))) {
                return false;
            }
            f.setAccessible(false);
        }
        return true;
    }


    public static boolean isAllFieldNull(Object obj) throws Exception {
        // 得到类对象
        Class stuCla = (Class) obj.getClass();
        //得到属性集合
        Field[] fs = stuCla.getDeclaredFields();
        boolean flag = true;
        //遍历属性
        for (Field f : fs) {
            // 设置属性是可以访问的(私有的也可以)
            f.setAccessible(true);
            // 得到此属性的值
            Object val = f.get(obj);
            //只要有1个属性不为空,那么就不是所有的属性值都为空
            if (val != null) {
                flag = false;
                break;
            }
        }
        return flag;
    }


    private static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof String && (object.toString().equals(""))) {
            return true;
        }
        if (object instanceof Collection && ((Collection) object).isEmpty()) {
            return true;
        }
        if (object instanceof Map && ((Map) object).isEmpty()) {
            return true;
        }
        if (object instanceof Object[] && ((Object[]) object).length == 0) {
            return true;
        }
        return false;
    }

}

