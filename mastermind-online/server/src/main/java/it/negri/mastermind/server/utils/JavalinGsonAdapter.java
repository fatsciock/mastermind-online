package it.negri.mastermind.server.utils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import io.javalin.http.BadRequestResponse;
import io.javalin.json.JsonMapper;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Objects;

public class JavalinGsonAdapter implements JsonMapper {

    private final Gson gson;

    public JavalinGsonAdapter(Gson gson) {
        this.gson = Objects.requireNonNull(gson);
    }

    @NotNull
    @Override
    public String toJsonString(@NotNull Object obj, @NotNull Type type) {
        return gson.toJson(obj, type);
    }

    @NotNull
    @Override
    public <T> T fromJsonString(@NotNull String json, @NotNull Type targetType) {
        try {
            return gson.fromJson(json, targetType);
        } catch (JsonParseException e) {
            throw new BadRequestResponse("JSON error: " + e.getMessage());
        }
    }
}
