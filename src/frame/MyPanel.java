package frame;

import tank.*;
import tool.MyTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

/**
 * @author CPF 创建于： 2021/7/1 10:34
 * @version 1.0
 */
public class MyPanel extends JPanel implements KeyListener, Runnable{//我的画板
	private final int DEFAULT_X = 250;//我方坦克坐标
	private final int DEFAULT_Y = 400;

	private Tank tank = new Hero(DEFAULT_X, DEFAULT_Y, Tank.UP);//本画板上可操控的坦克

	{
		tank.start();//我方坦克也运行
	}

	private Vector<Enemy> army = new Enemy().army(10);//此处其实Vector更好
	private Boss boss = new Boss(200, 200, Tank.UP);

	{//敌人坦克 要么在这里开启，要么在army函数内开启，都一样
		for(Enemy enemy : army){
			enemy.start();
		}
		boss.start();
	}

	@Override
	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.DARK_GRAY);//设置画板的背景颜色
		g.fillRect(0, 0, GameFrame.width, GameFrame.height);

		//在所有绘制前，先检测是否有坦克被我打死
		for(FireBall ball : tank.getBalls()){//检测我的每一发炮弹
			if(!ball.isLive())
				//break;//如果此子弹已经死掉，就不去下面比较了  重大低级失误，此处应该是continue,退出一颗炮弹的追踪！！！！！！
				continue;

			//这里一定要判断是否在存活，不然这些坦克就是死亡幽灵....
			if(boss.isLive() && MyTool.isSuccessHit(ball, boss)){//命中
				ball.setLive(false);
				boss.setLive(false);
				//break;//如果此子弹已经死掉，就不去下面比较了  重大低级失误，此处应该是continue,退出一颗炮弹的追踪！！！！！！
				continue;
			}

			for(Enemy enemy : army){
				if(enemy.isLive() && MyTool.isSuccessHit(ball, enemy)){//命中
					ball.setLive(false);
					enemy.setLive(false);
					//break;//如果此子弹已经死掉，就不去下面比较了  重大低级失误，此处应该是continue,退出一颗炮弹的追踪！！！！！！
					continue;
				}
			}
		}//检查命中 结束


		//绘制敌方坦克,并尽量避免相撞
		int bump = 0;
		for(Enemy enemy : army){
			/*for(Enemy enemy1 : army){
				if(enemy != enemy1){//自身不能相比
					if(enemy.isTouch(enemy1)){
						enemy.reverseDirect();
					}
				}//碰撞检测次数完毕
			}*/
			if(enemy.isLive())//存活的坦克才能绘制出来
				MyTool.drawTank(g, enemy);
			//绘制敌方炮弹
			Vector<FireBall> balls = enemy.getBalls();
			if(balls.size() != 0){
				for(FireBall ball : balls){
					if(ball.isLive())//只有存活的炮弹才能被刷新
						MyTool.drawFire(g, ball);//把每个炮弹都画出来
				}
			}
		}//敌方·普通坦克


		//绘制Boss 及炮弹
		if(boss.isLive())
			MyTool.drawTank(g, boss);
		Vector<FireBall> bossBalls = boss.getBalls();
		for(FireBall bossBall : bossBalls){
			if(bossBall.isLive())//只有存活的炮弹才能被刷新
				MyTool.drawFire(g, bossBall);//把每个炮弹都画出来
		}

		//一辆我方坦克向上 可移动
		//暂时我无敌
		MyTool.drawTank(g, tank.getX(), tank.getY(), tank.getDirection(), tank.getType());

		//绘制我方炮弹
		Vector<FireBall> balls = tank.getBalls();
		if(balls.size() != 0){
			for(FireBall ball : balls){
				if(ball.isLive())//只有存活的炮弹才能被刷新
					MyTool.drawFire(g, ball);//把每个炮弹都画出来
			}
		}


	}

	@Override
	public void keyPressed(KeyEvent e){
		int receive = e.getKeyCode();
		switch(receive){
			case KeyEvent.VK_DOWN:
				tank.setDirection(Tank.DOWN);//我方坦克也在运行中，按键改变方向
				break;
			case KeyEvent.VK_UP:
				tank.setDirection(Tank.UP);
				break;
			case KeyEvent.VK_RIGHT:
				tank.setDirection(Tank.RIGHT);
				break;
			case KeyEvent.VK_LEFT:
				tank.setDirection(Tank.LEFT);
				break;
			case KeyEvent.VK_SPACE://空格发射子弹
				tank.fire();
				break;
		}
		this.repaint();

	}

	@Override
	public void keyTyped(KeyEvent e){
	}

	@Override
	public void keyReleased(KeyEvent e){
	}

	@Override
	public void run(){
		while(true){


			this.repaint();//时刻重绘
			try{
				Thread.sleep(2);//其他进程都是10毫秒刷新一次，那面板要小于10秒刷新
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}

	}
}
