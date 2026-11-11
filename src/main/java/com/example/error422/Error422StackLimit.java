package com.example.error422;

import com.example.error422.config.Error422Config;

import java.util.Random;

/**
 * Oturum basina rastgele maksimum stack limiti: 0-99.
 * 0, creepypasta'daki "esyalari asla toplayamama / stackleyememe" etkisini verir.
 */
public final class Error422StackLimit {

    private static int limit = -1;

    private Error422StackLimit() {
    }

    public static int get() {
        if (limit < 0) {
            Random random = new Random();
            boolean allowZero;
            try {
                allowZero = Error422Config.ALLOW_ZERO_STACK_LIMIT.get();
            } catch (Exception ignored) {
                allowZero = true;
            }
            if (allowZero && random.nextInt(20) == 0) {
                limit = 0;
            } else {
                limit = 1 + random.nextInt(99);
            }
        }
        return limit;
    }
}
