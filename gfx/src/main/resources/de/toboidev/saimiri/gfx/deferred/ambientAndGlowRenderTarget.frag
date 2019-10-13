uniform sampler2D m_GDiffuse;
uniform sampler2D m_GGlow;
uniform vec4 m_Ambient;
varying vec2 texCoord1;

void main(){
    gl_FragColor = texture2D(m_GDiffuse, texCoord1) * m_Ambient + texture2D(m_GGlow, texCoord1);
}