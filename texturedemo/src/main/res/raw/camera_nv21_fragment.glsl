varying lowp vec2 v_TexCoord;
uniform sampler2D SamplerY;
uniform sampler2D SamplerU;

void main()
{
    float r, g, b, y, u, v;

    y = texture2D(SamplerY,v_TexCoord).r;
    v = texture2D(SamplerU,v_TexCoord).a -0.5 ;
    u = texture2D(SamplerU,v_TexCoord).r -0.5 ;

    r = y + 1.13983*v;
    g = y - 0.39465*u - 0.58060*v;
    b = y + 2.03211*u;

    gl_FragColor = vec4(r, g, b, 1.0);

 }
