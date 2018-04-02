attribute vec4 a_Position;
attribute vec2 a_TexCoord;
varying lowp vec2 v_TexCoord;
void main()
{
    gl_Position = a_Position;
    v_TexCoord =  a_TexCoord;
}
