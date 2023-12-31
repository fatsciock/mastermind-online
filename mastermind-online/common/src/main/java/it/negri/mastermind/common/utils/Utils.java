package it.negri.mastermind.common.utils;

public class Utils {

    private static final int CODE_LENGTH = 4;
    private static final int DEFAULT_ATTEMPTS = 9;
    private static final int HEARTBEAT_INTERVAL = 5000;
    private static final int HEARTBEAT_SKIPPABLE = 2;

    public static boolean validateCode(String codeToGuess) {
        return codeToGuess.length() == CODE_LENGTH && codeToGuess.matches("[0-9]+");
    }

    public static int getCodeLength() {
        return CODE_LENGTH;
    }

    public static int getDefaultAttempts() {
        return DEFAULT_ATTEMPTS;
    }

    public static int getHeartbeatInterval() {
        return HEARTBEAT_INTERVAL;
    }

    public static int getHeartbeatSkippable() {
        return HEARTBEAT_SKIPPABLE;
    }
}
