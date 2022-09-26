package me.libhycore;

import java.lang.reflect.Field;

public class InstanceMirror {
    public static void mirror(Object self, Object other) {
        if (self.getClass() != other.getClass()) {
            throw new IllegalArgumentException("The two objects must be of the same class");
        }

        Field[] fields = self.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                field.set(self, field.get(other));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
