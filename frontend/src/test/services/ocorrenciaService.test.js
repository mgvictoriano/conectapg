import { describe, it, expect, beforeEach, vi } from 'vitest'
import { ocorrenciaService } from '../../services/ocorrenciaService'
import api from '../../services/api'
import { mockOcorrencia, mockOcorrencias } from '../utils/test-utils'

// Mock do módulo api
vi.mock('../../services/api')

describe('ocorrenciaService', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('listarTodas', () => {
    it('deve listar todas as ocorrências sem filtros', async () => {
      // Dado
      api.get.mockResolvedValue({ data: mockOcorrencias })

      // Quando
      const resultado = await ocorrenciaService.listarTodas()

      // Então
      expect(api.get).toHaveBeenCalledWith('/ocorrencias?')
      expect(resultado).toEqual(mockOcorrencias)
      expect(resultado).toHaveLength(2)
    })

    it('deve listar ocorrências com filtro de status', async () => {
      // Dado
      const filtros = { status: 'ABERTA' }
      api.get.mockResolvedValue({ data: [mockOcorrencia] })

      // Quando
      const resultado = await ocorrenciaService.listarTodas(filtros)

      // Então
      expect(api.get).toHaveBeenCalledWith('/ocorrencias?status=ABERTA')
      expect(resultado).toHaveLength(1)
    })

    it('deve listar ocorrências com filtro de tipo', async () => {
      // Dado
      const filtros = { tipo: 'ILUMINACAO' }
      api.get.mockResolvedValue({ data: [mockOcorrencia] })

      // Quando
      const resultado = await ocorrenciaService.listarTodas(filtros)

      // Então
      expect(api.get).toHaveBeenCalledWith('/ocorrencias?tipo=ILUMINACAO')
      expect(resultado).toHaveLength(1)
    })

    it('deve lançar erro quando a requisição falhar', async () => {
      // Dado
      const mensagemErro = 'Erro ao listar ocorrências'
      api.get.mockRejectedValue({
        response: { data: { message: mensagemErro } }
      })

      // Quando/Então
      await expect(ocorrenciaService.listarTodas()).rejects.toThrow(mensagemErro)
    })
  })

  describe('buscarPorId', () => {
    it('deve buscar ocorrência por ID', async () => {
      // Dado
      const id = 1
      api.get.mockResolvedValue({ data: mockOcorrencia })

      // Quando
      const resultado = await ocorrenciaService.buscarPorId(id)

      // Então
      expect(api.get).toHaveBeenCalledWith('/ocorrencias/1')
      expect(resultado).toEqual(mockOcorrencia)
      expect(resultado.id).toBe(1)
    })

    it('deve lançar erro quando ocorrência não for encontrada', async () => {
      // Dado
      const id = 999
      api.get.mockRejectedValue({
        response: { data: { message: 'Ocorrência não encontrada' } }
      })

      // Quando/Então
      await expect(ocorrenciaService.buscarPorId(id)).rejects.toThrow('Ocorrência não encontrada')
    })
  })

  describe('criar', () => {
    it('deve criar uma nova ocorrência', async () => {
      // Dado
      const novaOcorrencia = {
        titulo: 'Nova Ocorrência',
        descricao: 'Descrição da nova ocorrência',
        localizacao: 'Local teste',
        tipo: 'LIXO',
        usuarioId: 1,
      }
      api.post.mockResolvedValue({ data: { ...mockOcorrencia, ...novaOcorrencia } })

      // Quando
      const resultado = await ocorrenciaService.criar(novaOcorrencia)

      // Então
      expect(api.post).toHaveBeenCalledWith('/ocorrencias', novaOcorrencia)
      expect(resultado.titulo).toBe('Nova Ocorrência')
    })

    it('deve lançar erro quando dados forem inválidos', async () => {
      // Dado
      const dadosInvalidos = { titulo: '' }
      api.post.mockRejectedValue({
        response: { data: { message: 'Dados inválidos' } }
      })

      // Quando/Então
      await expect(ocorrenciaService.criar(dadosInvalidos)).rejects.toThrow('Dados inválidos')
    })
  })

  describe('atualizar', () => {
    it('deve atualizar uma ocorrência existente', async () => {
      // Dado
      const id = 1
      const dadosAtualizados = { titulo: 'Título Atualizado' }
      api.put.mockResolvedValue({ data: { ...mockOcorrencia, ...dadosAtualizados } })

      // Quando
      const resultado = await ocorrenciaService.atualizar(id, dadosAtualizados)

      // Então
      expect(api.put).toHaveBeenCalledWith('/ocorrencias/1', dadosAtualizados)
      expect(resultado.titulo).toBe('Título Atualizado')
    })
  })

  describe('atualizarStatus', () => {
    it('deve atualizar o status de uma ocorrência', async () => {
      // Dado
      const id = 1
      const novoStatus = 'EM_ANDAMENTO'
      api.patch.mockResolvedValue({ 
        data: { ...mockOcorrencia, status: novoStatus } 
      })

      // Quando
      const resultado = await ocorrenciaService.atualizarStatus(id, novoStatus)

      // Então
      expect(api.patch).toHaveBeenCalledWith(
        '/ocorrencias/1/status',
        null,
        { params: { status: novoStatus } }
      )
      expect(resultado.status).toBe('EM_ANDAMENTO')
    })

    it('deve lançar erro ao atualizar status com valor inválido', async () => {
      // Dado
      const id = 1
      const statusInvalido = 'INVALIDO'
      api.patch.mockRejectedValue({
        response: { data: { message: 'Status inválido' } }
      })

      // Quando/Então
      await expect(
        ocorrenciaService.atualizarStatus(id, statusInvalido)
      ).rejects.toThrow('Status inválido')
    })
  })

  describe('deletar', () => {
    it('deve deletar uma ocorrência', async () => {
      // Dado
      const id = 1
      api.delete.mockResolvedValue({})

      // Quando
      await ocorrenciaService.deletar(id)

      // Então
      expect(api.delete).toHaveBeenCalledWith('/ocorrencias/1')
    })

    it('deve lançar erro ao deletar ocorrência inexistente', async () => {
      // Dado
      const id = 999
      api.delete.mockRejectedValue({
        response: { data: { message: 'Ocorrência não encontrada' } }
      })

      // Quando/Então
      await expect(ocorrenciaService.deletar(id)).rejects.toThrow('Ocorrência não encontrada')
    })
  })

  describe('obterEstatisticas', () => {
    it('deve calcular estatísticas corretamente', async () => {
      // Dado
      const ocorrencias = [
        { ...mockOcorrencia, status: 'ABERTA' },
        { ...mockOcorrencia, id: 2, status: 'ABERTA' },
        { ...mockOcorrencia, id: 3, status: 'EM_ANDAMENTO' },
        { ...mockOcorrencia, id: 4, status: 'RESOLVIDA' },
        { ...mockOcorrencia, id: 5, status: 'FECHADA' },
      ]
      api.get.mockResolvedValue({ data: ocorrencias })

      // Quando
      const resultado = await ocorrenciaService.obterEstatisticas()

      // Então
      expect(resultado.total).toBe(5)
      expect(resultado.abertas).toBe(2)
      expect(resultado.emAndamento).toBe(1)
      expect(resultado.resolvidas).toBe(1)
      expect(resultado.fechadas).toBe(1)
    })

    it('deve retornar estatísticas zeradas quando não houver ocorrências', async () => {
      // Dado
      api.get.mockResolvedValue({ data: [] })

      // Quando
      const resultado = await ocorrenciaService.obterEstatisticas()

      // Então
      expect(resultado.total).toBe(0)
      expect(resultado.abertas).toBe(0)
      expect(resultado.emAndamento).toBe(0)
      expect(resultado.resolvidas).toBe(0)
      expect(resultado.fechadas).toBe(0)
    })
  })
})
