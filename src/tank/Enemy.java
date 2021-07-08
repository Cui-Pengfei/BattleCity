package tank;

import java.awt.*;

/**
 * @author CPF 创建于： 2021/7/1 11:27
 * @version 1.0
 */
public class Enemy extends Tank{
	private static int number = 0;

	{//由于有空构造器，不得已而为之
		setSpeed(1);//速度搞慢点
		setBall(new FireBall(5, Color.ORANGE));
		setType(Tank.ENEMY);
		getBall().setType(Tank.ENEMY);
	}

	public Enemy(){
	}
	public Enemy(int x, int y, int direction){
		super(x, y, direction);
		number++;
		setName("enemy" + number);
	}

}
