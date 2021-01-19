package com.teamdevsolution.batch.writers;

import com.teamdevsolution.batch.callback.FormateurFooterCallBack;
import com.teamdevsolution.batch.callback.FormateurHeaderCallBack;
import com.teamdevsolution.batch.config.ApplicationProperties;
import com.teamdevsolution.batch.domain.Formateur;
import com.teamdevsolution.batch.mappers.FormateurLineAggregator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.core.io.FileSystemResource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FormateurWriter extends FlatFileItemWriter<Formateur> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormateurWriter.class);

    private final ApplicationProperties applicationProperties;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    public FormateurWriter(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        setResource(new FileSystemResource(getFile()));
        setAppendAllowed(false);
        setLineAggregator(new FormateurLineAggregator());
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        super.setHeaderCallback(new FormateurHeaderCallBack());
        super.setFooterCallback(new FormateurFooterCallBack());
        super.open(executionContext);
    }

    @Override
    public void write(List<? extends Formateur> items) throws Exception {
        super.write(items);
    }

    private String getFile(){
        Date now = new Date();
        DateFormat df = new SimpleDateFormat("yyyyMMddmmss");
        String timestamp = df.format(now);
        return String.format("%s/%s-%s%s",applicationProperties.getOutput().getPath(),applicationProperties.getOutput().getFilename(), timestamp,applicationProperties.getOutput().getFileext());
    }
}
