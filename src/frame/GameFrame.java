package frame;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import tool.Waves;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Scanner;

/**
 * @author CPF 创建于： 2021/7/1 11:02
 * @version 1.0
 */
public class GameFrame extends JFrame implements WindowListener{
	public static final int SCREEN_WIDTH = 1228;//全屏时整个屏幕最大长宽
	public static final int SCREEN_HEIGHT = 666;
	public static final int RECORD_WIDTH = 200;//给记录内容在右边腾出200像素的框架

	private MyPanel mp = null;

	public GameFrame(){
		System.out.println("请问是否继续上一把游戏？(y/n)");
		Scanner scanner = new Scanner(System.in);
		char select = scanner.next().charAt(0);

		File file = new File("src/data/Panel.dat");

		if(select == 'y' && file.exists()){//存在存档才会去读取
			ObjectInputStream ois = null;
			try{
				ois = new ObjectInputStream(new FileInputStream(file));
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
		}else{//不存在存档就去新开一局游戏，存档后自然会新建那个文件
			mp = new MyPanel();
		}

		this.addWindowListener(this);

		this.addKeyListener(mp);//把画板加入事件监听
		this.add(mp);
		this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);//一开始就设置最大化
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//将关闭按钮与关闭视图结合

		Thread panelThread = new Thread(mp);//面板线程启动，目的是刷新
		panelThread.start();

	}

	/**
	 * 关闭主窗口处理
	 */
	@Override
	public void windowClosed(WindowEvent e){
	}

	@Override
	public void windowClosing(WindowEvent e) {
		mp.warStop();//关闭游戏就先静止
		mp.saveWar();//保存游戏后关闭窗口
		System.out.println("关闭窗口，保存游戏成功！");
	}

	@Override
	public void windowOpened(WindowEvent e){

	}


	@Override
	public void windowIconified(WindowEvent e){

	}

	@Override
	public void windowDeiconified(WindowEvent e){

	}

	@Override
	public void windowActivated(WindowEvent e){

	}

	@Override
	public void windowDeactivated(WindowEvent e){

	}
}
