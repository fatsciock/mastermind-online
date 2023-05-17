package it.negri.mastermind.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
                .registerTypeAdapter(ResultLabel.class, new ResultLabelSerializer())
                .registerTypeAdapter(ResultLabel.class, new ResultLabelDeserializer())
                .registerTypeAdapter(Role.class, new RoleSerializer())
                .registerTypeAdapter(Role.class, new RoleDeserializer())
                .create();
    }

}
