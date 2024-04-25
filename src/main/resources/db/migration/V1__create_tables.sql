create table pautas(

    id SERIAL PRIMARY KEY,
    descricao varchar(100) not null
);

create table usuarios(

    id SERIAL PRIMARY KEY,
    nome varchar(100) not null,
    cpf varchar(14) not null unique
);

create table sessao(
    id SERIAL PRIMARY KEY,
    pauta_id bigint not null,
    data_hora_inicio TIMESTAMP not null,
    data_hora_fim TIMESTAMP not null,
    FOREIGN KEY (pauta_id) REFERENCES Pautas (id)
);

create table votacao(
    id SERIAL PRIMARY KEY,
    usuario_id bigint not null,
    pauta_id bigint not null,
    voto char(3),
    data_hora_votacao TIMESTAMP not null,
    FOREIGN KEY (usuario_id) REFERENCES Usuarios (id),
    FOREIGN KEY (pauta_id) REFERENCES Pautas (id)
);