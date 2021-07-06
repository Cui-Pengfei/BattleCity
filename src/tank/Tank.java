package tank;

import frame.GameFrame;

import java.awt.*;
import java.util.Vector;

/**
 * @author CPF 创建于： 2021/7/1 9:46
 * @version 1.0
 */
public class Tank extends Thread{
	//规定坦克方向与对应的数字
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;

	//规定坦克种类与对应的数字
	public static final int HERO = 0;//我方坦克
	public static final int ENEMY = 1;//敌方普通坦克
	public static final int BOSS = 2;//敌方boss坦克


	private int x;//坦克的横坐标
	private int y;//坦克的纵坐标
	private int direction;//坦克炮口指向的方向（上下左右）
	private int type;//坦克的种类（我方、敌方、boss）
	private int speed = 5;//坦克的速度
	private boolean live = true;//是否存活

	//集合每个元素都是线程，但是只有一个MyPanel线程来操作集合，所以不存在多线程操作集合的问题
	private Vector<FireBall> balls = new Vector<>();


	public Vector<FireBall> getBalls(){
		return balls;
	}

	public Tank(){
	}

	public Tank(int x, int y, int direction){
		this.x = x;
		this.y = y;
		this.direction = direction;
	}

	/**
	 * 坦克也做成线程，虽然Hero不需要，但是Enemy Boss都需要，
	 * 到时候，只要hero不startDirect就可以.
	 */
	@Override
	public void run(){
		long startDirect = System.currentTimeMillis();//改变方向起始时间
		long startFire = System.currentTimeMillis();//开火时间控制
		long startClear = System.currentTimeMillis();//清空弹夹时间控制

		long nowDirect, nowFire,nowClear;
		int fireTime = 1000;
		int oneDirectionTime = 3 * 1000;//5秒改变一个方向
		int clearTime = 10 * 1000;//10秒清理一次弹夹
		int randomDirect;
		while(live){//存活的坦克才有线程
			nowDirect = System.currentTimeMillis();
			nowFire = System.currentTimeMillis();
			nowClear = System.currentTimeMillis();
			//我方坦克不自动改变方向
			if(type != Tank.HERO && (nowDirect - startDirect > oneDirectionTime)){
				startDirect = nowDirect;//进来后，起始时间就改变了，不然控制条件控制不住
				randomDirect = (int) (Math.random() * 4);//[0,1,2,3]
				direction = randomDirect;
			}//得到一个随机方向

			//每隔一秒发射一个炮弹
			if(type != Tank.HERO && (nowFire - startFire > fireTime)){
				startFire = nowFire;
				fire();
			}

			//每10秒清理一下弹夹    坦克仓库
			if(nowClear - startClear > clearTime){
				startClear = nowClear;
				balls.removeIf(ball -> ball != null && !ball.isLive());//清理弹夹
			}

			move(direction);//敌人坦克也要动来动去

			try{
				Thread.sleep(10);//线程不能一直循环着
			}catch(InterruptedException e){
				e.printStackTrace();
			}

		}
	}

	public void move(int direction){
		switch(direction){
			case UP:
				moveUp();
				break;
			case DOWN:
				moveDown();
				break;
			case RIGHT:
				moveRight();
				break;
			case LEFT:
				moveLeft();
				break;
			default:
				System.out.println("为什么会匹配到这个呢？");
				//不会有default
		}
	}

	//向上移动，参数是速度
	public void moveUp(){
		setDirection(Tank.UP);
		if(y - speed <= 0){
			y = 0;
			if(type != Tank.HERO)//我方坦克碰壁不改变方向
				direction = Tank.DOWN;
		}else{
			y -= speed;
		}
		setY(y);
	}

	//向下移动
	public void moveDown(){
		setDirection(Tank.DOWN);
		if(y + 60 + speed >= GameFrame.height){
			y = GameFrame.height - 60;
			if(type != Tank.HERO)
				direction = Tank.UP;
		}else{
			y += speed;
		}
		setY(y);
	}

	//向右移动
	public void moveRight(){
		setDirection(Tank.RIGHT);
		if(x + 50 >= GameFrame.width){
			x = GameFrame.width - 50;
			if(type != Tank.HERO)
				direction = Tank.LEFT;
		}else{
			x += speed;
		}
		setX(x);
	}

	//向左移动
	public void moveLeft(){
		setDirection(Tank.LEFT);
		if(x - 10 - speed <= 0){
			x = 10;
			if(type != Tank.HERO)
				direction = Tank.RIGHT;
		}else{
			x -= speed;
		}
		setX(x);
	}

	//获得坦克中心坐标
	public int centerX(){
		return x + 20;
	}
	public int centerY(){
		return y + 30;
	}

	/**
	 * 判断此坦克是否与另一辆坦克即将相撞.
	 * 在绘制坦克的时候，虽然坦克方向会改变，但是坦克的核心点不会随着方向改变，
	 * 计算出核心点是 (x+20,y+30).
	 *
	 * @param tank 另一辆坦克
	 * @return 返回true为即将撞上
	 */
	public boolean isTouch(Tank tank){
		int thisCenterX = x + 20;
		int thisCenterY = y + 30;
		int tankCenterX = tank.x + 20;
		int tankCenterY = tank.y + 30;
		if(direction != tank.direction){//方向不相同才会有可能相撞
			//仔细的情况有很多，此处只模糊一下
			if(Math.abs(thisCenterX - tankCenterX) < 50 &&
					Math.abs(thisCenterY - tankCenterY) < 50){
				return true;
			}
		}
		return false;
	}

	public void reverseDirect(){//转向相反方向
		switch(direction){
			case UP:
				direction = DOWN;
				break;
			case DOWN:
				direction = UP;
				break;
			case RIGHT:
				direction = LEFT;
				break;
			case LEFT:
				direction = RIGHT;
				break;
			default:
				System.out.println("为什么会匹配到这个呢？");
				//不会有default
		}
	}


	//开火函数
	public void fire(){
		FireBall ball = new FireBall(x, y, direction);//临时量，为了配合得到炮弹大小，有待优化
		int ballX = x;
		int ballY = y;
		switch(direction){
			case Tank.UP:
				ballX = x + 20 - ball.getSize() / 2;
				break;
			case Tank.DOWN:
				ballX = x + 20 - ball.getSize() / 2;
				ballY = y + 60;
				break;
			case Tank.LEFT:
				ballX = x - 10;
				ballY = y + 30 - ball.getSize() / 2;
				break;
			case Tank.RIGHT:
				ballX = x + 50;
				ballY = y + 30 - ball.getSize() / 2;
				break;
		}
		switch(type){
			case HERO:
				ball.setColor(Color.CYAN);
				//英雄使用默认炮弹大小10
				break;
			case ENEMY:
				ball.setColor(Color.ORANGE);
				ball.setSize(5);
				break;
			case BOSS:
				ball.setColor(Color.RED);
				ball.setSize(15);
				break;
			default:
				System.out.println("不会有这一种坦克...发射炮弹");

		}
		ball = new FireBall(ballX, ballY, direction, ball.getSize(), ball.getColor());
		balls.add(ball);
		ball.start();
	}


	//getter\setter
	public int getX(){
		return x;
	}

	public void setX(int x){
		this.x = x;
	}

	public int getSpeed(){
		return speed;
	}

	public void setSpeed(int speed){
		this.speed = speed;
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

	public boolean isLive(){
		return live;
	}

	public void setLive(boolean live){
		this.live = live;
	}
}
