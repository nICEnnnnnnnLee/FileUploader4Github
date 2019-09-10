package nicelee.github.runnable;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import nicelee.github.config.Config;
import nicelee.github.upload.FileUploader;

public class ThUpload implements Runnable {

	public static ExecutorService uploadThreadPool = Executors.newFixedThreadPool(1);
	public static Integer count = 0;
	static String token;
	static JTextArea consoleArea;
	static JTextField fPath;

	boolean isRename;
	File file;

	/**
	 * 初始化参数
	 * 
	 * @param token
	 * @param label
	 * @param fPath
	 */
	public static void init(String token, JTextArea consoleArea, JTextField fPath) {
		ThUpload.token = token;
		ThUpload.consoleArea = consoleArea;
		ThUpload.fPath = fPath;
	}

	/**
	 * 将任务加入队列
	 * 
	 * @param fName
	 * @param file
	 */
	public static void addUploadTask(File file, boolean isRename) {
		synchronized (count) {
			count++;
		}
		uploadThreadPool.execute(new ThUpload(file, isRename));
	}

	private ThUpload(File file, boolean isRename) {
		this.isRename = isRename;
		this.file = file;
	}

	@Override
	public void run() {
		synchronized (fPath) {
			fPath.setText(file.getAbsolutePath());
			consoleArea.append("当前任务数： " + count + "\n");
			consoleArea.append("正在上传 " + file.getName() + "\n");
		}
		// 构造文件名
		String fName = file.getName();
		if (isRename) {
			int pointer = fName.lastIndexOf(".");
			if (pointer > -1) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH.mm.ss");
				fName = sdf.format(new Date()) + fName.substring(pointer);
				System.out.println(fName);
			}
		}else {
			try {
				fName = URLEncoder.encode(fName, "UTF-8");
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 构造url  // https://api.github.com/repos/:owner/:repo/contents/:path
		String url = String.format("https://api.github.com/repos/%s/%s/contents%s%s", Config.owner, Config.repo,
				Config.path, fName);
		boolean result = FileUploader.create(url, file, token);
		synchronized (count) {
			count--;
			if (result) {
				consoleArea.append("上传成功！ " + file.getName() + "\n外链为： \n");
				// https://github.com/:owner/:repo/blob/master/:path?raw=true
				// https://raw.githubusercontent.com/:owner/:repo/master/:path
				String shareLink = String.format("https://raw.githubusercontent.com/%s/%s/master%s%s\n", Config.owner, Config.repo,
						Config.path, fName);
				consoleArea.append(shareLink);
				// shareLink 复制到系统剪贴板
				if(count == 0) {
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					Transferable trans = new StringSelection(shareLink);
					clipboard.setContents(trans, null);
				}
			} else
				consoleArea.append("上传失败！ " + file.getName() + "\n");
		}
	}

}
