package nicelee.github.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {

	public static String owner;
	public static String repo;
	public static String path; // 想要上传的文件路径，请确保"/"开头，"/"结尾
	public static String token;

	final static Pattern patternConfig = Pattern.compile("^[ ]*([0-9|a-z|A-Z|.|_]+)[ ]*=[ ]*([^ ]+.*$)");
	public static void readFromFile() {
		// 从配置文件读取
		BufferedReader buReader = null;
		System.out.println("----Config init begin...----");
		try {
			buReader = new BufferedReader(new FileReader("./app.config"));
			String config;
			while ((config = buReader.readLine()) != null) {
				Matcher matcher = patternConfig.matcher(config);
				if (matcher.find()) {
					switch (matcher.group(1)) {
					case "owner":
						owner = matcher.group(2).trim();
						break;
					case "repo":
						repo = matcher.group(2).trim();
						break;
					case "path":
						path = matcher.group(2).trim();
						break;
					case "token":
						token = matcher.group(2).trim();
						break;
					default:
						break;
					}
					System.out.printf("  key-->value:  %s --> %s\r\n", matcher.group(1), matcher.group(2));
				}
			}
		} catch (IOException e) {
			// e.printStackTrace();
		} finally {
			try {
				buReader.close();
			} catch (Exception e) {
			}
		}
		System.out.println("----Config ini end...----");
		// 下载设置相关
	}
}
