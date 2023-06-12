package it.negri.mastermind.common;

import it.negri.mastermind.common.exceptions.MissingException;
import it.negri.mastermind.common.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HeartbeatManager {
    private final Map<String, HeartbeatTask> heartbeatsByNickname = new HashMap<>();
    private final Map<String, Integer> heartbeatsCounterByNickname = new HashMap<>();
    private final Map<String, Integer> lobbyByNickname = new HashMap<>();
    private final LocalMastermind localMastermind;
    private final ExecutorService executorService;

    public HeartbeatManager(LocalMastermind localMastermind) {
        this.localMastermind = localMastermind;
        this.executorService = Executors.newCachedThreadPool();
    }

    public synchronized void registerNewPlayer(final String nick) {
        var heartTask = new HeartbeatTask(this, nick);
        heartbeatsByNickname.put(nick, heartTask);
        heartbeatsCounterByNickname.put(nick, Utils.getHeartbeatSkippable());
        executorService.submit(heartTask);
    }

    public synchronized void deletePlayer(final String nick) {
        heartbeatsByNickname.remove(nick);
        heartbeatsCounterByNickname.remove(nick);
    }

    public synchronized void registerPlayerToLobby(final String nick, final int lobbyId) {
        lobbyByNickname.put(nick, lobbyId);
    }

    public synchronized void deletePlayerFromLobby(final String nick, final int lobbyId) {
        lobbyByNickname.remove(nick);
    }

    public synchronized void heartbeat(final String nick) {
        var actualValue = heartbeatsCounterByNickname.get(nick);
        if(actualValue < Utils.getHeartbeatSkippable()) {
            actualValue++;
            heartbeatsCounterByNickname.put(nick, actualValue);
        }
    }

    private synchronized int getHeartbeatsCountOfPlayer(final String nick) {
        return heartbeatsCounterByNickname.get(nick);
    }

    private synchronized void decrementHeartbeatsCountOfPlayer(final String nick) {
        var actualValue = heartbeatsCounterByNickname.get(nick);
        actualValue--;
        heartbeatsCounterByNickname.put(nick, actualValue);
    }

    private void killPlayer(final String nick) {
        if(heartbeatsByNickname.containsKey(nick)) {
            //Il client viene considerato come non raggiungibile e quindi disconnesso se ancora presente
            if(lobbyByNickname.containsKey(nick)) {
                try {
                    localMastermind.deletePlayerFromLobby(nick, lobbyByNickname.get(nick));
                    System.err.println("HeartbeatManager: PLAYER " + nick + " CANCELLATO DALLA LOBBY");
                } catch (MissingException e) {
                    System.err.println("HeartbeatManager: player " + nick + " non trovato all'interno della lobby...");
                }
            }
            try {
                localMastermind.deletePlayer(nick);
                System.err.println("HeartbeatManager: PLAYER " + nick + " CANCELLATO");
            } catch (MissingException e) {
                System.err.println("HeartbeatManager: player " + nick + " non trovato...");
            }
        }
    }


    private class HeartbeatTask implements Runnable {
        private final HeartbeatManager heartbeatManager;
        private final String nickname;

        public HeartbeatTask(final HeartbeatManager heartbeatManager, final String nickname) {
            this.heartbeatManager = heartbeatManager;
            this.nickname = nickname;
        }

        @Override
        public void run() {
            while (heartbeatManager.getHeartbeatsCountOfPlayer(nickname) > 0) {
                try {
                    Thread.sleep(Utils.getHeartbeatInterval());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                heartbeatManager.decrementHeartbeatsCountOfPlayer(nickname);
            }

            heartbeatManager.killPlayer(nickname);
        }
    }

}
