package com.teamdevsolution.batch.listeners;

import com.teamdevsolution.batch.domain.Formateur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

import java.util.List;

public class JobListener extends JobExecutionListenerSupport implements ItemReadListener<Formateur>,ItemWriteListener<Formateur> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobListener.class);

    @Override
    public void afterJob(JobExecution jobExecution) {
        LOGGER.info("Exportation des donnees termine");
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        LOGGER.info("Lancement du Batch");
    }

    @Override
    public void beforeRead() {
        LOGGER.info("Lecture des donnees depuis le BD");
    }

    @Override
    public void afterRead(Formateur formateur) {
        LOGGER.info("Lecture terminee");
    }

    @Override
    public void onReadError(Exception e) {
        LOGGER.error("Une erreure s'est produite pendant la lecture des donnees", e);
    }

    @Override
    public void beforeWrite(List<? extends Formateur> list) {
        LOGGER.info("Ecriture des donnees dans un fichier");
    }

    @Override
    public void afterWrite(List<? extends Formateur> list) {
        LOGGER.info("Ecriture terminee");
    }

    @Override
    public void onWriteError(Exception e, List<? extends Formateur> list) {
        LOGGER.error("Une erreure s'est produite pendant l'ecriture du fichier", e);
    }
}
