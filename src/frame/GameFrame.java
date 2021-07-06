package frame;

import javax.swing.*;

/**
 * @author CPF 创建于： 2021/7/1 11:02
 * @version 1.0
 */
public class GameFrame extends JFrame{
	public static final int width = 1280;
	public static final int height = 666;

	public GameFrame(){
		MyPanel mp = new MyPanel();
		this.addKeyListener(mp);//把画板加入事件监听
		this.add(mp);
		this.setSize(width, height);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Thread panelThread = new Thread(mp);//面板线程启动，目的是刷新
		panelThread.start();
	}
}
