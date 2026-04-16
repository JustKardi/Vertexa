package com.vertexa.vertexa.Renderer;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

import com.vertexa.vertexa.Environment.DirectionalLight;
import com.vertexa.vertexa.Utility.Color;

public class Shader {
    private int shaderProgramID;
    private boolean beingUsed = false;
    private final java.util.Map<String, Integer> uniforms = new java.util.HashMap<>();

    public Shader(String vertexPath, String fragmentPath) {

        String vertexSource = loadResource(vertexPath);
        String fragmentSource = loadResource(fragmentPath);

        int vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);
        checkCompileErrors(vertexID, "VERTEX");

        int fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);
        checkCompileErrors(fragmentID, "FRAGMENT");

        this.shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);
        checkLinkErrors(shaderProgramID);

        glDeleteShader(vertexID);
        glDeleteShader(fragmentID);
    }

    public void use() {
        if (!beingUsed) {
            glUseProgram(shaderProgramID);
            beingUsed = true;
        }
    }

    public void detach() {
        glUseProgram(0);
        beingUsed = false;
    }

    public void uploadMat4f(String varName, Matrix4f mat4) {
        if (!uniforms.containsKey(varName)) {
            try {
                createUniform(varName);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    
        int varLocation = uniforms.get(varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadVec3f(String varName, org.joml.Vector3f vec) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

    public void uploadMat3f(String varName, org.joml.Matrix3f mat3) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        float[] matBuffer = new float[9];
        mat3.get(matBuffer);
        org.lwjgl.opengl.GL20.glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    public void uploadVec4f(String varName, org.joml.Vector4f vec) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        org.lwjgl.opengl.GL20.glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }

    private void checkCompileErrors(int shader, String type) {
        if (glGetShaderi(shader, GL_COMPILE_STATUS) == 0) {
            System.err.println("ERROR: Shader Compilation Failed (" + type + ")\n" + glGetShaderInfoLog(shader));
        }
    }

    private void checkLinkErrors(int program) {
        if (glGetProgrami(program, GL_LINK_STATUS) == 0) {
            System.err.println("ERROR: Shader Linking Failed\n" + glGetProgramInfoLog(program));
        }
    }

    private String loadResource(String filePath) {
        try (java.io.InputStream in = getClass().getResourceAsStream(filePath);
             java.util.Scanner scanner = new java.util.Scanner(in, "UTF-8")) {
            return scanner.useDelimiter("\\A").next();
        } catch (Exception e) {
            throw new RuntimeException("Could not load shader: " + filePath, e);
        }
    }

    /////////////////////////////////////////
    ////////  U N I F O R M S  //////////////
    ////////////////////////////////////////

    public void createUniform(String uniformName) {
        int uniformLocation = glGetUniformLocation(shaderProgramID, uniformName);
        if (uniformLocation < 0) {
            System.out.println("Warning: Uniform [" + uniformName + "] not used or not found.");
        }
        uniforms.put(uniformName, uniformLocation);
    }


    public void createDirectionalLightUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".color");
        createUniform(uniformName + ".direction");
        createUniform(uniformName + ".intensity");
    }

    public void setUniform(String varName, org.joml.Vector3f vec) {
        if (!uniforms.containsKey(varName)) {
            try { createUniform(varName); } catch (Exception e) { e.printStackTrace(); }
        }
        int location = uniforms.get(varName);
        use();
        glUniform3f(location, vec.x, vec.y, vec.z);
    }

    public void setUniform(String varName, float value) {
        if (!uniforms.containsKey(varName)) {
            try { createUniform(varName); } catch (Exception e) { e.printStackTrace(); }
        }
        int location = uniforms.get(varName);
        use();
        org.lwjgl.opengl.GL20.glUniform1f(location, value);
    }

    public void setUniform(String varName, Color color) {
        if (!uniforms.containsKey(varName)) {
            try { createUniform(varName); } catch (Exception e) { e.printStackTrace(); }
        }
        int location = uniforms.get(varName);
        use();
        glUniform3f(location, color.getSingleColor('r'), color.getSingleColor('g'), color.getSingleColor('b'));
    }

    public void setUniform(String uniformName, DirectionalLight dl) {
        setUniform(uniformName + ".color", dl.color);
        setUniform(uniformName + ".direction", dl.direction);
        setUniform(uniformName + ".intensity", dl.intensity);
    }

}
