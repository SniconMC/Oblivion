package com.github.sniconmc.oblivion.config;

import net.minestom.server.coordinate.Pos;

public class OblivionPosition {
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;
    private boolean look_at_player;

    public Double getX() {
        return x;
    }

    public boolean shouldLookAtPlayer() {
        return look_at_player;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public Double getZ() {
        return z;
    }

    public Double getY() {
        return y;
    }

    public Pos getPosition() {
        return new Pos(x, y, z);
    }

    public Pos getPositionWithPitchAndYaw() {
        return new Pos(x, y, z, yaw, pitch);
    }
}
