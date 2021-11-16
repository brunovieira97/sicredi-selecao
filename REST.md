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
