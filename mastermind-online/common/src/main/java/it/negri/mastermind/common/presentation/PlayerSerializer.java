package it.negri.mastermind.common.presentation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.negri.mastermind.common.model.Player;

import java.lang.reflect.Type;

public class PlayerSerializer implements JsonSerializer<Player> {
    @Override
    public JsonElement serialize(Player src, Type typeOfSrc, JsonSerializationContext context) {
        var object = new JsonObject();
        object.addProperty("nickname", src.getNickname());
        object.add("role", context.serialize(src.getRole()));
        return object;
    }
}
