package com.softwarefoundation.faturacartaocreditospringbatch.writer;

import com.softwarefoundation.faturacartaocreditospringbatch.model.FaturaCartaoCredito;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.*;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemWriterBuilder;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.io.Writer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

@Slf4j
@Configuration
public class ArquivoFaturaCartaoCreditoWriterConfig {

    @Bean
    public MultiResourceItemWriter<FaturaCartaoCredito> arquivosFaturaWriter() {
        return new MultiResourceItemWriterBuilder<FaturaCartaoCredito>()
                .name("arquivosFaturaWriter")
                .resource(new FileSystemResource("files/fatura"))
                .itemCountLimitPerResource(1)
                .resourceSuffixCreator(suffix())
                .delegate(arquivoFatura())
                .build();
    }

    /**
     * @return
     */
    private FlatFileItemWriter<FaturaCartaoCredito> arquivoFatura() {
        return new FlatFileItemWriterBuilder<FaturaCartaoCredito>()
                .name("arquivoFatura")
                .resource(new FileSystemResource("files/fatura.txt"))
                .lineAggregator(aggregator())
                .headerCallback(headerCallback())
                .footerCallback(footerCallback())
                .build();
    }


    @Bean
    public FlatFileFooterCallback footerCallback() {
        return new TotalTransacoesFooterCallback();
    }

    public FlatFileHeaderCallback headerCallback() {
        return new FlatFileHeaderCallback() {
            @Override
            public void writeHeader(Writer writer) throws IOException {
                writer.append(String.format("%107s", "CARTÃO DE CRÉDITO SUPER BANK"));
                //writer.append(String.format("%107s\n\n", "Rua Verqueiro, 131"));
            }
        };
    }

    

    /**
     * @return
     */
    private LineAggregator<FaturaCartaoCredito> aggregator() {
        return new LineAggregator<FaturaCartaoCredito>() {
            @Override
            public String aggregate(FaturaCartaoCredito fatura) {
                StringBuilder writer = new StringBuilder();
                writer.append(String.format("Nome: %s\n", fatura.getCliente().getNome()));
                writer.append(String.format("Endereço: %s\n\n\n", fatura.getCliente().getEndereco()));
                writer.append(String.format("Fatura completa do cartão %d\n", fatura.getCartaoCredito().getNumeroCartaoCredito()));
                writer.append("-----------------------------------------------------------------------------------------------------------\n");
                writer.append("DATA DESCRICAO VALOR \n");
                writer.append("-----------------------------------------------------------------------------------------------------------\n");

                fatura.getTransacoes().forEach(transacao -> {
                    writer.append(String.format("\n[%10s] %-80s - %s", new SimpleDateFormat("dd/MM/yyyy").format(transacao.getData())
                            , transacao.getDescricao()
                            , NumberFormat.getCurrencyInstance().format(transacao.getValor())));
                });

                return writer.toString();
            }
        };
    }

    /**
     * @return
     */
    private ResourceSuffixCreator suffix() {
        return new ResourceSuffixCreator() {
            @Override
            public String getSuffix(int index) {
                return String.format("%d.txt", index);
            }
        };
    }

}
