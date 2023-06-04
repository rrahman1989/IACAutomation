package com.automation.utility.ssh;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHCommandExecutor {
	
    private String host;
    private String username;
    private File privateKeyFile;
    private int port;
    private Session session;

    public SSHCommandExecutor(String host, String username, File privateKeyFile) {
        this(host, username, privateKeyFile, 22);
    }

    public SSHCommandExecutor(String host, String username, File privateKeyFile, int port) {
        this.host = host;
        this.username = username;
        this.privateKeyFile = privateKeyFile;
        this.port = port;
    }

    public void connect() throws JSchException {
        JSch jsch = new JSch();
        jsch.addIdentity(privateKeyFile.getAbsolutePath());

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");

        session = jsch.getSession(username, host, port);
        session.setConfig(config);
        session.connect();
    }

    public void executeCommand(String command) throws JSchException, IOException {
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(command);

        InputStream commandOutput = channelExec.getInputStream();
        channelExec.connect();

        byte[] buffer = new byte[1024];
        StringBuilder outputBuffer = new StringBuilder();

        while (commandOutput.available() > 0) {
            int bytesRead = commandOutput.read(buffer);
            outputBuffer.append(new String(buffer, 0, bytesRead));
        }

        channelExec.disconnect();

        System.out.println(outputBuffer.toString());
    }

    public void disconnect() {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }

}
