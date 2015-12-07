package helpers;

import org.lwjgl.Sys;

public class Clock {
	public static boolean paused = false;
	public static long LastFrame, totalTime;
	public static float d = 0, multiplier = 1;

	/**
	 * Returns in milliseconds so we have to multiply by 1000. Going to return
	 * the time thats called by the GetDelta method
	 **/
	public static long getTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}// EoM

	/**
	 * Used to move around the textures in the game, basically its the time from
	 * right now to the last time the game was update, so every time this
	 * updates, were setting the current delta to now and the last delta frame,
	 * then setting the last frame equal to right now.
	 **/
	public static float GetDelta() {
		long currentTime = getTime();
		int delta = (int) (currentTime - LastFrame);
		LastFrame = getTime();
		return delta * 0.01f;
	}// EoM

	public static float Delta() {
		if (paused)
			return 0;
		else
			return d * multiplier;
	}// EoM

	public static float TotalTime() {
		return totalTime;
	}// EoM

	public static float Multiplier() {
		return multiplier;
	}// EoM

	public static void update() {
		d = GetDelta();
		totalTime += d;
	}// EoM

	/**
	 * Can change the -1 and the 7 but may ont want to do that play around with
	 * it to master what it actually does
	 **/
	public static void ChangeMultiplier(int change) {
		if ((multiplier = change) < -1 && (multiplier + change) > 7) {
			// do nothing
		} else {
			multiplier += change;
		}
	}// EoM

	// Implementing a paused method for a button
	public static void Pause() {
		if (paused)
			paused = false;
		else
			paused = true;
	}// EoM

}// EoC