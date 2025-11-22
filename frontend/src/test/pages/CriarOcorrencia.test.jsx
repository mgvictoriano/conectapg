import { describe, it, expect, beforeEach, vi } from 'vitest'
import { screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { renderWithRouter, mockOcorrencia, mockUsuario } from '../utils/test-utils'
import CriarOcorrencia from '../../pages/CriarOcorrencia'
import { ocorrenciaService } from '../../services/ocorrenciaService'

// Mock dos serviços
vi.mock('../../services/ocorrenciaService')
vi.mock('../../context/authStore', () => ({
  useAuthStore: () => ({
    user: mockUsuario,
  }),
}))

const mockNavigate = vi.fn()
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom')
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  }
})

describe('CriarOcorrencia', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('deve renderizar o formulário de criação', () => {
    // Dado/Quando
    renderWithRouter(<CriarOcorrencia />)

    // Então
    expect(screen.getByText('Nova Ocorrência')).toBeInTheDocument()
    expect(screen.getByLabelText(/título/i)).toBeInTheDocument()
    expect(screen.getByLabelText(/tipo de ocorrência/i)).toBeInTheDocument()
    expect(screen.getByLabelText(/descrição/i)).toBeInTheDocument()
    expect(screen.getByLabelText(/localização/i)).toBeInTheDocument()
  })

  it('deve exibir botões de ação', () => {
    // Dado/Quando
    renderWithRouter(<CriarOcorrencia />)

    // Então
    expect(screen.getByRole('button', { name: /cancelar/i })).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /criar ocorrência/i })).toBeInTheDocument()
  })

  it('deve validar campo título obrigatório', async () => {
    // Dado
    const user = userEvent.setup()
    renderWithRouter(<CriarOcorrencia />)

    // Quando
    const submitButton = screen.getByRole('button', { name: /criar ocorrência/i })
    await user.click(submitButton)

    // Então
    await waitFor(() => {
      expect(screen.getByText(/título é obrigatório/i)).toBeInTheDocument()
    })
  })

  it('deve validar tamanho mínimo do título', async () => {
    // Dado
    const user = userEvent.setup()
    renderWithRouter(<CriarOcorrencia />)

    // Quando
    const tituloInput = screen.getByLabelText(/título/i)
    await user.type(tituloInput, 'abc')
    
    const submitButton = screen.getByRole('button', { name: /criar ocorrência/i })
    await user.click(submitButton)

    // Então
    await waitFor(() => {
      expect(screen.getByText(/título deve ter no mínimo 5 caracteres/i)).toBeInTheDocument()
    })
  })

  it('deve validar campo tipo obrigatório', async () => {
    // Dado
    const user = userEvent.setup()
    renderWithRouter(<CriarOcorrencia />)

    // Quando
    const tituloInput = screen.getByLabelText(/título/i)
    await user.type(tituloInput, 'Título válido')
    
    const submitButton = screen.getByRole('button', { name: /criar ocorrência/i })
    await user.click(submitButton)

    // Então
    await waitFor(() => {
      expect(screen.getByText(/tipo é obrigatório/i)).toBeInTheDocument()
    })
  })

  it('deve validar campo descrição obrigatório', async () => {
    // Dado
    const user = userEvent.setup()
    renderWithRouter(<CriarOcorrencia />)

    // Quando
    const submitButton = screen.getByRole('button', { name: /criar ocorrência/i })
    await user.click(submitButton)

    // Então
    await waitFor(() => {
      expect(screen.getByText(/descrição é obrigatória/i)).toBeInTheDocument()
    })
  })

  it('deve validar tamanho mínimo da descrição', async () => {
    // Dado
    const user = userEvent.setup()
    renderWithRouter(<CriarOcorrencia />)

    // Quando
    const descricaoInput = screen.getByLabelText(/descrição/i)
    await user.type(descricaoInput, 'curta')
    
    const submitButton = screen.getByRole('button', { name: /criar ocorrência/i })
    await user.click(submitButton)

    // Então
    await waitFor(() => {
      expect(screen.getByText(/descrição deve ter no mínimo 10 caracteres/i)).toBeInTheDocument()
    })
  })

  it('deve validar campo localização obrigatório', async () => {
    // Dado
    const user = userEvent.setup()
    renderWithRouter(<CriarOcorrencia />)

    // Quando
    const submitButton = screen.getByRole('button', { name: /criar ocorrência/i })
    await user.click(submitButton)

    // Então
    await waitFor(() => {
      expect(screen.getByText(/localização é obrigatória/i)).toBeInTheDocument()
    })
  })

  it('deve criar ocorrência com dados válidos', async () => {
    // Dado
    const user = userEvent.setup()
    ocorrenciaService.criar.mockResolvedValue(mockOcorrencia)
    renderWithRouter(<CriarOcorrencia />)

    // Quando
    await user.type(screen.getByLabelText(/título/i), 'Poste queimado')
    await user.selectOptions(screen.getByLabelText(/tipo de ocorrência/i), 'ILUMINACAO')
    await user.type(screen.getByLabelText(/descrição/i), 'Descrição detalhada do problema')
    await user.type(screen.getByLabelText(/localização/i), 'Rua das Flores, 123')
    
    const submitButton = screen.getByRole('button', { name: /criar ocorrência/i })
    await user.click(submitButton)

    // Então
    await waitFor(() => {
      expect(ocorrenciaService.criar).toHaveBeenCalledWith(
        expect.objectContaining({
          titulo: 'Poste queimado',
          tipo: 'ILUMINACAO',
          descricao: 'Descrição detalhada do problema',
          localizacao: 'Rua das Flores, 123',
          usuarioId: mockUsuario.id,
        })
      )
    })
  })

  it('deve exibir mensagem de sucesso após criar', async () => {
    // Dado
    const user = userEvent.setup()
    ocorrenciaService.criar.mockResolvedValue(mockOcorrencia)
    renderWithRouter(<CriarOcorrencia />)

    // Quando
    await user.type(screen.getByLabelText(/título/i), 'Teste')
    await user.selectOptions(screen.getByLabelText(/tipo/i), 'LIXO')
    await user.type(screen.getByLabelText(/descrição/i), 'Descrição teste longa')
    await user.type(screen.getByLabelText(/localização/i), 'Local teste')
    
    await user.click(screen.getByRole('button', { name: /criar ocorrência/i }))

    // Então
    await waitFor(() => {
      expect(screen.getByText(/ocorrência criada com sucesso/i)).toBeInTheDocument()
    })
  })

  it('deve exibir erro quando criação falhar', async () => {
    // Dado
    const user = userEvent.setup()
    const mensagemErro = 'Erro ao criar ocorrência'
    ocorrenciaService.criar.mockRejectedValue(new Error(mensagemErro))
    renderWithRouter(<CriarOcorrencia />)

    // Quando
    await user.type(screen.getByLabelText(/título/i), 'Teste')
    await user.selectOptions(screen.getByLabelText(/tipo/i), 'LIXO')
    await user.type(screen.getByLabelText(/descrição/i), 'Descrição teste')
    await user.type(screen.getByLabelText(/localização/i), 'Local')
    
    await user.click(screen.getByRole('button', { name: /criar ocorrência/i }))

    // Então
    await waitFor(() => {
      expect(screen.getByText(mensagemErro)).toBeInTheDocument()
    })
  })

  it('deve desabilitar botão durante criação', async () => {
    // Dado
    const user = userEvent.setup()
    ocorrenciaService.criar.mockImplementation(() => new Promise(resolve => setTimeout(resolve, 100)))
    renderWithRouter(<CriarOcorrencia />)

    // Quando
    await user.type(screen.getByLabelText(/título/i), 'Teste')
    await user.selectOptions(screen.getByLabelText(/tipo/i), 'LIXO')
    await user.type(screen.getByLabelText(/descrição/i), 'Descrição teste')
    await user.type(screen.getByLabelText(/localização/i), 'Local')
    
    const submitButton = screen.getByRole('button', { name: /criar ocorrência/i })
    await user.click(submitButton)

    // Então
    expect(submitButton).toBeDisabled()
    expect(screen.getByText(/criando.../i)).toBeInTheDocument()
  })

  it('deve voltar para página anterior ao clicar em cancelar', async () => {
    // Dado
    const user = userEvent.setup()
    renderWithRouter(<CriarOcorrencia />)

    // Quando
    const cancelButton = screen.getByRole('button', { name: /cancelar/i })
    await user.click(cancelButton)

    // Então
    expect(mockNavigate).toHaveBeenCalledWith(-1)
  })

  it('deve exibir todos os tipos de ocorrência no select', () => {
    // Dado/Quando
    renderWithRouter(<CriarOcorrencia />)

    // Então
    const tipoSelect = screen.getByLabelText(/tipo de ocorrência/i)
    expect(tipoSelect).toContainHTML('Iluminação Pública')
    expect(tipoSelect).toContainHTML('Buraco na Via')
    expect(tipoSelect).toContainHTML('Lixo/Entulho')
    expect(tipoSelect).toContainHTML('Vandalismo')
    expect(tipoSelect).toContainHTML('Outros')
  })
})
