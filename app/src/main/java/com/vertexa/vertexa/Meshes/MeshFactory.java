package com.vertexa.vertexa.Meshes;

import com.vertexa.vertexa.Utility.Color;

public class MeshFactory {

    public Mesh generateCube(Color color) {
        float r = color.getSingleColor('r');
        float g = color.getSingleColor('g');
        float b = color.getSingleColor('b');

        float[] vertices = new float[] {
            -0.5f, -0.5f,  0.5f,  r, g, b,  0, 0, 1,
            0.5f, -0.5f,  0.5f,  r, g, b,  0, 0, 1,
            0.5f,  0.5f,  0.5f,  r, g, b,  0, 0, 1,
            -0.5f, -0.5f,  0.5f,  r, g, b,  0, 0, 1,
            0.5f,  0.5f,  0.5f,  r, g, b,  0, 0, 1,
            -0.5f,  0.5f,  0.5f,  r, g, b,  0, 0, 1,
            -0.5f, -0.5f, -0.5f,  r, g, b,  0, 0, -1,
            -0.5f,  0.5f, -0.5f,  r, g, b,  0, 0, -1,
            0.5f,  0.5f, -0.5f,  r, g, b,  0, 0, -1,
            -0.5f, -0.5f, -0.5f,  r, g, b,  0, 0, -1,
            0.5f,  0.5f, -0.5f,  r, g, b,  0, 0, -1,
            0.5f, -0.5f, -0.5f,  r, g, b,  0, 0, -1,
            -0.5f,  0.5f,  0.5f,  r, g, b, -1, 0, 0,
            -0.5f,  0.5f, -0.5f,  r, g, b, -1, 0, 0,
            -0.5f, -0.5f, -0.5f,  r, g, b, -1, 0, 0,
            -0.5f,  0.5f,  0.5f,  r, g, b, -1, 0, 0,
            -0.5f, -0.5f, -0.5f,  r, g, b, -1, 0, 0,
            -0.5f, -0.5f,  0.5f,  r, g, b, -1, 0, 0,
            0.5f,  0.5f,  0.5f,  r, g, b,  1, 0, 0,
            0.5f, -0.5f, -0.5f,  r, g, b,  1, 0, 0,
            0.5f,  0.5f, -0.5f,  r, g, b,  1, 0, 0,
            0.5f,  0.5f,  0.5f,  r, g, b,  1, 0, 0,
            0.5f, -0.5f,  0.5f,  r, g, b,  1, 0, 0,
            0.5f, -0.5f, -0.5f,  r, g, b,  1, 0, 0,
            -0.5f,  0.5f, -0.5f,  r, g, b,  0, 1, 0,
            -0.5f,  0.5f,  0.5f,  r, g, b,  0, 1, 0,
            0.5f,  0.5f,  0.5f,  r, g, b,  0, 1, 0,
            -0.5f,  0.5f, -0.5f,  r, g, b,  0, 1, 0,
            0.5f,  0.5f,  0.5f,  r, g, b,  0, 1, 0,
            0.5f,  0.5f, -0.5f,  r, g, b,  0, 1, 0,
            -0.5f, -0.5f, -0.5f,  r, g, b,  0, -1, 0,
            0.5f, -0.5f,  0.5f,  r, g, b,  0, -1, 0,
            -0.5f, -0.5f,  0.5f,  r, g, b,  0, -1, 0,
            -0.5f, -0.5f, -0.5f,  r, g, b,  0, -1, 0,
            0.5f, -0.5f, -0.5f,  r, g, b,  0, -1, 0,
            0.5f, -0.5f,  0.5f,  r, g, b,  0, -1, 0
        };

        return new Mesh(vertices);
    }

}
