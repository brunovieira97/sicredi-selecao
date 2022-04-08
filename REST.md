# Projeto das Requisições e Respostas REST para a Aplicação

## Pautas

### Listar todas

```jsonc
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

```jsonc
// GET /pautas/{id}

// 200 OK
{
	"id": 1,
	"nome": "Nome"
}
```

### Cadastrar

```jsonc
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

```jsonc
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

```jsonc
// DELETE /pautas/{id}
// 200 OK
```

## Sessões de Votação

### Listar todas (da Pauta)

```jsonc
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

```jsonc
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

```jsonc
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

```jsonc
// DELETE /pautas/{idPauta}/sessoes/{idSessao}

// 200 OK
```

### Resultado da sessão

```jsonc
// GET /pautas/{idPauta}/sessoes/{idSessao}/resultado

// 200 OK
{
	"sim": 10,
	"nao": 5
}
```


## Votos da Sessão

### Listar todos

```jsonc
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

```jsonc
// GET /pautas/{idPauta}/sessoes/{idSessao}/votos/{idVoto}

// 200 OK
{
	"id": 1,
	"cpfAssociado": 01234567890,
	"voto": "S"
}
```

### Incluir

```jsonc
// POST /pautas/{id}/sessoes/{id}/votos
// 201 Created
{
	"cpfAssociado": 01234567890,
	"voto": "S"
}
```
