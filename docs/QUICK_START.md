# ⚡ Quick Start - ConectaPG

## 🚀 Iniciar o Projeto (3 passos)

### 1️⃣ Certifique-se que o Docker está rodando

```bash
docker --version
# Deve mostrar: Docker version 29.0.1 ou superior
```

### 2️⃣ Inicie a aplicação

```bash
./start.sh
```

Aguarde cerca de 30-60 segundos para tudo inicializar.

### 3️⃣ Acesse a aplicação

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **API Base**: http://localhost:8080/api

## 🎯 Testando a API

### Via Swagger (Recomendado)

1. Abra: http://localhost:8080/api/swagger-ui.html
2. Teste os endpoints diretamente pela interface

### Via cURL

```bash
# Listar usuários
curl http://localhost:8080/api/usuarios

# Listar ocorrências
curl http://localhost:8080/api/ocorrencias

# Criar novo usuário
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Teste Silva",
    "email": "teste@example.com",
    "senha": "senha123",
    "tipo": "CIDADAO"
  }'
```

## 📊 Comandos Úteis

```bash
./logs.sh          # Ver logs em tempo real
./stop.sh          # Parar a aplicação
./rebuild.sh       # Rebuild após mudanças no código
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

## 🆘 Problemas?

### Porta 8080 em uso

```bash
# Descubra o processo
sudo lsof -i :8080

# Ou mude a porta no docker-compose.yml
```

### Erro ao iniciar

```bash
# Veja os logs
./logs.sh backend

# Tente rebuild
./rebuild.sh
```

### Resetar tudo

```bash
docker compose down -v
./start.sh
```

## 📚 Mais Informações

- **Setup Completo**: [SETUP.md](./SETUP.md)
- **Documentação**: [README.md](./README.md)

---

**Pronto para desenvolver! 🎉**
