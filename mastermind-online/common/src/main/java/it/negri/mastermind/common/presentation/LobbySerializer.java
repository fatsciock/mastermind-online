package it.negri.mastermind.common.presentation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.negri.mastermind.common.model.Lobby;

import java.lang.reflect.Type;

public class LobbySerializer implements JsonSerializer<Lobby> {
    @Override
    public JsonElement serialize(Lobby src, Type typeOfSrc, JsonSerializationContext context) {
        var object = new JsonObject();
        object.addProperty("id", src.getId());
        object.add("playerA", context.serialize(src.getPlayerA()));
        object.add("playerB", context.serialize(src.getPlayerB()));
        return object;
    }
}
