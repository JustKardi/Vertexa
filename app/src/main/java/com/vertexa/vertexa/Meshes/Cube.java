package com.vertexa.vertexa.Meshes;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import com.vertexa.vertexa.Renderer.Material;
import com.vertexa.vertexa.Renderer.Shader;
import com.vertexa.vertexa.Utility.Color;

public class Cube {
    
    public float width, height, depth, x, y, z;

    public float vertices[];

    private int vao, vbo;

    private Color color;

    private Material material;

    public Cube(float width, float height, float depth, float x, float y, float z) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setMaterial(Material material) {
        this.material = material;
        this.color = material.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void generate() {
        if (this.color == null) {
            throw new IllegalStateException("Color must be set before generating vertices.");
        }

        float r = color.getSingleColor('r');
        float g = color.getSingleColor('g');
        float b = color.getSingleColor('b');

        float x2 = x + width;
        float y2 = y + height;
        float z2 = z + depth;

        float[] nF = {0,0,-1}, nB = {0,0,1}, nL = {-1,0,0},
        nR = {1,0,0},  nT = {0,1,0}, nBo = {0,-1,0};

        vertices = new float[] {
            x,  y,  z,  r,g,b,  0,0,-1,   x2, y,  z,  r,g,b,  0,0,-1,   x2, y2, z,  r,g,b,  0,0,-1,
            x,  y,  z,  r,g,b,  0,0,-1,   x2, y2, z,  r,g,b,  0,0,-1,   x,  y2, z,  r,g,b,  0,0,-1,
            x,  y,  z2, r,g,b,  0,0,1,    x2, y,  z2, r,g,b,  0,0,1,    x2, y2, z2, r,g,b,  0,0,1,
            x,  y,  z2, r,g,b,  0,0,1,    x2, y2, z2, r,g,b,  0,0,1,    x,  y2, z2, r,g,b,  0,0,1,
            x,  y,  z,  r,g,b,  -1,0,0,   x,  y2, z,  r,g,b,  -1,0,0,   x,  y2, z2, r,g,b,  -1,0,0,
            x,  y,  z,  r,g,b,  -1,0,0,   x,  y2, z2, r,g,b,  -1,0,0,   x,  y,  z2, r,g,b,  -1,0,0,
            x2, y,  z,  r,g,b,  1,0,0,    x2, y2, z,  r,g,b,  1,0,0,    x2, y2, z2, r,g,b,  1,0,0,
            x2, y,  z,  r,g,b,  1,0,0,    x2, y2, z2, r,g,b,  1,0,0,    x2, y,  z2, r,g,b,  1,0,0,
            x,  y2, z,  r,g,b,  0,1,0,    x,  y2, z2, r,g,b,  0,1,0,    x2, y2, z2, r,g,b,  0,1,0,
            x,  y2, z,  r,g,b,  0,1,0,    x2, y2, z2, r,g,b,  0,1,0,    x2, y2, z,  r,g,b,  0,1,0,
            x,  y,  z,  r,g,b,  0,-1,0,   x2, y,  z,  r,g,b,  0,-1,0,   x2, y,  z2, r,g,b,  0,-1,0,
            x,  y,  z,  r,g,b,  0,-1,0,   x2, y,  z2, r,g,b,  0,-1,0,   x,  y,  z2, r,g,b,  0,-1,0,
        };

        init();
    }

    private void init() {
        java.nio.FloatBuffer buffer = org.lwjgl.BufferUtils.createFloatBuffer(vertices.length);
        buffer.put(vertices).flip();

        vao = org.lwjgl.opengl.GL30.glGenVertexArrays(); 
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);


        int stride = 9 * Float.BYTES;

        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, 3, GL_FLOAT, false, stride, 6 * Float.BYTES);
        glEnableVertexAttribArray(2);
    }



    public void render(Shader shader) {
        if (material != null) {
            shader.setUniform("uRoughness",   material.roughness);
            shader.setUniform("uMetallic",    material.metallic);
            shader.setUniform("uReflectance", material.reflectance);
        }
        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, 36);
        glBindVertexArray(0);
    }
}
