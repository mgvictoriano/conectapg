import { describe, it, expect, beforeEach, vi } from 'vitest'
import { usuarioService } from '../../services/usuarioService'
import api from '../../services/api'
import { mockUsuario, mockUsuarioAdmin } from '../utils/test-utils'

// Mock do módulo api
vi.mock('../../services/api')

describe('usuarioService', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('login', () => {
    it('deve fazer login com credenciais válidas', async () => {
      // Dado
      const email = 'teste@example.com'
      const senha = 'senha123'
      api.get.mockResolvedValue({ data: mockUsuario })

      // Quando
      const resultado = await usuarioService.login(email, senha)

      // Então
      expect(api.get).toHaveBeenCalledWith(`/usuarios/email/${email}`)
      expect(resultado.user).toEqual(mockUsuario)
      expect(resultado.token).toBe('fake-jwt-token')
    })

    it('deve lançar erro quando usuário não for encontrado', async () => {
      // Dado
      const email = 'inexistente@example.com'
      api.get.mockRejectedValue({
        response: { data: { message: 'Usuário não encontrado' } }
      })

      // Quando/Então
      await expect(usuarioService.login(email, 'senha')).rejects.toThrow('Usuário não encontrado')
    })

    it('deve lançar erro quando email for inválido', async () => {
      // Dado
      const email = 'email-invalido'
      api.get.mockRejectedValue({
        response: { status: 400 }
      })

      // Quando/Então
      await expect(usuarioService.login(email, 'senha')).rejects.toThrow()
    })
  })

  describe('listarTodos', () => {
    it('deve listar todos os usuários', async () => {
      // Dado
      const usuarios = [mockUsuario, mockUsuarioAdmin]
      api.get.mockResolvedValue({ data: usuarios })

      // Quando
      const resultado = await usuarioService.listarTodos()

      // Então
      expect(api.get).toHaveBeenCalledWith('/usuarios')
      expect(resultado).toEqual(usuarios)
      expect(resultado).toHaveLength(2)
    })

    it('deve retornar lista vazia quando não houver usuários', async () => {
      // Dado
      api.get.mockResolvedValue({ data: [] })

      // Quando
      const resultado = await usuarioService.listarTodos()

      // Então
      expect(resultado).toEqual([])
      expect(resultado).toHaveLength(0)
    })

    it('deve lançar erro quando a requisição falhar', async () => {
      // Dado
      api.get.mockRejectedValue({
        response: { data: { message: 'Erro ao listar usuários' } }
      })

      // Quando/Então
      await expect(usuarioService.listarTodos()).rejects.toThrow('Erro ao listar usuários')
    })
  })

  describe('buscarPorId', () => {
    it('deve buscar usuário por ID', async () => {
      // Dado
      const id = 1
      api.get.mockResolvedValue({ data: mockUsuario })

      // Quando
      const resultado = await usuarioService.buscarPorId(id)

      // Então
      expect(api.get).toHaveBeenCalledWith('/usuarios/1')
      expect(resultado).toEqual(mockUsuario)
      expect(resultado.id).toBe(1)
    })

    it('deve lançar erro quando usuário não for encontrado', async () => {
      // Dado
      const id = 999
      api.get.mockRejectedValue({
        response: { data: { message: 'Usuário não encontrado' } }
      })

      // Quando/Então
      await expect(usuarioService.buscarPorId(id)).rejects.toThrow('Usuário não encontrado')
    })
  })

  describe('criar', () => {
    it('deve criar um novo usuário', async () => {
      // Dado
      const novoUsuario = {
        nome: 'Novo Usuário',
        email: 'novo@example.com',
        senha: 'senha123',
        tipo: 'CIDADAO',
      }
      api.post.mockResolvedValue({ data: { ...mockUsuario, ...novoUsuario } })

      // Quando
      const resultado = await usuarioService.criar(novoUsuario)

      // Então
      expect(api.post).toHaveBeenCalledWith('/usuarios', novoUsuario)
      expect(resultado.nome).toBe('Novo Usuário')
      expect(resultado.email).toBe('novo@example.com')
    })

    it('deve lançar erro quando email já existir', async () => {
      // Dado
      const usuarioDuplicado = {
        nome: 'Teste',
        email: 'teste@example.com',
        senha: 'senha123',
        tipo: 'CIDADAO',
      }
      api.post.mockRejectedValue({
        response: { data: { message: 'Email já cadastrado' } }
      })

      // Quando/Então
      await expect(usuarioService.criar(usuarioDuplicado)).rejects.toThrow('Email já cadastrado')
    })

    it('deve lançar erro quando dados forem inválidos', async () => {
      // Dado
      const dadosInvalidos = {
        nome: '',
        email: 'email-invalido',
        senha: '123',
      }
      api.post.mockRejectedValue({
        response: { data: { message: 'Dados inválidos' } }
      })

      // Quando/Então
      await expect(usuarioService.criar(dadosInvalidos)).rejects.toThrow('Dados inválidos')
    })
  })

  describe('atualizar', () => {
    it('deve atualizar um usuário existente', async () => {
      // Dado
      const id = 1
      const dadosAtualizados = {
        nome: 'Nome Atualizado',
        email: 'atualizado@example.com',
      }
      api.put.mockResolvedValue({ data: { ...mockUsuario, ...dadosAtualizados } })

      // Quando
      const resultado = await usuarioService.atualizar(id, dadosAtualizados)

      // Então
      expect(api.put).toHaveBeenCalledWith('/usuarios/1', dadosAtualizados)
      expect(resultado.nome).toBe('Nome Atualizado')
      expect(resultado.email).toBe('atualizado@example.com')
    })

    it('deve lançar erro ao atualizar usuário inexistente', async () => {
      // Dado
      const id = 999
      const dados = { nome: 'Teste' }
      api.put.mockRejectedValue({
        response: { data: { message: 'Usuário não encontrado' } }
      })

      // Quando/Então
      await expect(usuarioService.atualizar(id, dados)).rejects.toThrow('Usuário não encontrado')
    })

    it('deve lançar erro ao tentar atualizar com email duplicado', async () => {
      // Dado
      const id = 1
      const dados = { email: 'admin@example.com' }
      api.put.mockRejectedValue({
        response: { data: { message: 'Email já cadastrado' } }
      })

      // Quando/Então
      await expect(usuarioService.atualizar(id, dados)).rejects.toThrow('Email já cadastrado')
    })
  })
})
