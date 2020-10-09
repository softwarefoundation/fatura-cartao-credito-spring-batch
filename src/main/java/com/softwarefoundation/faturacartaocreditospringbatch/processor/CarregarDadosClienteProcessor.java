package com.softwarefoundation.faturacartaocreditospringbatch.processor;

import com.softwarefoundation.faturacartaocreditospringbatch.model.Cliente;
import com.softwarefoundation.faturacartaocreditospringbatch.model.FaturaCartaoCredito;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.ValidationException;

@Slf4j
@Component
public class CarregarDadosClienteProcessor implements ItemProcessor<FaturaCartaoCredito, FaturaCartaoCredito> {

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public FaturaCartaoCredito process(FaturaCartaoCredito fatura) throws Exception {

        String url = String.format("https://my-json-server.typicode.com/giuliana-bezerra/demo/profile/%d",fatura.getCliente().getId());
        ResponseEntity<Cliente> response = restTemplate.getForEntity(url, Cliente.class);

        if(!response.getStatusCode().equals(HttpStatus.OK)){
            throw new ValidationException("Cliente não encontrado");
        }
        fatura.setCliente(response.getBody());

        log.info("Cliente: {}",fatura.getCliente().getNome());
        return fatura;
    }
}
