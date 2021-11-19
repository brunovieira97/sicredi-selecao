# Seleção Sicredi - Dev Back-End

Este projeto foi desenvolvido como parte do processo seletivo para o cargo de Desenvolvedor no Sicredi.

A aplicação consiste em uma API para controle de sessões de votação em uma cooperativa.

## Requisitos

A especificação do desafio pode ser lida em [REQUIREMENTS.md](REQUIREMENTS.md).

Os seguintes itens foram cobertos:

 - API com CRUD de Pauta, Sessão e Voto
 - Validação dos dados
 - Endpoint para contabilização de votos

Tarefas bônus:

 - Integração com sistema externo (able to vote)
 - Versionamento da API

## Documentação

Foi utilizado Swagger para documentar todos os endpoints disponíveis.
É possível acessá-lo através da URL `http://localhost:8080/swagger-ui.html`.

A especificação das requisições e respostas REST pode ser lida em [REST.md](REST.md).

## O que foi utilizado

As seguintes decisões foram feitas quanto à stack:
 
 - Java
 - Spring (Boot, JPA)
 - JUnit (testes de integração)
 - Banco de Dados H2 (em disco)
 - ModelMapper
 - Rest Assured
 - Swagger (documentação)

A stack, framework e libs foram selecionados com base em afinidade (para Java, Spring e suite de testes), assim como referências de experiências anteriores e estudos que realizei - como no caso de Swagger, ModelMapper e Rest Assured.

No tocante à arquitetura e organização do projeto, muito se deve à base que tive (em minha experiência como dev Java) e nos cursos que realizei. Tirando alguns detalhes no versionamento e no uso de hierarquia de endpoints, mantive a estrutura dentro das minhas preferências, de uma forma legível e de fácil manutenibilidade, ao meu ver.

## O que faltou

### Tarefas bônus

 - Mensageria
 - Testes de performance

### Arquitetura

 - A organização da arquitetura REST ficou um pouco complexa ao meu ver. Por falta de exemplos melhores com base na minha experiência, segui com esse desenho hierárquico, que tenho consciência de que não escala tão bem quanto algo mais simplificado (flat).

### Testes

 - Não foram feitos testes a nível de camada (unit tests), apenas de integração
 - Ao adicionar a integração com a API externa, que decide se um associado pode ou não votar, os testes de integração dos endpoints de voto deixaram de funcionar de forma consistente.
 - Minha experiência com testes é breve, se limitando a projetos pessoais. Por conta disso, fiquei com o sentimento de over-engineering e over-complexity nos meus testes, que me parecem verbosos demais.

### Outros

 - Lidar com os enums consistentemente entre JSON, DTO, entidade e Banco de Dados foi complicado. Devido a isso, consegui manter a comunicação externa (JSON) e interna dentro do esperado, porém ao realizar a persistência, o enum `ValorVoto` é armazenado como Integer e não Character.

---

Agradeço todo e qualquer feedback a respeito do que foi desenvolvido.
