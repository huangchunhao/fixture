package slim.ssh;

import java.io.BufferedReader;
import java.io.IOException;

public class SshFixture {
	//static Logger log = LogManager.getLogger("com.maiya");
	static String s_clog = " Ssh_Fixture: ";
	private SSHOperation sshOpt;
	private BufferedReader brs = null;

	/**
	 * <p>ssh���ʷ���</p>
	 * <p>���ӣ�</p>
	 * <p>|script|ssh fixture|192.168.0.65|22|</p>
	 * <p>|ensure|login user|root| and password|frbao.com|</p>
	 * <p>|ensure|excute command|!-ls -l | grep ks-!|</p>
	 * <p>|show|handle result|</p>
	 * @param hostname ip��ַ
	 * @param port �˿ں�
	 */
	public SshFixture(String hostname, int port) {
		//log.info("######"+s_clog+"######"+" Create a ssh fixture ");
		sshOpt = new SSHOperation(hostname, port);
		//log.debug(s_clog+"will to access "+ hostname +" and the port is "+port);
	}

	/**
	 * �����û���������
	 * @param username  �û���
	 * @param password ����
	 * @return ִ�н��
	 */
	public boolean loginUserAndPassword(String username, String password) {
		//log.debug(s_clog+"the username is "+ username +" and ths password is "+ password);
		boolean result = false;
		result = sshOpt.doLogin(username, password);
		if(result){
			//log.debug(s_clog+"Login successfully ");
		}else{
			//log.debug(s_clog+"Fail to login ");
		}
		return result;
	}

	/**
	 * �ϴ������ļ���Զ��Ŀ��Ŀ¼
	 * @param localFile �����ļ�
	 * @param remoteTargetDirectory Զ��Ŀ¼
	 * @return ִ�н��
	 */
	public boolean uploadLocalFileToRemoteTargetDirectory(String localFile, String remoteTargetDirectory) {
		//log.debug(s_clog+"the localFile is "+ localFile +" and ths remoteTargetDirectory is "+ remoteTargetDirectory);
		boolean result = false;
		result = sshOpt.uploadFile(localFile, remoteTargetDirectory);
		if(result){
			//log.debug(s_clog+"Upload successfully ");
		}else{
			//log.debug(s_clog+"Fail to upload the file: "+ localFile );
		}
		return result;
	}

	/**
	 * ����Զ���ļ�������Ŀ��Ŀ¼
	 * @param remoteFile Զ���ļ�
	 * @param localTargetDirectory ����Ŀ��Ŀ¼
	 * @return ִ�н��
	 */
	public boolean downloadRemoteFileToLocalTargetDirectory(String remoteFile, String localTargetDirectory) {
		//log.debug(s_clog+"the remoteFile is "+ remoteFile +" and ths localTargetDirectory is "+ localTargetDirectory);
		boolean result = false;
		result = sshOpt.downloadFile(remoteFile, localTargetDirectory);
		if(result){
			//log.debug(s_clog+"Download successfully ");
		}else{
			//log.debug(s_clog+"Fail to download the file: "+ remoteFile );
		}
		return result;
	}

	/**
	 * ִ������
	 * @param command  ����
	 * @return ִ�н��
	 */
	public boolean executeCommand(String command) {
		//log.debug(s_clog+"the command is "+ command );
		boolean result = false;
		result = sshOpt.executeCommand(command);
		if(result){
			//log.debug(s_clog+"Execute successfully ");
		}else{
			//log.debug(s_clog+"Fail to execute the command: "+ command );
		}
		return result;
	}


	private void getBackInfo() {
		brs = sshOpt.getInfo();
	}

	/**
	 * ���ش�����
	 * @return ��������Ϣ
	 */
	public String handleResult() {
		StringBuffer sb = new StringBuffer();
		sb.append("![");
		this.getBackInfo();
		while (true) {
			String line;
			try {
				line = brs.readLine();
				if (line == null) {
					sb.append("]!");
					break;
				} else {
					sb.append(line);
					sb.append("\r\n");
				}
				System.out.println(line);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		//log.debug(s_clog+"the result is "+ sb.toString() );
		return sb.toString();
	}

	/*
	 * public static void main(String[] args){ SshFixture sf= new
	 * SshFixture("192.168.0.65",22); sf.loginUserAndPassword("root",
	 * "frbao.com"); sf.excuteCommand("ls -l | grep ks");
	 * System.out.println(sf.handleResult()); }
	 */

	public boolean plog(String s) {
		//log.info(s);
		return true;
	}
}
