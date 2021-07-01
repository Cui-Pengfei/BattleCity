import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author CPF 创建于： 2021/7/1 10:34
 * @version 1.0
 */
public class MyPanel extends JPanel implements KeyListener{//我的画板
	private int DEFAULT_X = 200;//坦克坐标
	private int DEFAULT_Y = 200;
	public static int TANK_SPEED = 3;//坦克移速

	private Tank tank = new Hero(DEFAULT_X, DEFAULT_Y, Tank.UP);//本画板上坦克的接收器




	@Override
	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.DARK_GRAY);//设置画板的背景颜色
		g.fillRect(0, 0, 800, 800);

		drawTank(g, tank.getX(), tank.getY(), tank.getDirection(), tank.getType());

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
		int tankY = tank.getY();
		int tankX = tank.getX();
		switch(receive){
			case KeyEvent.VK_DOWN:
				tank.setDirection(Tank.DOWN);
				if(tankY + 60 + TANK_SPEED >= 600){
					tankY = 600 - 60;
				}else{
					tankY += TANK_SPEED;
				}
				tank.setY(tankY);
				break;
			case KeyEvent.VK_UP:
				tank.setDirection(Tank.UP);
				if(tankY - TANK_SPEED <= 0){
					tankY = 0;
				}else{
					tankY -= TANK_SPEED;
				}
				tank.setY(tankY);
				break;
			case KeyEvent.VK_RIGHT:
				tank.setDirection(Tank.RIGHT);
				if(tankX + 50 + TANK_SPEED >= 600){
					tankX = 600 - 50;
				}else{
					tankX += TANK_SPEED;
				}
				tank.setX(tankX);
				break;
			case KeyEvent.VK_LEFT:
				tank.setDirection(Tank.LEFT);
				if(tankX - 10 - TANK_SPEED <= 0){
					tankX = 0;
				}else{
					tankX -= TANK_SPEED;
				}
				tank.setX(tankX);
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
