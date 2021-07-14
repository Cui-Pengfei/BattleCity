package frame;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Scanner;

/**
 * @author CPF 创建于： 2021/7/1 11:02
 * @version 1.0
 */
public class GameFrame extends JFrame{
	public static final int SCREEN_WIDTH = 1228;//全屏时整个屏幕最大长宽
	public static final int SCREEN_HEIGHT = 666;
	public static final int RECORD_WIDTH = 200;//给记录内容在右边腾出200像素的框架

	public GameFrame(){
		MyPanel mp = null;

		System.out.println("请问重新开始游戏？(y/n)");
		Scanner scanner = new Scanner(System.in);
		char select = scanner.next().charAt(0);
		if(select == 'y'){
			ObjectInputStream ois = null;
			try{
				ois = new ObjectInputStream(new FileInputStream("src/data/Panel.dat"));
				Object myPanel = ois.readObject();
				mp = (MyPanel)myPanel;
				mp.reStart();
			}catch(IOException | ClassNotFoundException e){
				System.out.println("打开对象文件异常：" + e);
			}finally{
				if(ois != null)
					try{
						ois.close();
					}catch(IOException e){
						System.out.println("关闭对象输入流异常：" + e);
					}
			}
		}else{
			mp = new MyPanel(SCREEN_WIDTH - RECORD_WIDTH, SCREEN_HEIGHT);//规定战场的长宽
		}

		this.addKeyListener(mp);//把画板加入事件监听
		this.add(mp);
		this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);//一开始就设置最大化
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//将关闭按钮与关闭视图结合

		Thread panelThread = new Thread(mp);//面板线程启动，目的是刷新
		panelThread.start();
	}
}
