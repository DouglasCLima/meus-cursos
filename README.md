
# Cursos Realizados API


Projeto API de Gerenciamento de Cursos
Este projeto é uma API RESTful desenvolvida com Quarkus, com o objetivo de gerenciar cursos, escolas e linguagens aprendidas. O sistema permite cadastro, atualização e listagem dessas entidades, além de implementar recursos avançados como idempotência, autenticação via chave de API, rate limiting, validações, e documentação OpenAPI.

🚀 Tecnologias Utilizadas
Java 17+

Quarkus 3.x

Hibernate ORM com Panache

H2 Database (em memória)

Bean Validation (Jakarta Validation)

OpenAPI / Swagger UI

RESTEasy Reactive

JSON-B (serialização)

Maven

📦 Funcionalidades Implementadas
1. CRUD Completo
   Escolas: criação, listagem, atualização e remoção

Cursos: com associação a escolas e suporte a datas

Linguagens Aprendidas: com enumeração de níveis

2. Recursos Avançados de API
   ✅ Idempotência para requisições POST (evita duplicações acidentais)

✅ Autenticação via chave de API (API Key baseada em filtro)

✅ Rate Limiting configurado por IP e endpoint

✅ CORS configurado para acesso de domínios específicos

✅ Versionamento de API via URL (/api/v1/)

✅ Validações robustas com mensagens de erro claras

✅ Tratamento global de exceções

✅ Documentação Swagger/OpenAPI totalmente gerada via anotações

🔑 Autenticação com API Key
Cada requisição a rotas protegidas deve conter o cabeçalho:

vbnet
Copiar
Editar
Authorization: Api-Key SUA_CHAVE_AQUI
As chaves são gerenciadas manualmente ou via endpoint (se implementado).

⛔ Rate Limiting
Limite configurado por IP para evitar abuso da API

Cabeçalhos retornados:

makefile
Copiar
Editar
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 95
Excesso de requisições retorna HTTP 429 Too Many Requests

🔁 Idempotência
Para garantir que um POST não seja executado duas vezes por engano, use o cabeçalho:

makefile
Copiar
Editar
Idempotency-Key: um_valor_unico
Se a mesma chave for usada novamente, a API retorna a mesma resposta da primeira requisição.

🌐 Documentação da API
A interface Swagger UI está disponível em:

bash
Copiar
Editar
http://localhost:8080/q/swagger-ui
Todos os endpoints estão documentados com exemplos, parâmetros, e códigos de resposta.

📁 Estrutura do Projeto
bash
Copiar
Editar
src/main/java/
├── org.acme.model/           # Entidades (Curso, Escola, LinguagemAprendida)
├── org.acme.dtos/            # DTOs de entrada
├── org.acme.resource/        # Endpoints REST (CursoResource, EscolaResource etc.)
├── org.acme.service/         # Lógica de negócio (se aplicável)
├── org.acme.filter/          # Filtros de autenticação e rate limiting
└── org.acme.exception/       # Manipuladores globais de erro

src/main/resources/
├── application.properties    # Configurações do Quarkus
🛠️ Executando o Projeto
bash
Copiar
Editar
./mvnw quarkus:dev
Acesse: http://localhost:8080/q/swagger-ui

📌 Endpoints Principais
Método	Rota	Descrição
GET	/api/v1/cursos	Lista todos os cursos
POST	/api/v1/cursos	Cadastra novo curso (idempotente)
PUT	/api/v1/cursos/{id}	Atualiza um curso existente
DELETE	/api/v1/cursos/{id}	Remove um curso
GET	/api/v1/escolas	Lista todas as escolas
POST	/api/v1/linguagens	Cadastra nova linguagem aprendida

🧪 Testes (opcional)
Testes podem ser adicionados com JUnit e RestAssured.

📃 Licença
Este projeto é apenas para fins acadêmicos e de aprendizado. Não possui licença comercial.
