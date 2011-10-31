package es.eucm.eadventure.engine.core.util.impl;

public class EAdInterpolator {

	/**
	 * Interpolator f(x) = x
	 */
	public static final EAdInterpolator LINEAR = new EAdInterpolator(
			new float[] { 0.0f, 1.0f });
	
	public static final EAdInterpolator BOUNCE_END = new EAdInterpolator(
			new float[] { 0.0f, 3.5f, -2.5f });
	
	public static final EAdInterpolator ACCELERATE = new EAdInterpolator(
			new float[] { 0.0f, 0.0f, 1.0f });
	
	public static final EAdInterpolator DESACCELERATE = new EAdInterpolator(
			new float[] { 0.0f, 0.0f, 1.0f });
	private float[] polynomial;

	public EAdInterpolator(float[] polynomial) {
		this.polynomial = polynomial;
	}

	/**
	 * 
	 * @param v
	 *            a value for the interpolation between 0 and 1.
	 * @return
	 */
	public float interpolate(float currentTime, float totalTime, float totalLength ) {
		float v = currentTime / totalTime;
		float r = 0;
		for (int i = 0; i < polynomial.length; i++) {
			r += Math.pow(v, i) * polynomial[i];
		}
		return r * totalLength;
	}

}
