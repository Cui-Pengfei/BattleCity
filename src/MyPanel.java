import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

/**
 * @author CPF 创建于： 2021/7/1 10:34
 * @version 1.0
 */
public class MyPanel extends JPanel implements KeyListener{//我的画板
	private final int DEFAULT_X = 200;//坦克坐标
	private final int DEFAULT_Y = 200;

	private Tank tank = new Hero(DEFAULT_X, DEFAULT_Y, Tank.UP);//本画板上可操控的坦克


	@Override
	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.DARK_GRAY);//设置画板的背景颜色
		g.fillRect(0, 0, 800, 800);

		//三辆敌方向下坦克
		Enemy enemy = new Enemy();
		Vector<Enemy> army = enemy.army(10);
		for(Enemy enemy1 : army){
			drawTank(g, enemy1);
		}

		//一辆我方坦克向上 可移动
		drawTank(g, tank.getX(), tank.getY(), tank.getDirection(), tank.getType());

	}

	public void drawTank(Graphics g, Tank tank){//接收坦克直接绘制坦克
		drawTank(g, tank.getX(),tank.getY(),tank.getDirection(),tank.getType());
	}

	public void drawTank(Graphics g, int x, int y, int direction, int type){//坐标绘制
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

	@Override
	public void keyPressed(KeyEvent e){
		int receive = e.getKeyCode();
		switch(receive){
			case KeyEvent.VK_DOWN:
				tank.moveDown();
				break;
			case KeyEvent.VK_UP:
				tank.moveUp();
				break;
			case KeyEvent.VK_RIGHT:
				tank.moveRight();
				break;
			case KeyEvent.VK_LEFT:
				tank.moveLeft();
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
}
