#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aColor;
layout (location = 2) in vec3 aNormal;

uniform mat4 uMVP;
uniform mat4 uModel;

out vec3 fColor;
out vec3 fNormal;
out vec3 fFragPos;

void main() {
    fColor = aColor;
    mat3 normalMatrix = transpose(inverse(mat3(uModel)));
    fNormal = normalMatrix * aNormal;
    fFragPos = vec3(uModel * vec4(aPos, 1.0));
    gl_Position = uMVP * vec4(aPos, 1.0);
}