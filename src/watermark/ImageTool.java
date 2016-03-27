package watermark;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
/**
 * 
 * @author CSDN's Cannel_2020
 *
 */
public class ImageTool {

	private final static int DOWN_RIGHT = 0;
	private final static int DOWN_LEFT= 1;
	private final static int UP_RIGHT = 2;
	private final static int UP_LEFT= 3;
	private final static int MIDDLE = 4;
	private final static int UP_LEFT_TO_DOWN_RIGHT = 5;
	private final static int UP_RIGHT_TO_DOWN_LEFT = 6;
	// ʵ�ּ�ˮӡ����
	public static BufferedImage watermark(String filepath, Font font, Color color,  int toward, String mark, float alpha,
			float scale) {
		return watermark(new File(filepath), font, color, toward, mark, alpha, scale);
	}
	private static Image getFileImage(File file){
		Image image = null;
		if(Common.getFileExtension(file.getName()).equals("bmp")){
			FileInputStream in = null;
			try {
				in = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			image = BMPLoader.read(in);
		}else{
			try {
				image = ImageIO.read(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return image;
	}
	/**
	 * ����һ��Ԥ����ͼ�����͵�BufferedImage
	 * @param filepath ͼƬ�ļ���·��
	 */
	private static BufferedImage getMyBufferedImage(File file, float scale){
		
		Image image = getFileImage(file);// �õ�Image����
		BufferedImage buffImg = null;
		try {
			buffImg = javax.imageio.ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//�����
		int width = (int) (image.getWidth(null));
		int height = (int) (image.getHeight(null));
		// ����һ��Ԥ����ͼ�����͵�BufferedImage
		buffImg = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
		// ����Graphics2D��������BufferedImage�����ϻ�ͼ
		Graphics2D g2d = buffImg.createGraphics();
		// ����ͼ�������ĵĵ�ǰ��ɫΪ��ɫ
		g2d.setColor(Color.white);
		// ���ָ���ľ�������
		g2d.fillRect(0, 0, width, height);
		g2d.drawImage(image, 0, 0, width, height, null);
		if(scale != 1.0f){
			//����ͼƬ
			BufferedImage filteredBufImage =new BufferedImage((int) (width*scale), (int) (height*scale),BufferedImage.TYPE_INT_RGB); //���˺��ͼ��
			AffineTransform transform = new AffineTransform(); //����任����
			transform.setToScale(scale, scale); //���÷���任�ı�������	
			AffineTransformOp imageOp = new AffineTransformOp(transform, null);//��������任��������			
			imageOp.filter(buffImg, filteredBufImage);//����ͼ��Ŀ��ͼ����filteredBufImage
			buffImg = filteredBufImage;
		}
		return buffImg;
	}
	/**
	 * ��ȡ�滭���ַ�������ĸ߶ȺͿ��
	 * @param str String ��Ҫ�������ַ���
	 * @param g2d Graphics2D
	 * @return Point
	 */
	private static Point getStringWidthAndHeight(String str, Graphics2D g2d){
		FontMetrics fontMetrics = g2d.getFontMetrics();
		int stringWidth = fontMetrics.stringWidth(str);
		int stringAscent = fontMetrics.getAscent();
		return new Point(stringWidth, stringAscent);
	}
	/** 
	 * ���ˮӡ
	 * @param font ˮӡ��͸����
	 * @param font ˮӡ������
	 * @param mark ˮӡ������
	 * @param g2d Graphics2D
	 * @param degree ��ת�ĽǶ�
	 * @param x ˮӡ��ʼ��x����
	 * @param y ˮӡ��ʼ��y����
	 */
	private static void printWatemark(float alpha, String mark, Graphics2D g2d, int x, int y){
		//��ͼ�κ�ͼ����ʵ�ֻ�Ϻ�͸��Ч��
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
				alpha));
		
		g2d.drawString(mark, x, y);
	}
	/**
	 * �㷨ѡ��
	 * @return RenderingHints��һ������
	 */
	private static RenderingHints getMyRenderingHints(){
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,// �������ʾ����
				RenderingHints.VALUE_ANTIALIAS_ON);// �������ʾֵ����ʹ�ÿ����ģʽ��ɳ��֡�
		rh.put(RenderingHints.KEY_TEXT_ANTIALIASING ,// �ı��������ʾ����
				RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB);//Ҫ����� LCD ��ʾ���Ż��ı���ʾ
		rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION,// Alpha ��ֵ��ʾֵ
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY );// Alpha ��ֵ��ʾֵ����ѡ��ƫ���ھ�ȷ�Ⱥ��Ӿ������� alpha ����㷨
		rh.put(RenderingHints.KEY_RENDERING,// ������ʾ����
				RenderingHints.VALUE_RENDER_QUALITY);// ������ʾֵ����ѡ��ƫ����������ĳ����㷨
		rh.put(RenderingHints.KEY_STROKE_CONTROL ,//�ʻ��淶��������ʾ����
				RenderingHints.VALUE_STROKE_NORMALIZE);//������״Ӧ���淶��������߾����Ի�ֱ�߼�����������ۡ�
		rh.put(RenderingHints.KEY_COLOR_RENDERING  ,//��ɫ������ʾ����
				RenderingHints.VALUE_COLOR_RENDER_QUALITY );// ����ߵľ�ȷ�Ⱥ��Ӿ�����ִ����ɫת�����㡣
		return rh;
	}
	private static Point calculate(Point p, int toward, double imgWidth, double imgHeight){
		int x = 0, y = 0;
		if(toward == UP_LEFT_TO_DOWN_RIGHT){
			double hypotenuse = Math.sqrt(Math.pow(imgWidth, 2)
					+ Math.pow(imgHeight, 2));
			double tempX = (p.x/2) * imgWidth/hypotenuse;
			double tempY = (p.x/2) * imgHeight/hypotenuse;
			x = (int) (imgWidth/2 - tempX);
			y = (int) (imgHeight/2 - tempY);
		}else if(toward == UP_RIGHT_TO_DOWN_LEFT){
			double hypotenuse = Math.sqrt(Math.pow(imgWidth, 2)
					+ Math.pow(imgHeight, 2));
			double tempX = (p.x/2) * imgWidth/hypotenuse;
			double tempY = (p.x/2) * imgHeight/hypotenuse;
			x = (int) (imgWidth/2 - tempX);
			y = (int) (imgHeight/2 + tempY);
		}else if(toward == DOWN_RIGHT){
			x = (int)imgWidth - p.x;
			y = (int)imgHeight - p.y;
		}else if(toward == DOWN_LEFT){
			x = 5;
			y = (int)imgHeight - p.y;
		}else if(toward == UP_RIGHT){
			x = (int)imgWidth - p.x - 3;
			y = p.y;
		}else if(toward == UP_LEFT){
			x = 5;
			y = p.y;
		}else if(toward == MIDDLE){
			x = (int)imgWidth/2-p.x/2;
			y = (int)imgHeight/2;
		}
		return new Point(x, y);
	}
	public static BufferedImage watermark(File file, Font font,
			Color color, int toward, String mark, float alpha, float scale) {
		BufferedImage buffImg = getMyBufferedImage(file, scale);
		// ����Graphics2D��������BufferedImage�����ϻ�ͼ
		Graphics2D g2d = buffImg.createGraphics();
		g2d.setRenderingHints(getMyRenderingHints());
		g2d.setFont(font);
		g2d.setColor(color);
		
		double imgWidth = buffImg.getWidth();
		double imgHeight = buffImg .getHeight();
		
		//�滭�ַ�������Ŀ�͸�
		Point p = getStringWidthAndHeight(mark, g2d);		
		
		//����滭�ַ��������
		Point p1 = calculate(p, toward, imgWidth, imgHeight);
		
		// ����ˮӡ��ת 
		//ƽ�Ƶ�ָ��λ�ã���תָ������
		if(toward == UP_LEFT_TO_DOWN_RIGHT){
			g2d.rotate(Math.atan(imgHeight/imgWidth), //Math.toRadians(135)
		            p1.x, p1.y); 
		}else if(toward == UP_RIGHT_TO_DOWN_LEFT){
			g2d.rotate(-Math.atan(imgHeight/imgWidth), //Math.toRadians(135)
		            p1.x, p1.y);
		}
		printWatemark(alpha, mark, g2d, p1.x, p1.y);
		g2d.dispose();// �ͷ�ͼ��������ʹ�õ�ϵͳ��Դ
		return buffImg;
	}
}
