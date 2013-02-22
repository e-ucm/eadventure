#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif
varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
void main()
{
  v_color.r += 0.1;
  v_color.g -= 0.5;
  v_color.a -= 0.5;
  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
}