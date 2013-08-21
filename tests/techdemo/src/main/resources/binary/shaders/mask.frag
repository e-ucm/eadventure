#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif
varying LOWP vec4 v_color;
varying vec2 v_texCoords;
varying vec2 v_screen_size;
varying vec2 v_scale;
uniform sampler2D u_texture;
uniform sampler2D mask_texture;
void main()
{
  vec2 coords = v_texCoords * v_scale;
  coords.x = fract(coords.x);
  coords.y = fract(coords.y);
  vec4 t_color = v_color * texture2D(mask_texture, coords);
  t_color.r = 1.0;
  t_color.g = 1.0;
  t_color.b = 1.0;
  gl_FragColor = t_color * texture2D(u_texture, v_texCoords );
}