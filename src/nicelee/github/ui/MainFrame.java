package nicelee.github.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import nicelee.github.config.Config;
import nicelee.github.runnable.ThUpload;

public class MainFrame extends JFrame implements ActionListener, DropTargetListener {

	private static final long serialVersionUID = 1L;
	private final int HEIGHT = 30;

	// 用于上传的文件选择
	static JTextField fileText = new JTextField();
	JButton btnFileChooser = new JButton("...");
	JLabel dragArea = new JLabel("拖拽文件至此处", JLabel.CENTER);
	JButton btnUpload = new JButton("上传");
	// 是否自动重命名
	JCheckBox isRename = new JCheckBox("按时间重命名");
	// 用于输出提示
	static JTextArea consoleArea = new JTextArea(12, 50);

	public static void main(String[] args) {
		System.out.println(System.getProperty("file.encoding"));
		MainFrame log = new MainFrame();
		log.InitUI();
		Config.readFromFile();
		ThUpload.init(Config.token, consoleArea, fileText);
		consoleArea.append("owner --> " + Config.owner);
		consoleArea.append("\nrepo --> " + Config.repo);
		consoleArea.append("\npath --> " + Config.path);
		consoleArea.append("\ntoken --> " + Config.token);
		consoleArea.append("\n");
	}

	public void InitUI() {
		// 设置窗口名称
		this.setTitle("Github文件上传");
		this.setSize(620, 500);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImageIcon icon = new ImageIcon(this.getClass().getResource("/resources/github_logo.png"));
		this.setIconImage(icon.getImage());

		// 此处使用流式布局FlowLayout，流式布局类似于word的布局
		FlowLayout f1 = new FlowLayout(FlowLayout.LEFT);
		// this窗口设置为f1的流式左对齐
		this.setLayout(f1);

		/**
		 * 文件夹选项
		 */
		this.addBlank(40, HEIGHT);
		JLabel lbDstFolder = new JLabel("目标文件：");
		lbDstFolder.setPreferredSize(new Dimension(80, HEIGHT));
		this.add(lbDstFolder);

		fileText.setPreferredSize(new Dimension(270, HEIGHT));
		this.add(fileText);

		btnFileChooser.addActionListener(this);
		btnFileChooser.setPreferredSize(new Dimension(20, HEIGHT));
		this.add(btnFileChooser);
		
		btnUpload.addActionListener(this);
		this.add(btnUpload);
		
		isRename.setSelected(true);
		this.add(isRename);
		/**
		 * dragArea
		 */
		this.addBlank(20, HEIGHT);
		dragArea.setBorder(BorderFactory.createLineBorder(Color.red));
		dragArea.setPreferredSize(new Dimension(550, 200));
		dragArea.setFont(new Font("黑体", Font.BOLD, 50));
		this.add(dragArea);
		new DropTarget(dragArea, DnDConstants.ACTION_NONE, this);

		/**
		 * console
		 */
		this.addBlank(20, HEIGHT);
		this.addBlank(20, HEIGHT);
		consoleArea.setEditable(false);
		consoleArea.setLineWrap(true);
		JScrollPane js = new JScrollPane(consoleArea);
		// 分别设置水平和垂直滚动条出现方式
		js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.add(js);

		this.setVisible(true);
	}

	/**
	 * 增加空白格，用于调整位置
	 */
	private void addBlank(int width, int height) {
		JLabel blank = new JLabel();
		blank.setPreferredSize(new Dimension(width, height));
//		blank.setBorder(BorderFactory.createLineBorder(Color.red));
		this.add(blank);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnFileChooser) {
			JFileChooser fileChooser = null;
			String currentFolder = fileText.getText();
			File f = new File(currentFolder);
			if (f.exists()) {
				fileChooser = new JFileChooser(f);
			} else {
				fileChooser = new JFileChooser(".");

			}
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setDialogTitle("请选择文件...");
			fileChooser.setApproveButtonText("确定");
			fileChooser.showOpenDialog(this);// 显示打开的文件对话框
			f = fileChooser.getSelectedFile();// 使用文件类获取选择器选择的文件
			if (f != null) {
				String s = f.getAbsolutePath();// 返回路径名
				fileText.setText(s);
			}
		}else if(e.getSource() == btnUpload) {
			uploadFile(new File(fileText.getText()));
		}
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		dragArea.setText("松开鼠标上传");
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {

	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {

	}

	@Override
	public void dragExit(DropTargetEvent dte) {
		dragArea.setText("拖拽文件至此处");
	}

	@Override
	public void drop(DropTargetDropEvent dtde) {
		dragArea.setText("处理中...");
		try {
			if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);// 接收拖拽来的数据
				List<File> list = (List<File>) (dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
				for (File file : list) {
					uploadFile(file);
				}

				dtde.dropComplete(true);// 指示拖拽操作已完成
			} else {
				dtde.rejectDrop();// 否则拒绝拖拽来的数据
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		dragArea.setText("拖拽文件至此处");
	}

	void uploadFile(File file) {
		if (file.isDirectory())
			return;
		String fName = file.getName();
		if (isRename.isSelected()) {
			int pointer = fName.lastIndexOf(".");
			if(pointer > -1) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH.mm.ss");
				fName = sdf.format(new Date()) + fName.substring(pointer);
			}
		}
		ThUpload.addUploadTask(file, isRename.isSelected());
	}
}
