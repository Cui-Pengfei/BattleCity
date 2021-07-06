package tank;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @author CPF 创建于： 2021/7/1 11:27
 * @version 1.0
 */
public class Enemy extends Tank{
	{
		setSpeed(1);//速度搞慢点
	}

	public Enemy(){
	}

	public Enemy(int x, int y, int direction){
		super(x, y, direction);
		setType(Tank.ENEMY);
	}

}
