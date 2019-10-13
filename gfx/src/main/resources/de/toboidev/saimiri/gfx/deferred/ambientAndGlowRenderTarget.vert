attribute vec3 inPosition;

attribute vec2 inTexCoord;
varying vec2 texCoord1;

void main(){
    gl_Position = vec4(inPosition*2.0-vec3(1.0,1.0,0),1.0);
    texCoord1 = inTexCoord;
}