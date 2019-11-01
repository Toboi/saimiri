uniform sampler2D m_Color;
varying vec2 texCoord;

void main(){
    vec4 color = texture2D(m_Color, texCoord);
    gl_FragColor = color;
}