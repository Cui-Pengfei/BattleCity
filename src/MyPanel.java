import javax.swing.*;
import java.awt.*;

/**
 * @author CPF 创建于： 2021/7/1 10:34
 * @version 1.0
 */
public class MyPanel extends JPanel{//我的画板
	private Tank tank = null;


	@Override
	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.DARK_GRAY);//设置画板的背景颜色
		g.fillRect(0, 0, 800, 800);

		drawTank(g, 100, 200, Tank.UP, Tank.HERO);
		drawTank(g, 200, 200, Tank.DOWN, Tank.ENEMY);
		drawTank(g, 300, 200, Tank.LEFT, Tank.HERO);
		drawTank(g, 400, 200, Tank.RIGHT, Tank.BOSS);
	}

	public void drawTank(Graphics g, int x, int y, int direction, int type){
		switch(type){
			case Tank.HERO://我方坦克
				tank = new Hero(x, y, direction);
				g.setColor(Color.cyan);
				break;
			case Tank.ENEMY://敌方普通坦克
				tank = new Enemy(x, y, direction);
				g.setColor(Color.orange);
				break;
			case Tank.BOSS://敌方boss坦克
				tank = new Boss(x, y, direction);
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

			case Tank.RIGHT:
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
}
