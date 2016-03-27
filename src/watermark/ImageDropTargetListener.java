package watermark;

import java.awt.Color;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageDropTargetListener extends DropTargetAdapter {
	private List<File> fileList;;
	private MainFrame mainFrame;
	@SuppressWarnings("unchecked")
	public void drop(DropTargetDropEvent event) {
		// ���ܸ��Ʋ���
		event.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
		// ��ȡ�Ϸŵ�����
		Transferable transferable = event.getTransferable();
		DataFlavor[] flavors = transferable.getTransferDataFlavors();
		
		// �����Ϸ���������������ݸ�ʽ
		for (int i = 0; i < flavors.length; i++) {
			DataFlavor d = flavors[i];
			try {
				// ����Ϸ����ݵ����ݸ�ʽ���ļ��б�
				if (d.equals(DataFlavor.javaFileListFlavor)) {
					// ȡ���ϷŲ�������ļ��б�
					fileList = (List<File>) transferable
							.getTransferData(d);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// ǿ���ϷŲ���������ֹͣ�����Ϸ�Դ
			event.dropComplete(true);
		}

		mainFrame = MainFrame.getInstance();
		if(fileList == null || fileList.size() == 0){//�������κ��ļ�������
			System.out.println("���᣺����϶�ʱȴΪ�գ�");
			return;
		}
		List<File> imgFiles= getImageFiles(fileList);
		if(imgFiles.size() == 0){//������ͼƬ�ļ�������
			return;
		}
		File file = getImageFiles(fileList).get(0);
		String filename = file.getAbsolutePath();
		if(imgFiles.size() == 1){
			mainFrame.getFilepathTF().setText(filename);
			mainFrame.getSavepathTF().setText(filename.substring(0, filename.lastIndexOf("\\"))
					+ Common.getNewFileorDirName(filename));
		}else if(imgFiles.size() > 1){
			mainFrame.getFilepathTF().setText(filename.substring(0, filename.lastIndexOf("\\")));
			mainFrame.getSavepathTF().setText(filename.substring(0, filename.lastIndexOf("\\"))
					+ "\\��ˮӡ��");
			ButtonAction.array = getBufferImages(imgFiles);
			ButtonAction.isDrag = true;
			ButtonAction.isSingle = false;
		}
		BufferedImage buffImg = addWatermark(file);
		new PreviewImage(buffImg);
	}
	/**
	 * @param filepath ͼƬ�ľ���·��
	 * @return ͼ���ˮӡ֮���BufferedImage����
	 */
	private BufferedImage addWatermark(File file) {
		Color fontColor = mainFrame.getFontcolor();
		String mark = mainFrame.getMark();
		int toward = mainFrame.getToward();
		Font font = mainFrame.getWaterMarkFont();
		float alpha = mainFrame.getWaterMarkAlpha();
		float scale = mainFrame.getScale();
		BufferedImage buffImg = ImageTool.watermark(file, font, fontColor,
				toward, mark, alpha, scale);
		return buffImg;
	}
	/**
	 * ���˵���ͼƬ�ļ�
	 * @param fileList ���е��ļ�
	 * @return ͼƬ�ļ���List<File>
	 */
	private List<File> getImageFiles(List<File> fileList){
		List<File> imgFileList = new ArrayList<File>();
		for (int i = 0; i < fileList.size(); i++) {
			File file = fileList.get(i);
			if (!file.isDirectory()) {
				String filename = file.getName();
				if(Common.isImageFile(filename)){
					imgFileList.add(file);
				}
			}
		}
		return imgFileList;
	}
	private ArrayList<FileBean> getBufferImages(List<File> imgFileList){
		ArrayList<FileBean> array = new ArrayList<FileBean>();
		for (int i = 0; i < imgFileList.size(); i++) {
			String filename = imgFileList.get(i).getName();
			// ��ͼ���ˮӡ
			BufferedImage buffImg = addWatermark(imgFileList.get(i));
			array.add(new FileBean(filename, buffImg));
		}
		return array;
	}
}