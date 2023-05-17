package it.negri.mastermind.common.presentation;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.negri.mastermind.common.model.HintLabel;

import java.lang.reflect.Type;

public class ResultLabelDeserializer implements JsonDeserializer<HintLabel> {
    @Override
    public HintLabel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return HintLabel.valueOf(json.getAsString().toUpperCase());
        } catch (IllegalArgumentException | ClassCastException e) {
            throw new JsonParseException("Invalid result label: " + json, e);
        }
    }
}
