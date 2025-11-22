import { render } from '@testing-library/react'
import { BrowserRouter } from 'react-router-dom'

// Wrapper customizado para testes com Router
export function renderWithRouter(ui, options = {}) {
  return render(ui, {
    wrapper: ({ children }) => <BrowserRouter>{children}</BrowserRouter>,
    ...options,
  })
}

// Mock de dados para testes
export const mockUsuario = {
  id: 1,
  nome: 'Usuário Teste',
  email: 'teste@example.com',
  tipo: 'CIDADAO',
  ativo: true,
  dataCriacao: '2024-01-01T10:00:00',
  totalOcorrencias: 2,
}

export const mockUsuarioAdmin = {
  id: 2,
  nome: 'Admin Teste',
  email: 'admin@example.com',
  tipo: 'ADMIN',
  ativo: true,
  dataCriacao: '2024-01-01T10:00:00',
  totalOcorrencias: 0,
}

export const mockOcorrencia = {
  id: 1,
  titulo: 'Poste queimado',
  descricao: 'Poste da rua está sem iluminação',
  localizacao: 'Rua das Flores, 123',
  tipo: 'ILUMINACAO',
  status: 'ABERTA',
  usuario: {
    id: 1,
    nome: 'Usuário Teste',
    email: 'teste@example.com',
  },
  dataCriacao: '2024-01-01T10:00:00',
  dataAtualizacao: '2024-01-01T10:00:00',
}

export const mockOcorrencias = [
  mockOcorrencia,
  {
    id: 2,
    titulo: 'Buraco na via',
    descricao: 'Grande buraco na avenida principal',
    localizacao: 'Avenida Principal, 456',
    tipo: 'BURACO',
    status: 'EM_ANDAMENTO',
    usuario: {
      id: 1,
      nome: 'Usuário Teste',
      email: 'teste@example.com',
    },
    dataCriacao: '2024-01-02T14:30:00',
    dataAtualizacao: '2024-01-03T09:15:00',
  },
]

// Helper para esperar por async
export const waitFor = (callback, options) => {
  return new Promise((resolve) => {
    const interval = setInterval(() => {
      try {
        callback()
        clearInterval(interval)
        resolve()
      } catch (error) {
        // Continua tentando
      }
    }, options?.interval || 50)

    setTimeout(() => {
      clearInterval(interval)
      resolve()
    }, options?.timeout || 3000)
  })
}
