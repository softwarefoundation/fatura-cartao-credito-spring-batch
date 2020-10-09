package com.softwarefoundation.faturacartaocreditospringbatch.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class FaturaCartaoCredito {

    private Cliente cliente;
    private CartaoCredito cartaoCredito;
    private List<Transacao> transacoes = new ArrayList();

    public Double getTotal() {
        return this.transacoes.stream().mapToDouble( Transacao::getValor)
                .reduce(0.0, Double::sum);
    }
}
