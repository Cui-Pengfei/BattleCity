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


	private int enemyNum = 20; //enemy数量
	private Tank tank = new Hero(DEFAULT_X, DEFAULT_Y, Tank.UP);//我方坦克
	private Vector<Enemy> army = enemyArmy(enemyNum);//敌人坦克军团
	private Boss boss = new Boss(200, 200, Tank.UP);//敌方Boss

	{//敌人坦克 要么在这里开启，要么在army函数内开启，都一样
		for(Enemy enemy : army){
			enemy.start();
		}
		boss.start();
		tank.start();//我方坦克也运行
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
				ball.setLive(false);//立刻使炮弹死亡，不能让它和小球血量一起掉
				int blood = boss.getBlood() - 1;
				boss.setBlood(blood);
				if(blood == 0){
					boss.setLive(false);
				}
				//break;//如果此子弹已经死掉，就不去下面比较了  重大低级失误，此处应该是continue,退出一颗炮弹的追踪！！！！！！
				continue;
			}

			for(Enemy enemy : army){
				//首先要有敌方(因为会清理集合) 其次敌方要或者 再者要命中 才算打倒了！
				if(enemy != null && enemy.isLive() && MyTool.isSuccessHit(ball, enemy)){//命中
					ball.setLive(false);
					enemy.setLive(false);
					army.remove(enemy);//把被摧毁的坦克从集合中清理出去
					break;//一发炮弹只能毁掉一辆坦克。因此退出其他坦克检查，
					// 但是没有退出我方这发炮弹的检查，好在此处位于检查的地步，也就要退出了
				}
			}
		}//检查命中 结束

		//此处防止碰撞
		for(Enemy enemy : army){
			if(!enemy.isLive()){
				continue;//死掉的坦克不判断
			}
			for(Enemy enemy1 : army){
				if(enemy == enemy1)//自己不与自己比较
					//break;//又犯了致命错误;此处应该是结束本轮循环，不是全部结束
					continue;
				if(enemy.isTouch(enemy1)){
					enemy.touchReact(enemy1);//碰撞反应
					break;//与一辆碰撞就不要检测其他的了
				}else{//因为有可能之前置为false
					enemy.setRunnable(true);//畅通行
				}

			}
		}


		//绘制敌方坦克
		for(Enemy enemy : army){
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
			MyTool.drawBoss(g, boss);
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


	}//end paint()

	//各品种大军
	public Vector<Enemy> enemyArmy(int number){
		Vector<Enemy> army = new Vector<>(number);
		for(int i = 0; i < number; i++){
			army.add(new Enemy((i % 5 + 1) * 100, ((i / 5) + 1) * 100, Tank.DOWN));
		}
		return army;
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
			case KeyEvent.VK_S://停止画面
				boss.setStop(true);
				for(Enemy enemy : army){
					enemy.setStop(true);
				}
				break;
			case KeyEvent.VK_B://启动画面
				for(Enemy enemy : army){
					enemy.setStop(false);
				}
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
			if(army.size() < enemyNum){//只要小于额定的数量，就补充一辆坦克
				int x = (int) (Math.random() * GameFrame.width);
				int y = (int) (Math.random() * GameFrame.height);
				int direction = (int)(Math.random() * 4);
				Enemy enemy = new Enemy(x, y, direction);
				enemy.start();
				army.add(enemy);
			}

			this.repaint();//时刻重绘
			try{
				Thread.sleep(1);//其他进程都是10毫秒刷新一次，那面板要小于10秒刷新
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}

	}
}
