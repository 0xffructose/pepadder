package pepgames.plugins.pepitemadder.mechanics.types;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public enum Conditions {

    ON_INTERACT("RIGHT","LEFT");

    @Getter
    final List<String> conditions;

    Conditions(String... conditions) {
        this.conditions = Arrays.asList(conditions);
    }

    public static boolean isConditionalEvent(String eventType) {
        for (Conditions condition : Conditions.values()) {
            if (condition.name().equalsIgnoreCase(eventType)) return true;
        }
        return false;
    }

    public static boolean isCondition(String eventType , String condition) {
        if (Conditions.valueOf(eventType.toUpperCase()) == null) return false;
        return Conditions.valueOf(eventType.toUpperCase()).getConditions().contains(condition.toUpperCase());
    }

    public static String getCondition(String eventType , String wantedCondition) {
        if (Conditions.valueOf(eventType.toUpperCase()) == null) return null;
        for (String condition : Conditions.valueOf(eventType.toUpperCase()).getConditions()) {
            if (condition.equalsIgnoreCase(wantedCondition)) return condition;
        }
        return null;
    }

}
