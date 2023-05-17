package it.negri.mastermind.common.presentation;

import com.google.gson.*;
import it.negri.mastermind.common.model.Game;
import it.negri.mastermind.common.model.HintLabel;

import java.lang.reflect.Type;

public class GameSerializer implements JsonSerializer<Game> {
    @Override
    public JsonElement serialize(Game src, Type typeOfSrc, JsonSerializationContext context) {
        var object = new JsonObject();
        object.addProperty("id", src.getId());
        object.add("playerA", context.serialize(src.getPlayerA()));
        object.add("playerB", context.serialize(src.getPlayerB()));
        object.add("winner", context.serialize(src.getWinner()));
        object.addProperty("remainingAttempts", src.getRemainingAttempts());
        object.addProperty("code", src.getCode());

        var attempts = new JsonArray();
        for (var attempt : src.getAttempts()) {
            attempts.add(new JsonPrimitive(attempt));
        }
        object.add("attempts", attempts);

        var hints = new JsonArray();
        for (var hint : src.getHintPerAttempt()) {
            var temp = new JsonObject();
            temp.addProperty(HintLabel.RIGHT_NUMBER_IN_RIGHT_PLACE.toString().toLowerCase(), hint.get(HintLabel.RIGHT_NUMBER_IN_RIGHT_PLACE));
            temp.addProperty(HintLabel.RIGHT_NUMBER_IN_WRONG_PLACE.toString().toLowerCase(), hint.get(HintLabel.RIGHT_NUMBER_IN_WRONG_PLACE));
            hints.add(temp);
        }
        object.add("hintPerAttempt", hints);

        return object;
    }
}
