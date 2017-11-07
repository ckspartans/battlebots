/**
 * 
 */
package bots;

import java.awt.Graphics;
import java.awt.Image;

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
	
	//Name;
	String name;
	
	//Images:
	Image[][] images;
	Image currentImage;
	
	//Storing move
	private int move = BattleBotArena.UP;
	//Position:
	double x;
	double y;
	
	//Which helmet to draw
		//0 = Dallas
		//1 = MSU
	int helmetType = 1;
	
	Bullet[] threats;
	
	
	public KhanBot() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see bots.Bot#newRound()
	 */
	@Override
	public void newRound() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see bots.Bot#getMove(arena.BotInfo, boolean, arena.BotInfo[], arena.BotInfo[], arena.Bullet[])
	 */
	@Override
	public int getMove(BotInfo me, boolean shotOK, BotInfo[] liveBots, BotInfo[] deadBots, Bullet[] bullets) {
		// TODO Auto-generated method stub
		updateInfo(me);
		
	}

	/* (non-Javadoc)
	 * @see bots.Bot#draw(java.awt.Graphics, int, int)
	 */
	@Override
	public void draw(Graphics g, int x, int y) {
		if (images != null) {
			g.drawImage(currentImage, x,y,Bot.RADIUS*2, Bot.RADIUS*2, null);
		}
	}

	/* (non-Javadoc)
	 * @see bots.Bot#getName()
	 */
	@Override
	public String getName() {
		if (name == null)
			name = "Khan Bot"+(botNumber<10?"0":"")+botNumber;
		return name;
	}

	/* (non-Javadoc)
	 * @see bots.Bot#getTeamName()
	 */
	@Override
	public String getTeamName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bots.Bot#outgoingMessage()
	 */
	@Override
	public String outgoingMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see bots.Bot#incomingMessage(int, java.lang.String)
	 */
	@Override
	public void incomingMessage(int botNum, String msg) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see bots.Bot#imageNames()
	 */
	@Override
	public String[] imageNames() {
		//return new String[] {"KhanBot_dallas.jpeg"};
		String[] paths = {"KhanBot_Dallas-0.png","KhanBot_Dallas-1.png","KhanBot_Dallas-2.png","KhanBot_Dallas-3.png",
						 "KhanBot_MSU-0.png","KhanBot_MSU-1.png","KhanBot_MSU-2.png","KhanBot_MSU-3.png" };
		return paths;
	}

	/* (non-Javadoc)
	 * @see bots.Bot#loadedImages(java.awt.Image[])
	 */
	@Override
	public void loadedImages(Image[] sentImages) {
		if (sentImages != null && sentImages.length > 7) {
			images= new Image[2][4];
			for(int i = 0; i<images.length; i++) {
				for(int j = 0; j<images[0].length; j++) {
						images[i][j] =sentImages[images[0].length*i + j];
				}
			}
		}	
	}
	//-----------------My Methods--------------------------------
	//Updates all the local copies of bot information when new data is sent in
	void updateInfo(BotInfo info) {
		x = info.getX();
		y = info.getY();
		
	}
	boolean touchingBounds() {
		return false;
	}
	//Sets the image based on the move
	int returnMove(int _move) {
		move = _move;
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
		return _move;
	}
	boolean isThreat(Bullet b) {
		if (b.getX() > x-RADIUS) {
			
		}
		return true;
	}
	
	//Returns an Array of Bullets that are threats
	Bullet[] getThreats(Bullet[] allBullets){
		return null;
		
	}
	


}
