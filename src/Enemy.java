import java.util.Vector;

/**
 * @author CPF 创建于： 2021/7/1 11:27
 * @version 1.0
 */
public class Enemy extends Tank{
	Vector<Enemy> enemies = new Vector<>();

	public Enemy(){
	}

	public Enemy(int x, int y, int direction){
		super(x, y, direction);
		setType(Tank.ENEMY);
	}

	public Vector<Enemy> army(int number){
		for(int i = 0; i < number; i++){
			enemies.add(new Enemy((i % 5 + 1) * 100, ((i / 5) + 1) * 100, Tank.DOWN));
		}
		return enemies;
	}
}
