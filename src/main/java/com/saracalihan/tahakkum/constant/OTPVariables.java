package com.saracalihan.tahakkum.constant;

import java.util.Optional;

public enum OTPVariables {
    Id("__id__"),
    Code("__code__"),
    VerifyUrl("__verifyUrl__"),
    Secret("__secret__");
    private final String name;

    private OTPVariables(String s) {
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
