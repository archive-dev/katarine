package io.github.whoisamyy.utils.serialization.serializers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class JsonDeserializer {
    private final String json;

    public JsonDeserializer(String json) {
        this.json = json;
    }

    public JsonDeserializer(File json) throws IOException {
        this(new String(Files.readAllBytes(json.toPath())));
    }

//    public <T> T deserialize(Class<T> type) {
//
//    }
}
