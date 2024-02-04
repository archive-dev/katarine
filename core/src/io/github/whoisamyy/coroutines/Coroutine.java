package io.github.whoisamyy.coroutines;

import io.github.whoisamyy.utils.input.Action;

public class Coroutine {
    private Coroutine(){}

    public static Coroutine start(Action action) {
        Thread t = new Thread(action::execute);
        t.start();
        return new Coroutine();
    }

    @Deprecated //TODO
    public static <T> T start() {
        return null;
    }
}
