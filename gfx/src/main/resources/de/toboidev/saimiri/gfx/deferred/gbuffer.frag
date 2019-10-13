uniform sampler2D m_Diffuse;
uniform sampler2D m_Glow;
uniform sampler2D m_Normal;
varying vec2 texCoord;


void main(){

    vec4 color = texture2D(m_Diffuse, texCoord);
    gl_FragData[0] = color;

    #ifdef HAS_NORMALMAP
    gl_FragData[1] = vec4(texture2D(m_Normal, texCoord).rgb, color.a);
    #else
    gl_FragData[1] = vec4(0.5, 0.5, 1.0, color.a);
    #endif

    #ifdef HAS_GLOWMAP
    gl_FragData[2] = texture2D(m_Glow, texCoord);
    #else
    gl_FragData[2] = vec4(0.0, 0.0, 0.0, color.a);
    #endif
}