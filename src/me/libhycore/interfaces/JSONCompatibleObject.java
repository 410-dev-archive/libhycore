package me.libhycore.interfaces;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public interface JSONCompatibleObject {
    public default JsonObject toJsonObject(JSONCompatibleObject object) {
        JsonObject toReturn = new JsonObject();
        JsonObject data = new JsonObject();
        toReturn.addProperty("class", object.getClass().getName());
        toReturn.addProperty("version", 1);
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                String typeName = field.getType().getTypeName().toLowerCase().replaceAll("java.lang.", "");
                switch (typeName) {
                    case "short" -> data.addProperty(field.getName(), field.getShort(this));
                    case "int" -> data.addProperty(field.getName(), field.getInt(this));
                    case "long" -> data.addProperty(field.getName(), field.getLong(this));
                    case "float" -> data.addProperty(field.getName(), field.getFloat(this));
                    case "double" -> data.addProperty(field.getName(), field.getDouble(this));
                    case "boolean" -> data.addProperty(field.getName(), field.getBoolean(this));
                    case "char" -> data.addProperty(field.getName(), field.getChar(this));
                    case "string" -> data.addProperty(field.getName(), field.get(this).toString());
                    case "arraylist" -> {
                        JsonArray array = new JsonArray();
                        for (Object o : (ArrayList<?>) field.get(this)) {
                            if (o instanceof JSONCompatibleObject) {
                                array.add(toJsonObject((JSONCompatibleObject) o));
                            } else {
                                array.add(o.toString());
                            }
                        }
                    }
                    default -> {
                        if (field.get(this) instanceof JSONCompatibleObject) {
                            data.add(field.getName(), ((JSONCompatibleObject) field.get(this)).toJsonObject((JSONCompatibleObject) field.get(this)));
                        }else{
                            data.addProperty(field.getName(), field.get(this).toString());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        toReturn.add("data", data);
        return toReturn;
    }

    public default Object fromJsonObject(JsonObject jsonObject) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> obj = Class.forName(jsonObject.get("class").getAsString());
        Object instance = obj.getDeclaredConstructor().newInstance();
        JsonObject data = jsonObject.get("data").getAsJsonObject();
        Field[] fields = obj.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String typeName = field.getType().getTypeName().toLowerCase().replaceAll("java.lang.", "");
            switch (typeName) {
                case "short" -> field.setShort(instance, data.get(field.getName()).getAsShort());
                case "int" -> field.setInt(instance, data.get(field.getName()).getAsInt());
                case "long" -> field.setLong(instance, data.get(field.getName()).getAsLong());
                case "float" -> field.setFloat(instance, data.get(field.getName()).getAsFloat());
                case "double" -> field.setDouble(instance, data.get(field.getName()).getAsDouble());
                case "boolean" -> field.setBoolean(instance, data.get(field.getName()).getAsBoolean());
                case "char" -> field.setChar(instance, data.get(field.getName()).getAsCharacter());
                case "string" -> field.set(instance, data.get(field.getName()).getAsString());
                case "arraylist" -> {
                    ArrayList<Object> array = new ArrayList<>();
                    for (Object o : data.get(field.getName()).getAsJsonArray()) {
                        if (o instanceof JsonObject) {
                            array.add(fromJsonObject((JsonObject) o));
                        } else {
                            array.add(o.toString());
                        }
                    }
                    field.set(instance, array);
                }
                default -> {
                    if (field.get(instance) instanceof JSONCompatibleObject) {
                        field.set(instance, ((JSONCompatibleObject) field.get(instance)).fromJsonObject(data.get(field.getName()).getAsJsonObject()));
                    }else{
                        field.set(instance, data.get(field.getName()).getAsString());
                    }
                }
            }
        }

        return instance;
    }
}
