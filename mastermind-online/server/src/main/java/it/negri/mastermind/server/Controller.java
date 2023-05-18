package it.negri.mastermind.server;

import io.javalin.Javalin;

public interface Controller {
    void registerRoutes(Javalin app);
}
