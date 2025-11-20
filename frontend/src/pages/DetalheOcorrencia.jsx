import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useAuthStore } from '../context/authStore'
import { ocorrenciaService } from '../services/ocorrenciaService'
import { FiArrowLeft, FiMapPin, FiClock, FiUser, FiAlertCircle, FiCheckCircle } from 'react-icons/fi'
import { format } from 'date-fns'
import { ptBR } from 'date-fns/locale'

const DetalheOcorrencia = () => {
  const { id } = useParams()
  const navigate = useNavigate()
  const { user } = useAuthStore()
  const [ocorrencia, setOcorrencia] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [atualizandoStatus, setAtualizandoStatus] = useState(false)
  const [successMessage, setSuccessMessage] = useState('')
  
  const isAdmin = user?.tipo === 'ADMIN' || user?.tipo === 'GESTOR'
  
  const statusOptions = [
    { value: 'ABERTA', label: 'Aberta', color: 'yellow' },
    { value: 'EM_ANDAMENTO', label: 'Em Andamento', color: 'blue' },
    { value: 'RESOLVIDA', label: 'Resolvida', color: 'green' },
    { value: 'FECHADA', label: 'Fechada', color: 'gray' },
  ]
  
  useEffect(() => {
    carregarOcorrencia()
  }, [id])
  
  const carregarOcorrencia = async () => {
    setLoading(true)
    setError('')
    
    try {
      const data = await ocorrenciaService.buscarPorId(id)
      setOcorrencia(data)
    } catch (err) {
      setError(err.message || 'Erro ao carregar ocorrência')
    } finally {
      setLoading(false)
    }
  }
  
  const handleAtualizarStatus = async (novoStatus) => {
    setAtualizandoStatus(true)
    setError('')
    setSuccessMessage('')
    
    try {
      await ocorrenciaService.atualizarStatus(id, novoStatus)
      setSuccessMessage('Status atualizado com sucesso!')
      await carregarOcorrencia()
      
      setTimeout(() => setSuccessMessage(''), 3000)
    } catch (err) {
      setError(err.message || 'Erro ao atualizar status')
    } finally {
      setAtualizandoStatus(false)
    }
  }
  
  const getStatusColor = (status) => {
    const option = statusOptions.find(opt => opt.value === status)
    return option?.color || 'gray'
  }
  
  const formatDate = (dateString) => {
    try {
      return format(new Date(dateString), "dd 'de' MMMM 'de' yyyy 'às' HH:mm", { locale: ptBR })
    } catch {
      return dateString
    }
  }
  
  const getTipoLabel = (tipo) => {
    const labels = {
      ILUMINACAO: 'Iluminação Pública',
      BURACO: 'Buraco na Via',
      LIXO: 'Lixo/Entulho',
      VANDALISMO: 'Vandalismo',
      OUTROS: 'Outros',
    }
    return labels[tipo] || tipo
  }
  
  if (loading) {
    return (
      <div className="container mx-auto px-4 py-8">
        <div className="text-center py-12">
          <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
          <p className="text-gray-600 mt-4">Carregando ocorrência...</p>
        </div>
      </div>
    )
  }
  
  if (error && !ocorrencia) {
    return (
      <div className="container mx-auto px-4 py-8">
        <div className="max-w-2xl mx-auto">
          <div className="p-6 bg-red-50 border border-red-200 rounded-lg">
            <FiAlertCircle className="w-8 h-8 text-red-600 mb-2" />
            <h3 className="text-lg font-semibold text-red-800 mb-2">Erro ao carregar</h3>
            <p className="text-red-600">{error}</p>
            <button onClick={() => navigate(-1)} className="btn-secondary mt-4">
              Voltar
            </button>
          </div>
        </div>
      </div>
    )
  }
  
  return (
    <div className="container mx-auto px-4 py-8">
      <div className="max-w-4xl mx-auto">
        {/* Header */}
        <div className="mb-8">
          <button
            onClick={() => navigate(-1)}
            className="flex items-center space-x-2 text-gray-600 hover:text-gray-800 mb-4"
          >
            <FiArrowLeft className="w-5 h-5" />
            <span>Voltar</span>
          </button>
        </div>
        
        {/* Alertas */}
        {successMessage && (
          <div className="mb-6 p-4 bg-green-50 border border-green-200 rounded-lg flex items-start space-x-2">
            <FiCheckCircle className="w-5 h-5 text-green-600 flex-shrink-0 mt-0.5" />
            <p className="text-sm text-green-600">{successMessage}</p>
          </div>
        )}
        
        {error && (
          <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg flex items-start space-x-2">
            <FiAlertCircle className="w-5 h-5 text-red-600 flex-shrink-0 mt-0.5" />
            <p className="text-sm text-red-600">{error}</p>
          </div>
        )}
        
        {/* Card Principal */}
        <div className="card">
          {/* Título e Status */}
          <div className="flex flex-col md:flex-row justify-between items-start mb-6 pb-6 border-b">
            <div className="flex-1 mb-4 md:mb-0">
              <h1 className="text-3xl font-bold text-gray-800 mb-2">
                {ocorrencia.titulo}
              </h1>
              <span className="inline-block px-4 py-2 bg-gray-100 text-gray-700 rounded-full text-sm font-medium">
                {getTipoLabel(ocorrencia.tipo)}
              </span>
            </div>
            
            <div className={`px-4 py-2 rounded-lg text-sm font-semibold bg-${getStatusColor(ocorrencia.status)}-100 text-${getStatusColor(ocorrencia.status)}-800 border border-${getStatusColor(ocorrencia.status)}-300`}>
              {ocorrencia.status.replace('_', ' ')}
            </div>
          </div>
          
          {/* Descrição */}
          <div className="mb-6">
            <h2 className="text-lg font-semibold text-gray-800 mb-3">Descrição</h2>
            <p className="text-gray-700 leading-relaxed whitespace-pre-wrap">
              {ocorrencia.descricao}
            </p>
          </div>
          
          {/* Informações */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
            <div className="flex items-start space-x-3">
              <FiMapPin className="w-5 h-5 text-gray-500 mt-1 flex-shrink-0" />
              <div>
                <p className="text-sm font-medium text-gray-500">Localização</p>
                <p className="text-gray-800">{ocorrencia.localizacao}</p>
              </div>
            </div>
            
            <div className="flex items-start space-x-3">
              <FiClock className="w-5 h-5 text-gray-500 mt-1 flex-shrink-0" />
              <div>
                <p className="text-sm font-medium text-gray-500">Data de Criação</p>
                <p className="text-gray-800">{formatDate(ocorrencia.dataCriacao)}</p>
              </div>
            </div>
            
            {ocorrencia.usuario && (
              <div className="flex items-start space-x-3">
                <FiUser className="w-5 h-5 text-gray-500 mt-1 flex-shrink-0" />
                <div>
                  <p className="text-sm font-medium text-gray-500">Reportado por</p>
                  <p className="text-gray-800">{ocorrencia.usuario.nome}</p>
                  <p className="text-sm text-gray-500">{ocorrencia.usuario.email}</p>
                </div>
              </div>
            )}
            
            {ocorrencia.dataAtualizacao && (
              <div className="flex items-start space-x-3">
                <FiClock className="w-5 h-5 text-gray-500 mt-1 flex-shrink-0" />
                <div>
                  <p className="text-sm font-medium text-gray-500">Última Atualização</p>
                  <p className="text-gray-800">{formatDate(ocorrencia.dataAtualizacao)}</p>
                </div>
              </div>
            )}
          </div>
          
          {/* Atualizar Status (apenas para admin) */}
          {isAdmin && (
            <div className="pt-6 border-t">
              <h3 className="text-lg font-semibold text-gray-800 mb-4">
                Atualizar Status
              </h3>
              <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
                {statusOptions.map(option => (
                  <button
                    key={option.value}
                    onClick={() => handleAtualizarStatus(option.value)}
                    disabled={atualizandoStatus || ocorrencia.status === option.value}
                    className={`px-4 py-3 rounded-lg font-medium transition-all ${
                      ocorrencia.status === option.value
                        ? `bg-${option.color}-100 text-${option.color}-800 border-2 border-${option.color}-500 cursor-default`
                        : `bg-gray-100 text-gray-700 hover:bg-${option.color}-50 hover:text-${option.color}-700 border border-gray-300`
                    } disabled:opacity-50 disabled:cursor-not-allowed`}
                  >
                    {option.label}
                  </button>
                ))}
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

export default DetalheOcorrencia
