# 🚀 Guia de Setup - ConectaPG

Este guia vai te ajudar a configurar e rodar o projeto ConectaPG localmente.

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Docker** (versão 20.10 ou superior)
- **Docker Compose** (versão 2.0 ou superior)

### Verificar instalação

```bash
docker --version
docker compose version
```

## 🎯 Início Rápido (Recomendado)

### 1. Clone o repositório (se ainda não fez)

```bash
git clone <url-do-repositorio>
cd conectapg
```

### 2. Inicie o projeto

```bash
./start.sh
```

Esse script vai:
- ✅ Criar o arquivo `.env` (se não existir)
- ✅ Subir o PostgreSQL
- ✅ Buildar e subir a aplicação Spring Boot
- ✅ Executar as migrations do Flyway automaticamente

### 3. Aguarde a inicialização

Aguarde cerca de 30-60 segundos para a aplicação inicializar completamente.

### 4. Acesse a aplicação

- **API Base**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **API Docs**: http://localhost:8080/api/v3/api-docs

## 🛠️ Comandos Úteis

### Ver logs em tempo real

```bash
./logs.sh
```

Ou para ver logs de um serviço específico:

```bash
./logs.sh backend    # Logs da aplicação Spring Boot
./logs.sh postgres   # Logs do PostgreSQL
```

### Parar a aplicação

```bash
./stop.sh
```

### Rebuild completo (após mudanças no código)

```bash
./rebuild.sh
```

### Comandos Docker Compose diretos

```bash
# Iniciar
docker compose up -d

# Parar
docker compose down

# Ver status
docker compose ps

# Ver logs
docker compose logs -f

# Rebuild
docker compose up -d --build

# Parar e remover volumes (apaga dados do banco)
docker compose down -v
```

## 🗄️ Banco de Dados

### Credenciais padrão

- **Host**: localhost
- **Port**: 5432
- **Database**: conectapg
- **Username**: postgres
- **Password**: postgres123

### Conectar via psql

```bash
docker exec -it conectapg-postgres psql -U postgres -d conectapg
```

### Conectar via cliente GUI

Use qualquer cliente PostgreSQL (DBeaver, pgAdmin, etc.) com as credenciais acima.

### Dados de exemplo

O banco já vem com dados de exemplo criados pela migration:

**Usuários:**
- Admin: `admin@conectapg.com` / senha: `password` (hash BCrypt)
- Cidadão: `joao@example.com` / senha: `password` (hash BCrypt)

**Ocorrências:**
- 2 ocorrências de exemplo já cadastradas

## 🧪 Testando a API

### Via Swagger UI (Recomendado)

1. Acesse: http://localhost:8080/api/swagger-ui.html
2. Explore os endpoints disponíveis
3. Teste diretamente pela interface

### Via cURL

```bash
# Listar todos os usuários
curl http://localhost:8080/api/usuarios

# Buscar usuário por ID
curl http://localhost:8080/api/usuarios/1

# Criar novo usuário
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria Santos",
    "email": "maria@example.com",
    "senha": "senha123",
    "tipo": "CIDADAO",
    "ativo": true
  }'

# Listar todas as ocorrências
curl http://localhost:8080/api/ocorrencias
```

## 🔧 Desenvolvimento Local (sem Docker)

Se preferir rodar sem Docker:

### 1. Instale o PostgreSQL localmente

```bash
# Ubuntu/Debian
sudo apt install postgresql postgresql-contrib

# macOS
brew install postgresql
```

### 2. Crie o banco de dados

```bash
createdb conectapg
```

### 3. Configure as variáveis de ambiente

```bash
export DB_USERNAME=postgres
export DB_PASSWORD=sua_senha
```

### 4. Execute a aplicação

```bash
cd backend
./mvnw spring-boot:run
```

## 🐛 Troubleshooting

### Porta 8080 já está em uso

```bash
# Descubra qual processo está usando a porta
sudo lsof -i :8080

# Ou mude a porta no docker-compose.yml
ports:
  - "8081:8080"  # Acesse via localhost:8081
```

### Porta 5432 já está em uso

Se você já tem PostgreSQL rodando localmente:

```bash
# Opção 1: Pare o PostgreSQL local
sudo systemctl stop postgresql

# Opção 2: Mude a porta no docker-compose.yml
ports:
  - "5433:5432"  # PostgreSQL no Docker na porta 5433
```

### Erro de permissão no Docker

```bash
# Adicione seu usuário ao grupo docker
sudo usermod -aG docker $USER

# Faça logout e login novamente
```

### Container não inicia

```bash
# Veja os logs detalhados
docker compose logs backend

# Tente rebuild
./rebuild.sh

# Ou remova tudo e comece do zero
docker compose down -v
docker system prune -a
./start.sh
```

### Flyway migration error

Se houver erro nas migrations:

```bash
# Conecte no banco e limpe as tabelas
docker exec -it conectapg-postgres psql -U postgres -d conectapg

# No psql:
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
\q

# Reinicie a aplicação
docker compose restart backend
```

## 📝 Variáveis de Ambiente

Você pode customizar as configurações editando o arquivo `.env`:

```env
# Database
POSTGRES_DB=conectapg
POSTGRES_USER=postgres
POSTGRES_PASSWORD=sua_senha_aqui

# Spring Boot
SPRING_PROFILES_ACTIVE=dev
DB_USERNAME=postgres
DB_PASSWORD=sua_senha_aqui

# Server
SERVER_PORT=8080
```

## 🔐 Segurança

⚠️ **IMPORTANTE**: As credenciais padrão são apenas para desenvolvimento local!

Para produção:
- ✅ Mude todas as senhas
- ✅ Use variáveis de ambiente seguras
- ✅ Configure HTTPS
- ✅ Implemente autenticação JWT
- ✅ Configure CORS adequadamente

## 📚 Próximos Passos

Depois de configurar o ambiente:

1. ✅ Explore a API via Swagger
2. ✅ Teste os endpoints de usuários e ocorrências
3. ✅ Leia a documentação no README.md
4. ✅ Comece a desenvolver novas features!

## 💡 Dicas

- Use o Swagger UI para testar a API de forma interativa
- Os logs do Spring Boot aparecem em tempo real com `./logs.sh backend`
- O banco de dados persiste entre restarts (dados não são perdidos)
- Para resetar tudo: `docker compose down -v && ./start.sh`

## 🆘 Precisa de Ajuda?

Se encontrar problemas:

1. Verifique os logs: `./logs.sh`
2. Verifique o status: `docker compose ps`
3. Tente rebuild: `./rebuild.sh`
4. Consulte a seção de Troubleshooting acima

---

**Bom desenvolvimento! 🚀**
