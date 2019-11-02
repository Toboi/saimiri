uniform sampler2D m_ColorMap;
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
    gl_FragColor = color;
}