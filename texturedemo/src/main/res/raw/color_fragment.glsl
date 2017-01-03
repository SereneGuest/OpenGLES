#version 300 es
precision mediump float;
out vec4 color;
uniform vec3 lightColor;
uniform vec3 objectColor;

void main() {
    color = vec4(lightColor * objectColor,1.0f);
}
