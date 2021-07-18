package tool;

import tank.Boss;
import tank.FireBall;
import tank.Tank;

import java.awt.*;

/**
 * @author CPF 创建于： 2021/7/5 11:45
 * @version 1.0
 */
public class MyTool{

	/*绘制一局游戏的记录数据*/
	public static void drawRecord(Graphics g, Record record){
		int x = record.getX();
		int y = record.getY();

		/*标题*/
		g.setColor(Color.DARK_GRAY);//文字黑色
		g.setFont(new Font("宋体", Font.BOLD, 20));//设置字体、加粗、字号
		g.drawString("玩家游戏数据：", x, y);
		/*游戏时间*/
		long time = (record.getGameEndTime() - record.getGameStartTime()) / 1000;//一共多少秒
		/*
		1 hour = 60 minutes
		1 minute = 60 seconds
		1 second = 1000 millisecond
		假如有100000秒：
		100000 % 60             = 秒数
		(100000 % 3600) / 60    = 分钟数
		100000 / 3600           = 小时数
		*/
		long hour = time / 3600;          //有多少小时
		long minute = (time % 3600) / 60;   //有多少分钟
		long second = time % 60;            //有多少秒
		String gameTime = "游戏持续时间：";
		if(hour != 0) gameTime += hour + "时";
		if(minute != 0) gameTime += minute + "分";
		gameTime += second + "秒";

		g.setFont(new Font("宋体", Font.BOLD, 15));
		g.drawString(gameTime, x, y + 30);

		g.drawString("击毁坦克数量如下：", x, y + 60);
		drawTank(g, x, y + 90, Tank.UP, Tank.ENEMY);
		g.drawString("X " + record.getDestroyEnemyNum(), x + 50, y + 120);
		drawTank(g, x, y + 165, Tank.UP, Tank.BOSS);
		g.drawString("X " + record.getDestroyBossNum(), x + 50, y + 195);

		g.setColor(Color.DARK_GRAY);
		g.drawString("被击毁次数如下：", x, y + 250);
		drawTank(g, x, y + 270, Tank.UP, Tank.HERO);
		g.drawString("X " + record.getDeaths(), x + 50, y + 300);




	}

	public static int reverseDirection(int direction){
		switch(direction){
			case Tank.UP:
				return Tank.DOWN;
			case Tank.DOWN:
				return Tank.UP;
			case Tank.LEFT:
				return Tank.RIGHT;
			case Tank.RIGHT:
				return Tank.LEFT;
			default:
				return 0;
		}
	}


	//绘制Boss坦克
	public static void drawBoss(Graphics g, Boss boss){
		int x = boss.getX();
		int y = boss.getY();
		int direction = boss.getDirection();
		int type = boss.getType();
		int blood = boss.getBlood();
		switch(blood){
			case 5:
				g.setColor(Color.RED);
				break;
			case 4:
				g.setColor(Color.magenta);
				break;
			case 3:
				g.setColor(Color.PINK);
				break;
			case 2:
				g.setColor(Color.blue);
				break;
			case 1:
				g.setColor(Color.GREEN);
				break;
			default:
				g.setColor(Color.DARK_GRAY);
				break;
		}
		drawDirection(g, x, y, direction);
	}


	//绘制普通坦克
	public static void drawTank(Graphics g, Tank tank){//接收坦克直接绘制坦克 最常用！
		int x = tank.getX();
		int y = tank.getY();
		int direction = tank.getDirection();
		int type = tank.getType();
		drawTank(g, x, y, direction, type);
	}

	public static void drawTank(Graphics g, int x, int y, int direction, int type){//坐标绘制
		switch(type){
			case Tank.HERO://我方坦克
				g.setColor(Color.cyan);
				break;
			case Tank.ENEMY://敌方普通坦克
				g.setColor(Color.orange);
				break;
			case Tank.BOSS://敌方boss坦克
				g.setColor(Color.RED);
				break;
			default:
				System.out.println("其他的尚未绘制...");
		}
		drawDirection(g, x, y, direction);
	}//end drawTank

	private static void drawDirection(Graphics g, int x, int y, int direction){
		switch(direction){
			case Tank.UP://炮口朝上的坦克
				g.fill3DRect(x, y, 10, 60, false);//轮子
				g.fill3DRect(x + 30, y, 10, 60, false);//轮子
				g.fill3DRect(x + 10, y + 10, 20, 40, false);//车体

				g.setColor(Color.green);
				g.fillOval(x + 10, y + 20, 20, 20);//炮台

				g.setColor(Color.orange);
				g.drawLine(x + 20, y + 30, x + 20, y);
				break;

			case Tank.DOWN://炮口朝下的坦克
				g.fill3DRect(x, y, 10, 60, false);//轮子
				g.fill3DRect(x + 30, y, 10, 60, false);//轮子
				g.fill3DRect(x + 10, y + 10, 20, 40, false);//车体

				g.setColor(Color.green);
				g.fillOval(x + 10, y + 20, 20, 20);//炮台

				g.setColor(Color.orange);
				g.drawLine(x + 20, y + 30, x + 20, y + 60);//炮管
				break;

			case Tank.LEFT://炮口朝左的坦克
				g.fill3DRect(x - 10, y + 10, 60, 10, false);//车轮
				g.fill3DRect(x - 10, y + 40, 60, 10, false);//车轮
				g.fill3DRect(x, y + 20, 40, 20, false);//车体


				g.setColor(Color.green);
				g.fillOval(x + 10, y + 20, 20, 20);//炮台

				g.setColor(Color.orange);
				g.drawLine(x + 20, y + 30, x - 10, y + 30);//炮管
				break;

			case Tank.RIGHT://炮口朝右的坦克
				g.fill3DRect(x - 10, y + 10, 60, 10, false);//车轮
				g.fill3DRect(x - 10, y + 40, 60, 10, false);//车轮
				g.fill3DRect(x, y + 20, 40, 20, false);//车体


				g.setColor(Color.green);
				g.fillOval(x + 10, y + 20, 20, 20);//炮台

				g.setColor(Color.orange);
				g.drawLine(x + 20, y + 30, x + 50, y + 30);//炮管
				break;
			default:
				System.out.println("其他方向暂时没有绘制...");

		}
	}

	public static void drawFire(Graphics g, FireBall ball){
		int x = ball.getX();
		int y = ball.getY();
		Color color = ball.getColor();
		int size = ball.getSize();
		drawFire(g, x, y, size, color);
	}

	public static void drawFire(Graphics g, int x, int y, int size, Color color){
		g.setColor(color);
		g.fillOval(x, y, size, size);
	}

	//判断一个炮弹是否命中了坦克
	public static boolean isSuccessHit(FireBall ball, Tank tank){
		if(Math.pow(ball.centerX() - tank.centerX(), 2)
				+ Math.pow(ball.centerY() - tank.centerY(), 2)
				< Math.pow(30 + (ball.getSize() / 2.0), 2)){
			return true;
		}
		return false;
	}

}
