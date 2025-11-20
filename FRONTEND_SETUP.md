# 🎨 Setup do Frontend - ConectaPG

## 📋 Pré-requisitos

Para rodar o frontend, você precisa ter o **Node.js** instalado.

### Instalar Node.js no Ubuntu/Debian

```bash
# Atualizar repositórios
sudo apt update

# Instalar Node.js 18.x (LTS)
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt-get install -y nodejs

# Verificar instalação
node --version
npm --version
```

### Alternativa: Usar NVM (Node Version Manager)

```bash
# Instalar NVM
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash

# Recarregar o terminal
source ~/.bashrc

# Instalar Node.js
nvm install 18
nvm use 18

# Verificar
node --version
```

## 🚀 Iniciar o Frontend

### 1. Instalar Dependências

```bash
cd frontend
npm install
```

Isso vai instalar todas as dependências do `package.json`.

### 2. Iniciar em Modo Desenvolvimento

```bash
npm run dev
```

O frontend estará disponível em: **http://localhost:3000**

### 3. Acessar a Aplicação

Abra seu navegador e acesse:
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8081/api
- **Swagger**: http://localhost:8081/api/swagger-ui.html

## 🔄 Workflow Completo

### Iniciar Backend + Frontend

**Terminal 1 - Backend:**
```bash
cd /home/michellevictoriano/Documentos/conectapg
./start-docker.sh
```

**Terminal 2 - Frontend:**
```bash
cd /home/michellevictoriano/Documentos/conectapg/frontend
npm run dev
```

## 📦 Build para Produção

```bash
cd frontend
npm run build
```

Os arquivos otimizados estarão em `frontend/dist/`

Para testar a build:
```bash
npm run preview
```

## 🎯 Estrutura Criada

```
frontend/
├── src/
│   ├── components/
│   │   ├── Navbar.jsx           ✅ Barra de navegação
│   │   ├── Footer.jsx           ✅ Rodapé
│   │   └── CardOcorrencia.jsx   ✅ Card de ocorrência
│   ├── pages/
│   │   ├── Login.jsx            ✅ Tela de login
│   │   ├── CriarOcorrencia.jsx  ✅ Criar nova ocorrência
│   │   ├── ListaOcorrencias.jsx ✅ Lista com filtros
│   │   ├── DetalheOcorrencia.jsx ✅ Detalhes completos
│   │   └── PainelPrefeitura.jsx ✅ Dashboard admin
│   ├── services/
│   │   ├── api.js               ✅ Cliente Axios
│   │   ├── ocorrenciaService.js ✅ API de ocorrências
│   │   └── usuarioService.js    ✅ API de usuários
│   ├── context/
│   │   └── authStore.js         ✅ Estado de autenticação
│   ├── styles/
│   │   └── index.css            ✅ Estilos globais
│   ├── App.jsx                  ✅ Rotas e layout
│   └── main.jsx                 ✅ Entry point
├── public/                      ✅ Arquivos estáticos
├── index.html                   ✅ HTML base
├── vite.config.js              ✅ Config Vite + Proxy
├── tailwind.config.js          ✅ Config Tailwind
├── postcss.config.js           ✅ Config PostCSS
├── package.json                ✅ Dependências
└── README.md                   ✅ Documentação
```

## ✨ Funcionalidades Implementadas

### 🔐 Autenticação
- [x] Tela de login com validação
- [x] Persistência de sessão (localStorage)
- [x] Rotas protegidas
- [x] Logout

### 📋 Ocorrências
- [x] Listar todas as ocorrências
- [x] Filtros por status e tipo
- [x] Criar nova ocorrência
- [x] Ver detalhes completos
- [x] Atualizar status (admin)

### 📊 Dashboard
- [x] Estatísticas gerais
- [x] Gráficos de distribuição
- [x] Ocorrências recentes
- [x] Acesso restrito a admins

### 🎨 UI/UX
- [x] Design moderno com TailwindCSS
- [x] Responsivo (mobile-first)
- [x] Feedback visual (loading, erros, sucesso)
- [x] Navegação intuitiva
- [x] Ícones com React Icons

## 🔧 Configurações Importantes

### Proxy para API

O Vite está configurado para fazer proxy das requisições `/api` para o backend:

```javascript
// vite.config.js
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

### TailwindCSS

Classes utilitárias personalizadas:

```css
.btn-primary      /* Botão primário azul */
.btn-secondary    /* Botão secundário cinza */
.input-field      /* Campo de input estilizado */
.card             /* Card com sombra */
```

### Gerenciamento de Estado

Usando Zustand para autenticação:

```javascript
import { useAuthStore } from './context/authStore'

const { user, isAuthenticated, login, logout } = useAuthStore()
```

## 🧪 Testando

### Usuários de Teste

```
Admin/Gestor:
- Email: admin@conectapg.com
- Senha: password (qualquer senha funciona no mock)

Cidadão:
- Email: joao@example.com
- Senha: password
```

### Fluxo de Teste

1. **Login** → Use admin@conectapg.com
2. **Ver Ocorrências** → Lista com 2 ocorrências de exemplo
3. **Criar Ocorrência** → Preencha o formulário
4. **Ver Detalhes** → Clique em uma ocorrência
5. **Atualizar Status** → (Admin) Mude o status
6. **Painel** → Veja estatísticas e gráficos

## 🐛 Troubleshooting

### Porta 3000 em uso

```bash
# Mude a porta no vite.config.js
server: {
  port: 3001, // ou outra porta
}
```

### Erro ao instalar dependências

```bash
# Limpe o cache
rm -rf node_modules package-lock.json
npm cache clean --force
npm install
```

### Erro de conexão com API

1. Verifique se o backend está rodando: `docker ps`
2. Teste a API: `curl http://localhost:8081/api/usuarios`
3. Verifique o proxy no `vite.config.js`

### Hot reload não funciona

```bash
# Reinicie o servidor dev
npm run dev
```

## 📝 Próximos Passos

### Melhorias Sugeridas

1. **Upload de Imagens**
   - Adicionar campo de upload no formulário
   - Integrar com backend para armazenamento

2. **Mapa Interativo**
   - Usar Leaflet ou Google Maps
   - Marcar localização das ocorrências

3. **Notificações**
   - WebSockets para atualizações em tempo real
   - Notificações push

4. **PWA**
   - Service Worker
   - Instalável no mobile

5. **Testes**
   - Jest + React Testing Library
   - Cypress para E2E

## 🚀 Deploy

### Build para Produção

```bash
npm run build
```

### Servir com Nginx

```nginx
server {
    listen 80;
    server_name conectapg.com;
    
    root /var/www/conectapg/dist;
    index index.html;
    
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    location /api {
        proxy_pass http://localhost:8081;
    }
}
```

### Deploy no Vercel/Netlify

1. Conecte o repositório
2. Configure build command: `npm run build`
3. Configure output directory: `dist`
4. Configure variáveis de ambiente se necessário

---

**Frontend pronto para desenvolvimento! 🎉**
