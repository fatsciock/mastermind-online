package it.negri.mastermind.server;

public class MastermindService {

    private static final String API_VERSION = "0.1.0";

    public static final String BASE_URL = "/mastermind/v" + API_VERSION;

    public MastermindService(int port) {

    }

    public void start() {

    }

    public void stop() {

    }

    private static String path(String subPath) {
        return BASE_URL + subPath;
    }

    public static void main(String[] args) {

    }
}
