package tank;

import frame.GameFrame;

import java.awt.*;
import java.io.Serializable;

/**
 * @author CPF 创建于： 2021/7/5 15:24
 * @version 1.0
 * 这是坦克发射的炮弹,是独立的线程
 */
public class FireBall extends Thread implements Serializable{
	private int x;
	private int y;//坐标
	private int direction;//炮弹打向的方向 上下左右，与坦克炮管方向一致
	private int size;//直径
	private Color color;//默认炮弹颜色是红色
	private int type;//哪种坦克的炮弹
	private boolean live = true;//炮弹是否存活
	private int speed;//越大速度越快
	private boolean stop = false;

	public FireBall(int x, int y, int type, int direction, int size, Color color){
		this.color = color;
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.size = size;
		this.type = type;
	}

	//这是最基本的构造器，颜色和大小用默认值
	public FireBall(int x, int y, int direction, int type){
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.type = type;
	}

	public FireBall(int size, Color color){
		this.size = size;
		this.color = color;
	}

	public FireBall(int size, Color color, int type, int speed){
		this.size = size;
		this.color = color;
		this.type = type;
		this.speed = speed;
	}

	public FireBall(int x, int y, int direction, int size, Color color, int type, boolean live, int speed){
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.size = size;
		this.color = color;
		this.type = type;
		this.live = live;
		this.speed = speed;
	}

	//炮弹的圆心横坐标
	public int centerX(){
		return x + size / 2;
	}
	//炮弹的圆心纵坐标
	public int centerY(){
		return y + size / 2;
	}


	@Override
	public void run(){
		while(live){
			if(!stop){//此处控制炮弹静止
				switch(direction){
					case Tank.UP:
						y -= speed;
						break;
					case Tank.DOWN:
						y += speed;
						break;
					case Tank.LEFT:
						x -= speed;
						break;
					case Tank.RIGHT:
						x += speed;
						break;
					//不会有default,为了使编译器限定死，以后可以把方向设计成枚举类型
				}
			}

			try{
				Thread.sleep(10);//无限循环不能总是运行呀
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			if(x < 0 || x > GameFrame.SCREEN_WIDTH - GameFrame.RECORD_WIDTH || y < 0 || y > GameFrame.SCREEN_HEIGHT){//炮弹超出战场就退线程
				live = false;
				//break;
			}
		}
	}//end run

	public static FireBall copyBall(FireBall ball){
		int x = ball.x;
		int y = ball.y;//坐标
		int direction = ball.direction;//炮弹打向的方向 上下左右，与坦克炮管方向一致
		int size = ball.size;//直径
		Color color = ball.color;//默认炮弹颜色是红色
		int type = ball.type;//哪种坦克的炮弹
		boolean live = ball.live;//炮弹是否存活
		int speed = ball.speed;//越大速度越快

		return new FireBall(x,y,direction,size,color,type,live,speed);
	}

	public boolean isLive(){
		return live;
	}

	public void setLive(boolean live){
		this.live = live;
	}

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

	public Color getColor(){
		return color;
	}

	public void setColor(Color color){
		this.color = color;
	}

	public int getSize(){
		return size;
	}

	public void setSize(int size){
		this.size = size;
	}

	public int getType(){
		return type;
	}
	public void setType(int type){
		this.type = type;
	}

	public boolean isStop(){
		return stop;
	}

	public void setStop(boolean stop){
		this.stop = stop;
	}
}
