import { describe, it, expect } from 'vitest'
import { screen } from '@testing-library/react'
import { renderWithRouter, mockOcorrencia } from '../utils/test-utils'
import CardOcorrencia from '../../components/CardOcorrencia'

describe('CardOcorrencia', () => {
  // Dado-Quando-Então: Dado uma ocorrência, Quando renderizar o card, Então deve exibir as informações
  it('deve renderizar as informações da ocorrência corretamente', () => {
    // Dado
    const ocorrencia = mockOcorrencia

    // Quando
    renderWithRouter(<CardOcorrencia ocorrencia={ocorrencia} />)

    // Então
    expect(screen.getByText('Poste queimado')).toBeInTheDocument()
    expect(screen.getByText('Poste da rua está sem iluminação')).toBeInTheDocument()
    expect(screen.getByText('Rua das Flores, 123')).toBeInTheDocument()
  })

  it('deve exibir o status da ocorrência com a cor correta', () => {
    // Dado
    const ocorrencia = { ...mockOcorrencia, status: 'ABERTA' }

    // Quando
    renderWithRouter(<CardOcorrencia ocorrencia={ocorrencia} />)

    // Então
    const statusBadge = screen.getByText('ABERTA')
    expect(statusBadge).toBeInTheDocument()
    expect(statusBadge).toHaveClass('bg-yellow-100')
  })

  it('deve exibir o tipo da ocorrência formatado', () => {
    // Dado
    const ocorrencia = mockOcorrencia

    // Quando
    renderWithRouter(<CardOcorrencia ocorrencia={ocorrencia} />)

    // Então
    expect(screen.getByText('Iluminação')).toBeInTheDocument()
  })

  it('deve exibir o nome do usuário quando disponível', () => {
    // Dado
    const ocorrencia = mockOcorrencia

    // Quando
    renderWithRouter(<CardOcorrencia ocorrencia={ocorrencia} />)

    // Então
    expect(screen.getByText('Usuário Teste')).toBeInTheDocument()
  })

  it('deve renderizar como link clicável', () => {
    // Dado
    const ocorrencia = mockOcorrencia

    // Quando
    renderWithRouter(<CardOcorrencia ocorrencia={ocorrencia} />)

    // Então
    const link = screen.getByRole('link')
    expect(link).toHaveAttribute('href', '/ocorrencias/1')
  })

  it('deve aplicar estilo de hover no card', () => {
    // Dado
    const ocorrencia = mockOcorrencia

    // Quando
    renderWithRouter(<CardOcorrencia ocorrencia={ocorrencia} />)

    // Então
    const card = screen.getByRole('link')
    expect(card.querySelector('.card')).toHaveClass('hover:scale-[1.02]')
  })

  it('deve exibir status EM_ANDAMENTO com cor azul', () => {
    // Dado
    const ocorrencia = { ...mockOcorrencia, status: 'EM_ANDAMENTO' }

    // Quando
    renderWithRouter(<CardOcorrencia ocorrencia={ocorrencia} />)

    // Então
    const statusBadge = screen.getByText('EM ANDAMENTO')
    expect(statusBadge).toHaveClass('bg-blue-100')
  })

  it('deve exibir status RESOLVIDA com cor verde', () => {
    // Dado
    const ocorrencia = { ...mockOcorrencia, status: 'RESOLVIDA' }

    // Quando
    renderWithRouter(<CardOcorrencia ocorrencia={ocorrencia} />)

    // Então
    const statusBadge = screen.getByText('RESOLVIDA')
    expect(statusBadge).toHaveClass('bg-green-100')
  })

  it('deve limitar a descrição em 2 linhas', () => {
    // Dado
    const ocorrencia = {
      ...mockOcorrencia,
      descricao: 'Descrição muito longa que deveria ser truncada após duas linhas de texto para não ocupar muito espaço no card',
    }

    // Quando
    renderWithRouter(<CardOcorrencia ocorrencia={ocorrencia} />)

    // Então
    const descricao = screen.getByText(/Descrição muito longa/)
    expect(descricao).toHaveClass('line-clamp-2')
  })
})
