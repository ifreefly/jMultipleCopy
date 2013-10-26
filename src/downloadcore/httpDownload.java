/*
 * author:idevcod@gmail.com
 * date:2013/10/24
 * description:http���أ���ȡhttp������
 * version:0.1
 * nextVersionDescription:ʵ�ֶ��߳�����
 * */

package downloadcore;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;
public class httpDownload {
	private String srcFile;
	private String ContentLength,ContentRange;
	private URL url=null;
	private HttpURLConnection httpUrl=null;
	private String fileName;
	public httpDownload(String srcFile){
		this.srcFile=srcFile;
		setFileName(srcFile);
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String srcFile) {
		fileName=srcFile.substring(srcFile.lastIndexOf("/")+1, srcFile.length());
		//System.out.println(fileName);
	}
}
