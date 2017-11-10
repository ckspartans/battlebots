package bots;

import java.util.ArrayList;
import arena.BotInfo;
import arena.Bullet;

/**
 * @author rowbottomn
 *
 */
public class BotHelper {

	/**
	 * BotHelper can: Return the the displacement in the x and y between your bot
	 * and a bullet. Return the euclidean distance between any two points Return the
	 * Manhattan Distance between two points Return the closest bullet out of the
	 * array Return the closest live bot Return the closest dead bot
	 * 
	 * TO USE: Make sure to instantiate a BotHelper Object call the function of your
	 * choice; making sure to send in the appropriate parameters ensure you receive
	 * the needed info
	 */

	// you can also use this to find the y displacement
	public double calcDisplacement(double botX, double bulletX) {
		return bulletX - botX;
	}

	public double calcDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(calcDisplacement(x1, x2), 2) + Math.pow(calcDisplacement(y1, y2), 2));
	}

	// Call this as BotHelper.manhattanDist( , )
	public static double calcManhattanDist(double _dX, double _dY) {
		return Math.abs(_dX) + Math.abs(_dY);
	}

	// Call this as BotHelper.manhattanDist( , , , )
	public static double manhattanDist(double x1, double y1, double x2, double y2) {
		return Math.abs(x2 - x1) + Math.abs(y2 - y1);
	}


}