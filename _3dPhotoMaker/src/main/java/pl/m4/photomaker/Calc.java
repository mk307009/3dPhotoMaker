package pl.m4.photomaker;

import java.util.List;

public final class Calc {

	public static double median(double[] data) {
		double median = 0;
		if (data.length % 2 == 0) {
			float average = (float) (data[data.length / 2] + data[(data.length / 2) - 1]);
			median = average / 2;
		} else {
			median = data[data.length / 2];
		}
		return median;
	}

	public static double median(List<Double> data) {
		double median = 0;
		if (data.size() % 2 == 0) {
			double average = data.get(data.size() / 2)
					+ data.get(data.size() / 2 - 1);
			median = average / 2;
		} else {
			median = data.get(data.size() / 2);
		}
		return median;
	}

	public static boolean isValidMotion(float delta, float tolerance) {
		if (Math.abs(delta) > tolerance) {
			return false;
		}
		return true;
	}

	public static int correctColorValue(int val) {
		if (val > 255)
			return 255;
		else if (val < 0)
			return 0;
		else
			return val;
	}

}
