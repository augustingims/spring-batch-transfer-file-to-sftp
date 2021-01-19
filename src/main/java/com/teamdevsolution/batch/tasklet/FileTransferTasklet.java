package com.teamdevsolution.batch.tasklet;

import com.teamdevsolution.batch.config.ApplicationProperties;
import com.teamdevsolution.batch.service.FileTransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FileTransferTasklet implements Tasklet {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileTransferTasklet.class);

    private final ApplicationProperties applicationProperties;

    private final FileTransferService fileTransferService;

    public FileTransferTasklet(ApplicationProperties applicationProperties, FileTransferService fileTransferService) {
        this.applicationProperties = applicationProperties;
        this.fileTransferService = fileTransferService;
    }


    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        final File folder = new File(applicationProperties.getOutput().getPath());
        if(folder.listFiles()!=null){
            List<File> files = Arrays.asList(Objects.requireNonNull(folder.listFiles()));
            LOGGER.info("File size : {}", files.size());
            for (File file: files){
                LOGGER.info("File Path : {}", file.getPath());
                LOGGER.info("Start upload file : {}", file.getName());
                String remoteFile = String.format("%s/%s", applicationProperties.getSftp().getDirRemote(),file.getName());
                LOGGER.info("Path remote file : {}", remoteFile);
                boolean isUploaded = fileTransferService.uploadFile(file.getPath(), remoteFile);
                LOGGER.info("Upload result: " + String.valueOf(isUploaded));
            }
        }
        return RepeatStatus.FINISHED;
    }
}
