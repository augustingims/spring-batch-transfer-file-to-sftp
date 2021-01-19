package com.teamdevsolution.batch.config;

import com.teamdevsolution.batch.domain.Formateur;
import com.teamdevsolution.batch.listeners.JobListener;
import com.teamdevsolution.batch.service.FileTransferService;
import com.teamdevsolution.batch.tasklet.FileHistoTasklet;
import com.teamdevsolution.batch.tasklet.FileRemoveTasklet;
import com.teamdevsolution.batch.tasklet.FileTransferTasklet;
import com.teamdevsolution.batch.readers.FormateurReader;
import com.teamdevsolution.batch.repository.FormateurRepository;
import com.teamdevsolution.batch.writers.FormateurWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    public BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public JobListener jobListener(){
        return new JobListener();
    }

    @Bean
    public FormateurReader formateurReader(final FormateurRepository formateurRepository){
        return new FormateurReader(formateurRepository);
    }

    @Bean
    public FormateurWriter formateurWriter(final ApplicationProperties applicationProperties){
        return new FormateurWriter(applicationProperties);
    }

    @Bean
    public FileTransferTasklet fileTransferTasklet(final ApplicationProperties applicationProperties, final FileTransferService fileTransferService){
        return new FileTransferTasklet(applicationProperties, fileTransferService);
    }

    @Bean
    public FileHistoTasklet fileHistoTasklet(final ApplicationProperties applicationProperties){
        return new FileHistoTasklet(applicationProperties);
    }

    @Bean
    public FileRemoveTasklet fileRemoveTasklet(final ApplicationProperties applicationProperties, final FileTransferService fileTransferService){
        return new FileRemoveTasklet(applicationProperties, fileTransferService);
    }


    @Bean
    public Job jobExport(){
        return jobBuilderFactory
                .get("jobExport")
                .incrementer(new RunIdIncrementer())
                .listener(jobListener())
                .flow(stepExport())
                .next(stepFileTransfer())
                .next(stepFileHisto())
                .end()
                .build();
    }

    @Bean
    public Step stepExport(){
        return stepBuilderFactory
                .get("stepExport")
                .<Formateur, Formateur>chunk(Integer.MAX_VALUE)
                .reader(formateurReader(null))
                .writer(formateurWriter(null))
                .build();
    }

    @Bean
    public Step stepFileTransfer(){
        return stepBuilderFactory
                .get("stepFileTransfer")
                .tasklet(fileTransferTasklet(null, null))
                .build();
    }

    @Bean
    public Step stepFileHisto(){
        return stepBuilderFactory
                .get("stepFileHisto")
                .tasklet(fileHistoTasklet(null))
                .build();
    }

    @Bean
    public Step stepFileRemove(){
        return stepBuilderFactory
                .get("stepFileRemove")
                .tasklet(fileRemoveTasklet(null, null))
                .build();
    }
}
