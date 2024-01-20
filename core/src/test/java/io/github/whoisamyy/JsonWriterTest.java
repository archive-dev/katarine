package io.github.whoisamyy;

import io.github.whoisamyy.components.Component;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.test.Game;
import io.github.whoisamyy.test.components.CircleController;
import io.github.whoisamyy.test.serialization.ExampleSerializer;
import io.github.whoisamyy.utils.serialization.Json;
import io.github.whoisamyy.utils.serialization.JsonObject;
import io.github.whoisamyy.utils.serialization.JsonWriter;
import io.github.whoisamyy.utils.serialization.ListSerializer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class JsonWriterTest {
    public static class Cerf {
        public String s;
        public int a;
        public float b;
        private CircleController cc;
        private List<Component> exampleList = new ArrayList<>();

        public Cerf(String s, int a, float b, CircleController cc, Component... strs) {
            this.s = s;
            this.a = a;
            this.b = b;
            this.cc = cc;
            exampleList.addAll(List.of(strs));
        }
    }

    public static void test_validJsonObjectWithPrimitiveFields() throws IllegalAccessException {
        new Game(1,1);

        // Arrange
        Json.addTypeSerializer(CircleController.class, new ExampleSerializer(CircleController.class));
        Json.addTypeSerializer(List.class, new ListSerializer<>(List.class));

        //System.out.println(Json.knownTypes.toString());

        StringBuilder jsonBuilder = new StringBuilder();
        JsonWriter writer = new JsonWriter(jsonBuilder);
        JsonObject jsonObject = new JsonObject()
                .setValue(GameObject.instantiate(GameObject.class, GameObject.instantiate(GameObject.class)))
                .setName("name");

        // Act
        String result = writer.writeObject(jsonObject);

        // Assert
        System.out.println(result);
    }

    public static void test_toJsonObject() throws IllegalAccessException {
        JsonObject object = JsonObject.toJsonObject(new Cerf("leps", 5, 0.1f, new CircleController()));
        for (Field f : object.getClass().getFields()) {
            System.out.println(f.get(object));
        }
        System.out.println();

        for (JsonObject obj : object.children) {
            System.out.println(obj.getValue());
        }
    }

    public static void main(String[] args) {
        try {
            test_validJsonObjectWithPrimitiveFields();
            //test_toJsonObject();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
