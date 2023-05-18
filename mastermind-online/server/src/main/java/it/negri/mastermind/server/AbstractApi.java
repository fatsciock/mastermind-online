package it.negri.mastermind.server;

import it.negri.mastermind.common.Mastermind;

public abstract class AbstractApi {
    private final Mastermind storage;
    public AbstractApi(Mastermind storage) {
        this.storage = storage;
    }
    public Mastermind getStorage() {
        return storage;
    }
}
