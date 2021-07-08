package tank;

import java.awt.*;

/**
 * @author CPF 创建于： 2021/7/1 10:34
 * @version 1.0
 */
public class Hero extends Tank{

	public Hero(int x, int y, int direction){
		super(x, y,direction);
		setType(Tank.HERO);
		setSpeed(2);//我方坦克快些
		setBall(new FireBall(10, Color.CYAN));
		setName("Hero");
		getBall().setType(Tank.HERO);
	}

	public void ballGrow(){
		int ballSize = getBall().getSize();
		if(ballSize <= 20){
			getBall().setSize(ballSize + 1);
		}
	}
}
