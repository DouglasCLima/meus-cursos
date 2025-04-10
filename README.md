
# Cursos Realizados API

API RESTful desenvolvida com **Quarkus** para gerenciar cursos realizados, suas respectivas escolas e linguagens aprendidas. Projeto final do curso de Desenvolvimento de APIs com Quarkus.

## 🔧 Tecnologias

- Java 11
- Quarkus
- Hibernate ORM + Panache
- Banco de dados H2
- Bean Validation
- OpenAPI/Swagger
- Maven

---

## 📦 Como executar o projeto

1. **Clone o repositório**
   ```bash
   git clone https://github.com/DouglasCLima/meus-cursos.git
   cd meus-cursos
   ```

2. **Execute com Quarkus dev mode**
   ```bash
   ./mvnw compile quarkus:dev
   ```

3. **Acesse o Swagger UI**
   ```
   http://localhost:8080/q/swagger-ui
   ```

---

## 🗂️ Estrutura das Entidades

### 🏫 Escola
- `id`: Long
- `nome`: String
- `local`: String
- `site`: String

### 📘 Curso
- `id`: Long
- `nome`: String
- `descricao`: String
- `cargaHoraria`: Integer
- `dataConclusao`: LocalDate
- `escola`: Escola (Many-to-One)
- `linguagensAprendidas`: List<LinguagemAprendida> (Many-to-Many)

### 💻 LinguagemAprendida
- `id`: Long
- `nome`: String
- `tipo`: Enum (BACKEND, FRONTEND, FULLSTACK, MOBILE, DATABASE)
- `descricao`: String

---

## 🔗 Relacionamentos

- **Curso** possui uma **Escola** (Many-to-One)
- **Curso** possui muitas **LinguagensAprendidas** (Many-to-Many)

---

## 📌 Endpoints

Cada entidade possui ao menos 5 endpoints REST:

### Escola
- `GET /escolas`
- `GET /escolas/{id}`
- `POST /escolas`
- `PUT /escolas/{id}`
- `DELETE /escolas/{id}`
- `GET /escolas/busca?nome={nome}`

### Curso
- `GET /cursos`
- `GET /cursos/{id}`
- `POST /cursos`
- `PUT /cursos/{id}`
- `DELETE /cursos/{id}`
- `GET /cursos/por-escola/{idEscola}`

### LinguagemAprendida
- `GET /linguagens`
- `GET /linguagens/{id}`
- `POST /linguagens`
- `PUT /linguagens/{id}`
- `DELETE /linguagens/{id}`
- `GET /linguagens/por-tipo/{tipo}`

---

## 📂 Coleção Postman

Você pode importar a [coleção Postman](CursosRealizados.postman_collection.json) no Postman para testar todos os endpoints.

---

## 📋 Exemplo de Requisições

### Criar uma escola
```json
POST /escolas
{
  "nome": "Alura",
  "local": "Online",
  "site": "https://www.alura.com.br"
}
```

### Criar uma linguagem
```json
POST /linguagens
{
  "nome": "Java",
  "tipo": "BACKEND",
  "descricao": "Linguagem voltada ao desenvolvimento backend"
}
```

### Criar um curso
```json
POST /cursos
{
  "nome": "Formação Java",
  "descricao": "Curso completo de Java com orientação a objetos",
  "cargaHoraria": 40,
  "dataConclusao": "2024-05-10",
  "escola": {
    "id": 1
  },
  "linguagensAprendidas": [
    { "id": 1 }
  ]
}
```

---

## 📘 Observações

- Todos os dados são persistidos em um banco de dados H2 em memória.
- O projeto pode ser reiniciado a qualquer momento sem necessidade de configuração adicional.
- A documentação completa pode ser acessada via Swagger UI após iniciar o projeto.