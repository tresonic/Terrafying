attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;
uniform mat4 u_projTrans;
varying vec4 v_color;
varying vec2 v_texCoords;

#define NUM_LIGHTS 10
#define PER_LIGHT 3
uniform float lights[NUM_LIGHTS * PER_LIGHT];

void main() {
//	vec4 mult = vec4(1, 1, 1, 1);
//	for(int i=0; i<NUM_LIGHTS*PER_LIGHT; i += PER_LIGHT) {
//		mult.xyz *= length(a_position - vec4(lights[i], lights[i+1], 0, 0));
//	}
    v_color = a_color;
    v_texCoords = a_texCoord0;
    gl_Position = u_projTrans * a_position;
}
