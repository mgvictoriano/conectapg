# ConectaPG - Frontend

Interface web do sistema ConectaPG para gerenciamento de ocorrências urbanas.

## 🚀 Tecnologias

- **React 18** - Biblioteca UI
- **Vite** - Build tool rápido
- **React Router DOM** - Navegação SPA
- **Axios** - Cliente HTTP
- **React Hook Form** - Gerenciamento de formulários
- **Zustand** - Gerenciamento de estado
- **TailwindCSS** - Estilização
- **React Icons** - Ícones
- **date-fns** - Manipulação de datas

## 📋 Pré-requisitos

- Node.js 18+ 
- npm ou yarn

## 🔧 Instalação

```bash
# Instalar dependências
npm install

# ou
yarn install
```

## 🏃 Executar

```bash
# Modo desenvolvimento
npm run dev

# Build para produção
npm run build

# Preview da build
npm run preview
```

A aplicação estará disponível em: **http://localhost:3000**

## 📁 Estrutura do Projeto

```
frontend/
├── src/
│   ├── components/       # Componentes reutilizáveis
│   │   ├── Navbar.jsx
│   │   ├── Footer.jsx
│   │   └── CardOcorrencia.jsx
│   ├── pages/           # Páginas da aplicação
│   │   ├── Login.jsx
│   │   ├── CriarOcorrencia.jsx
│   │   ├── ListaOcorrencias.jsx
│   │   ├── DetalheOcorrencia.jsx
│   │   └── PainelPrefeitura.jsx
│   ├── services/        # Serviços de API
│   │   ├── api.js
│   │   ├── ocorrenciaService.js
│   │   └── usuarioService.js
│   ├── context/         # Gerenciamento de estado
│   │   └── authStore.js
│   ├── styles/          # Estilos globais
│   │   └── index.css
│   ├── App.jsx          # Componente principal
│   └── main.jsx         # Entry point
├── public/              # Arquivos estáticos
├── index.html           # HTML base
├── vite.config.js       # Configuração Vite
├── tailwind.config.js   # Configuração Tailwind
└── package.json         # Dependências
```

## 🎨 Funcionalidades

### Tela de Login
- Autenticação de usuários
- Validação de formulário
- Usuários de teste disponíveis

### Lista de Ocorrências
- Visualização de todas as ocorrências
- Filtros por status e tipo
- Cards informativos

### Criar Ocorrência
- Formulário completo
- Validações em tempo real
- Feedback visual

### Detalhe da Ocorrência
- Informações completas
- Atualização de status (admin)
- Timeline de eventos

### Painel da Prefeitura
- Dashboard com estatísticas
- Gráficos de distribuição
- Ocorrências recentes
- Acesso restrito a admins

## 🔐 Usuários de Teste

```
Admin:
- Email: admin@conectapg.com
- Senha: password

Cidadão:
- Email: joao@example.com
- Senha: password
```

## 🌐 Integração com Backend

O frontend está configurado para se comunicar com o backend Spring Boot através de proxy:

```javascript
// vite.config.js
proxy: {
  '/api': {
    target: 'http://localhost:8081',
    changeOrigin: true,
  }
}
```

Certifique-se de que o backend está rodando em `http://localhost:8081`

## 🎯 Rotas

- `/login` - Tela de login
- `/` - Lista de ocorrências (protegida)
- `/ocorrencias` - Lista de ocorrências (protegida)
- `/ocorrencias/nova` - Criar ocorrência (protegida)
- `/ocorrencias/:id` - Detalhe da ocorrência (protegida)
- `/painel` - Painel da prefeitura (admin apenas)

## 🔒 Autenticação

A autenticação é gerenciada pelo Zustand e persiste no localStorage:

```javascript
// Fazer login
const { login } = useAuthStore()
login(userData, token)

// Fazer logout
const { logout } = useAuthStore()
logout()

// Verificar autenticação
const { isAuthenticated, user } = useAuthStore()
```

## 🎨 Customização

### Cores

As cores podem ser customizadas no `tailwind.config.js`:

```javascript
colors: {
  primary: {
    // Suas cores aqui
  }
}
```

### Estilos Globais

Classes utilitárias estão definidas em `src/styles/index.css`:

- `.btn-primary` - Botão primário
- `.btn-secondary` - Botão secundário
- `.input-field` - Campo de input
- `.card` - Card padrão

## 📦 Build para Produção

```bash
npm run build
```

Os arquivos otimizados estarão em `dist/`

## 🐛 Troubleshooting

### Erro de CORS
Certifique-se de que o backend está configurado para aceitar requisições do frontend.

### Proxy não funciona
Verifique se o backend está rodando na porta correta (8081).

### Erro ao instalar dependências
Tente limpar o cache:
```bash
rm -rf node_modules package-lock.json
npm install
```

## 📝 Próximas Melhorias

- [ ] Upload de imagens
- [ ] Notificações em tempo real
- [ ] Mapa interativo
- [ ] Timeline de eventos
- [ ] Modo escuro
- [ ] PWA (Progressive Web App)
- [ ] Testes unitários
- [ ] Testes E2E

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

---

**Desenvolvido com ❤️ para a Prefeitura de Praia Grande**
