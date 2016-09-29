#version 300 es
precision mediump float;
out vec4 color;
in vec3 myColor;
void main() {
    //color = vec4(1.0f,0.5f,0.2f,1.0f);
    color = vec4(myColor,1.0f);
}
