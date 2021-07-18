package tool;

import java.io.Serializable;

/**
 * @author CPF 创建于： 2021/7/18 22:29
 * @version 1.0
 */
public class Bomb implements Serializable{
	private int x;
	private int y;
	private int life = 90;
	private boolean isLive = true;

	public Bomb(int x, int y){
		this.x = x;
		this.y = y;
	}

	public void lifDown(){
		if(life > 0){//目的是时延
			life--;
		}else{
			isLive = false;
		}
	}

	public int getX(){
		return x;
	}

	public void setX(int x){
		this.x = x;
	}

	public int getY(){
		return y;
	}

	public void setY(int y){
		this.y = y;
	}

	public int getLife(){
		return life;
	}

	public void setLife(int life){
		this.life = life;
	}

	public boolean isLive(){
		return isLive;
	}

	public void setLive(boolean live){
		isLive = live;
	}
}
