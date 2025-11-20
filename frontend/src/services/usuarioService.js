import api from './api'

export const usuarioService = {
  // Login (simulado - adaptar quando implementar JWT no backend)
  login: async (email, senha) => {
    try {
      // Por enquanto, busca o usuário por email
      const response = await api.get(`/usuarios/email/${email}`)
      const usuario = response.data
      
      // Simulação de autenticação (REMOVER quando implementar JWT)
      if (usuario) {
        return {
          user: usuario,
          token: 'fake-jwt-token', // Substituir por token real
        }
      }
      
      throw new Error('Credenciais inválidas')
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Erro ao fazer login')
    }
  },
  
  listarTodos: async () => {
    try {
      const response = await api.get('/usuarios')
      return response.data
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Erro ao listar usuários')
    }
  },
  
  buscarPorId: async (id) => {
    try {
      const response = await api.get(`/usuarios/${id}`)
      return response.data
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Erro ao buscar usuário')
    }
  },
  
  criar: async (dados) => {
    try {
      const response = await api.post('/usuarios', dados)
      return response.data
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Erro ao criar usuário')
    }
  },
  
  atualizar: async (id, dados) => {
    try {
      const response = await api.put(`/usuarios/${id}`, dados)
      return response.data
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Erro ao atualizar usuário')
    }
  },
}
