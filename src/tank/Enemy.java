package tank;

import java.awt.*;

/**
 * @author CPF 创建于： 2021/7/1 11:27
 * @version 1.0
 */
public class Enemy extends Tank{
	private static int number = 0;

	{//初始化器
		setSpeed(1);//速度搞慢点
		setBall(new FireBall(5,Color.ORANGE, Tank.ENEMY, 6));
		setType(Tank.ENEMY);
		getBall().setType(Tank.ENEMY);
		setName("enemy" + number);
		number++;
	}

	public Enemy(){
	}
	public Enemy(int x, int y, int direction){
		super(x, y, direction);
	}

	@Override
	public void fire(){
		getBall().setDirection(getDirection());
		FireBall newBall = FireBall.copyBall(getBall());
		super.fire(newBall);
	}
}
