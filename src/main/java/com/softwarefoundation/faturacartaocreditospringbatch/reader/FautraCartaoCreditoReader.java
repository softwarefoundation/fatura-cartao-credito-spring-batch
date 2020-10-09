package com.softwarefoundation.faturacartaocreditospringbatch.reader;

import com.softwarefoundation.faturacartaocreditospringbatch.model.FaturaCartaoCredito;
import com.softwarefoundation.faturacartaocreditospringbatch.model.Transacao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;

import java.util.Objects;

@Slf4j
public class FautraCartaoCreditoReader implements ItemStreamReader<FaturaCartaoCredito> {

    private ItemStreamReader<Transacao> delegate;
    private Transacao transacaoAtual;

    public FautraCartaoCreditoReader(ItemStreamReader<Transacao> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        delegate.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        delegate.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        delegate.close();
    }


    @Override
    public FaturaCartaoCredito read() throws Exception {
        if (transacaoAtual == null) {
            transacaoAtual = delegate.read();
        }
        FaturaCartaoCredito fatura = null;
        Transacao transacao = transacaoAtual;
        transacaoAtual = null;

        if (Objects.nonNull(transacao)) {
            fatura = new FaturaCartaoCredito();
            fatura.setCartaoCredito(transacao.getCartaoCredito());
            fatura.setCliente(transacao.getCartaoCredito().getCliente());

            while (isTransacaoFatura(transacao)) {
                fatura.getTransacoes().add(transacaoAtual);
            }
            log.info("Fatura cartão: {}", fatura.getCartaoCredito().getNumeroCartaoCredito());
        }
        return fatura;
    }

    private boolean isTransacaoFatura(Transacao transacao) throws Exception {
        return Objects.nonNull(peek())
                && transacao.getCartaoCredito().getNumeroCartaoCredito().equals(transacaoAtual.getCartaoCredito().getNumeroCartaoCredito());
    }

    private Transacao peek() throws Exception {
        transacaoAtual = delegate.read();
        return transacaoAtual;
    }
}
