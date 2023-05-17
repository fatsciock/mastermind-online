package it.negri.mastermind.common.presentation;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.negri.mastermind.common.model.HintLabel;

import java.lang.reflect.Type;

public class ResultLabelSerializer implements JsonSerializer<HintLabel> {
    @Override
    public JsonElement serialize(HintLabel src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.name().toLowerCase());
    }
}
