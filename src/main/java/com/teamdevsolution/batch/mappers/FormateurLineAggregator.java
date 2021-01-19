package com.teamdevsolution.batch.mappers;

import com.teamdevsolution.batch.domain.Formateur;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.file.transform.LineAggregator;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormateurLineAggregator implements LineAggregator<Formateur> {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    @Override
    public String aggregate(Formateur formateur) {
        StringBuilder line = new StringBuilder();
        line.append(StringUtils.leftPad(formateur.getId().toString(),5, '0'));
        line.append(StringUtils.rightPad(formateur.getNom(),15));
        line.append(StringUtils.rightPad(formateur.getPrenom(),15));
        line.append(StringUtils.rightPad(formateur.getAdresseEmail(),25));
        line.append(sdf.format(new Date()));
        line.append(StringUtils.rightPad("",25));
        return line.toString();
    }
}
