package com.example.error422.entity;

public enum Error422State {

    STALK("stalk"),
    GLITCH("glitch"),
    HUNT("hunt"),
    FADE("fade");

    private final String id;

    Error422State(String id) {
        this.id = id;
    }

    public String id() {
        return this.id;
    }

    public static Error422State byId(String id) {
        for (Error422State state : values()) {
            if (state.id.equals(id)) {
                return state;
            }
        }
        return STALK;
    }
}
