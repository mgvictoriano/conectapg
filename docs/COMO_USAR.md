# 🚀 Como Usar - ConectaPG

## ✅ Está Funcionando Agora!

A aplicação está rodando em:
- **Swagger UI**: http://localhost:8081/api/swagger-ui.html
- **API**: http://localhost:8081/api

## 📋 Comandos Disponíveis

### Iniciar a aplicação
```bash
./start-docker.sh
```

### Ver logs em tempo real
```bash
./logs-docker.sh
```

### Parar a aplicação
```bash
./stop-docker.sh
```

### Rebuild após mudanças no código
```bash
./rebuild-docker.sh
```

## 🗄️ Banco de Dados

**Credenciais:**
- Host: `localhost:5432`
- Database: `conectapg`
- User: `postgres`
- Password: `postgres123`

**Dados de exemplo já incluídos:**
- 2 usuários (admin@conectapg.com, joao@example.com)
- 2 ocorrências de exemplo

## 🧪 Testando a API

### Via Swagger (Recomendado)
Acesse: http://localhost:8081/api/swagger-ui.html

### Via cURL

```bash
# Listar usuários
curl http://localhost:8081/api/usuarios

# Buscar usuário por ID
curl http://localhost:8081/api/usuarios/1

# Criar novo usuário
curl -X POST http://localhost:8081/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Teste Silva",
    "email": "teste@example.com",
    "senha": "senha123",
    "tipo": "CIDADAO"
  }'

# Listar ocorrências
curl http://localhost:8081/api/ocorrencias
```

## 🔧 Comandos Docker Úteis

```bash
# Ver containers rodando
docker ps

# Ver logs do backend
docker logs -f conectapg-backend

# Ver logs do PostgreSQL
docker logs -f conectapg-postgres

# Parar um container específico
docker stop conectapg-backend

# Iniciar um container parado
docker start conectapg-backend

# Entrar no PostgreSQL
docker exec -it conectapg-postgres psql -U postgres -d conectapg
```

## 🐛 Problemas Comuns

### Porta 8081 em uso
Edite o arquivo `start-docker.sh` e mude a linha:
```bash
-p 8081:8080 \
```
Para outra porta, exemplo:
```bash
-p 9090:8080 \
```

### Container não inicia
```bash
# Veja os logs
docker logs conectapg-backend

# Tente rebuild
./rebuild-docker.sh
```

### Resetar tudo
```bash
./stop-docker.sh
docker volume prune -f
./start-docker.sh
```

## 📝 Notas

- Os dados do banco são persistidos mesmo após parar os containers
- Para desenvolvimento, faça mudanças no código e execute `./rebuild-docker.sh`
- A aplicação demora cerca de 10-15 segundos para inicializar completamente

---

**Pronto para desenvolver! 🎉**
