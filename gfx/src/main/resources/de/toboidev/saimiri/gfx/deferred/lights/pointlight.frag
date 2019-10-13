uniform vec2 g_Resolution;

uniform sampler2D m_GDiffuse;
uniform sampler2D m_GNormal;
uniform vec4 m_Color;
uniform float m_Height;

varying vec2 texCoord;

void main(){

    vec2 screenPosition = vec2(gl_FragCoord) / vec2(g_Resolution.x, g_Resolution.y);

    vec3 lightDir = vec3(1.0, 1.0, m_Height) - vec3(texCoord, 0.0);
    lightDir = normalize(lightDir);

    vec3 normal = texture2D(m_GNormal, screenPosition).xyz*2.0-1.0;
    normal = normalize(normal);

    float dist = distance(texCoord, vec2(1.0, 1.0));

    float f = dot(lightDir, normal.xyz);

    gl_FragColor = texture2D(m_GDiffuse, screenPosition)//Diffuse color
    * vec4(m_Color.rgb, 1.0)//Light color
    * clamp(1.0 - sqrt(dist), 0.0, 1.0)//Light distance
    * vec4(f, f, f, 1.0);//Surface normal
}