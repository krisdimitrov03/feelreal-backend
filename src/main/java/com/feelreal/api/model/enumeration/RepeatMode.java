package com.feelreal.api.model.enumeration;

public enum RepeatMode {

    ONCE,
    DAILY,
    WEEKLY,
    MONTHLY,
    UNKNOWN;

    public static RepeatMode fromInt(int value) {
        return switch (value) {
            case 0 -> ONCE;
            case 1 -> DAILY;
            case 2 -> WEEKLY;
            case 3 -> MONTHLY;
            default -> UNKNOWN;
        };
    }

}
