package com.teamdevsolution.batch.callback;

import com.teamdevsolution.batch.utils.ConstantUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.file.FlatFileHeaderCallback;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormateurHeaderCallBack implements FlatFileHeaderCallback {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    @Override
    public void writeHeader(Writer writer) throws IOException {
        Date date = new Date();
        writer.append(ConstantUtils.CODE_HEADER);
        writer.append(ConstantUtils.CODE_GENERATE);
        writer.append(sdf.format(date));
        writer.append(StringUtils.rightPad("",50));
    }
}
