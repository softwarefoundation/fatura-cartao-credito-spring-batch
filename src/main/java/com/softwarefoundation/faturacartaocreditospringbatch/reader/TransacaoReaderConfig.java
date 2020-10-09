package com.softwarefoundation.faturacartaocreditospringbatch.reader;

import com.softwarefoundation.faturacartaocreditospringbatch.model.CartaoCredito;
import com.softwarefoundation.faturacartaocreditospringbatch.model.Cliente;
import com.softwarefoundation.faturacartaocreditospringbatch.model.Transacao;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class TransacaoReaderConfig {

    @Bean
    public JdbcCursorItemReader<Transacao> transacaoReader(DataSource dataSource){

        JdbcCursorItemReader<Transacao> reader = new JdbcCursorItemReaderBuilder<Transacao>()
                .name("transacaoReader")
                .dataSource(dataSource)
                .sql("select * from transacao t inner join cartao_credito c on c.numero_cartao_credito = t.numero_cartao_credito order by t.numero_cartao_credito")
                .rowMapper(rowMapperTransacao())
                .build();
        return reader;
    }

    private RowMapper<Transacao> rowMapperTransacao() {
        RowMapper<Transacao> mapper = new RowMapper<Transacao>() {
            @Override
            public Transacao mapRow(ResultSet rs, int i) throws SQLException {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getLong("cliente"));

                CartaoCredito cartaoCredito = new CartaoCredito();
                cartaoCredito.setNumeroCartaoCredito(rs.getLong("numero_cartao_credito"));
                cartaoCredito.setCliente(cliente);

                Transacao transacao = new Transacao();
                transacao.setId(rs.getLong("id"));
                transacao.setData(rs.getDate("data"));
                transacao.setDescricao(rs.getString("descricao"));
                transacao.setValor(rs.getDouble("valor"));
                transacao.setCartaoCredito(cartaoCredito);

                return transacao;
            }
        };
        return mapper;
    }

}
