uniform sampler2D m_ColorMap;
uniform sampler2D m_GlowMap;
uniform sampler2D m_NormalMap;
uniform vec4 m_Color;
varying vec2 texCoord;


void main(){

    vec4 color = vec4(1.0);
    #ifdef HAS_COLORMAP
    color *= texture2D(m_ColorMap, texCoord);
    #endif
    #ifdef HAS_COLOR
    color *= m_Color;
    #endif

    gl_FragData[0] = color;

    #ifdef HAS_NORMALMAP
    gl_FragData[1] = vec4(texture2D(m_NormalMap, texCoord).rgb, color.a);
    #else
    gl_FragData[1] = vec4(0.5, 0.5, 1.0, color.a);
    #endif

    #ifdef HAS_GLOWMAP
    gl_FragData[2] = texture2D(m_GlowMap, texCoord);
    #else
    gl_FragData[2] = vec4(0.0, 0.0, 0.0, color.a);
    #endif
}