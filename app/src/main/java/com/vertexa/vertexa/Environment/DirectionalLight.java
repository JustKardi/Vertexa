package com.vertexa.vertexa.Environment;

import org.joml.Vector3f;

public class DirectionalLight {
    public Vector3f direction;
    public Vector3f color;
    public float intensity; // Change this to float

    public DirectionalLight(Vector3f direction, Vector3f color, float intensity) {
        this.direction = direction;
        this.color = color;
        this.intensity = intensity;
    }
}

