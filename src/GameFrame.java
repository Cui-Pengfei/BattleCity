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
		this.add(mp);
		this.setSize(800,800);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
