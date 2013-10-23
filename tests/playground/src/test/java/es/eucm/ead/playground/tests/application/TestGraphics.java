/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.eucm.ead.playground.tests.application;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLCommon;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class TestGraphics implements Graphics {

	private GL gl;

	private int width;

	private int height;

	public TestGraphics(int width, int height) {
		gl = new GL();
		this.width = width;
		this.height = height;
	}

	@Override
	public boolean isGL11Available() {
		return true;
	}

	@Override
	public boolean isGL20Available() {
		return true;
	}

	@Override
	public GLCommon getGLCommon() {
		return gl;
	}

	@Override
	public GL10 getGL10() {
		return gl;
	}

	@Override
	public GL11 getGL11() {
		return gl;
	}

	@Override
	public GL20 getGL20() {
		return gl;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public float getDeltaTime() {
		return 1f / 33f;
	}

	@Override
	public float getRawDeltaTime() {
		return 1f / 33f;
	}

	@Override
	public int getFramesPerSecond() {
		return 60;
	}

	@Override
	public GraphicsType getType() {
		return GraphicsType.LWJGL;
	}

	@Override
	public float getPpiX() {
		return 1;
	}

	@Override
	public float getPpiY() {
		return 1;
	}

	@Override
	public float getPpcX() {
		return 1;
	}

	@Override
	public float getPpcY() {
		return 1;
	}

	@Override
	public float getDensity() {
		return 1;
	}

	@Override
	public boolean supportsDisplayModeChange() {
		return false;
	}

	@Override
	public DisplayMode[] getDisplayModes() {
		return new DisplayMode[0];
	}

	@Override
	public DisplayMode getDesktopDisplayMode() {
		return null;
	}

	@Override
	public boolean setDisplayMode(DisplayMode displayMode) {
		return false;
	}

	@Override
	public boolean setDisplayMode(int width, int height, boolean fullscreen) {
		return false;
	}

	@Override
	public void setTitle(String title) {
	}

	@Override
	public void setVSync(boolean vsync) {
	}

	@Override
	public BufferFormat getBufferFormat() {
		return null;
	}

	@Override
	public boolean supportsExtension(String extension) {
		return false;
	}

	@Override
	public void setContinuousRendering(boolean isContinuous) {
	}

	@Override
	public boolean isContinuousRendering() {
		return false;
	}

	@Override
	public void requestRendering() {
	}

	@Override
	public boolean isFullscreen() {
		return false;
	}

	public static class GL implements GLCommon, GL10, GL11, GL20 {

		@Override
		public void glClipPlanef(int plane, float[] equation, int offset) {
		}

		@Override
		public void glClipPlanef(int plane, FloatBuffer equation) {
		}

		@Override
		public void glGetClipPlanef(int pname, float[] eqn, int offset) {
		}

		@Override
		public void glGetClipPlanef(int pname, FloatBuffer eqn) {
		}

		@Override
		public void glGetFloatv(int pname, float[] params, int offset) {
		}

		@Override
		public void glGetFloatv(int pname, FloatBuffer params) {
		}

		@Override
		public void glGetFramebufferAttachmentParameteriv(int target,
				int attachment, int pname, IntBuffer params) {
		}

		@Override
		public void glGetProgramiv(int program, int pname, IntBuffer params) {
			if (pname == GL20.GL_LINK_STATUS) {
				params.put(1);
			}
		}

		@Override
		public String glGetProgramInfoLog(int program) {
			return "";
		}

		@Override
		public void glGetRenderbufferParameteriv(int target, int pname,
				IntBuffer params) {
		}

		@Override
		public void glGetShaderiv(int shader, int pname, IntBuffer params) {
			if (pname == GL20.GL_COMPILE_STATUS) {
				params.put(1);
			}
		}

		@Override
		public String glGetShaderInfoLog(int shader) {
			return "";
		}

		@Override
		public void glGetShaderPrecisionFormat(int shadertype,
				int precisiontype, IntBuffer range, IntBuffer precision) {
		}

		@Override
		public void glGetLightfv(int light, int pname, float[] params,
				int offset) {
		}

		@Override
		public void glGetLightfv(int light, int pname, FloatBuffer params) {
		}

		@Override
		public void glGetMaterialfv(int face, int pname, float[] params,
				int offset) {
		}

		@Override
		public void glGetMaterialfv(int face, int pname, FloatBuffer params) {
		}

		@Override
		public void glGetTexParameterfv(int target, int pname, float[] params,
				int offset) {
		}

		@Override
		public void glGetTexParameterfv(int target, int pname,
				FloatBuffer params) {
		}

		@Override
		public void glPointParameterf(int pname, float param) {
		}

		@Override
		public void glPointParameterfv(int pname, float[] params, int offset) {
		}

		@Override
		public void glPointParameterfv(int pname, FloatBuffer params) {
		}

		@Override
		public void glTexParameterfv(int target, int pname, float[] params,
				int offset) {
		}

		@Override
		public void glTexParameterfv(int target, int pname, FloatBuffer params) {
		}

		@Override
		public void glAttachShader(int program, int shader) {
		}

		@Override
		public void glBindAttribLocation(int program, int index, String name) {
		}

		@Override
		public void glBindBuffer(int target, int buffer) {
		}

		@Override
		public void glBindFramebuffer(int target, int framebuffer) {
		}

		@Override
		public void glBindRenderbuffer(int target, int renderbuffer) {
		}

		@Override
		public void glBlendColor(float red, float green, float blue, float alpha) {
		}

		@Override
		public void glBlendEquation(int mode) {
		}

		@Override
		public void glBlendEquationSeparate(int modeRGB, int modeAlpha) {
		}

		@Override
		public void glBlendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha,
				int dstAlpha) {
		}

		@Override
		public void glBufferData(int target, int size, Buffer data, int usage) {
		}

		@Override
		public void glBufferSubData(int target, int offset, int size,
				Buffer data) {
		}

		@Override
		public int glCheckFramebufferStatus(int target) {
			return 0;
		}

		@Override
		public void glCompileShader(int shader) {
		}

		@Override
		public int glCreateProgram() {
			return 1;
		}

		@Override
		public int glCreateShader(int type) {
			return 1;
		}

		@Override
		public void glColor4ub(byte red, byte green, byte blue, byte alpha) {
		}

		@Override
		public void glDeleteBuffers(int n, int[] buffers, int offset) {
		}

		@Override
		public void glDeleteBuffers(int n, IntBuffer buffers) {
		}

		@Override
		public void glDeleteFramebuffers(int n, IntBuffer framebuffers) {
		}

		@Override
		public void glDeleteProgram(int program) {
		}

		@Override
		public void glDeleteRenderbuffers(int n, IntBuffer renderbuffers) {
		}

		@Override
		public void glDeleteShader(int shader) {
		}

		@Override
		public void glDetachShader(int program, int shader) {
		}

		@Override
		public void glDisableVertexAttribArray(int index) {
		}

		@Override
		public void glGetBooleanv(int pname, boolean[] params, int offset) {
		}

		@Override
		public void glGetBooleanv(int pname, IntBuffer params) {
		}

		@Override
		public void glGetBufferParameteriv(int target, int pname, int[] params,
				int offset) {
		}

		@Override
		public void glGetBufferParameteriv(int target, int pname,
				IntBuffer params) {
		}

		@Override
		public void glGenBuffers(int n, int[] buffers, int offset) {
		}

		@Override
		public void glGenBuffers(int n, IntBuffer buffers) {
		}

		@Override
		public void glGenerateMipmap(int target) {
		}

		@Override
		public void glGenFramebuffers(int n, IntBuffer framebuffers) {
		}

		@Override
		public void glGenRenderbuffers(int n, IntBuffer renderbuffers) {
		}

		@Override
		public String glGetActiveAttrib(int program, int index, IntBuffer size,
				Buffer type) {
			return null;
		}

		@Override
		public String glGetActiveUniform(int program, int index,
				IntBuffer size, Buffer type) {
			return null;
		}

		@Override
		public void glGetAttachedShaders(int program, int maxcount,
				Buffer count, IntBuffer shaders) {
		}

		@Override
		public int glGetAttribLocation(int program, String name) {
			return 0;
		}

		@Override
		public void glGetBooleanv(int pname, Buffer params) {
		}

		@Override
		public void glGetPointerv(int pname, Buffer[] params) {
		}

		@Override
		public void glGetTexEnviv(int env, int pname, int[] params, int offset) {
		}

		@Override
		public void glGetTexEnviv(int env, int pname, IntBuffer params) {
		}

		@Override
		public void glGetTexParameteriv(int target, int pname, int[] params,
				int offset) {
		}

		@Override
		public void glGetTexParameteriv(int target, int pname, IntBuffer params) {
		}

		@Override
		public void glGetUniformfv(int program, int location, FloatBuffer params) {
		}

		@Override
		public void glGetUniformiv(int program, int location, IntBuffer params) {
		}

		@Override
		public int glGetUniformLocation(int program, String name) {
			return 0;
		}

		@Override
		public void glGetVertexAttribfv(int index, int pname, FloatBuffer params) {
		}

		@Override
		public void glGetVertexAttribiv(int index, int pname, IntBuffer params) {
		}

		@Override
		public void glGetVertexAttribPointerv(int index, int pname,
				Buffer pointer) {
		}

		@Override
		public boolean glIsBuffer(int buffer) {
			return false;
		}

		@Override
		public boolean glIsEnabled(int cap) {
			return false;
		}

		@Override
		public boolean glIsFramebuffer(int framebuffer) {
			return false;
		}

		@Override
		public boolean glIsProgram(int program) {
			return false;
		}

		@Override
		public boolean glIsRenderbuffer(int renderbuffer) {
			return false;
		}

		@Override
		public boolean glIsShader(int shader) {
			return false;
		}

		@Override
		public boolean glIsTexture(int texture) {
			return false;
		}

		@Override
		public void glLinkProgram(int program) {
		}

		@Override
		public void glReleaseShaderCompiler() {
		}

		@Override
		public void glRenderbufferStorage(int target, int internalformat,
				int width, int height) {
		}

		@Override
		public void glTexEnvi(int target, int pname, int param) {
		}

		@Override
		public void glTexEnviv(int target, int pname, int[] params, int offset) {
		}

		@Override
		public void glTexEnviv(int target, int pname, IntBuffer params) {
		}

		@Override
		public void glTexParameteri(int target, int pname, int param) {
		}

		@Override
		public void glTexParameteriv(int target, int pname, int[] params,
				int offset) {
		}

		@Override
		public void glTexParameteriv(int target, int pname, IntBuffer params) {
		}

		@Override
		public void glUniform1f(int location, float x) {
		}

		@Override
		public void glUniform1fv(int location, int count, FloatBuffer v) {
		}

		@Override
		public void glUniform1i(int location, int x) {
		}

		@Override
		public void glUniform1iv(int location, int count, IntBuffer v) {
		}

		@Override
		public void glUniform2f(int location, float x, float y) {
		}

		@Override
		public void glUniform2fv(int location, int count, FloatBuffer v) {
		}

		@Override
		public void glUniform2i(int location, int x, int y) {
		}

		@Override
		public void glUniform2iv(int location, int count, IntBuffer v) {
		}

		@Override
		public void glUniform3f(int location, float x, float y, float z) {
		}

		@Override
		public void glUniform3fv(int location, int count, FloatBuffer v) {
		}

		@Override
		public void glUniform3i(int location, int x, int y, int z) {
		}

		@Override
		public void glUniform3iv(int location, int count, IntBuffer v) {
		}

		@Override
		public void glUniform4f(int location, float x, float y, float z, float w) {
		}

		@Override
		public void glUniform4fv(int location, int count, FloatBuffer v) {
		}

		@Override
		public void glUniform4i(int location, int x, int y, int z, int w) {
		}

		@Override
		public void glUniform4iv(int location, int count, IntBuffer v) {
		}

		@Override
		public void glUniformMatrix2fv(int location, int count,
				boolean transpose, FloatBuffer value) {
		}

		@Override
		public void glUniformMatrix3fv(int location, int count,
				boolean transpose, FloatBuffer value) {
		}

		@Override
		public void glUniformMatrix4fv(int location, int count,
				boolean transpose, FloatBuffer value) {
		}

		@Override
		public void glUseProgram(int program) {
		}

		@Override
		public void glValidateProgram(int program) {
		}

		@Override
		public void glVertexAttrib1f(int indx, float x) {
		}

		@Override
		public void glVertexAttrib1fv(int indx, FloatBuffer values) {
		}

		@Override
		public void glVertexAttrib2f(int indx, float x, float y) {
		}

		@Override
		public void glVertexAttrib2fv(int indx, FloatBuffer values) {
		}

		@Override
		public void glVertexAttrib3f(int indx, float x, float y, float z) {
		}

		@Override
		public void glVertexAttrib3fv(int indx, FloatBuffer values) {
		}

		@Override
		public void glVertexAttrib4f(int indx, float x, float y, float z,
				float w) {
		}

		@Override
		public void glVertexAttrib4fv(int indx, FloatBuffer values) {
		}

		@Override
		public void glVertexAttribPointer(int indx, int size, int type,
				boolean normalized, int stride, Buffer ptr) {
		}

		@Override
		public void glVertexAttribPointer(int indx, int size, int type,
				boolean normalized, int stride, int ptr) {
		}

		@Override
		public void glPointSizePointerOES(int type, int stride, Buffer pointer) {
		}

		@Override
		public void glVertexPointer(int size, int type, int stride, int pointer) {
		}

		@Override
		public void glColorPointer(int size, int type, int stride, int pointer) {
		}

		@Override
		public void glNormalPointer(int type, int stride, int pointer) {
		}

		@Override
		public void glTexCoordPointer(int size, int type, int stride,
				int pointer) {
		}

		@Override
		public void glDrawElements(int mode, int count, int type, int indices) {
		}

		@Override
		public void glEnableVertexAttribArray(int index) {
		}

		@Override
		public void glFramebufferRenderbuffer(int target, int attachment,
				int renderbuffertarget, int renderbuffer) {
		}

		@Override
		public void glFramebufferTexture2D(int target, int attachment,
				int textarget, int texture, int level) {
		}

		@Override
		public void glAlphaFunc(int func, float ref) {
		}

		@Override
		public void glClientActiveTexture(int texture) {
		}

		@Override
		public void glColor4f(float red, float green, float blue, float alpha) {
		}

		@Override
		public void glColorPointer(int size, int type, int stride,
				Buffer pointer) {
		}

		@Override
		public void glDeleteTextures(int n, int[] textures, int offset) {
		}

		@Override
		public void glDisableClientState(int array) {
		}

		@Override
		public void glEnableClientState(int array) {
		}

		@Override
		public void glFogf(int pname, float param) {
		}

		@Override
		public void glFogfv(int pname, float[] params, int offset) {
		}

		@Override
		public void glFogfv(int pname, FloatBuffer params) {
		}

		@Override
		public void glFrustumf(float left, float right, float bottom,
				float top, float zNear, float zFar) {
		}

		@Override
		public void glGenTextures(int n, int[] textures, int offset) {
		}

		@Override
		public void glGetIntegerv(int pname, int[] params, int offset) {
		}

		@Override
		public void glLightModelf(int pname, float param) {
		}

		@Override
		public void glLightModelfv(int pname, float[] params, int offset) {
		}

		@Override
		public void glLightModelfv(int pname, FloatBuffer params) {
		}

		@Override
		public void glLightf(int light, int pname, float param) {
		}

		@Override
		public void glLightfv(int light, int pname, float[] params, int offset) {
		}

		@Override
		public void glLightfv(int light, int pname, FloatBuffer params) {
		}

		@Override
		public void glLoadIdentity() {
		}

		@Override
		public void glLoadMatrixf(float[] m, int offset) {
		}

		@Override
		public void glLoadMatrixf(FloatBuffer m) {
		}

		@Override
		public void glLogicOp(int opcode) {
		}

		@Override
		public void glMaterialf(int face, int pname, float param) {
		}

		@Override
		public void glMaterialfv(int face, int pname, float[] params, int offset) {
		}

		@Override
		public void glMaterialfv(int face, int pname, FloatBuffer params) {
		}

		@Override
		public void glMatrixMode(int mode) {
		}

		@Override
		public void glMultMatrixf(float[] m, int offset) {
		}

		@Override
		public void glMultMatrixf(FloatBuffer m) {
		}

		@Override
		public void glMultiTexCoord4f(int target, float s, float t, float r,
				float q) {
		}

		@Override
		public void glNormal3f(float nx, float ny, float nz) {
		}

		@Override
		public void glNormalPointer(int type, int stride, Buffer pointer) {
		}

		@Override
		public void glOrthof(float left, float right, float bottom, float top,
				float zNear, float zFar) {
		}

		@Override
		public void glPointSize(float size) {
		}

		@Override
		public void glPopMatrix() {
		}

		@Override
		public void glPushMatrix() {
		}

		@Override
		public void glRotatef(float angle, float x, float y, float z) {
		}

		@Override
		public void glSampleCoverage(float value, boolean invert) {
		}

		@Override
		public void glShaderBinary(int n, IntBuffer shaders, int binaryformat,
				Buffer binary, int length) {
		}

		@Override
		public void glShaderSource(int shader, String string) {
		}

		@Override
		public void glStencilFuncSeparate(int face, int func, int ref, int mask) {
		}

		@Override
		public void glStencilMaskSeparate(int face, int mask) {
		}

		@Override
		public void glStencilOpSeparate(int face, int fail, int zfail, int zpass) {
		}

		@Override
		public void glScalef(float x, float y, float z) {
		}

		@Override
		public void glShadeModel(int mode) {
		}

		@Override
		public void glTexCoordPointer(int size, int type, int stride,
				Buffer pointer) {
		}

		@Override
		public void glTexEnvf(int target, int pname, float param) {
		}

		@Override
		public void glTexEnvfv(int target, int pname, float[] params, int offset) {
		}

		@Override
		public void glTexEnvfv(int target, int pname, FloatBuffer params) {
		}

		@Override
		public void glTranslatef(float x, float y, float z) {
		}

		@Override
		public void glVertexPointer(int size, int type, int stride,
				Buffer pointer) {
		}

		@Override
		public void glPolygonMode(int face, int mode) {
		}

		@Override
		public void glActiveTexture(int texture) {
		}

		@Override
		public void glBindTexture(int target, int texture) {
		}

		@Override
		public void glBlendFunc(int sfactor, int dfactor) {
		}

		@Override
		public void glClear(int mask) {
		}

		@Override
		public void glClearColor(float red, float green, float blue, float alpha) {
		}

		@Override
		public void glClearDepthf(float depth) {
		}

		@Override
		public void glClearStencil(int s) {
		}

		@Override
		public void glColorMask(boolean red, boolean green, boolean blue,
				boolean alpha) {
		}

		@Override
		public void glCompressedTexImage2D(int target, int level,
				int internalformat, int width, int height, int border,
				int imageSize, Buffer data) {
		}

		@Override
		public void glCompressedTexSubImage2D(int target, int level,
				int xoffset, int yoffset, int width, int height, int format,
				int imageSize, Buffer data) {
		}

		@Override
		public void glCopyTexImage2D(int target, int level, int internalformat,
				int x, int y, int width, int height, int border) {
		}

		@Override
		public void glCopyTexSubImage2D(int target, int level, int xoffset,
				int yoffset, int x, int y, int width, int height) {
		}

		@Override
		public void glCullFace(int mode) {
		}

		@Override
		public void glDeleteTextures(int n, IntBuffer textures) {
		}

		@Override
		public void glDepthFunc(int func) {
		}

		@Override
		public void glDepthMask(boolean flag) {
		}

		@Override
		public void glDepthRangef(float zNear, float zFar) {
		}

		@Override
		public void glDisable(int cap) {
		}

		@Override
		public void glDrawArrays(int mode, int first, int count) {
		}

		@Override
		public void glDrawElements(int mode, int count, int type, Buffer indices) {
		}

		@Override
		public void glEnable(int cap) {
		}

		@Override
		public void glFinish() {
		}

		@Override
		public void glFlush() {
		}

		@Override
		public void glFrontFace(int mode) {
		}

		@Override
		public void glGenTextures(int n, IntBuffer textures) {
		}

		@Override
		public int glGetError() {
			return 0;
		}

		@Override
		public void glGetIntegerv(int pname, IntBuffer params) {
		}

		@Override
		public String glGetString(int name) {
			return null;
		}

		@Override
		public void glHint(int target, int mode) {
		}

		@Override
		public void glLineWidth(float width) {
		}

		@Override
		public void glPixelStorei(int pname, int param) {
		}

		@Override
		public void glPolygonOffset(float factor, float units) {
		}

		@Override
		public void glReadPixels(int x, int y, int width, int height,
				int format, int type, Buffer pixels) {
		}

		@Override
		public void glScissor(int x, int y, int width, int height) {
		}

		@Override
		public void glStencilFunc(int func, int ref, int mask) {
		}

		@Override
		public void glStencilMask(int mask) {
		}

		@Override
		public void glStencilOp(int fail, int zfail, int zpass) {
		}

		@Override
		public void glTexImage2D(int target, int level, int internalformat,
				int width, int height, int border, int format, int type,
				Buffer pixels) {
		}

		@Override
		public void glTexParameterf(int target, int pname, float param) {
		}

		@Override
		public void glTexSubImage2D(int target, int level, int xoffset,
				int yoffset, int width, int height, int format, int type,
				Buffer pixels) {
		}

		@Override
		public void glViewport(int x, int y, int width, int height) {
		}
	}
}
