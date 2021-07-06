package tank;

/**
 * @author CPF 创建于： 2021/7/1 10:34
 * @version 1.0
 */
public class Hero extends Tank{
	{
		setSpeed(2);//我方坦克快些
	}
	public Hero(int x, int y, int direction){
		super(x, y,direction);
		setType(Tank.HERO);
	}
}
