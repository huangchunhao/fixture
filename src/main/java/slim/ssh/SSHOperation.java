package slim.ssh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class SSHOperation {
	static Logger log = LogManager.getLogger("com.maiya");
		
	private String hostname;;
    private int port;
    private String username;
    private String password;
    private boolean isconn;
    private Session ssh=null;
    private InputStream is=null;
    private BufferedReader brs=null;
    
    private Connection conn;
	
	public SSHOperation(String hostname,int port){
		this.hostname=hostname;
		this.port=port;
		this.getConnection();
	}
	
	private Connection getConnection(){
		log.info(hostname+ this.port);
		this.conn = new Connection(this.hostname,this.port);
		return this.conn;
	}
	
	public boolean doLogin(String username,String password){
		this.username=username;
		this.password=password;
		
		this.isconn = false;
		try {
			this.conn.connect();
			System.out.println(username + password);
			isconn = this.conn.authenticateWithPassword(this.username, this.password);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return isconn;		
	}
	
	public boolean executeCommand(String Command){
		boolean result=false;
		 if (!isconn) 
         {
			 log.info("�û����ƻ��������벻��ȷ");
         }else{
        	 log.info("�Ѿ�����OK");
        	 try {
				ssh = conn.openSession();
				ssh.execCommand(Command);
				this.is = new StreamGobbler(ssh.getStdout());
				this.brs = new BufferedReader(new InputStreamReader(is));
				if(this.brs==null){
					try {
						throw new Exception("there is not any infomation after executing command."+ "The command is " + Command);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					result=true;
				}
			} catch (IOException e) {
				
				e.printStackTrace();
			} 				           
         }
		return result; 
	}
	
	public boolean uploadFile(String localFile,String remoteTargetDirectory){
		boolean result=false;
		 //�������ļ����䵽Զ��������ָ��Ŀ¼��
        SCPClient clt;
		try {
			clt = conn.createSCPClient();
			clt.put(localFile, remoteTargetDirectory);
			result=true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isconn;                
        
	}
	
	public boolean downloadFile(String remoteFile,String localTargetDirectory){
		boolean result=false;
		 //�������ļ����䵽Զ��������ָ��Ŀ¼��
        SCPClient clt;
		try {
			clt = conn.createSCPClient();
			clt.get(remoteFile, localTargetDirectory);
			result=true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isconn;                
        
	}
		
	public BufferedReader getInfo(){		
		return brs;		
	}

}
