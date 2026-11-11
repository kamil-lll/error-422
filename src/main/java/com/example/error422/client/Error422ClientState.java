package com.example.error422.client;

/**
 * Istemci tarafinda paylasilan gecici durumlar.
 */
public final class Error422ClientState {

    public static boolean xray;
    public static boolean anaglyph;
    public static int inventoryGlitchTicks;

    public static boolean headlessPlayer = true;
    public static String versionString = "1.2.1";

    private Error422ClientState() {
    }
}
