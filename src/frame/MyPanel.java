package frame;

import tank.*;
import tool.MyTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

/**
 * @author CPF 创建于： 2021/7/1 10:34
 * @version 1.0
 */
public class MyPanel extends JPanel implements KeyListener, Runnable, Serializable{//我的画板
	private final int width;//战场的长宽 通过构造器由游戏框架传过来
	private final int height;

	public MyPanel(int width, int height){
		this.width = width;
		this.height = height;
	}

	@Override
	public int getWidth(){
		return width;
	}

	@Override
	public int getHeight(){
		return height;
	}

	private final int DEFAULT_X = 250;//我方坦克坐标
	private final int DEFAULT_Y = 400;

	/*
	事实证明，cache是必不可少的，否则，在判断坦克军团的循环中，又会摧毁坦克军团，这不是进程同步的问题，
	这是一个逻辑漏洞，
	还有，final的集合居然是可以添加元素的，
	 */

	private final int enemyNum = 25; //enemy数量
	private Hero hero = new Hero(DEFAULT_X, DEFAULT_Y, Tank.UP);//我方坦克
	private final Vector<Enemy> army = enemyArmy(enemyNum);//敌人坦克军团
	private final Vector<Enemy> armyCutCache = new Vector<>();//敌人坦克军团缓存

	private final Boss boss = new Boss(200, 200, Tank.UP);//敌方Boss
	private final Vector<Boss> bosses = new Vector<>();//boss军团
	private final Vector<Boss> bossAddCache = new Vector<>();//boss军团新增添的成员
	private final Vector<Boss> bossCutCache = new Vector<>();//boss军团被消灭的成员

	private final Vector<Tank> tankCutCache = new Vector<>();//所有废弃坦克集中处理，主要是它们射出去的炮弹问题

	public void reStart(){
		//hero
		if(hero.isLive())
			hero.start();
		for(FireBall ball : hero.getBalls()){
			//if(ball.isLive())
			ball.start();
		}
		//boss
		for(Boss boss : bosses){
			if(boss.isLive())
				boss.start();
			for(FireBall ball : boss.getBalls()){
				//if(ball.isLive())
					ball.start();
			}
		}
		//enemy
		for(Enemy enemy : army){
			if(enemy.isLive())
				enemy.start();
			for(FireBall ball : enemy.getBalls()){
				//if(ball.isLive())
					ball.start();
			}
		}
	}


	{//敌人坦克 要么在这里开启，要么在army函数内开启，都一样,将来可以设置按钮
		///////////////////////

		bosses.add(boss);
		for(Enemy enemy : army){
			enemy.start();
		}
		boss.start();
		hero.start();//我方坦克也运行
	}

	@Override
	public void paint(Graphics g){
		super.paint(g);
		/*设置战场的背景颜色*/
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, width, height);

		/*检测是否有坦克被我打死*/
		if(hero.getBalls().size() != 0)
		for(FireBall ball : hero.getBalls()){//检测我的每一发炮弹
			if(isHitTarget(ball)){
				hero.ballGrow();//我方坦克击中目标增益
			}
		}

		//再检测是否有坦克被boss击中(击中enemy是没有反馈的)
		for(Boss boss : bosses){
			for(FireBall ball : boss.getBalls()){
				if(ball.isLive() && isHitTarget(ball)){
					boss.returnBlood();
				}
			}
		}

		//检测是否有坦克被enemy炮弹击中
		for(Enemy enemy : army){
			for(FireBall ball : enemy.getBalls()){
				if(ball.isLive() && isHitTarget(ball)){
					//他立功了，也变成了Boss(偷梁换柱，boss没有进入army集合)
					Boss thisBoss = new Boss(enemy.getX(),enemy.getY(),enemy.getDirection());
					thisBoss.setBlood(2);//2滴血 这样就相当于回血了 只后就走boss的路线了
					bosses.add(thisBoss);
					thisBoss.start();
					enemy.setLive(false);//让原来的enemy死掉并移除集合，狸猫换太子
					armyCutCache.add(enemy);
				}
			}
		}

		for(Enemy enemy : army){//这是防止enemy碰撞其他坦克
			insureNoOverlap(enemy);
		}

		for(Boss boss : bosses){//此处防止Boss重叠坦克
			insureNoOverlap(boss);
		}

		insureNoOverlap(hero);//此处防止我方坦克重叠敌方坦克


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
		for(Boss boss1 : bosses){
			if(boss1.isLive())
				MyTool.drawBoss(g, boss1);
			Vector<FireBall> bossBalls = boss1.getBalls();
			for(FireBall bossBall : bossBalls){
				if(bossBall.isLive())//只有存活的炮弹才能被刷新
					MyTool.drawFire(g, bossBall);//把每个炮弹都画出来
			}
		}


