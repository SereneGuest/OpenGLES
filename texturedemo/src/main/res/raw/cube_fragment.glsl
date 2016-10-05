#version 300 es
precision mediump float;
out vec4 color;
in vec2 myTexCoord;
uniform sampler2D ourTexture;
uniform sampler2D ourTexture1;
void main() {
    color = mix(texture(ourTexture,myTexCoord),texture(ourTexture1,myTexCoord),0.1);
}
