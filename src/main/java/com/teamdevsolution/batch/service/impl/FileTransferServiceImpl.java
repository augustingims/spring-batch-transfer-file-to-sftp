package com.teamdevsolution.batch.service.impl;

import com.jcraft.jsch.*;
import com.teamdevsolution.batch.config.ApplicationProperties;
import com.teamdevsolution.batch.service.FileTransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class FileTransferServiceImpl implements FileTransferService {

    private Logger logger = LoggerFactory.getLogger(FileTransferServiceImpl.class);

    private final ApplicationProperties applicationProperties;

    public FileTransferServiceImpl(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public boolean uploadFile(String localFilePath, String remoteFilePath) {
        ChannelSftp channelSftp = createChannelSftp();
        try {
            if (channelSftp != null) {
                channelSftp.put(localFilePath, remoteFilePath);
                logger.info("Remote path : {}",channelSftp.getHome());
                return true;
            }
        } catch(SftpException ex) {
            logger.error("Error upload file", ex);
        } finally {
            disconnectChannelSftp(channelSftp);
        }
        return false;
    }

    @Override
    public boolean downloadFile(String localFilePath, String remoteFilePath) {
        ChannelSftp channelSftp = createChannelSftp();
        OutputStream outputStream;
        try {
            File file = new File(localFilePath);
            outputStream = new FileOutputStream(file);
            if (channelSftp != null) {
                channelSftp.get(remoteFilePath, outputStream);
                return file.createNewFile();
            }
        } catch(SftpException | IOException ex) {
            logger.error("Error download file", ex);
        } finally {
            disconnectChannelSftp(channelSftp);
        }

        return false;
    }

    private ChannelSftp createChannelSftp() {
        try {
            JSch jSch = new JSch();
            Session session = jSch.getSession(applicationProperties.getSftp().getUsername(), applicationProperties.getSftp().getHost(), applicationProperties.getSftp().getPort());
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(applicationProperties.getSftp().getPassword());
            session.connect(applicationProperties.getSftp().getSessionTimeout());
            Channel channel = session.openChannel("sftp");
            channel.connect(applicationProperties.getSftp().getChannelTimeout());
            return (ChannelSftp) channel;
        } catch(JSchException ex) {
            logger.error("Create ChannelSftp error", ex);
        }

        return null;
    }

    private void disconnectChannelSftp(ChannelSftp channelSftp) {
        try {
            if( channelSftp == null)
                return;

            if(channelSftp.isConnected())
                channelSftp.disconnect();

            if(channelSftp.getSession() != null)
                channelSftp.getSession().disconnect();

        } catch(Exception ex) {
            logger.error("SFTP disconnect error", ex);
        }
    }
}
