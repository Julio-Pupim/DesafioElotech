CREATE TABLE IF NOT EXISTS pessoa (
    idkey SERIAL,
    nome TEXT,
    cpf CHAR(11),
    datanascimento DATE,
    CONSTRAINT pessoa_pk PRIMARY KEY (idkey)
);