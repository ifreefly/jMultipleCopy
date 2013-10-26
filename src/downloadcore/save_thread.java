package downloadcore;

import static java.lang.System.out;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class save_thread extends Thread {
	private String name;
	private String contentRange;
	private String srcFile,desFile;
	InputStream input=null; 
	private RandomAccessFile rfwrite=null;
	private long beginPos=0,endPos=0,currentPos=0;
	private int c;
	private byte b[]=new byte[1024];
	public save_thread(String name){
		this.name=name;
	}
	public save_thread(ThreadGroup tg,String name,String srcFile,String desFile,long beginPos,long endPos){
		super(tg,name);
		this.name=name;
		this.beginPos=beginPos;
		this.endPos=endPos;
		this.srcFile=srcFile;
		this.desFile=desFile;
		currentPos=beginPos;
	}

	@Override
	public void run() {
		try{
		URL url=new URL(srcFile);
		HttpURLConnection httpurl=(HttpURLConnection)url.openConnection();
		rfwrite=new RandomAccessFile(desFile,"rw");
		httpurl.setRequestProperty("User-Agent", "jmultidownload");//����user-agent
		contentRange="bytes="+beginPos+"-";//�����ļ�����ʼλ��
		httpurl.setRequestProperty("RANGE", contentRange);//�����ļ�����ʼλ��
		out.println("�߳�"+name+"ContentRange="+httpurl.getHeaderField("Content-Range"));
		input = httpurl.getInputStream();
		rfwrite.seek(beginPos);
		out.println();
		out.println("�߳�"+name+"��ʼ�ֽ���"+currentPos);
		out.println("�߳�"+name+"���۽����ֽ���"+endPos);
		out.println();
		}catch(IOException e){
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		try {
			while((c=input.read(b,0,1024))>0){
				currentPos+=c;
				if(currentPos>=endPos){//�ÿ��Ѵ������
					rfwrite.write(b, 0, (c-(int)((currentPos-endPos))));
					break;
				}else{
					rfwrite.write(b,0,c);
				}
			}
			out.println("�߳�"+name+"�����ֽ���"+currentPos);
			input.close();
			rfwrite.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}