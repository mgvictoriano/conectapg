import api from './api'

export const ocorrenciaService = {
  listarTodas: async (filtros = {}) => {
    try {
      const params = new URLSearchParams()
      
      if (filtros.status) params.append('status', filtros.status)
      if (filtros.tipo) params.append('tipo', filtros.tipo)
      
      const response = await api.get(`/ocorrencias?${params.toString()}`)
      return response.data
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Erro ao listar ocorrências')
    }
  },
  
  buscarPorId: async (id) => {
    try {
      const response = await api.get(`/ocorrencias/${id}`)
      return response.data
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Erro ao buscar ocorrência')
    }
  },
  
  criar: async (dados) => {
    try {
      const response = await api.post('/ocorrencias', dados)
      return response.data
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Erro ao criar ocorrência')
    }
  },
  
  atualizar: async (id, dados) => {
    try {
      const response = await api.put(`/ocorrencias/${id}`, dados)
      return response.data
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Erro ao atualizar ocorrência')
    }
  },
  
  atualizarStatus: async (id, status) => {
    try {
      const response = await api.patch(`/ocorrencias/${id}/status`, null, {
        params: { status }
      })
      return response.data
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Erro ao atualizar status')
    }
  },
  
  deletar: async (id) => {
    try {
      await api.delete(`/ocorrencias/${id}`)
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Erro ao deletar ocorrência')
    }
  },
  
  // Estatísticas para o dashboard
  obterEstatisticas: async () => {
    try {
      const response = await api.get('/ocorrencias')
      const ocorrencias = response.data
      
      return {
        total: ocorrencias.length,
        abertas: ocorrencias.filter(o => o.status === 'ABERTA').length,
        emAndamento: ocorrencias.filter(o => o.status === 'EM_ANDAMENTO').length,
        resolvidas: ocorrencias.filter(o => o.status === 'RESOLVIDA').length,
        fechadas: ocorrencias.filter(o => o.status === 'FECHADA').length,
      }
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Erro ao obter estatísticas')
    }
  },
}
