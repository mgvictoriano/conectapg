import { describe, it, expect, beforeEach, vi } from 'vitest'
import { screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { renderWithRouter, mockUsuario } from '../utils/test-utils'
import Login from '../../pages/Login'
import { usuarioService } from '../../services/usuarioService'

// Mock dos serviços
vi.mock('../../services/usuarioService')
vi.mock('../../context/authStore', () => ({
  useAuthStore: () => ({
    login: vi.fn(),
    isAuthenticated: false,
  }),
}))

// Mock do useNavigate
const mockNavigate = vi.fn()
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom')
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  }
})

describe('Login', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('deve renderizar o formulário de login', () => {
    // Dado/Quando
    renderWithRouter(<Login />)

    // Então
    expect(screen.getByText('ConectaPG')).toBeInTheDocument()
    expect(screen.getByText('Entrar no Sistema')).toBeInTheDocument()
    expect(screen.getByLabelText(/e-mail/i)).toBeInTheDocument()
    expect(screen.getByLabelText(/senha/i)).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /entrar/i })).toBeInTheDocument()
  })

  it('deve exibir informações de usuários de teste', () => {
    // Dado/Quando
    renderWithRouter(<Login />)

    // Então
    expect(screen.getByText(/admin@conectapg.com/i)).toBeInTheDocument()
    expect(screen.getByText(/joao@example.com/i)).toBeInTheDocument()
  })

  it('deve validar campo de email obrigatório', async () => {
    // Dado
    const user = userEvent.setup()
    renderWithRouter(<Login />)

    // Quando
    const submitButton = screen.getByRole('button', { name: /entrar/i })
    await user.click(submitButton)

    // Então
    await waitFor(() => {
      expect(screen.getByText(/e-mail é obrigatório/i)).toBeInTheDocument()
    })
  })

  it('deve validar formato de email', async () => {
    // Dado
    const user = userEvent.setup()
    renderWithRouter(<Login />)

    // Quando
    const emailInput = screen.getByLabelText(/e-mail/i)
    await user.type(emailInput, 'email-invalido')
    
    const submitButton = screen.getByRole('button', { name: /entrar/i })
    await user.click(submitButton)

    // Então
    await waitFor(() => {
      expect(screen.getByText(/e-mail inválido/i)).toBeInTheDocument()
    })
  })

  it('deve validar campo de senha obrigatório', async () => {
    // Dado
    const user = userEvent.setup()
    renderWithRouter(<Login />)

    // Quando
    const emailInput = screen.getByLabelText(/e-mail/i)
    await user.type(emailInput, 'teste@example.com')
    
    const submitButton = screen.getByRole('button', { name: /entrar/i })
    await user.click(submitButton)

    // Então
    await waitFor(() => {
      expect(screen.getByText(/senha é obrigatória/i)).toBeInTheDocument()
    })
  })

  it('deve validar tamanho mínimo da senha', async () => {
    // Dado
    const user = userEvent.setup()
    renderWithRouter(<Login />)

    // Quando
    const emailInput = screen.getByLabelText(/e-mail/i)
    const senhaInput = screen.getByLabelText(/senha/i)
    
    await user.type(emailInput, 'teste@example.com')
    await user.type(senhaInput, '123')
    
    const submitButton = screen.getByRole('button', { name: /entrar/i })
    await user.click(submitButton)

    // Então
    await waitFor(() => {
      expect(screen.getByText(/senha deve ter no mínimo 6 caracteres/i)).toBeInTheDocument()
    })
  })

  it('deve fazer login com credenciais válidas', async () => {
    // Dado
    const user = userEvent.setup()
    usuarioService.login.mockResolvedValue({
      user: mockUsuario,
      token: 'fake-token',
    })
    renderWithRouter(<Login />)

    // Quando
    const emailInput = screen.getByLabelText(/e-mail/i)
    const senhaInput = screen.getByLabelText(/senha/i)
    
    await user.type(emailInput, 'teste@example.com')
    await user.type(senhaInput, 'senha123')
    
    const submitButton = screen.getByRole('button', { name: /entrar/i })
    await user.click(submitButton)

    // Então
    await waitFor(() => {
      expect(usuarioService.login).toHaveBeenCalledWith('teste@example.com', 'senha123')
    })
  })

  it('deve exibir erro quando login falhar', async () => {
    // Dado
    const user = userEvent.setup()
    const mensagemErro = 'Credenciais inválidas'
    usuarioService.login.mockRejectedValue(new Error(mensagemErro))
    renderWithRouter(<Login />)

    // Quando
    const emailInput = screen.getByLabelText(/e-mail/i)
    const senhaInput = screen.getByLabelText(/senha/i)
    
    await user.type(emailInput, 'teste@example.com')
    await user.type(senhaInput, 'senhaerrada')
    
    const submitButton = screen.getByRole('button', { name: /entrar/i })
    await user.click(submitButton)

    // Então
    await waitFor(() => {
      expect(screen.getByText(mensagemErro)).toBeInTheDocument()
    })
  })

  it('deve desabilitar botão durante o login', async () => {
    // Dado
    const user = userEvent.setup()
    usuarioService.login.mockImplementation(() => new Promise(resolve => setTimeout(resolve, 100)))
    renderWithRouter(<Login />)

    // Quando
    const emailInput = screen.getByLabelText(/e-mail/i)
    const senhaInput = screen.getByLabelText(/senha/i)
    const submitButton = screen.getByRole('button', { name: /entrar/i })
    
    await user.type(emailInput, 'teste@example.com')
    await user.type(senhaInput, 'senha123')
    await user.click(submitButton)

    // Então
    expect(submitButton).toBeDisabled()
    expect(screen.getByText(/entrando.../i)).toBeInTheDocument()
  })

  it('deve limpar mensagem de erro ao tentar novamente', async () => {
    // Dado
    const user = userEvent.setup()
    usuarioService.login.mockRejectedValueOnce(new Error('Erro'))
      .mockResolvedValueOnce({ user: mockUsuario, token: 'token' })
    renderWithRouter(<Login />)

    const emailInput = screen.getByLabelText(/e-mail/i)
    const senhaInput = screen.getByLabelText(/senha/i)
    const submitButton = screen.getByRole('button', { name: /entrar/i })

    // Quando - Primeira tentativa (erro)
    await user.type(emailInput, 'teste@example.com')
    await user.type(senhaInput, 'senha123')
    await user.click(submitButton)

    await waitFor(() => {
      expect(screen.getByText('Erro')).toBeInTheDocument()
    })

    // Quando - Segunda tentativa (sucesso)
    await user.clear(senhaInput)
    await user.type(senhaInput, 'senhacorreta')
    await user.click(submitButton)

    // Então
    await waitFor(() => {
      expect(screen.queryByText('Erro')).not.toBeInTheDocument()
    })
  })
})
