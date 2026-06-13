#version 330 core

in vec3 vWorldPos;

out vec4 FragColor;

uniform vec3 uColor;
uniform vec3 uCameraPos;

void main() {

    float dist = distance(vWorldPos, uCameraPos);

    float alpha = exp(-dist * 0.05);

    FragColor = vec4(uColor, alpha);
}