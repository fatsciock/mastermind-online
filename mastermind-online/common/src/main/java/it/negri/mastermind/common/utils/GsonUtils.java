package it.negri.mastermind.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {

    public static Gson createGson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .create();
    }

}
