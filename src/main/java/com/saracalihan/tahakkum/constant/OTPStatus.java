package com.saracalihan.tahakkum.constant;

import java.util.Optional;

public enum OTPStatus {
    Active("Active"),
    Success("Success"),
    Fail("Fail"),
    Expired("Expired");
    private final String name;

    private OTPStatus(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }

    public static Optional<Roles> fromString(String v) {
        try {
            return Optional.ofNullable(Roles.valueOf(v));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}