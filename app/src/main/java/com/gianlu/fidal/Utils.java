package com.gianlu.fidal;

import androidx.annotation.NonNull;

public final class Utils {

    private Utils() {
    }

    public static float parsePerformance(@NonNull String val) {
        if (val.contains(":")) {
            String[] split = val.split(":");
            float result = 0f;
            for (int i = 0; i < split.length; i++) {
                if (i == split.length - 1) result += Float.parseFloat(split[i]);
                else result += Integer.parseInt(split[i]) * 60 * (i + 1);
            }

            return result;
        } else {
            return Float.parseFloat(val);
        }
    }
}
