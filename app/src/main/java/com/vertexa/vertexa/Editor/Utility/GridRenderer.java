package com.vertexa.vertexa.Editor.Utility;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import com.vertexa.vertexa.Cameras.ViewportCamera;
import com.vertexa.vertexa.Renderer.Shader;

public class GridRenderer {

    private int vao;
    private int vbo;
    private int vertexCount;

    private Shader gridShader;

    public GridRenderer() {
        
        float size = 100;
        int lines = 201;

        gridShader = new Shader("/shaders/grid/grid.vert", "/shaders/grid/grid.frag");

        vertexCount = lines * 4;

        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertexCount * 3);

        for (int i = -100; i <= 100; i++) {
            buffer.put(i).put(0).put(-size);
            buffer.put(i).put(0).put(size);

            buffer.put(-size).put(0).put(i);
            buffer.put(size).put(0).put(i);
        }

        buffer.flip();

        vao = glGenVertexArrays();
        vbo = glGenBuffers();

        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glBindVertexArray(0);

    }

    public void render(ViewportCamera camera, float width, float height) {

        gridShader.use();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        gridShader.uploadVec3f("uColor", new Vector3f(0.35f, 0.0f, 0.55f));
        gridShader.uploadVec3f("uCameraPos", camera.position);

        Matrix4f projection = new Matrix4f()
            .perspective(
                (float)Math.toRadians(45.0f),
                width / height,
                0.1f,
                1000.0f
            );
            

        Matrix4f view = camera.getMatrix();

        Matrix4f vp = new Matrix4f();
        projection.mul(view, vp);

        gridShader.uploadMat4f("uVP", vp);

        glBindVertexArray(vao);
        glDrawArrays(GL_LINES, 0, vertexCount);
        glBindVertexArray(0);

        glDisable(GL_BLEND);

        gridShader.detach();
    }

}
