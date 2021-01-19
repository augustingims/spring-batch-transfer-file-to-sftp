package com.teamdevsolution.batch.readers;

import com.teamdevsolution.batch.domain.Formateur;
import com.teamdevsolution.batch.repository.FormateurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormateurReader extends RepositoryItemReader<Formateur> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormateurReader.class);

    public FormateurReader(FormateurRepository formateurRepository) {
        setMethodName("findAll");
        setRepository(formateurRepository);
        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);
        setSort(sorts);
    }

    @Override
    protected List<Formateur> doPageRead() throws Exception {
        return super.doPageRead();
    }
}
