# 🧪 Testes Unitários - Frontend ConectaPG

## 📋 Visão Geral

Testes unitários implementados seguindo o padrão **Dado-Quando-Então** (Given-When-Then) do backend, garantindo consistência e qualidade em todo o projeto.

## 🛠️ Tecnologias

- **Vitest** - Framework de testes (compatível com Vite)
- **React Testing Library** - Testes de componentes React
- **@testing-library/user-event** - Simulação de interações do usuário
- **@testing-library/jest-dom** - Matchers customizados

## 📁 Estrutura

```
src/test/
├── setup.js                    # Configuração global dos testes
├── utils/
│   └── test-utils.jsx         # Utilitários e mocks compartilhados
├── components/
│   └── CardOcorrencia.test.jsx
├── pages/
│   ├── Login.test.jsx
│   └── CriarOcorrencia.test.jsx
└── services/
    ├── ocorrenciaService.test.js
    └── usuarioService.test.js
```

## 🎯 Padrão Dado-Quando-Então

Todos os testes seguem o padrão BDD (Behavior-Driven Development):

```javascript
it('deve fazer login com credenciais válidas', async () => {
  // Dado - Setup do teste
  const user = userEvent.setup()
  const email = 'teste@example.com'
  const senha = 'senha123'
  
  // Quando - Ação sendo testada
  await user.type(emailInput, email)
  await user.type(senhaInput, senha)
  await user.click(submitButton)
  
  // Então - Verificação do resultado
  expect(usuarioService.login).toHaveBeenCalledWith(email, senha)
})
```

## 🧪 Executar Testes

### Todos os testes
```bash
npm test
```

### Modo watch (reexecuta ao salvar)
```bash
npm test -- --watch
```

### Com interface gráfica
```bash
npm run test:ui
```

### Cobertura de código
```bash
npm run test:coverage
```

## 📊 Cobertura de Testes

### Componentes
- ✅ **CardOcorrencia** - 9 testes
  - Renderização de informações
  - Cores de status
  - Formatação de dados
  - Links e navegação

### Páginas
- ✅ **Login** - 11 testes
  - Renderização do formulário
  - Validações de campos
  - Fluxo de autenticação
  - Tratamento de erros
  
- ✅ **CriarOcorrencia** - 13 testes
  - Validações de formulário
  - Criação de ocorrência
  - Feedback de sucesso/erro
  - Navegação

### Serviços
- ✅ **ocorrenciaService** - 15 testes
  - CRUD completo
  - Filtros
  - Estatísticas
  - Tratamento de erros
  
- ✅ **usuarioService** - 11 testes
  - Autenticação
  - CRUD de usuários
  - Validações
  - Tratamento de erros

**Total: 59 testes implementados**

## 🎨 Exemplos de Testes

### Teste de Componente

```javascript
describe('CardOcorrencia', () => {
  it('deve renderizar as informações da ocorrência corretamente', () => {
    // Dado
    const ocorrencia = mockOcorrencia

    // Quando
    renderWithRouter(<CardOcorrencia ocorrencia={ocorrencia} />)

    // Então
    expect(screen.getByText('Poste queimado')).toBeInTheDocument()
    expect(screen.getByText(/Rua das Flores/)).toBeInTheDocument()
  })
})
```

### Teste de Serviço

```javascript
describe('ocorrenciaService', () => {
  it('deve listar todas as ocorrências sem filtros', async () => {
    // Dado
    api.get.mockResolvedValue({ data: mockOcorrencias })

    // Quando
    const resultado = await ocorrenciaService.listarTodas()

    // Então
    expect(api.get).toHaveBeenCalledWith('/ocorrencias?')
    expect(resultado).toHaveLength(2)
  })
})
```

### Teste de Página com Interação

```javascript
describe('Login', () => {
  it('deve fazer login com credenciais válidas', async () => {
    // Dado
    const user = userEvent.setup()
    usuarioService.login.mockResolvedValue({ user: mockUsuario, token: 'token' })
    renderWithRouter(<Login />)

    // Quando
    await user.type(screen.getByLabelText(/e-mail/i), 'teste@example.com')
    await user.type(screen.getByLabelText(/senha/i), 'senha123')
    await user.click(screen.getByRole('button', { name: /entrar/i }))

    // Então
    await waitFor(() => {
      expect(usuarioService.login).toHaveBeenCalled()
    })
  })
})
```

## 🔧 Utilitários de Teste

### renderWithRouter

Renderiza componentes que usam React Router:

```javascript
import { renderWithRouter } from '../utils/test-utils'

renderWithRouter(<MeuComponente />)
```

### Mocks Compartilhados

```javascript
import { 
  mockUsuario, 
  mockUsuarioAdmin, 
  mockOcorrencia, 
  mockOcorrencias 
} from '../utils/test-utils'
```

## 📝 Convenções

### Nomenclatura de Testes

- ✅ **deve** + ação + resultado esperado
- ✅ Exemplos:
  - `deve renderizar o formulário de login`
  - `deve validar campo de email obrigatório`
  - `deve criar ocorrência com dados válidos`
  - `deve exibir erro quando login falhar`

### Organização

1. **describe** - Agrupa testes relacionados
2. **it/test** - Teste individual
3. **beforeEach** - Setup antes de cada teste
4. **afterEach** - Cleanup após cada teste

### Estrutura de um Teste

```javascript
it('deve fazer algo específico', async () => {
  // Dado - Preparação
  const dados = preparaDados()
  const mock = configuraMock()
  
  // Quando - Ação
  const resultado = await executaAcao(dados)
  
  // Então - Verificação
  expect(resultado).toBe(esperado)
  expect(mock).toHaveBeenCalled()
})
```

## 🎯 Boas Práticas

### ✅ Fazer

- Testar comportamento, não implementação
- Um conceito por teste
- Nomes descritivos e claros
- Usar mocks para dependências externas
- Testar casos de sucesso e erro
- Seguir padrão Dado-Quando-Então

### ❌ Evitar

- Testes dependentes entre si
- Testar detalhes de implementação
- Múltiplas asserções não relacionadas
- Testes muito complexos
- Ignorar casos de erro

## 🚀 Próximos Passos

### Testes a Implementar

- [ ] ListaOcorrencias.test.jsx
- [ ] DetalheOcorrencia.test.jsx
- [ ] PainelPrefeitura.test.jsx
- [ ] Navbar.test.jsx
- [ ] Footer.test.jsx
- [ ] authStore.test.js
- [ ] Testes de integração E2E

### Melhorias

- [ ] Aumentar cobertura para 90%+
- [ ] Adicionar testes de acessibilidade
- [ ] Testes de performance
- [ ] Snapshot testing para componentes visuais
- [ ] Testes de responsividade

## 📚 Recursos

- [Vitest Documentation](https://vitest.dev/)
- [React Testing Library](https://testing-library.com/react)
- [Testing Best Practices](https://kentcdodds.com/blog/common-mistakes-with-react-testing-library)

## 🎉 Resultado

**Testes garantem:**
- ✅ Qualidade do código
- ✅ Confiança em refatorações
- ✅ Documentação viva
- ✅ Menos bugs em produção
- ✅ Desenvolvimento mais rápido

---

**Mantenha os testes atualizados e sempre escreva testes para novas funcionalidades!**
