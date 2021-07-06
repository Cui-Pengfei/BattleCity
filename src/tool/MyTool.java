package tool;

import tank.FireBall;
import tank.Tank;

import java.awt.*;

/**
 * @author CPF 创建于： 2021/7/5 11:45
 * @version 1.0
 */
public class MyTool{

	public static void drawTank(Graphics g, Tank tank){//接收坦克直接绘制坦克
		drawTank(g, tank.getX(), tank.getY(), tank.getDirection(), tank.getType());
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
	}//end drawTank

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
