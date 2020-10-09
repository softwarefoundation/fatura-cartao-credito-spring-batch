package com.softwarefoundation.faturacartaocreditospringbatch.step;

import com.softwarefoundation.faturacartaocreditospringbatch.model.FaturaCartaoCredito;
import com.softwarefoundation.faturacartaocreditospringbatch.model.Transacao;
import com.softwarefoundation.faturacartaocreditospringbatch.reader.FautraCartaoCreditoReader;
import com.softwarefoundation.faturacartaocreditospringbatch.writer.TotalTransacoesFooterCallback;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FaturaCartaoCreditoStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step faturaCartaoCreditoStep(ItemStreamReader<Transacao> faturaCartaoCreditoItemReader
    , ItemProcessor<FaturaCartaoCredito,FaturaCartaoCredito> faturaCartaoCreditoItemProcessor
    , ItemWriter<FaturaCartaoCredito> faturaCartaoCreditoItemWriter
    , TotalTransacoesFooterCallback callbackListner){
        return stepBuilderFactory.get("faturaCartaoCreditoStep")
                .<FaturaCartaoCredito,FaturaCartaoCredito>chunk(1)
                .reader(new FautraCartaoCreditoReader(faturaCartaoCreditoItemReader))
                .processor(faturaCartaoCreditoItemProcessor)
                .writer(faturaCartaoCreditoItemWriter)
                .listener(callbackListner)
                .build();
    }

}
