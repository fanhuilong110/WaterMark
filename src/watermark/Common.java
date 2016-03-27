package watermark;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;
/**
 * �����ķ���
 * @author CSDN's Cannel_2020
 *
 */
public class Common {
	/**
	 * ���ô��ھ���
	 * @param window ��Ҫ���е�Window����
	 */
	public static void setCentered(Window window) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation(new Point((screenSize.width - window.getWidth()) / 2,
				(screenSize.height - window.getHeight()) / 2));
	}
	/**
	 * ���ϵͳ�����еĿ�������
	 * @return ϵͳ�����еĿ�������
	 */
	public static String[] getAvailableFontFamilyNames(){
		// ��ȡϵͳ�е����п�������
		GraphicsEnvironment e = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		String[] temps = e.getAvailableFontFamilyNames();
		String[] fontFamily = new String[temps.length];
		int i = temps.length-1;
		for(String temp : temps){
			fontFamily[i--] = temp;
		}
		return fontFamily;
	}
	/**
	 * ��������и������������
	 * @param panel JPanel��һ��ʵ��
	 */
	public static void setComponentsFont(JPanel panel, Font font) {
		Component[] components = panel.getComponents();
		for (Component cp : components) {
			cp.setFont(font);
		}
	}
	/**
	 * 
	 * @param filename �ļ�������
	 * @return �ļ��ĺ�׺��
	 */
	public static String getFileExtension(String filename){
		return filename.substring(filename.lastIndexOf(".")+1).toLowerCase();
	}
	/**
	 * @param filename
	 * @return ͼƬ�ļ�("bmp", "gif", "jpg", "jpeg", "png")���أ�true
	 */
	public static boolean isImageFile(String filename){
		String imageExtendsion[] = {"bmp", "gif", "jpg", "jpeg", "png"};
		String fileExtendsion = getFileExtension(filename);
		for(int i = 0; i < imageExtendsion.length; ++i){
			if(imageExtendsion[i].equals(fileExtendsion)){
				return true;
			}
		}
		return false;
	}
	/**
	 * ���һ���ļ����µ�����ͼƬ�ļ�
	 * @param filepath �ļ��е�·��
	 * @return �ļ����µ�����ͼƬ�ļ��ľ���·��
	 */
	public static ArrayList<String> getImageFiles(String filepath){
		ArrayList<String> imgFileList = new  ArrayList<String>();
		File file = new File(filepath);
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (!files[i].isDirectory()) {
				String filename = files[i].getName();
				if(Common.isImageFile(filename)){
					imgFileList.add(filepath + "\\"+filename);
				}
			}
		}
		return imgFileList;
	}
	/**
	 * @return ��ˮӡ����ļ������ļ�������
	 */
	public static String getNewFileorDirName(String filepath) {
		String new_Filename = "\\New_"
				+ filepath.substring(filepath.lastIndexOf("\\") + 1);
		return new_Filename;
	}

}
