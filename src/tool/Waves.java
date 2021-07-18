package tool;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * @author CPF 创建于： 2021/7/19 0:14
 * @version 1.0
 */
public enum Waves implements Runnable{

	START_MUSIC("src/data/startMusic.wav"),
	HERO_SHORT("src/data/shot.wav"),
	BOMB("src/data/bomb.wav"),
	HERO_DIE("src/data/heroDie.wav"),
	BOSS_HURT("src/data/bossHurt.wav"),
	TURN_BOSS("src/data/turnBoss.wav");

	private String wavePath;

	Waves(String wavePath){
		this.wavePath = wavePath;
	}

	@Override
	public void run() {

		File soundFile = new File(wavePath);

		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}

		AudioFormat format = audioInputStream.getFormat();
		SourceDataLine auline = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

		try {
			auline = (SourceDataLine) AudioSystem.getLine(info);
			auline.open(format);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		auline.start();
		int nBytesRead = 0;
		//这是缓冲
		byte[] abData = new byte[512];

		try {
			while (nBytesRead != -1) {
				nBytesRead = audioInputStream.read(abData, 0, abData.length);
				if (nBytesRead >= 0)
					auline.write(abData, 0, nBytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {
			auline.drain();
			auline.close();
		}

	}
}
