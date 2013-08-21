attribute vec4 a_position;
attribute vec4 a_offset;
attribute vec4 a_color;
attribute vec2 a_texCoord0;
attribute vec4 a_sizes;
uniform mat4 u_projTrans;
varying vec4 v_color;
varying vec2 v_texCoords;
varying vec2 v_scale;

void main()
{
   v_color = a_color;
   v_texCoords = a_texCoord0;
   v_scale = vec2( a_sizes.x / a_sizes.z, a_sizes.y / a_sizes.w );
   gl_Position =  u_projTrans * ( a_position - a_offset );
}