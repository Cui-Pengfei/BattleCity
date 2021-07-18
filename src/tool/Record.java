package tool;

import java.io.Serializable;

/**
 * @author CPF 创建于： 2021/7/17 15:04
 * @version 1.0
 * 记录玩家本局操纵hero的数据，包括：
 * 玩游戏时间：gameTime
 * 击毁enemy数量：destroyEnemyNum
 * 击毁boss数量：destroyBossNum
 * 死亡次数：deaths
 */
public class Record implements Serializable{//必须也是可序列化的，因为要作为MyPanel的属性
	private int x;
	private int y;//记录所在的起始位置

	private long gameStartTime;
	private long gameEndTime;
	private int destroyEnemyNum;
	private int destroyBossNum;
	private int deaths;

	public Record(int x, int y){
		this.x = x;
		this.y = y;
	}

	public void destroyBossNumPlus(){
		destroyBossNum++;
	}
	public void destroyEnemyNumPlus(){
		destroyEnemyNum++;
	}
	public void deathPlus(){
		deaths++;
	}

	public long getGameStartTime(){
		return gameStartTime;
	}

	public void setGameStartTime(long gameStartTime){
		this.gameStartTime = gameStartTime;
	}

	public long getGameEndTime(){
		return gameEndTime;
	}

	public void setGameEndTime(long gameEndTime){
		this.gameEndTime = gameEndTime;
	}

	public int getDestroyEnemyNum(){
		return destroyEnemyNum;
	}

	public void setDestroyEnemyNum(int destroyEnemyNum){
		this.destroyEnemyNum = destroyEnemyNum;
	}

	public int getDestroyBossNum(){
		return destroyBossNum;
	}

	public void setDestroyBossNum(int destroyBossNum){
		this.destroyBossNum = destroyBossNum;
	}

	public int getDeaths(){
		return deaths;
	}

	public void setDeaths(int deaths){
		this.deaths = deaths;
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
}
