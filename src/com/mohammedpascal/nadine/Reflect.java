package com.mohammedpascal.nadine;

import java.lang.reflect.Field;

public class Reflect {
	
	public static String[] fields(Object o){
		return fields(o.getClass());
	}

	public static String[] fields(Class<?> clazz){
		Field[] fields = getFields(clazz);
		
		String[] columns = new String[fields.length];
		int i = 0;
		for (Field field : fields) {
			columns[i++] = field.getName();
		}

		return columns;
	}

	public static Field[] getFields(Object o){
		return getFields(o.getClass());
	}

	public static Field[] getFields(Class<?> clazz){
		return clazz.getDeclaredFields();
	}
	
	public static Object getFieldValue(Object o, String fieldName) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException{
		Field field = o.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(o);
	}

}
