package tank;

import frame.GameFrame;

import java.io.Serializable;
import java.util.Vector;

/**
 * @author CPF 创建于： 2021/7/1 9:46
 * @version 1.0
 */
public abstract class Tank extends Thread implements Serializable{
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
	private int speed ;//坦克的速度
	private boolean live = true;//是否存活
	private FireBall ball ;//每个坦克都有自己的炮弹
	private boolean runnable = true;
	private boolean stop = false;

	//炮弹本身自己就是一个线程，要自我操作自己的数据，MyPanel也是一个线程，绘制炮弹时，也要操作炮弹数据，balls所属类Tank也是一个线程
	//因此必须使用Vector,才能避免出现异常
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
		int fireTime = 3000;
		int oneDirectionTime = 5 * 1000;//5秒改变一个方向
		int clearTime = 10 * 1000;//10秒清理一次弹夹
		int randomDirect;
		while(live){//存活的坦克才有线程
			nowDirect = System.currentTimeMillis();
			nowFire = System.currentTimeMillis();
			nowClear = System.currentTimeMillis();
			//我方坦克不自动改变方向
			if(!stop && type != Tank.HERO && (nowDirect - startDirect > oneDirectionTime)){
				startDirect = nowDirect;//进来后，起始时间就改变了，不然控制条件控制不住
				randomDirect = (int) (Math.random() * 4);//[0,1,2,3]
				direction = randomDirect;
			}//得到一个随机方向

			//每隔3秒发射一个炮弹
			if(!stop && type != Tank.HERO && (nowFire - startFire > fireTime)){
				startFire = nowFire;
				fire();
				runnable = true;
			}

			//每10秒清理一下弹夹，频繁清理不利于性能
			if(nowClear - startClear > clearTime){
				startClear = nowClear;
				balls.removeIf(ball -> ball != null && !ball.isLive());//清理弹夹
			}

			if(!stop && runnable){//敌人坦克也要动来动去
				move(direction);
			}

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
		if(y + 60 + speed >= GameFrame.SCREEN_HEIGHT){
			y = GameFrame.SCREEN_HEIGHT - 60;
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
		if(x + 50 >= GameFrame.SCREEN_WIDTH - GameFrame.RECORD_WIDTH){
			x = GameFrame.SCREEN_WIDTH - GameFrame.RECORD_WIDTH - 50;
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
	
	public void touchReact(Tank tank){
		//如果相向运动，建议其中一个往相反方向走
		if(this.direction == UP && tank.direction == DOWN
			|| this.direction == DOWN && tank.direction == UP
			|| this.direction == LEFT && tank.direction == RIGHT
			|| this.direction == RIGHT && tank.direction == LEFT){
			this.reverseDirect();
		}else if(
				(this.direction == UP && tank.direction == UP
				|| this.direction == DOWN && tank.direction == DOWN
				|| this.direction == LEFT && tank.direction == LEFT//同向,并且一方触墙,双方都换方向,防止一直卡在墙边
				|| this.direction == RIGHT && tank.direction == RIGHT) && tank.isTouchBlock()
				|| tank.isTouch(this)){//双方头部互相进入，是最麻烦的，只能让双反都向相反方向,防止一直卡
			this.reverseDirect();
			tank.reverseDirect();
		}else{
			this.runnable = false;//其他情况均建议前者等待
		}
	}

	public boolean isTouchBlock(){
		int threshold = 5;
		switch(direction){
			case UP:
				if(y < threshold)
					return true;
			case DOWN:
				if(y > GameFrame.SCREEN_HEIGHT - threshold)
					return true;
			case LEFT:
				if(y < threshold)
					return true;
			case RIGHT:
				if(y > GameFrame.SCREEN_WIDTH - GameFrame.RECORD_WIDTH - threshold)
					return true;
		}
		return false;
	}

	/**
	 * 判断此坦克是否与另一辆坦克即将相撞.
	 * 种类非常多，而且需要不同的应变方式：
	 * 一.此坦克向上运动
	 *      1.遇到向上/向下的坦克 判断条件是一样的
	 *
	 * @param tank 另一辆坦克
	 * @return 返回true为即将撞上
	 */
	public boolean isTouch(Tank tank){
		class Point{//内部点类
			int x;
			int y;
			public Point(){}
			public Point(int x, int y){
				this.x = x;
				this.y = y;
			}
		}
		Point left = new Point(),right = new Point();

		switch(direction){
			case Tank.UP://此坦克 向上
				left = new Point(x,y);
				right = new Point(x + 40, y);
				break;
				//由于上面是return 此处不用break了
			case Tank.DOWN:
				left = new Point(x,y + 60);
				right = new Point(x + 40, y + 60);
				break;
			case Tank.LEFT:
				left = new Point(x - 10,y + 10);
				right = new Point(x - 10, y + 50);
				break;
			case Tank.RIGHT:
				left = new Point(x + 50,y + 10);
				right = new Point(x + 50, y + 50);
				break;
			default:
				System.out.println("坦克不可能有第四个方向！");
				break;
		}

		if(isPointInTank(left.x,left.y,tank) || isPointInTank(right.x, right.y, tank)){
			return true;//只要这两个点在另一坦克范围内，就算碰撞
		}
		return false;
	}

	public boolean isPointInTank(int x, int y, Tank tank){
		switch(tank.direction){
			case Tank.UP:
			case Tank.DOWN:
				if(x > tank.x && x < tank.x + 40 && y > tank.y && y < tank.y + 60)
					return true;
			case LEFT:
			case RIGHT:
				if(x > tank.x - 10 && x < tank.x + 50 && y > tank.y + 10 && y < tank.y + 50)
					return true;
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

	//开火函数 各种坦克都有自己的方式，此处定义他们相同的模板
	class Point{
		int x;
		int y;
		public Point(){
		}
		public Point(int x, int y){
			this.x = x;
			this.y = y;
		}
	}

	public Point getTankMouth(){
		int mouthX = x;
		int mouthY = y;
		switch(direction){//不同方向要不同地调整像素，保证炮弹从炮口射出
			case Tank.UP:
				mouthX = x + 20 - ball.getSize() / 2;
				mouthY = y - ball.getSize() / 2;
				break;
			case Tank.DOWN:
				mouthX = x + 20 - ball.getSize() / 2;
				mouthY = y + 60 - ball.getSize() / 2;;
				break;
			case Tank.LEFT:
				mouthX = x - 10 - ball.getSize() / 2;;
				mouthY = y + 30 - ball.getSize() / 2;
				break;
			case Tank.RIGHT:
				mouthX = x + 50 - ball.getSize() / 2;;
				mouthY = y + 30 - ball.getSize() / 2;
				break;
		}
		return new Point(mouthX,mouthY);
	}

	abstract void fire();

	public void fire(FireBall ball){
		Point mouth = this.getTankMouth();
		ball.setX(mouth.x);
		ball.setY(mouth.y);

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

	public FireBall getBall(){
		return ball;
	}

	public void setBall(FireBall ball){
		this.ball = ball;
	}

	public boolean isRunnable(){
		return runnable;
	}

	public void setRunnable(boolean runnable){
		this.runnable = runnable;
	}

	public boolean isStop(){
		return stop;
	}

	public void setStop(boolean stop){
		this.stop = stop;
	}
}
