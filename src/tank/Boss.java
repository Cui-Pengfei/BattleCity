package tank;

import java.awt.*;

/**
 * @author CPF 创建于： 2021/7/1 11:29
 * @version 1.0
 */
public class Boss extends Tank{
	private int blood = 5;//血量设置，打掉5滴血才会死掉，还会根据血量改变颜色

	public Boss(int x, int y, int direction){
		super(x, y, direction);
		setType(Tank.BOSS);
		setSpeed(1);
		setBall(new FireBall(15, Color.RED));
	}

	public int getBlood(){
		return blood;
	}

	public void setBlood(int blood){
		this.blood = blood;
	}

	@Override
	public void fire(){//重写fire方法，根据血量，改变炮弹颜色
		switch(blood){
			case 5:
				getBall().setColor(Color.RED);
				break;
			case 4:
				getBall().setColor(Color.magenta);
				break;
			case 3:
				getBall().setColor(Color.PINK);
				break;
			case 2:
				getBall().setColor(Color.blue);
				break;
			case 1:
				getBall().setColor(Color.GREEN);
				break;
			default:
				getBall().setColor(Color.DARK_GRAY);
				break;
		}
		super.fire();
	}
}
