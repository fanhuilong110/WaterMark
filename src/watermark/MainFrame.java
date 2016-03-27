package watermark;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import watermark.CommandButton.ButtonStyle;
/**
 * ������
 * @author CSDN's Cannel_2020
 *
 */

public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private String toward[] = { "����", "����", "����", "����", "����", "����->����",
			"����->����" };
	private String fontstyle[] = {"��ͨ","����","б��","����&б��"};
	private int fontstyles[] = {Font.PLAIN, Font.BOLD, Font.ITALIC, Font.BOLD | Font.ITALIC};
	private String fontcolor[] = {"��ɫ","��ɫ","��ɫ","��ɫ","��ɫ","���ɫ","ǳ��ɫ",
			"���ɫ","�ۻ�ɫ","�ۺ�ɫ","��ɫ","��ɫ","��ɫ"};
	private String scales[] = {"������", "25%", "50%", "75%","125%","150%","175%","200%"};
	private float imgScales[] = {1.0f, 0.25f, 0.5f, 0.75f, 1.25f, 1.5f, 1.75f, 2.0f};
	private Color colors[] = {Color.blue, Color.cyan, Color.green, Color.red, Color.gray,
			Color.darkGray, Color.lightGray, Color.magenta, Color.orange, Color.pink,
			Color.yellow, Color.black, Color.white};
	private JComboBox fontCB, towardCB, fontstyleCB, fontcolorCB, scaleCB;
	private JTextField filepathTF, savepathTF, markTF, fontsizeTF;
	private JSlider alphaSlider;
	private Font font = new Font("΢���ź�", Font.PLAIN, 14);
	private Border border = new BevelBorder(BevelBorder.RAISED);// �򵥵�˫��б��߿�
	private int mainFrameWidth = 550;// MainFrame�Ŀ��
	private int mainFrameHeight = 410;// MainFrame�ĸ߶�
	private JButton selectPathBT, batchingBT;
	private float alpha = 0.5f;
	private JProgressBar progressBar;

	//���������۵�ö��
	protected enum UIClassName {
		systemLookAndFeel(UIManager.getSystemLookAndFeelClassName()), 
		motifLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel"), 
		crossPlatformLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()), 
		metalLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		private String className;

		UIClassName(String className) {
			this.className = className;
		}

		public String getUIClassName() {
			return className;
		}
	}

	//����ʽ 
	private static final MainFrame mainFrame = new MainFrame();
	static MainFrame getInstance() {
		return mainFrame;
	}
	private MainFrame() {
		// �������
		setUI(UIClassName.systemLookAndFeel);
		setTitle("���ˮӡ-��ǰ״̬�������ļ�");
		setSize(mainFrameWidth, mainFrameHeight);
		Common.setCentered(this);// ������ʾ
		setLayout(new BorderLayout());

		JPanel panel = getNorthPanel();
		add(panel, BorderLayout.NORTH);

		panel = getCenterPanel();
		add(panel, BorderLayout.CENTER);

		panel = getSouthPanel();
		add(panel, BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		// ����ǰ���ڴ������Ϸ�Դ
		new DropTarget(this, DnDConstants.ACTION_COPY,
				new ImageDropTargetListener());
	}

	private JPanel getNorthPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(new TitledBorder(border, "�ļ���ȡ",
				Font.LAYOUT_LEFT_TO_RIGHT, Font.LAYOUT_LEFT_TO_RIGHT, font));

		GridBagConstraints gbc = new GridBagConstraints();
		// �������ʾ������������������ʾ����Ĵ�Сʱʹ�ô��ֶΡ�HORIZONTAL����ˮƽ��������Ǵ�ֱ�����ϵ��������С��
		gbc.fill = GridBagConstraints.HORIZONTAL;
		// insets���������ʾ�����Ե֮�������С��
		gbc.insets = new Insets(5, 10, 5, 10);
		// ָ�������������ʾ����ʼ�ߵ�"��Ԫ��"�������еĵ�һ����Ԫ��Ϊ gridx=0��
		gbc.gridx = 0;
		gbc.weightx = 1;
		filepathTF = new JTextField();
		panel.add(filepathTF, gbc);

		savepathTF = new JTextField();
		panel.add(savepathTF, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0;
		
		selectPathBT = makeButton(CommandButton.ButtonStyle.selectImage, panel, gbc);
		makeButton(CommandButton.ButtonStyle.selectSavepath, panel, gbc);
		Common.setComponentsFont(panel, font);
		return panel;

	}

	private JPanel getCenterPanel() {

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(new TitledBorder(border, "����",
				Font.LAYOUT_LEFT_TO_RIGHT, Font.LAYOUT_LEFT_TO_RIGHT, font));

		GridBagConstraints gbc = new GridBagConstraints();
		// �������ʾ������������������ʾ����Ĵ�Сʱʹ�ô��ֶΡ�HORIZONTAL����ˮƽ��������Ǵ�ֱ�����ϵ��������С��
		gbc.fill = GridBagConstraints.HORIZONTAL;
		// insets���������ʾ�����Ե֮�������С��
		gbc.insets = new Insets(5, 10, 5, 10);
		gbc.fill = GridBagConstraints.BOTH;
		
		
		gbc.gridwidth = 1;
		JLabel label;
		label = new JLabel("ѡ������:", JLabel.RIGHT);
		panel.add(label, gbc);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER; // ������ǰ��
		fontCB = new JComboBox(Common.getAvailableFontFamilyNames());
		panel.add(fontCB, gbc);

		gbc.weightx = 1.0;// ϵͳ�Ὣ����Ŀռ䰴����Ȩ�ر����ֲ���ÿһ��
		gbc.gridwidth = 1;

		label = new JLabel("�����С:", JLabel.RIGHT);
		panel.add(label, gbc);

		fontsizeTF = new JTextField("15");
		panel.add(fontsizeTF, gbc);

		label = new JLabel("ˮӡλ��:", JLabel.RIGHT);
		panel.add(label, gbc);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		towardCB = new JComboBox(toward);
		panel.add(towardCB, gbc);
		
		//TODO
		gbc.gridwidth = 1;
		label = new JLabel("������ʽ:", JLabel.RIGHT);
		panel.add(label, gbc);

		fontstyleCB = new JComboBox(fontstyle);
		panel.add(fontstyleCB, gbc);

		label = new JLabel("ˮӡ��ɫ:", JLabel.RIGHT);
		panel.add(label, gbc);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		fontcolorCB = new JComboBox(fontcolor);
		panel.add(fontcolorCB, gbc);
		//TODO
		gbc.weightx = 1.0;// ϵͳ�Ὣ����Ŀռ䰴����Ȩ�ر����ֲ���ÿһ��
		gbc.gridwidth = 1;
		label = new JLabel("͸����:", JLabel.RIGHT);
		panel.add(label, gbc);
		alphaSlider = new JSlider(JSlider.HORIZONTAL);
		alphaSlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				alpha = (float)alphaSlider.getValue()/100;
			}
		});
		panel.add(alphaSlider, gbc);

		label = new JLabel("���Ŵ�С:", JLabel.RIGHT);
		panel.add(label, gbc);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		scaleCB = new JComboBox(scales);
		panel.add(scaleCB, gbc);
		
		gbc.gridwidth = 1;
		label = new JLabel("ˮӡ���ݣ�", JLabel.RIGHT);
		panel.add(label, gbc);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		markTF = new JTextField("From CSDN Cannel_2020's blog");
		panel.add(markTF, gbc);
		
		
		gbc.gridwidth = 1;
		label = new JLabel("ִ�н���:", JLabel.RIGHT);
		panel.add(label, gbc);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		progressBar = new JProgressBar(0, 1);  
        //���� stringPainted ���Ե�ֵ  
        //������ȷ���������Ƿ�Ӧ�ó��ֽ����ַ�����  
        progressBar.setStringPainted(true);  
        panel.add(progressBar, gbc);
		Common.setComponentsFont(panel, font);
		return panel;
	}
	private JPanel getSouthPanel() {
		JPanel panel = new JPanel();
		makeButton(CommandButton.ButtonStyle.preview, panel);
		batchingBT = makeButton(CommandButton.ButtonStyle.batching, panel);
		makeButton(CommandButton.ButtonStyle.moreSetting, panel);
		makeButton(CommandButton.ButtonStyle.drirect, panel);
		Common.setComponentsFont(panel, font);
		return panel;
	}
	/**
	 * �ư�ť ��������
	 * @param style ö��ButtonStyle�е�һ��ֵ
	 * @param panel ���øð�ť��JPanel
	 * @return JButton
	 */
	private JButton makeButton(ButtonStyle style, JPanel panel) {
		return makeButton(style, panel, null);
	}
	/**
	 * �ư�ť ��������
	 * @param style ö��ButtonStyle�е�һ��ֵ
	 * @param panel ���øð�ť��JPanel
	 * @param constraints ����
	 * @return JButton
	 */
	private JButton makeButton(ButtonStyle style, JPanel panel, Object constraints) {
		JButton button = new CommandButton(style);
		if (constraints == null) {
			panel.add(button);
		} else {
			panel.add(button, constraints);
		}
		return button;
	}
	
	/**
	 * �������
	 * @param className ö��UIClassName
	 */
	protected void setUI(UIClassName className) {
		try {
			UIManager.setLookAndFeel(className.getUIClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * ��ȡ��Ҫ���ˮӡ���ļ������ļ�����
	 * @return 
	 */
	public String getFilepath() {
		return filepathTF.getText();
	}
	/**
	 * ��ȡ���ˮӡ�󱣴��ļ���·��
	 * @return 
	 */
	public String getSavePath() {
		return savepathTF.getText();
	}
	/**
	 * @return ��Ҫ��ӵ�ˮӡ������
	 */
	public Font getWaterMarkFont() {
		return new Font(getFontname(), getFontstyle(), getFontsize());
	}
	/**
	 * @return ��Ҫ��ӵ�ˮӡ������
	 */
	public String getMark() {
		return markTF.getText();
	}
	/**
	 * @return ���ý�Ҫ���ˮӡ���ļ�·����JTextField
	 */
	public JTextField getFilepathTF() {
		return filepathTF;
	}
	/**
	 * @return ���ý�Ҫ���ˮӡ�󱣴�·����JTextField
	 */
	public JTextField getSavepathTF() {
		return savepathTF;
	}
	/**
	 * @return ˮӡ����ɫ
	 */
	public Color getFontcolor() {
		return colors[fontcolorCB.getSelectedIndex()];
	}
	/**
	 * @return ˮӡ��λ��
	 */
	public int getToward() {
		return towardCB.getSelectedIndex();
	}
	/**
	 * @return ˮӡ��ѡ�����������
	 */
	private String getFontname() {
		return String.valueOf(fontCB.getSelectedItem());
	}
	/**
	 * @return ˮӡ��ʹ�õ�������ʽ
	 */
	private int getFontstyle() {
		return fontstyles[fontstyleCB.getSelectedIndex()];
	}
	/**
	 * @return ˮӡ��ʹ�õ������С
	 */
	private int getFontsize() {
		return (int) (Integer.parseInt(fontsizeTF.getText())*getScale());
	}
	/**
	 * @param text �µİ�ť������
	 */
	public void setSelectPathBtText(String text){
		selectPathBT.setText(text);
	}
	/**
	 * @param text �µİ�ť������
	 */
	public void setBatchingBtText(String text){
		batchingBT.setText(text);
	}
	/**
	 * 
	 * @return ˮӡ͸����
	 */
	public float getWaterMarkAlpha() {
		return alpha ;
	}
	/**
	 * 
	 * @return ��������
	 */
	public float getScale() {
		return imgScales[scaleCB.getSelectedIndex()];
	}
	/**
	 * 
	 * @return ������
	 */
	public JProgressBar getProgressBar() {
		return progressBar;
	}
}
