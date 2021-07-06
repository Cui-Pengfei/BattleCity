package tank;

/**
 * @author CPF 创建于： 2021/7/1 11:29
 * @version 1.0
 */
public class Boss extends Tank{
	{
		setSpeed(1);
	}
	public Boss(int x, int y, int direction){
		super(x, y, direction);
		setType(Tank.BOSS);
	}
}
