package com.emsp.infrastructure.util;

import java.util.regex.Pattern;

public class EMAIDValidator {
    private static final Pattern EMAID_PATTERN =
            Pattern.compile("(?i)^[A-Z]{2}[\\dA-Z]{3}[\\dA-Z]{9}$");

    public static boolean isValid(String emaid) {
        if (emaid == null) return false;
        return EMAID_PATTERN.matcher(emaid).matches();
    }
}