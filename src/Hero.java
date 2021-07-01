/**
 * @author CPF 创建于： 2021/7/1 10:34
 * @version 1.0
 */
public class Hero extends Tank{
	public Hero(int x, int y, int direction){
		super(x, y,direction);
		setType(Tank.HERO);
	}
}
