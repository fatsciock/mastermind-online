package it.negri.mastermind.common.presentation;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.negri.mastermind.common.model.Player;
import it.negri.mastermind.common.model.Role;

import java.lang.reflect.Type;
import java.util.Objects;

import static it.negri.mastermind.common.utils.GsonUtils.getPropertyAs;

public class PlayerDeserializer implements JsonDeserializer<Player> {
    @Override
    public Player deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            var object = json.getAsJsonObject();
            var nickname = getPropertyAs(object, "nickname", String.class, context);
            Role role = getPropertyAs(object, "role", Role.class, context);
            Player result = new Player(nickname);
            switch (Objects.requireNonNull(role)) {
                case ENCODER -> result.setAsEncoder();
                case DECODER -> result.setAsDecoder();
            }
            return result;
        } catch (ClassCastException e) {
            throw new JsonParseException("Invalid player: " + json, e);
        }
    }
}
