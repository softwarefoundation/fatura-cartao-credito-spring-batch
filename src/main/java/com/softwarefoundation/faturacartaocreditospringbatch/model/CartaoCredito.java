package com.softwarefoundation.faturacartaocreditospringbatch.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CartaoCredito {

    private Long numeroCartaoCredito;
    private Cliente cliente;

}
