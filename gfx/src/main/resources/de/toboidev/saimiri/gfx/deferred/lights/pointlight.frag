uniform vec2 g_Resolution;

uniform sampler2D m_GDiffuse;
uniform sampler2D m_GNormal;
uniform vec4 m_Color;
uniform float m_Height;

varying vec2 texCoord;

void main(){

    vec2 screenPosition = vec2(gl_FragCoord) / vec2(g_Resolution.x, g_Resolution.y);

    vec3 lightDir = normalize(vec3(-texCoord, m_Height));

    vec3 normal = normalize(texture2D(m_GNormal, screenPosition).xyz*2.0-1.0);

    float dist = length(texCoord);

    float f = dot(lightDir, normal);

    gl_FragColor = texture2D(m_GDiffuse, screenPosition)//Diffuse color
    * vec4(m_Color.rgb * f, 1.0)//Light color and surface normal
    * clamp(1.0 - sqrt(dist), 0.0, 1.0);//Light distance
}