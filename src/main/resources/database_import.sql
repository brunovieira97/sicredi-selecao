insert into pauta (nome) values ('Utilização de Java');
insert into pauta (nome) values ('Utilização de React');
insert into sessao (data_hora_abertura, data_hora_fechamento, pauta_id) values ('2021-12-10T08:00:00', '2021-12-12T08:00:00', 1);
insert into sessao (data_hora_abertura, data_hora_fechamento, pauta_id) values ('2022-12-10T08:00:00', '2023-12-10T08:00:00', 2);
insert into voto (cpf_associado, sessao_id, valor_voto) values ('76235917031', 1, 'S');
insert into voto (cpf_associado, sessao_id, valor_voto) values ('76235917031', 2, 'N');
