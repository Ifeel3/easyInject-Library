package com.ifeel3.easyInject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public final class AnnotationProcessor {
    private static AnnotationProcessor processor = null;

    private AnnotationProcessor() {}

    public static AnnotationProcessor getProcessor(){
        if (processor == null) {
            processor = new AnnotationProcessor();
        }
        return processor;
    }

    public Object createAndInject(Class<?> clss, String str) {
        String[] temp = new String[1];
        temp[0] = str;
        return createAndInject(clss, temp);
    }

    public Object createAndInject(Class<?> clss, String[] args) {
        Object temp = null;
        try {
            temp = clss.newInstance();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        inject(temp, args);
        return temp;
    }

    public void inject(Object obj, String str) {
        String[] temp = new String[1];
        temp[0] = str;
        inject(obj, temp);
    }

    public void inject(Object obj, String[] args) {
        Field[] fields = getFieldsWithAnnotation(obj.getClass(), Inject.class);
        for (Field field : fields) {
            String value = getValueFromArray(field.getAnnotation(Inject.class).key(), args);
            injectInField(obj, field, value);
        }
    }

    private void injectInField(Object obj, Field field, String value) {
        boolean isPrivate = Modifier.isPrivate(field.getModifiers());
        if (isPrivate) {
            field.setAccessible(true);
        }
        try {
            switch (field.getType().toString()) {
                case "byte": injectByte(obj, field, value);
                    break;
                case "short": injectShort(obj, field, value);
                    break;
                case "char": incjectChar(obj, field, value);
                    break;
                case "int": injectInt(obj, field, value);
                    break;
                case "long": injectLong(obj, field, value);
                    break;
                case "double": injectDouble(obj, field, value);
                    break;
                case "float": injectFloat(obj, field, value);
                    break;
                case "class java.lang.String": injectString(obj, field, value);
                    break;
                default: System.err.println(field.getType() + " not implemented!");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
        if (isPrivate) {
            field.setAccessible(false);
        }
    }

    private void injectString(Object obj, Field field, String value) throws Exception {
            if (value != null) {
                field.set(obj, value);
            } else {
                field.set(obj, "");
            }
    }

    private void injectFloat(Object obj, Field field, String value) throws Exception {
        if (value != null && value.matches("\\d+.?\\d*")) {
            field.setFloat(obj, Float.parseFloat(value));
        } else {
            field.setFloat(obj, 0.0f);
        }
    }

    private void injectDouble(Object obj, Field field, String value) throws Exception {
        if (value != null && value.matches("\\d+.?\\d*")) {
            field.setDouble(obj, Double.parseDouble(value));
        } else {
            field.setDouble(obj, 0.0);
        }
    }

    private void injectLong(Object obj, Field field, String value) throws Exception{
        if (value != null && value.matches("\\d+")) {
            field.setLong(obj, Long.parseLong(value));
        } else {
            field.setLong(obj, 0);
        }
    }

    private void injectInt(Object obj, Field field, String value) throws Exception {
        if (value != null && value.matches("\\d+")) {
            field.setInt(obj, Integer.parseInt(value));
        } else {
            field.setInt(obj, 0);
        }
    }

    private void incjectChar(Object obj, Field field, String value) throws Exception {
        if (value != null) {
            if (value.matches("\\d+")) {
                field.setChar(obj, (char)Integer.parseInt(value));
            } else {
                field.setChar(obj, value.charAt(0));
            }
        } else {
            field.setChar(obj, (char)0);
        }
    }

    private void injectShort(Object obj, Field field, String value) throws Exception {
        if (value != null && value.matches("\\d+")) {
            field.setShort(obj, Short.parseShort(value));
        } else {
            field.setShort(obj, (short)0);
        }
    }

    private void injectByte(Object obj, Field field, String value) throws Exception {
        if (value != null && value.matches("\\d+")) {
            field.setByte(obj, Byte.parseByte(value));
        } else {
            field.setByte(obj, (byte)0);
        }
    }

    private String getValueFromArray(String key, String[] array) {
        for (String str : array) {
            if (str.matches(key + "=\\S+")) {
                return str.split("=")[1];
            }
        }
        return null;
    }

    private Field[] getFieldsWithAnnotation(Class<?> clss, Class<? extends Annotation> annotation) {
        Field[] fields = clss.getDeclaredFields();
        ArrayList<Field> result = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(annotation)) {
                result.add(field);
            }
        }
        return fromArrayListToArray(result);
    }

    private Field[] fromArrayListToArray(ArrayList<Field> list) {
        Field[] result = new Field[list.size()];
        for (int i = 0; i <= list.size() / 2; i++) {
            result[list.size() - i - 1] = list.get(list.size() - i - 1);
            result[i] = list.get(i);
        }
        return result;
    }
}
