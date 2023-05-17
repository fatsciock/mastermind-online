package it.negri.mastermind.common.utils;

import com.google.gson.*;
import it.negri.mastermind.common.model.*;
import it.negri.mastermind.common.presentation.*;

public class GsonUtils {
    public static Gson createGson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .registerTypeAdapter(Game.class, new GameSerializer())
                .registerTypeAdapter(Game.class, new GameDeserializer())
                .registerTypeAdapter(Lobby.class, new LobbySerializer())
                .registerTypeAdapter(Lobby.class, new LobbyDeserializer())
                .registerTypeAdapter(Player.class, new PlayerSerializer())
                .registerTypeAdapter(Player.class, new PlayerDeserializer())
                .registerTypeAdapter(HintLabel.class, new HintLabelSerializer())
                .registerTypeAdapter(HintLabel.class, new HintLabelDeserializer())
                .registerTypeAdapter(Role.class, new RoleSerializer())
                .registerTypeAdapter(Role.class, new RoleDeserializer())
                .create();
    }

    public static <T> T getPropertyAs(JsonObject object, String name, Class<T> type, JsonDeserializationContext context) {
        if (object.has(name)) {
            JsonElement value = object.get(name);
            if (value.isJsonNull()) return null;
            return context.deserialize(value, type);
        }
        return null;
    }

}
