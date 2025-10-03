package com.example.cab302a1.shared;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class HiddenQuizRegistry {
    private static final Set<Integer> HIDDEN = ConcurrentHashMap.newKeySet();

    private HiddenQuizRegistry() {}

    public static void hide(int quizId) {
        HIDDEN.add(quizId);
    }

    public static boolean isHidden(int quizId) {
        return HIDDEN.contains(quizId);
    }

    public static void unhide(int quizId) {
        HIDDEN.remove(quizId);
    }

    public static void clear() {
        HIDDEN.clear();
    }

    public static Set<Integer> snapshot() {
        return Collections.unmodifiableSet(HIDDEN);
    }
}