		//一辆我方坦克向上 可移动
		if(hero.isLive())
		MyTool.drawTank(g, hero.getX(), hero.getY(), hero.getDirection(), hero.getType());

		//绘制我方炮弹
		Vector<FireBall> balls = hero.getBalls();
		if(balls.size() != 0){
			for(FireBall ball : balls){
				if(ball.isLive())//只有存活的炮弹才能被刷新
					MyTool.drawFire(g, ball);//把每个炮弹都画出来
			}
		}

		//所有废弃的坦克都集中起来，要绘制它们发射的子弹，只有一个坦克发射的子弹也死亡了，才彻底清除掉他
		tankCutCache.addAll(armyCutCache);
		tankCutCache.addAll(bossCutCache);
		Vector<Tank> cache = new Vector<>();
		boolean tankClear = true;
		for(Tank tank : tankCutCache){
			for(FireBall ball : tank.getBalls()){
				if(ball.isLive()){
					tankClear = false;
					MyTool.drawFire(g, ball);//把每个炮弹都画出来
				}
			}
			if(tankClear){
				cache.add(tank);
			}
		}
		tankCutCache.removeAll(cache);


		//清除坦克的变化，集合发生变动
		army.removeAll(armyCutCache);//防止在遍历集合的同时减少集合元素数量
		armyCutCache.clear();
		bosses.addAll(bossAddCache);//只能等本轮检查完毕再添加，不然就成了【在本轮遍历的同时，增加了遍历次数】
		bossAddCache.clear();
		bosses.removeAll(bossCutCache);
		bossCutCache.clear();

	}//end paint()

	public Vector<Enemy> enemyArmy(int number){//敌方坦克大军
		Vector<Enemy> army = new Vector<>(number);
		for(int i = 0; i < number; i++){
			army.add(new Enemy((i % 5 + 1) * 100, ((i / 5) + 1) * 100, Tank.DOWN));
		}
		return army;
	}

	/**
	 * 此方法可以防着一辆坦克重叠画面中的其他坦克.
	 * @param tank 此坦克不会重叠其他坦克
	 */
	public void insureNoOverlap(Tank tank){
		///////////////////////////////////////////////////////////////need to optimize
		if(!tank.isLive())
			return;//如果这个坦克死掉，就不检测了(由于清除坦克的迟滞性，死亡坦克可能被检测到) 好像也检测不到，再看了
		Vector<Tank> needDetectTanks = new Vector<>();
		needDetectTanks.addAll(army);
		needDetectTanks.addAll(bosses);
		needDetectTanks.add(hero);

		for(Tank needDetectTank : needDetectTanks){
			if(needDetectTank == tank || !needDetectTank.isLive())
				continue;//自身、死掉的坦克 不用比较
			if(tank.isTouch(needDetectTank)){
				tank.touchReact(needDetectTank);//即将重叠的坦克 一般会静止
				break;//只检测一次事故
			}else{
				tank.setRunnable(true);//由于break的存在，没有任何坦克即将重叠，才会到这一步
			}
		}
	}//end detect

	/**
	 * 监测一个小球是否击中目标.
	 * 并完成击中目标所导致的目标的变化，此炮弹给发射者带来的反馈不在此处完成.
	 * @param ball 被监测的小球.
	 */
	public boolean isHitTarget(FireBall ball){
		if(!ball.isLive())
			return false;//死掉的炮弹当然击不中

		Vector<Tank> tanks = findTargetByBall(ball);//得到的都是此炮弹可攻击的坦克

		for(Tank tank : tanks){
			if(tank.isLive() && MyTool.isSuccessHit(ball, tank)){//命中
				ball.setLive(false);//立刻使炮弹死亡

			//击中boss
				if(tank.getType() == Tank.BOSS){//Boss坦克被击中只是掉血，血掉完才会死亡
					Boss boss = (Boss)tank;//此处boss就近原则，遮盖掉了全局变量
					boss.lessBlood();
					if(boss.getBlood() == 0){
						boss.setLive(false);
						bossCutCache.add(boss);
					}
			//击中hero
				}else if(tank.getType() == Tank.HERO){//hero被击中，会立刻销毁，子弹所带来的回馈不在此处完成
					tank.setLive(false);
					tankCutCache.add(tank);
			//击中enemy
				}else{//如果被击中的是enemy，那要看是被谁击中的？
					Enemy enemy = (Enemy)tank;
					if(ball.getType() == Tank.BOSS){//被Boss击中，enemy获得增益，加血
						Boss thisBoss = new Boss(enemy.getX(),enemy.getY(),enemy.getDirection());
						thisBoss.setBlood(2);//2滴血 这样就相当于回血了 只后就走boss的路线了
						bossAddCache.add(thisBoss);
						thisBoss.start();

						enemy.setLive(false);//让原来的enemy死掉并移除集合，狸猫换太子
						armyCutCache.add(enemy);//不能在遍历集合的时候删除集合元素，只能利用缓冲区
						return false;//boss击中enemy没有回馈
					}else{//不然就是hero打击的，就死掉了，不可能是自己打的，一开始就限定了坦克类型
						tank.setLive(false);
						armyCutCache.add(enemy);
					}
				}
				return true;//只要击中任意一个坦克，都不会再去检测其他坦克了
			}
		}
		return false;//能到达这里就是没有击中
	}

	public Vector<Tank> findTargetByBall(FireBall ball){
		Vector<Tank> tanks = new Vector<>();
		switch(ball.getType()){
			case Tank.HERO:
				tanks.addAll(army);
				tanks.addAll(bosses);
				break;
			case Tank.ENEMY:
				tanks.add(hero);
				break;
			case Tank.BOSS:
				tanks.add(hero);
				tanks.addAll(army);//暂时设定成【boss可以打击enemy】
				break;
		}
		return tanks;
	}

	@Override
	public void keyPressed(KeyEvent e){
		int receive = e.getKeyCode();

		if(hero.isLive())//只有坦克活着，才会执行坦克的操作
		switch(receive){
			case KeyEvent.VK_DOWN:
				hero.setDirection(Tank.DOWN);//我方坦克也在运行中，按键改变方向
				break;
			case KeyEvent.VK_UP:
				hero.setDirection(Tank.UP);
				break;
			case KeyEvent.VK_RIGHT:
				hero.setDirection(Tank.RIGHT);
				break;
			case KeyEvent.VK_LEFT:
				hero.setDirection(Tank.LEFT);
				break;
			case KeyEvent.VK_SPACE://空格发射子弹
				hero.fire();
				break;
		}

		switch(receive){
			case KeyEvent.VK_S://停止画面 hero不停止
				//boss及其炮弹静止
				for(Boss boss1 : bosses){
					boss1.setStop(true);
					for(FireBall ball : boss1.getBalls()){
						ball.setStop(true);
					}

				}
				//enemy及其炮弹静止
				for(Enemy enemy : army){
					enemy.setStop(true);
					for(FireBall ball : enemy.getBalls()){
						ball.setStop(true);
					}
				}
				//在那一瞬间死掉的坦克的炮弹
				for(Tank tank : tankCutCache){
					for(FireBall ball : tank.getBalls()){
						ball.setStop(true);
					}
				}
				break;
			case KeyEvent.VK_B://启动画面
				for(Enemy enemy : army){
					enemy.setStop(false);
					for(FireBall ball : enemy.getBalls()){
						ball.setStop(false);
					}

				}
				for(Boss boss1 : bosses){
					boss1.setStop(false);
					for(FireBall ball : boss1.getBalls()){
						ball.setStop(false);
					}
				}
				for(Tank tank : tankCutCache){
					for(FireBall ball : tank.getBalls()){
						ball.setStop(false);
					}
				}
				break;
			case KeyEvent.VK_V://保存战场类到文件
				ObjectOutputStream oos = null;
				try{
					oos = new ObjectOutputStream(new FileOutputStream("src/data/Panel.dat"));
					oos.writeObject(this);
				}catch(IOException ioException){
					System.out.println("打开文件异常：" + e);
				}finally{
					if(oos!=null){
						try{
							oos.close();
						}catch(IOException ioException){
							System.out.println("对象输出流关闭异常：" + e);
						}
					}
				}
				break;

		}
		this.repaint();//其实可有可无

	}

	@Override
	public void keyTyped(KeyEvent e){
	}

	@Override
	public void keyReleased(KeyEvent e){
	}

	@Override
	public void run(){
		long start = System.currentTimeMillis();
		long end;
		while(true){
			end = System.currentTimeMillis();
			if(end - start > 3 * 1000){
				start = end;
				if(!hero.isLive()){//3秒检测以下死没死，死掉了就复活
					hero = new Hero(0,0,Tank.DOWN);
					hero.start();
				}
			}

			/*if(army.size() < enemyNum){//只要小于额定的数量，就补充一辆坦克
				int x = (int) (Math.random() * (GameFrame.SCREEN_WIDTH - GameFrame.RECORD_WIDTH));
				int y = (int) (Math.random() * GameFrame.SCREEN_HEIGHT);
				int direction = (int)(Math.random() * 4);
				Enemy enemy = new Enemy(x, y, direction);
				enemy.start();
				army.add(enemy);
			}*/

			this.repaint();//时刻重绘
			try{
				Thread.sleep(5);//其他进程都是10毫秒刷新一次，那面板要小于10秒刷新
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}

	}
}
