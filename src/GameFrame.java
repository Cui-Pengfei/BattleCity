import javax.swing.*;
import java.awt.*;

/**
 * @author CPF 创建于： 2021/7/1 11:02
 * @version 1.0
 */
public class GameFrame extends JFrame{

	public static void main(String[] args){
		GameFrame game = new GameFrame();

	}//end main

	public GameFrame(){
		MyPanel mp = new MyPanel();
		this.addKeyListener(mp);//把画板加入事件监听
		this.add(mp);
		this.setSize(600,600);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
