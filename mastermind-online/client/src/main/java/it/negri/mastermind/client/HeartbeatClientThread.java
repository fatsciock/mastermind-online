package it.negri.mastermind.client;

import it.negri.mastermind.common.exceptions.MissingException;
import it.negri.mastermind.common.exceptions.ServerUnavailableException;
import it.negri.mastermind.common.utils.Utils;

public class HeartbeatClientThread extends Thread {

    private final String nickname;
    private final RemoteMastermind client;

    private int ATTEMPTS_BEFORE_DISCONNECT = 3;

    public HeartbeatClientThread(String nickname, RemoteMastermind client) {
        this.nickname = nickname;
        this.client = client;
    }

    @Override
    public void run() {
        super.run();

        while (true) {
            try {
                client.heartbeat(nickname);
            } catch (MissingException e) {
                System.err.println("Il nikcname non e' più presente sul server, disconnessione in corso...");
                System.exit(1);
            } catch (ServerUnavailableException e) {
                System.err.println("Impossibile connettersi al server...");
                if(ATTEMPTS_BEFORE_DISCONNECT == 0) {
                    System.err.println("Server non raggiungibile, disconnessione in corso...");
                    System.exit(1);
                }
            }
            try {
                Thread.sleep(Utils.getHeartbeatInterval());
                ATTEMPTS_BEFORE_DISCONNECT--;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
