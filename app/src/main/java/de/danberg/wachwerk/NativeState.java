package de.danberg.wachwerk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

public final class NativeState {
    private static final String PREFS = "wachwerk_native";
    private static final String KEY_SETTINGS = "settings_json";
    private static final String KEY_COMPLETED = "completed_one_time";

    private NativeState() {}

    public static void saveSettings(Context context, String json) {
        if (json == null || json.isBlank()) json = "{}";
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putString(KEY_SETTINGS, json).apply();
    }

    public static JSONObject settings(Context context) {
        try { return new JSONObject(context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_SETTINGS, "{}")); }
        catch (Exception ignored) { return new JSONObject(); }
    }

    /** Records only data still used by alarms and the optional morning app block. */
    public static void recordWake(Context context, Intent source) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        try {
            String alarmId = AlarmScheduler.value(source, "alarmId", "alarm");
            long firedAt = source.getLongExtra("firstFiredAt", System.currentTimeMillis());
            String eventId = AlarmScheduler.value(source, AlarmSessionStore.SESSION, alarmId + "-" + firedAt);
            if (eventId.equals(prefs.getString("lastRecordedWake", ""))) return;
            prefs.edit().putString("lastRecordedWake", eventId).apply();
            MorningBlockStore.startForWake(context, eventId);
            if (new JSONArray(AlarmScheduler.value(source, "daysJson", "[]")).length() == 0) appendCompleted(prefs, alarmId);
        } catch (Exception ignored) {
            // The alarm is already stopped; bookkeeping must not restart it.
        }
    }

    private static void appendCompleted(SharedPreferences prefs, String alarmId) {
        try {
            JSONArray old = new JSONArray(prefs.getString(KEY_COMPLETED, "[]"));
            JSONArray updated = new JSONArray();
            boolean found = false;
            for (int i = 0; i < old.length(); i++) {
                String id = old.optString(i);
                if (alarmId.equals(id)) found = true;
                updated.put(id);
            }
            if (!found) updated.put(alarmId);
            prefs.edit().putString(KEY_COMPLETED, updated.toString()).apply();
        } catch (Exception ignored) {}
    }

    public static String getState(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        try {
            return new JSONObject()
                .put("completedOneTimeIds", new JSONArray(prefs.getString(KEY_COMPLETED, "[]")))
                .put("morningBlock", MorningBlockStore.state(context))
                .put("alarmRinging", AlarmSessionStore.current(context) != null)
                .toString();
        } catch (Exception ignored) {
            return "{\"completedOneTimeIds\":[],\"alarmRinging\":false}";
        }
    }
}
