# 🔗 Conexão Frontend ↔ Backend

## ✅ Status: TOTALMENTE CONECTADO

O frontend está **100% configurado** para se comunicar com o backend!

## 📊 Diagrama de Conexão

```
┌─────────────────────────────────────────────────────────────┐
│                    NAVEGADOR (Browser)                      │
│                  http://localhost:3000                      │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      │ Requisição: /api/usuarios
                      ↓
┌─────────────────────────────────────────────────────────────┐
│              VITE DEV SERVER (Frontend)                     │
│                   Porta: 3000                               │
│                                                             │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  PROXY CONFIGURADO                                   │  │
│  │  /api/* → http://localhost:8081/api/*               │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      │ Encaminha para: http://localhost:8081/api/usuarios
                      ↓
┌─────────────────────────────────────────────────────────────┐
│           SPRING BOOT API (Backend)                         │
│                  Porta: 8081                                │
│            Context Path: /api                               │
│                                                             │
│  Endpoints disponíveis:                                     │
│  • GET  /api/usuarios                                       │
│  • GET  /api/usuarios/{id}                                  │
│  • POST /api/usuarios                                       │
│  • GET  /api/ocorrencias                                    │
│  • POST /api/ocorrencias                                    │
│  • etc...                                                   │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ↓
┌─────────────────────────────────────────────────────────────┐
│              POSTGRESQL (Database)                          │
│                  Porta: 5432                                │
│                Database: conectapg                          │
└─────────────────────────────────────────────────────────────┘
```

## 🔧 Configuração Atual

### 1. Vite Proxy (vite.config.js)

```javascript
server: {
  port: 3000,
  proxy: {
    '/api': {
      target: 'http://localhost:8081',
      changeOrigin: true,
    }
  }
}
```

**O que isso faz:**
- Frontend roda na porta **3000**
- Qualquer requisição para `/api/*` é automaticamente encaminhada para `http://localhost:8081/api/*`
- Resolve problemas de CORS

### 2. Cliente Axios (src/services/api.js)

```javascript
const api = axios.create({
  baseURL: '/api',  // Todas as requisições começam com /api
  headers: {
    'Content-Type': 'application/json',
  },
})
```

**Interceptors configurados:**
- ✅ Adiciona token de autenticação automaticamente
- ✅ Trata erros 401 (não autorizado)
- ✅ Redireciona para login se sessão expirar

### 3. Serviços de API

#### ocorrenciaService.js
```javascript
listarTodas()     → GET  /api/ocorrencias
buscarPorId(id)   → GET  /api/ocorrencias/{id}
criar(dados)      → POST /api/ocorrencias
atualizar(id)     → PUT  /api/ocorrencias/{id}
atualizarStatus() → PATCH /api/ocorrencias/{id}/status
```

#### usuarioService.js
```javascript
login(email)      → GET  /api/usuarios/email/{email}
listarTodos()     → GET  /api/usuarios
buscarPorId(id)   → GET  /api/usuarios/{id}
criar(dados)      → POST /api/usuarios
```

## 🧪 Como Testar a Conexão

### Teste Manual

1. **Inicie o backend:**
```bash
./start-docker.sh
```

2. **Teste a API diretamente:**
```bash
curl http://localhost:8081/api/usuarios
```

3. **Inicie o frontend:**
```bash
./start-frontend.sh
```

4. **Acesse no navegador:**
```
http://localhost:3000
```

5. **Faça login:**
- Email: `admin@conectapg.com`
- Senha: `password`

6. **Verifique no console do navegador (F12):**
- Aba Network → Veja as requisições para `/api/*`
- Devem retornar status 200

### Teste Automático

Execute o script de teste:
```bash
./testar-conexao.sh
```

## 🔍 Fluxo de uma Requisição

### Exemplo: Listar Ocorrências

1. **Usuário clica em "Ocorrências" no frontend**

2. **React chama o serviço:**
```javascript
const ocorrencias = await ocorrenciaService.listarTodas()
```

3. **Serviço faz requisição Axios:**
```javascript
const response = await api.get('/ocorrencias')
```

4. **Axios monta a URL:**
```
baseURL (/api) + endpoint (/ocorrencias) = /api/ocorrencias
```

5. **Vite Proxy intercepta:**
```
/api/ocorrencias → http://localhost:8081/api/ocorrencias
```

6. **Spring Boot recebe:**
```
GET http://localhost:8081/api/ocorrencias
```

7. **Controller processa:**
```java
@GetMapping("/ocorrencias")
public ResponseEntity<List<OcorrenciaResponse>> listar()
```

8. **Retorna JSON:**
```json
[
  {
    "id": 1,
    "titulo": "Poste queimado",
    "status": "ABERTA",
    ...
  }
]
```

9. **Frontend recebe e renderiza:**
```jsx
{ocorrencias.map(ocorrencia => (
  <CardOcorrencia key={ocorrencia.id} ocorrencia={ocorrencia} />
))}
```

## ✅ Checklist de Conexão

- [x] Backend rodando na porta 8081
- [x] Frontend configurado para porta 3000
- [x] Proxy Vite configurado para `/api`
- [x] Cliente Axios com baseURL `/api`
- [x] Interceptors para autenticação
- [x] Serviços de API implementados
- [x] CORS configurado no backend
- [x] Context path `/api` no Spring Boot

## 🚨 Possíveis Problemas

### 1. Erro de CORS
**Sintoma:** `Access-Control-Allow-Origin` error

**Solução:** Já está configurado no backend!
```java
// SecurityConfig.java
.cors(cors -> cors.configurationSource(request -> {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(Arrays.asList(
        "http://localhost:3000",  // ✅ Frontend permitido
        "http://localhost:5173"
    ));
}))
```

### 2. Backend não responde
**Sintoma:** `ERR_CONNECTION_REFUSED`

**Solução:**
```bash
# Verificar se backend está rodando
docker ps

# Se não estiver, iniciar
./start-docker.sh
```

### 3. Proxy não funciona
**Sintoma:** Requisições vão para porta errada

**Solução:** Reiniciar o Vite
```bash
# Ctrl+C para parar
npm run dev
```

## 📝 Endpoints Mapeados

| Frontend | Proxy | Backend |
|----------|-------|---------|
| `/api/usuarios` | → | `http://localhost:8081/api/usuarios` |
| `/api/ocorrencias` | → | `http://localhost:8081/api/ocorrencias` |
| `/api/usuarios/email/admin@conectapg.com` | → | `http://localhost:8081/api/usuarios/email/admin@conectapg.com` |

## 🎯 Resumo

**SIM, o frontend está 100% conectado ao backend!**

✅ Proxy configurado
✅ Cliente HTTP pronto
✅ Serviços implementados
✅ CORS resolvido
✅ Autenticação preparada
✅ Tratamento de erros

**Para usar:**
1. Inicie o backend: `./start-docker.sh`
2. Inicie o frontend: `./start-frontend.sh`
3. Acesse: http://localhost:3000
4. Faça login e use normalmente!

---

**A conexão está perfeita! 🎉**
