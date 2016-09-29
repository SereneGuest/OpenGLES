#version 300 es
precision mediump float;
out vec4 color;
in vec3 myColor;
in vec2 myTexCoord;
uniform sampler2D ourTexture;
uniform sampler2D ourTexture1;
void main() {
    //color = vec4(1.0f,0.5f,0.2f,1.0f);
    color = mix(texture(ourTexture,myTexCoord),texture(ourTexture1,myTexCoord),0.0);
}
