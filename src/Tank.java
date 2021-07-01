/**
 * @author CPF 创建于： 2021/7/1 9:46
 * @version 1.0
 */
public class Tank{
	//规定坦克方向与对应的数字
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 3;
	public static final int RIGHT = 4;

	//规定坦克种类与对应的数字
	public static final int HERO = 0;//我方坦克
	public static final int ENEMY = 1;//敌方普通坦克
	public static final int BOSS = 2;//敌方boss坦克


	private int x;//坦克的横坐标
	private int y;//坦克的纵坐标
	private int direction;//坦克炮口指向的方向（上下左右）
	private int type;//坦克的种类（我方、敌方、boss）
	public static int speed = 3;

	public Tank(int x, int y, int direction){
		this.x = x;
		this.y = y;
		this.direction = direction;
	}

	//向上移动，参数是速度
	public void moveUp(){
		setDirection(Tank.UP);
		if(y- speed <= 0){
			y = 0;
		}else{
			y -= speed;
		}
		setY(y);
	}

	//向下移动
	public void moveDown(){
		setDirection(Tank.DOWN);
		if(x + 60 + speed >= 600){
			y = 600 - 60;
		}else{
			y += speed;
		}
		setY(y);
	}
	
	//向右移动
	public void moveRight(){
		setDirection(Tank.RIGHT);
		if(x + 50 + speed >= 600){
			x = 600 - 50;
		}else{
			x += speed;
		}
		setX(x);
	}
	
	public void moveLeft(){
		setDirection(Tank.LEFT);
		if(x - 10 - speed <= 0){
			x = 0;
		}else{
			x -= speed;
		}
		setX(x);
	}


	//getter\setter
	public int getX(){
		return x;
	}

	public void setX(int x){
		this.x = x;
	}

	public int getY(){
		return y;
	}

	public void setY(int y){
		this.y = y;
	}

	public int getDirection(){
		return direction;
	}

	public void setDirection(int direction){
		this.direction = direction;
	}

	public int getType(){
		return type;
	}

	public void setType(int type){
		this.type = type;
	}
}
