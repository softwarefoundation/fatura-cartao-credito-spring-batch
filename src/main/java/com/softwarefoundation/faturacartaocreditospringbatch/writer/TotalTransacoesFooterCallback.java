package com.softwarefoundation.faturacartaocreditospringbatch.writer;

import com.softwarefoundation.faturacartaocreditospringbatch.model.FaturaCartaoCredito;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.file.FlatFileFooterCallback;

import java.io.IOException;
import java.io.Writer;
import java.text.NumberFormat;
import java.util.List;

@Slf4j
public class TotalTransacoesFooterCallback implements FlatFileFooterCallback {

    private Double total = 0.0D;

    @Override
    public void writeFooter(Writer writer) throws IOException {
        writer.write(String.format("\n%107s", "Total: "+ NumberFormat.getCurrencyInstance().format(total)));
    }

    @BeforeWrite
    public void beforeWriteList(List<FaturaCartaoCredito> faturas){
        faturas.forEach( fatura-> total+= fatura.getTotal());
        log.info("Valor total da fatura: {}", NumberFormat.getCurrencyInstance().format(total));
    }

    @AfterChunk
    public void afterChunck(ChunkContext context){
        total = 0.0D;
    }

}
