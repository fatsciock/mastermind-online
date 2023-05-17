package it.negri.mastermind.common.presentation;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.negri.mastermind.common.model.Lobby;
import it.negri.mastermind.common.model.Player;

import java.lang.reflect.Type;

import static it.negri.mastermind.common.utils.GsonUtils.getPropertyAs;

public class LobbyDeserializer implements JsonDeserializer<Lobby> {
    @Override
    public Lobby deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            var object = json.getAsJsonObject();
            var id = getPropertyAs(object, "id", Integer.class, context);
            Player playerA = getPropertyAs(object, "playerA", Player.class, context);
            Player playerB = getPropertyAs(object, "playerB", Player.class, context);
            return new Lobby(id, playerA, playerB);
        } catch (ClassCastException e) {
            throw new JsonParseException("Invalid lobby: " + json, e);
        }
    }
}
