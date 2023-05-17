package it.negri.mastermind.common.presentation;

import com.google.gson.*;
import it.negri.mastermind.common.model.Game;
import it.negri.mastermind.common.model.HintLabel;
import it.negri.mastermind.common.model.Player;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.negri.mastermind.common.utils.GsonUtils.getPropertyAs;

public class GameDeserializer implements JsonDeserializer<Game> {
    @Override
    public Game deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            var object = json.getAsJsonObject();
            int id = getPropertyAs(object, "id", Integer.class, context);
            Player playerA = getPropertyAs(object, "playerA", Player.class, context);
            Player playerB = getPropertyAs(object, "playerB", Player.class, context);
            Player winner = getPropertyAs(object, "winner", Player.class, context);
            int remainingAttempts = getPropertyAs(object, "remainingAttempts", Integer.class, context);
            String code = getPropertyAs(object, "code", String.class, context);

            var attemptsArray = object.getAsJsonArray("attempts");
            List<String> attempts = new ArrayList<>();
            for (var attempt : attemptsArray) {
                if (attempt.isJsonNull()) continue;
                attempts.add(attempt.getAsString());
            }

            var hintPerAttemptArray = object.getAsJsonArray("hintPerAttempt");
            List<Map<HintLabel, Integer>> hintPerAttempt = new ArrayList<>();
            for (var hint : hintPerAttemptArray) {
                if (hint.isJsonNull()) continue;
                var tempMap = new HashMap<HintLabel, Integer>();
                var hintObject = hint.getAsJsonObject();
                tempMap.put(HintLabel.RIGHT_NUMBER_IN_RIGHT_PLACE,
                        getPropertyAs(hintObject, HintLabel.RIGHT_NUMBER_IN_RIGHT_PLACE.toString().toLowerCase(), Integer.class, context));
                tempMap.put(HintLabel.RIGHT_NUMBER_IN_WRONG_PLACE,
                        getPropertyAs(hintObject, HintLabel.RIGHT_NUMBER_IN_WRONG_PLACE.toString().toLowerCase(), Integer.class, context));
                hintPerAttempt.add(tempMap);
            }

            return new Game(id, playerA, playerB, winner, remainingAttempts, attempts, hintPerAttempt, code);
        }
        catch (ClassCastException e) {
            throw new JsonParseException("Invalid lobby: " + json, e);
        }
    }
}
