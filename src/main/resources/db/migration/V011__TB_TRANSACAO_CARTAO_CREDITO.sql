CREATE TABLE transacao (
  id int NOT NULL,
  numero_cartao_credito int NOT NULL,
  descricao varchar(255) NOT NULL,
  valor float NOT NULL,
  data date NOT NULL,
  PRIMARY KEY (id)
);
