package kr.co.sist.admin.file;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import kr.co.sist.lunch.admin.run.LunchAdminMain;

public class FileServer extends Thread {
	
	@Override
	public void run() {
		ServerSocket server = null;
		
		try {
			try {
				server = new ServerSocket(25000);
				Socket client = null;
				DataInputStream dis = null;
				int cnt = 0;
				
				String[] fileNames = null;
				String[] serverFileNames = null;
				
				List<String> tempFileList = new ArrayList<String>();
				DataOutputStream dos = null;
				
				while(true) {
					System.out.println("서버가동");
					client = server.accept();
					System.out.println("접속자 있음");
					dis = new DataInputStream(client.getInputStream());
					
					cnt = dis.readInt(); //클라이언트가 보내오는 파일명의 수
					fileNames = new String[cnt];
					
					for(int i = 0; i<cnt; i++) {
						fileNames[i] = dis.readUTF();
						System.out.println(i+"번째 파일 : "+fileNames[i]);
					}//end for
					
					//서버에 존재하는 파일명을 배열로 복사
					serverFileNames = new String[LunchAdminMain.lunchImageList.size()];
					LunchAdminMain.lunchImageList.toArray(serverFileNames);
					
					//클라이언트가 보내온 파일명과 서버의 파일명을 비교하여 클라이언트가 없는 파일명을 출력
					for(String tName:LunchAdminMain.lunchImageList) {
						tempFileList.add(tName);
						tempFileList.add("s_"+tName);
					}//end for
					
					for(String rmName : fileNames) {
						tempFileList.remove(rmName);
						tempFileList.remove("s_"+rmName);
					}//end if
					
					dos = new DataOutputStream(client.getOutputStream());
					dos.writeInt(tempFileList.size()); //전송할 파일의 갯수를 보낸다.
					
					for(String fName : tempFileList) {
						fileSend(fName,dos);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}//end for
					
				}//end while
				
			} finally {
				if(server != null) {
					server.close();
				} //end if
			}//end finally
		}catch (IOException ie) {
			JOptionPane.showMessageDialog(null, "파일 보내기 실패");
			ie.printStackTrace();
		}//end catch
	}//run
	
	private void fileSend(String fName, DataOutputStream dos) throws IOException {
		FileInputStream fis = null;
		
		
		try {
			int fileData = 0;
			fis = new FileInputStream("C:/dev/workspace/lunch_prj/src/kr/co/sist/lunch/admin/img/"+fName);
			byte[] readData = new byte[512];
			
			int fileLen = 0;
			
			while((fileLen = fis.read(readData)) != -1) {
				fileData++;
			}//end while
			
			fis.close();
			
			
			dos.writeInt(fileData);
			dos.flush();
			
			dos.writeUTF(fName); //writeUTF
			
			fis = new FileInputStream("C:/dev/workspace/lunch_prj/src/kr/co/sist/lunch/admin/img/"+fName);
			while((fileLen = fis.read(readData)) != -1) {
				dos.write(readData,0,fileLen);
				fileData--;
			}//end while
			
		} finally {
			if(fis != null) {
				fis.close();
			}//end if
		}//end finally
		
	}//fileSend
	
}//class
