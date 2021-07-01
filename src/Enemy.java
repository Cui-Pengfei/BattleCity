/**
 * @author CPF 创建于： 2021/7/1 11:27
 * @version 1.0
 */
public class Enemy extends Tank{

	public Enemy(int x, int y, int direction){
		super(x, y, direction);
		setType(Tank.ENEMY);
	}
}
