package it.negri.mastermind.server;

import io.javalin.Javalin;
import it.negri.mastermind.common.LocalMastermind;
import it.negri.mastermind.common.Mastermind;
import it.negri.mastermind.common.utils.GsonUtils;
import it.negri.mastermind.server.games.GameController;
import it.negri.mastermind.server.heartbeat.HeartbeatController;
import it.negri.mastermind.server.lobbies.LobbyController;
import it.negri.mastermind.server.players.PlayerController;
import it.negri.mastermind.server.utils.Filters;
import it.negri.mastermind.server.utils.JavalinGsonAdapter;
import it.negri.mastermind.server.utils.Plugins;

public class MastermindService {

    private static final String API_VERSION = "0.1.0";
    public static final String BASE_URL = "/mastermind/v" + API_VERSION;
    private static final int DEFAULT_PORT = 10000;
    private final int port;
    private final Javalin server;

    public MastermindService(int port) {
        this.port = port;
        server = Javalin.create(config -> {
            config.plugins.enableDevLogging();
            config.jsonMapper(new JavalinGsonAdapter(GsonUtils.createGson()));
            config.plugins.register(Plugins.openApiPlugin(API_VERSION, "/doc"));
            config.plugins.register(Plugins.swaggerPlugin("/doc", "/ui"));
            config.plugins.register(Plugins.routeOverviewPlugin("/routes"));
        });

        server.before(path("/*"), Filters.putSingletonInContext(Mastermind.class, new LocalMastermind()));

        GameController.of(path("/games")).registerRoutes(server);
        LobbyController.of(path("/lobbies")).registerRoutes(server);
        PlayerController.of(path("/players")).registerRoutes(server);
        HeartbeatController.of(path("/heartbeat")).registerRoutes(server);
    }

    public void start() {
        server.start(port);
    }

    public void stop() {
        server.stop();
    }

    private static String path(String subPath) {
        return BASE_URL + subPath;
    }

    public static void main(String[] args) {
        new MastermindService(args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT).start();
    }
}
