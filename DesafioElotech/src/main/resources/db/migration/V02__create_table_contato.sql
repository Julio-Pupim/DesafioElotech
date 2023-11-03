CREATE TABLE IF NOT EXISTS contato(
    idkey SERIAL,
    idkey_pessoa INT,
    nome TEXT,
    telefone text,
    email TEXT,
    CONSTRAINT contato_pk PRIMARY KEY (idkey),
    CONSTRAINT pessoa_fk FOREIGN KEY (idkey_pessoa) REFERENCES pessoa(idkey)
);