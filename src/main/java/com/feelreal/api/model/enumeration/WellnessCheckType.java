package com.feelreal.api.model.enumeration;

public enum WellnessCheckType {

    OneToTen,
    CompareToYesterday;

    public static WellnessCheckType fromOrdinal(int ordinal) {
        return WellnessCheckType.values()[ordinal];
    }

}
