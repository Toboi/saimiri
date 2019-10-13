uniform sampler2D m_Diffuse;
varying vec2 texCoord;

void main(){
    vec4 color = texture2D(m_Diffuse, texCoord);
    gl_FragColor = color;
}