package me.libhycore.interfaces;

import com.google.gson.JsonObject;

import java.lang.reflect.Field;

public interface JSONCompatibleObject {
    public default JsonObject toJsonObject(JSONCompatibleObject object) {
        JsonObject jsonObject = new JsonObject();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                String typeName = field.getType().getTypeName().toLowerCase().replaceAll("java.lang.", "");
                switch (typeName) {
                    case "short" -> jsonObject.addProperty(field.getName(), field.getShort(this));
                    case "int" -> jsonObject.addProperty(field.getName(), field.getInt(this));
                    case "long" -> jsonObject.addProperty(field.getName(), field.getLong(this));
                    case "float" -> jsonObject.addProperty(field.getName(), field.getFloat(this));
                    case "double" -> jsonObject.addProperty(field.getName(), field.getDouble(this));
                    case "boolean" -> jsonObject.addProperty(field.getName(), field.getBoolean(this));
                    case "char" -> jsonObject.addProperty(field.getName(), field.getChar(this));
                    case "string" -> jsonObject.addProperty(field.getName(), field.get(this).toString());
                    default -> {
                        if (field.get(this) instanceof JSONCompatibleObject) {
                            jsonObject.add(field.getName(), ((JSONCompatibleObject) field.get(this)).toJsonObject((JSONCompatibleObject) field.get(this)));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    public Object fromJsonObject(JsonObject jsonObject);
}
