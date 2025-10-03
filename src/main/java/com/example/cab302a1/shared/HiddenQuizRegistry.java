package com.example.cab302a1.shared;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

public final class HiddenQuizRegistry {

    private static final Preferences PREFS = Preferences.userNodeForPackage(HiddenQuizRegistry.class);

    private static String KEY = "hidden_quiz_ids";

    private static final Set<Integer> HIDDEN = ConcurrentHashMap.newKeySet();

    static {
        load();
    }

    private HiddenQuizRegistry() {}

    public static synchronized void setScopeByUserId(int userId) {
        KEY = "hidden_quiz_ids_u" + userId;
        load();
    }

    public static synchronized void hide(int quizId) {
        HIDDEN.add(quizId);
        save();
    }

    public static synchronized boolean isHidden(int quizId) {
        return HIDDEN.contains(quizId);
    }

    public static synchronized void clear() {
        HIDDEN.clear();
        save();
    }

    public static synchronized void load() {
        HIDDEN.clear();
        String csv = PREFS.get(KEY, "");
        if (csv != null && !csv.isBlank()) {
            for (String tok : csv.split(",")) {
                try { HIDDEN.add(Integer.parseInt(tok.trim())); } catch (NumberFormatException ignored) { }
            }
        }
    }

    private static synchronized void save() {
        String csv = HIDDEN.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        PREFS.put(KEY, csv);
    }
}
