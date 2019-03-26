package de.tuberlin.dima.masternode;

import com.jcraft.jsch.*;

import java.io.*;
import java.util.Vector;

public class SessionManager {
    
    private JSch jsch = new JSch();
    private Session session;
    private ChannelSftp channelSftp;
    
    public SessionManager(String user, String host, int port, String pass) throws JSchException {
        this.session = jsch.getSession(user, host, port);
        JSch.setConfig("StrictHostKeyChecking", "no");
        this.session.setPassword(pass);
        this.session.connect();
    }
    
    public void sftpMkdir(String dir) throws JSchException, SftpException {
        if (channelSftp == null) {
            sftpConnect();
        }
        boolean exists = false;
        try {
            channelSftp.stat(dir);
            exists = true;
            
        } catch (Exception ex) {
        }
        if (!exists) {
            channelSftp.mkdir(dir);
        }
    }
    
    public void sftpFile(String fileLocation, File file) throws JSchException, FileNotFoundException, SftpException {
        this.sftpFile(fileLocation, file, file.getName());
    }
    
    public void sftpFile(String fileLocation, File file, String newName) throws JSchException, FileNotFoundException, SftpException {
        if (channelSftp == null) {
            sftpConnect();
        }
        String currentDirectory = channelSftp.pwd();
        channelSftp.cd(fileLocation);
        channelSftp.put(new FileInputStream(file), newName);
        channelSftp.cd(currentDirectory);
    }
    
    private void sftpConnect() throws JSchException {
        Channel channel = this.session.openChannel("sftp");
        channelSftp = (ChannelSftp) channel;
        channelSftp.connect();
    }
    
    public int execCommand(String command) throws JSchException, IOException {
        int exitStatus = 0;
        Channel channel = this.session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);
        
        ((ChannelExec) channel).setErrStream(System.err);
        
        channel.setInputStream(null);
        InputStream in = channel.getInputStream();
        channel.connect();
        
        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) {
                    break;
                }
                System.out.print(new String(tmp, 0, i));
            }
            if (channel.isClosed()) {
                if (in.available() > 0) {
                    continue;
                }
                exitStatus = channel.getExitStatus();
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
        }

        channel.disconnect();
        return exitStatus;
    }
    
    public void disconnect() {
        this.session.disconnect();
    }
}
