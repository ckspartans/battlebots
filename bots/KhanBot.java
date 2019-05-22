/**
 * 
 */
package bots;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;

import arena.BattleBotArena;
import arena.BotInfo;
import arena.Bullet;

/**
 * @author AnsarKhan
 *
 */
public class KhanBot extends Bot {

	/**
	 * 
	 */
	final double dodgeRadius = 3;
	final double shootRadius = RADIUS / 2;
	long timeAtLastShot = 0;
	long timeBetweenShots = 120;
	// System.currentTimeMillis();
	boolean useDistOnly = false;

	// Name;
	String name;

	// Images:
	Image[][] images;
	Image currentImage;

	// Storing move
	private int move = BattleBotArena.UP;
	// Position:
	double x;
	double y;
	double centerX;
	double centerY;
	// Which helmet to draw
	// 0 = Dallas
	// 1 = MSU
	int helmetType = 0;

	Bullet[] threats;

	BotInfo[] targets;

	BotInfo[] shittyBots;
	BotInfo victim;
	double maxVictimDist = 500;
	boolean moveX = true;

	public KhanBot() {
		// TODO Auto-generated constructor stub
		helmetType = (int) Math.round((Math.random()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bots.Bot#newRound()
	 */
	@Override
	public void newRound() {

		helmetType = (int) Math.round((Math.random()));

		if (images != null) {
			if (images.length > 0) {
				currentImage = images[helmetType][0];
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bots.Bot#getMove(arena.BotInfo, boolean, arena.BotInfo[],
	 * arena.BotInfo[], arena.Bullet[])
	 */
	@Override
	public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
		// TODO Auto-generated method stub
		updateInfo(me, bullets, liveBots);

		if (threats != null && me != null) {
			Bullet closestThreat = findClosest(me, threats);
			if (closestThreat != null) {
				return returnMove(dodgeBullet(closestThreat));
			}
		}
		if (victim != null) {
			if (manhattanDist(centerX, centerY, victim.getX() + RADIUS, victim.getY() + RADIUS) <= 3 * RADIUS) {
				return returnMove(shootTarget(victim));
			}
		}
		// System.out.println(targets);
		if (targets != null && me != null) {
			BotInfo closestTarget = findClosest(me, targets);
			if (closestTarget != null && shouldShoot()) {
				return returnMove(shootTarget(closestTarget));
			}
		}
		return returnMove(moveTo(victim.getX() + RADIUS, victim.getY() + RADIUS));

		// return (returnMove(0));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bots.Bot#draw(java.awt.Graphics, int, int)
	 */
	@Override
	public void draw(Graphics g, int x, int y) {
		if (images != null) {
			g.drawImage(currentImage, x, y, Bot.RADIUS * 2, Bot.RADIUS * 2, null);
		}
		g.setColor(Color.ORANGE);
		g.drawRect((int) victim.getX(), (int) victim.getY(), RADIUS * 2, RADIUS * 2);
		if (targets != null) {
			for (BotInfo b : targets) {
				g.setColor(Color.blue);
				if (sameX(b) && sameY(b)) {
					g.setColor(Color.red);
				} else if (sameX(b)) {
					g.setColor(Color.green);
				} else if (sameY(b)) {
					g.setColor(Color.blue);
				}
				g.drawRect((int) b.getX(), (int) b.getY(), RADIUS * 2, RADIUS * 2);

			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bots.Bot#getName()
	 */
	@Override
	public String getName() {
		if (name == null)
			name = "Khan Bot" + (botNumber < 10 ? "0" : "") + botNumber;
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bots.Bot#getTeamName()
	 */
	@Override
	public String getTeamName() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bots.Bot#outgoingMessage()
	 */
	@Override
	public String outgoingMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bots.Bot#incomingMessage(int, java.lang.String)
	 */
	@Override
	public void incomingMessage(int botNum, String msg) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bots.Bot#imageNames()
	 */
	@Override
	public String[] imageNames() {
		// return new String[] {"KhanBot_dallas.jpeg"};
		String[] paths = { "KhanBot_Dallas-0.png", "KhanBot_Dallas-1.png", "KhanBot_Dallas-2.png",
				"KhanBot_Dallas-3.png", "KhanBot_MSU-0.png", "KhanBot_MSU-1.png", "KhanBot_MSU-2.png",
				"KhanBot_MSU-3.png" };
		return paths;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bots.Bot#loadedImages(java.awt.Image[])
	 */
	@Override
	public void loadedImages(Image[] sentImages) {
		if (sentImages != null && sentImages.length > 7) {
			images = new Image[2][4];
			for (int i = 0; i < images.length; i++) {
				for (int j = 0; j < images[0].length; j++) {
					images[i][j] = sentImages[images[0].length * i + j];
				}
			}
		}
	}

	// -----------------My Methods--------------------------------
	// Updates all the local copies of bot information when new data is sent in
	void updateInfo(BotInfo info, Bullet[] bullets, BotInfo[] liveBots) {

		x = info.getX();
		y = info.getY();

		centerX = x + RADIUS;
		centerY = y + RADIUS;

		// System.out.println(victim.getX() + RADIUS + ", " + victim.getY() + RADIUS);
		if (bullets != null) {
			threats = getThreats(bullets);
		}
		if (liveBots != null) {
			targets = getTargets(liveBots);
		}

		shittyBots = getShittyBots(liveBots);
		updateVictim(liveBots);
		if (needNewVictim() || victim == null) {
			victim = chooseVictim(shittyBots);
		}

	}

	// Sets the image based on the move
	int returnMove(int _move) {
		move = _move;
		if (centerX + RADIUS >= BattleBotArena.RIGHT_EDGE) {
			if (move == BattleBotArena.RIGHT) {
				System.out.println("Can't Move");
				move = BattleBotArena.LEFT;
			}
		}
		if (centerX - RADIUS <= BattleBotArena.LEFT_EDGE) {
			if (move == BattleBotArena.LEFT) {
				move = BattleBotArena.RIGHT;
				System.out.println("Can't Move");
			}
		}
		if (centerY + RADIUS >= BattleBotArena.BOTTOM_EDGE) {
			if (move == BattleBotArena.DOWN) {
				move = BattleBotArena.UP;
				System.out.println("Can't Move");
			}
		}
		if (centerY - RADIUS <= BattleBotArena.TOP_EDGE) {
			if (move == BattleBotArena.UP) {
				move = BattleBotArena.DOWN;
				System.out.println("Can't Move");
			}
		}

		if (_move == BattleBotArena.UP) {
			currentImage = images[helmetType][0];
		}
		if (_move == BattleBotArena.RIGHT) {
			currentImage = images[helmetType][1];
		}
		if (_move == BattleBotArena.DOWN) {
			currentImage = images[helmetType][2];
		}
		if (_move == BattleBotArena.LEFT) {
			currentImage = images[helmetType][3];
		}
		if (_move == BattleBotArena.STAY) {
			// currentImage = currentImage;;
		}
		return move;
	}

	boolean isThreat(Bullet b) {

		double dist = dist(centerX, centerY, b.getX(), b.getY());

		if (dist > 100) {
			return false;
		}
		// Check if the bullet is directly above or below me
		if (sameX(b)) {
			// Check if bullet is coming to you
			if (b.getY() > centerY && b.getYSpeed() < 0) {
				return true;
			} else if (b.getY() < centerY && b.getYSpeed() > 0) {
				return true;
			}
		}
		// Check if bullet is to the right or left of me
		if (sameY(b)) {
			if (b.getX() > centerX && b.getXSpeed() < 0) {
				return true;
			} else if (b.getX() < centerX && b.getXSpeed() > 0) {
				return true;
			}

		}
		return false;
	}

	boolean isTarget(BotInfo b) {
		if (dist(centerX, centerY, b.getX(), b.getY()) > 30 * RADIUS) {
			return false;
		}
		if (dist(centerX, centerY, b.getX(), b.getY()) < 3 * RADIUS) {
			return false;
		}
		// Check if the bot is directly above or below me
		if (sameX(b)) {
			return true;

		}
		// Check if bullet is to the right or left of me
		if (sameY(b)) {
			return true;
		}
		return false;
	}

	// Returns an Array of Bullets that are threats
	Bullet[] getThreats(Bullet[] allBullets) {
		ArrayList<Bullet> listOfThreats = new ArrayList<Bullet>();
		for (Bullet b : allBullets) {

			if (isThreat(b)) {// Checks if something is a threat
				listOfThreats.add(b);
			}
		}
		Bullet[] bulletArr = listOfThreats.toArray(new Bullet[listOfThreats.size()]);
		return bulletArr;
	}

	BotInfo[] getTargets(BotInfo[] allBots) {
		ArrayList<BotInfo> listOfTargets = new ArrayList<BotInfo>();
		for (BotInfo b : allBots) {

			if (isTarget(b)) {
				listOfTargets.add(b);
			}
		}
		return listOfTargets.toArray(new BotInfo[listOfTargets.size()]);

	}

	int dodgeBullet(Bullet b) {
		if (b.getX() > centerX - (RADIUS + dodgeRadius) && b.getX() < centerX + (RADIUS + dodgeRadius)) {
			// System.out.println("Bulet is above or below");
			if (b.getYSpeed() != 0) {

				if (b.getX() > centerX) {
					return BattleBotArena.LEFT;
				} else if (b.getX() <= centerX) {
					return BattleBotArena.RIGHT;
				}

			}
		}
		if (b.getY() > centerY - (RADIUS + dodgeRadius) && b.getY() < centerY + (RADIUS + dodgeRadius)) {
			// System.out.println("Bullet is on the left or right");
			if (b.getXSpeed() != 0) {
				if (b.getY() >= centerY) {
					return BattleBotArena.UP;
				} else
					return BattleBotArena.DOWN;
			}

		}
		return BattleBotArena.STAY;

	}

	int shootTarget(BotInfo b) {

		if (b.getX() >= centerX && sameY(b) && shouldShootX(b)) {
			return BattleBotArena.FIRERIGHT;
		}
		if (b.getX() < centerX && shouldShootX(b) && sameY(b)) {
			return BattleBotArena.FIRELEFT;
		}
		if (b.getY() >= centerY && shouldShootY(b) && sameX(b)) {
			return BattleBotArena.FIREDOWN;
		}
		if (b.getY() < centerY && shouldShootY(b) && sameX(b)) {
			return BattleBotArena.FIREUP;
		}

		return BattleBotArena.STAY;

	}

	boolean shouldShoot() {
		if (System.currentTimeMillis() - 2000 > timeAtLastShot) {
			timeAtLastShot = System.currentTimeMillis();
			return true;
		}
		return false;
	}

	boolean shouldShootX(BotInfo b) {
		if (dist(centerX, centerY, b.getX() + RADIUS, b.getY() + RADIUS) < 5 * RADIUS) {
			return true;
		}
		return (b.getLastMove() != BattleBotArena.UP) && (b.getLastMove() != BattleBotArena.DOWN);
	}

	boolean shouldShootY(BotInfo b) {
		if (dist(centerX, centerY, b.getX() + RADIUS, b.getY() + RADIUS) < 5 * RADIUS) {
			// System.out.println("Yolo");
			return true;

		}
		return (b.getLastMove() != BattleBotArena.LEFT) && (b.getLastMove() != BattleBotArena.RIGHT);
	}

	BotInfo[] getShittyBots(BotInfo[] allBots) {
		BotInfo[] copy = Arrays.copyOf(allBots, allBots.length);
		BotInfo temp;
		if (allBots.length < 2) {
			System.err.println("There's Only One Bot");
			return (null);
		}
		for (int j = 0; j < copy.length; j++) {
			for (int i = 0; i < copy.length - 1; i++) {
				if (copy[i].getCumulativeScore() + copy[i].getScore() > copy[i + 1].getCumulativeScore()
						+ copy[i + 1].getScore()) {
					temp = copy[i + 1];
					copy[i + 1] = copy[i];
					copy[i] = temp;
					// System.err.println("Swapped");
				}
			}

		}

		return copy;

	}

	BotInfo chooseVictim(BotInfo[] shittyBots) {
		double highestScore = 0;
		int index = 0;
		// Score is a formula tha can be written as indexOfShittiness^-1 * dist*-1
		if (shittyBots == null || shittyBots.length < 1) {
			return null;
		}
		for (int i = 0; i < shittyBots.length; i++) {

			double dist = manhattanDist(centerX, centerY, shittyBots[i].getX(), shittyBots[i].getY());
			double tempScore;
			if (!useDistOnly) {
				tempScore = (1 / (i + 1)) * (1 / dist);
			} else {
				tempScore = (1 / dist);
			}

			if (dist > maxVictimDist) {
				tempScore = 0;
			}
			if (victim != null) {
				if (victim.getBotNumber() == shittyBots[i].getBotNumber()) {
					tempScore = 0;
				}
			}
			if (tempScore > highestScore) {
				highestScore = tempScore;
				index = i;
			}
		}
		// System.out.println("New Victim");
		return shittyBots[index];

	}

	boolean needNewVictim() {
		if (victim == null || dist(centerX, centerY, victim.getX() + RADIUS, victim.getY() + RADIUS) > maxVictimDist) {
			// System.out.println(victim.getTimeOfDeath() != 0);
			return true;
		}
		return victim.getTimeOfDeath() != 0;
	}

	void updateVictim(BotInfo[] liveBots) {
		if (victim == null) {
			return;
		}
		for (BotInfo b : liveBots) {
			if (b.getBotNumber() == victim.getBotNumber()) {
				victim = b;
				return;
			}
		}

	}

	int moveTo(double x, double y) {
		double dx = calcDisplacement(centerX, x);
		double dy = calcDisplacement(centerY, y);

		if (sameY(victim)) {
			if (dx > 0) {
				return BattleBotArena.RIGHT;
			} else if (dx < 0) {
				return BattleBotArena.LEFT;
			}
			if (dx == 0) {
				return BattleBotArena.STAY;
			}
		}
		if (sameX(victim)) {
			if (dy > 0) {
				return BattleBotArena.DOWN;
			} else if (dy < 0) {
				return BattleBotArena.UP;
			}
			if (dy == 0) {
				return BattleBotArena.STAY;
			}
		}

		if (moveX) {
			moveX = !moveX;
			if (dx > 0) {
				return BattleBotArena.RIGHT;
			} else if (dx < 0) {
				return BattleBotArena.LEFT;
			}
			if (dx == 0) {
				return BattleBotArena.STAY;
			}

		} else if (!moveX) {
			moveX = !moveX;
			if (dy > 0) {
				return BattleBotArena.DOWN;
			} else if (dy < 0) {
				return BattleBotArena.UP;
			}
			if (dy == 0) {
				return BattleBotArena.STAY;
			}
		}
		System.out.println("Need to stay: " + dx + ", " + dy);
		return BattleBotArena.STAY;
	}

	// you can also use this to find the y displacement
	public double calcDisplacement(double botX, double bulletX) {
		return bulletX - botX;
	}

	public double dist(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(calcDisplacement(x1, x2), 2) + Math.pow(calcDisplacement(y1, y2), 2));
	}

	public double manhattanDist(double x1, double y1, double x2, double y2) {
		return Math.abs(x2 - x1) + Math.abs(y2 - y1);
	}

	boolean sameX(Bullet b) {
		return b.getX() > centerX - (RADIUS + dodgeRadius) && b.getX() < centerX + (RADIUS + dodgeRadius);
	}

	boolean sameY(Bullet b) {
		return b.getY() > centerY - (RADIUS + dodgeRadius) && b.getY() < centerY + (RADIUS + dodgeRadius);
	}

	boolean sameX(BotInfo b) {
		return b.getX() > centerX - (RADIUS + shootRadius) && b.getX() < centerX + (RADIUS + shootRadius);
	}

	boolean sameY(BotInfo b) {
		return b.getY() > centerY - (RADIUS + shootRadius) && b.getY() < centerY + (RADIUS + shootRadius);
	}

	boolean checkSortedByScore(BotInfo[] bots) {
		for (int i = 0; i < bots.length - 1; i++) {
			if (!(bots[i].getCumulativeScore() + bots[i].getScore() <= bots[i + 1].getCumulativeScore()
					+ bots[i + 1].getScore())) {
				return false;
			}
		}
		return true;
	}

	public static Bullet findClosest(BotInfo _me, Bullet[] _bullets) {
		Bullet closest;
		double distance, closestDist;
		if (_bullets.length > 0) {
			closest = _bullets[0];
		} else {
			return null;
		}
		closestDist = Math.abs(_me.getX() - closest.getX()) + Math.abs(_me.getY() - closest.getY());
		for (int i = 1; i < _bullets.length; i++) {
			distance = Math.abs(_me.getX() - _bullets[i].getX()) + Math.abs(_me.getY() - _bullets[i].getY());
			if (distance < closestDist) {
				closest = _bullets[i];
				closestDist = distance;
			}
		}
		return closest;
	}

	public static BotInfo findClosest(BotInfo _me, BotInfo[] _bots) {
		BotInfo closest;
		double distance, closestDist;
		if (_bots.length > 0) {
			closest = _bots[0];
		} else {
			return null;
		}
		closest = _bots[0];
		closestDist = Math.abs(_me.getX() - closest.getX()) + Math.abs(_me.getY() - closest.getY());
		for (int i = 1; i < _bots.length; i++) {
			distance = Math.abs(_me.getX() - _bots[i].getX()) + Math.abs(_me.getY() - _bots[i].getY());
			if (distance < closestDist) {
				closest = _bots[i];
				closestDist = distance;
			}
		}
		return closest;
	}

}
