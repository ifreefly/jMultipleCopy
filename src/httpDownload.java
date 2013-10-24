/*
 * author:idevcod@gmail.com
 * date:2013/10/24
 * description:http下载，获取http数据流
 * version:0.1
 * nextVersionDescription:实现多线程下载
 * */

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
public class httpDownload {
	private String srcFile;
	private String ContentLength,ContentRange;
	private URL url=null;
	private HttpURLConnection httpUrl=null;
	public httpDownload(String srcFile){
		this.srcFile=srcFile;
		try {
			url=new URL(srcFile);
			httpUrl=(HttpURLConnection)url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getContentRange() {
		ContentRange=httpUrl.getHeaderField("Content-Range");
		return ContentRange;
	}
	public String getContentLength() {
		ContentLength=httpUrl.getHeaderField("Content-Length");
		return ContentLength;
	}
}
