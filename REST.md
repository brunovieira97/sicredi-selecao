# Projeto das Requisições e Respostas REST para a Aplicação

## Pautas

### Listar todas

```json
// GET /pautas

// 200 OK
[
	{
		"id": 1,
		"nome": "Pauta 1"
	},
	...
]
```

### Listar por ID

```json
// GET /pautas/{id}

// 200 OK
{
	"id": 1,
	"nome": "Nome"
}
```

### Cadastrar

```json
// POST /pautas
{
	"nome": "Nome da Pauta"
}

// 201 Created
{
	"id": 1,
	"nome": "Nome da Pauta"
}
```

### Atualizar

```json
// PUT /pautas/{id}
{
	"nome": "Novo nome"
}

// 200 OK
{
	"id": 1,
	"nome": "Novo nome"
}
```

### Excluir

```json
// DELETE /pautas/{id}
// 200 OK
```

## Sessões de Votação

### Listar todas (da Pauta)

```json
// GET /pautas/{idPauta}/sessoes

// 200 OK
[
	{
		"id": 1,
		"dataHoraAbertura": "2018-09-16T08:00:00",
		"dataHoraFechamento": "2018-09-16T08:00:00",
		// A -> Aberta
		// F -> Fechada
		"status": "A"
	},
	...
]
```

### Listar por ID

```json
// GET /pautas/{idPauta}/sessoes/{idSessao}

// 200 OK
{
	"id": 1,
	"dataHoraAbertura": "2018-09-16T08:00:00",
	"dataHoraFechamento": "2018-09-16T08:00:00",
	"status": "A"
}
```

### Cadastrar

```json
// POST /pautas/{idPauta}/sessoes
{
	"dataHoraAbertura": "2018-09-16T08:00:00",
	"dataHoraFechamento": "2018-09-16T08:00:00"
}

// 201 Created
{
	"id": 1,
	"dataHoraAbertura": "2018-09-16T08:00:00",
	"dataHoraFechamento": "2018-09-16T08:00:00",
	"status": "F"
}
```

### Deletar

```json
// DELETE /pautas/{idPauta}/sessoes/{idSessao}

// 200 OK
```

### Resultado da sessão

```json
// GET /pautas/{idPauta}/sessoes/{idSessao}/resultado

// 200 OK
{
	"sim": 10,
	"nao": 5
}
```


## Votos da Sessão

### Listar todos

```json
// GET /pautas/{idPauta}/sessoes/{idSessao}/votos

// 200 OK
[
	{
		"id": 1,
		"cpfAssociado": 01234567890,
		// S -> Sim
		// N -> Não
		"voto": "S"
	}
	...
]
```

### Listar por ID

```json
// GET /pautas/{idPauta}/sessoes/{idSessao}/votos/{idVoto}

// 200 OK
{
	"id": 1,
	"cpfAssociado": 01234567890,
	"voto": "S"
}
```

### Incluir

```json
// POST /pautas/{id}/sessoes/{id}/votos
// 201 Created
{
	"cpfAssociado": 01234567890,
	"voto": "S"
}
```
