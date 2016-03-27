package watermark;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileNameExtensionFilter;

import watermark.CommandButton.ButtonStyle;

public class ButtonAction implements ActionListener {
	private ButtonStyle style;
	private MainFrame mainFrame;
	public static boolean isSingle = true;
    private static int completeCount = 0;
	private JFileChooser fileChooser = new JFileChooser();
	public static ArrayList<FileBean> array;
	public static boolean isDrag = false;//��������
	public ButtonAction(ButtonStyle style){
		this.style = style;
	}
	public void actionPerformed(ActionEvent e) {

		fileChooser.updateUI();// �� UI ��������Ϊ��ǰ�����ֵ��
		mainFrame = MainFrame.getInstance();
		if (style.equals(ButtonStyle.batching)) {
			if (isSingle) {
				mainFrame.setSelectPathBtText("ѡ���ļ���");
				mainFrame.setBatchingBtText("�����ļ�");
				mainFrame.setTitle("���ˮӡ-��ǰ״̬���������");
				isSingle = false;
			} else {
				mainFrame.setSelectPathBtText("ѡ��ͼƬ");
				mainFrame.setBatchingBtText("�������");
				mainFrame.setTitle("���ˮӡ-��ǰ״̬�������ļ�");
				isSingle = true;
			}
		} else if (style.equals(ButtonStyle.selectImage)) {
			String result = getSelectResult();
			if (result != null) {
				mainFrame.getFilepathTF().setText(result);
				mainFrame.getSavepathTF().setText(
						result.substring(0, result.lastIndexOf("\\")) + Common.getNewFileorDirName(result));
			}
		} else if (style.equals(ButtonStyle.selectSavepath)) {
			String result = getSelectResult();
			if (result != null) {
				mainFrame.getSavepathTF().setText(
						result + Common.getNewFileorDirName(mainFrame.getFilepath()));
			}
		} else if (style.equals(ButtonStyle.preview)) {
			String firstFilepath = getFirstFilepath();
			if(firstFilepath ==  null){
				JOptionPane.showMessageDialog(null, "�Ҳ���ͼƬ�ļ���");
				return;
			}
			BufferedImage buffImg = addWatermark(firstFilepath);
			new PreviewImage(buffImg);
		} else if (style.equals(ButtonStyle.drirect)) {
			String savePath = mainFrame.getSavePath();
			if (savePath.equals("")) {
				JOptionPane.showMessageDialog(null, "��ѡ����·����");
				return;
			}
			if (isSingle) {
				// ��ͼ���ˮӡ
				BufferedImage buffImg = addWatermark(mainFrame.getFilepath());
				// ����ˮӡͼƬ
				generate(buffImg, savePath);
				mainFrame.getProgressBar().setValue(mainFrame.getProgressBar().getMaximum());
			} else {
				if(array == null){
					array = getBufferImages(mainFrame.getFilepath());
				}
				// �����ļ���
				File newDirs = new File(mainFrame.getSavePath());
				newDirs.mkdirs();
				completeCount = 0;
				//���������߳�
				Runnable update = getUpdate(mainFrame.getProgressBar(), array.size());
				for (int i = 0; i < array.size(); i++) {
					++completeCount;
					//����������
					new Thread(update).start();
					// ����ˮӡͼƬ
					generate(array.get(i).getBuffer(), 
							savePath + Common.getNewFileorDirName(array.get(i).getFilename()));
				}
			}
			if(isDrag){
				isDrag = false;
				isSingle = true;
			}
			JOptionPane.showMessageDialog(null, "�����ɹ���");
		}else{
			JOptionPane.showMessageDialog(null, "��û��Ӵ˹���ѽ���ף�");
		}
	}
	/**
	 * 
	 * @return Ԥ��ʱ��ͼƬ�ľ���·��
	 */
	private String getFirstFilepath() {
		String firstFilepath = null;
		if (isSingle) {
			firstFilepath = mainFrame.getFilepath();
		} else {
			ArrayList<String> imageFilenames = Common.getImageFiles(mainFrame.getFilepath());
			if(!imageFilenames.isEmpty()){
				firstFilepath = imageFilenames.get(0);
			}
		}
		return firstFilepath;
	}


	/**
	 * @param filepath ͼƬ�ľ���·��
	 * @return ͼ���ˮӡ֮���BufferedImage����
	 */
	private BufferedImage addWatermark(String filepath) {
		Color fontColor = mainFrame.getFontcolor();
		String mark = mainFrame.getMark();
		int toward = mainFrame.getToward();
		Font font = mainFrame.getWaterMarkFont();
		float alpha = mainFrame.getWaterMarkAlpha();
		float scale = mainFrame.getScale();
		BufferedImage buffImg = ImageTool.watermark(filepath, font, fontColor,
				toward, mark, alpha, scale);
		return buffImg;
	}
	/**
	 * ����ˮӡͼƬ
	 * @param buffImg ͼ���ˮӡ֮���BufferedImage����
	 * @param savePath ͼ���ˮӡ֮��ı���·��
	 */
	private void generate(BufferedImage buffImg, String savePath) {
		int temp = savePath.lastIndexOf(".") + 1;
		try {
			ImageIO.write(buffImg, savePath.substring(temp), new File(savePath));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	/**
	 * 
	 * @return ѡ�е��ļ����ļ��е�����
	 */
	private String getSelectResult(){
		if (isSingle && style.equals(ButtonStyle.selectImage)) {
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"ͼƬ�ļ�(*.bmp, *.gif, *.jpg, *.jpeg, *.png)", "bmp",
					"gif", "jpg", "jpeg", "png");
			fileChooser.setFileFilter(filter);
			fileChooser.setDialogTitle("ѡ��ͼƬ�ļ�");
		}else{
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.setDialogTitle("ѡ���ļ�");
		}
		int result = fileChooser.showDialog(null, "ȷ��");
		if (result == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile().getPath();
		}
		return null;
	}
	/**
	 * 
	 * @param progressBar ������
	 * @param maxValue �����������ֵ
	 * @return ���½�������һ��Runnableʵ��
	 */
	private Runnable getUpdate(final JProgressBar progressBar, int maxValue){
		progressBar.setMaximum(maxValue);
        Dimension d = progressBar.getSize();
        final Rectangle rect = new Rectangle(0,0, d.width, d.height);
        return new Runnable(){ 
			public void run() {
	            progressBar.setValue(completeCount);
	            progressBar.paintImmediately(rect);//TODO
	        }  
	    };
	}
	/**
	 * 
	 * @param filepath
	 * @return 
	 */
	private ArrayList<FileBean> getBufferImages(String filepath){
		ArrayList<String> imageFilenames = Common.getImageFiles(filepath);
		if(imageFilenames.size() <= 0){
			JOptionPane.showMessageDialog(null, "�Ҳ���ͼƬ�ļ���");
			return null ;
		}
		ArrayList<FileBean> array = new ArrayList<FileBean>();
		for (int i = 0; i < imageFilenames.size(); i++) {
			String filename = imageFilenames.get(i);
			// ��ͼ���ˮӡ
			BufferedImage buffImg = addWatermark(imageFilenames.get(i));
			array.add(new FileBean(filename, buffImg));
		}
		return array;
	}
}
