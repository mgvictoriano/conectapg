import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { ocorrenciaService } from '../services/ocorrenciaService'
import { FiAlertCircle, FiClock, FiCheckCircle, FiXCircle, FiFileText } from 'react-icons/fi'

const PainelPrefeitura = () => {
  const [estatisticas, setEstatisticas] = useState(null)
  const [ocorrenciasRecentes, setOcorrenciasRecentes] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  
  useEffect(() => {
    carregarDados()
  }, [])
  
  const carregarDados = async () => {
    setLoading(true)
    setError('')
    
    try {
      const [stats, ocorrencias] = await Promise.all([
        ocorrenciaService.obterEstatisticas(),
        ocorrenciaService.listarTodas()
      ])
      
      setEstatisticas(stats)
      // Pega as 5 mais recentes
      setOcorrenciasRecentes(ocorrencias.slice(0, 5))
    } catch (err) {
      setError(err.message || 'Erro ao carregar dados do painel')
    } finally {
      setLoading(false)
    }
  }
  
  const cards = [
    {
      title: 'Total de Ocorrências',
      value: estatisticas?.total || 0,
      icon: FiFileText,
      color: 'blue',
      bgColor: 'bg-blue-500',
    },
    {
      title: 'Abertas',
      value: estatisticas?.abertas || 0,
      icon: FiAlertCircle,
      color: 'yellow',
      bgColor: 'bg-yellow-500',
    },
    {
      title: 'Em Andamento',
      value: estatisticas?.emAndamento || 0,
      icon: FiClock,
      color: 'blue',
      bgColor: 'bg-blue-600',
    },
    {
      title: 'Resolvidas',
      value: estatisticas?.resolvidas || 0,
      icon: FiCheckCircle,
      color: 'green',
      bgColor: 'bg-green-500',
    },
  ]
  
  const getStatusColor = (status) => {
    const colors = {
      ABERTA: 'bg-yellow-100 text-yellow-800',
      EM_ANDAMENTO: 'bg-blue-100 text-blue-800',
      RESOLVIDA: 'bg-green-100 text-green-800',
      FECHADA: 'bg-gray-100 text-gray-800',
    }
    return colors[status] || colors.ABERTA
  }
  
  if (loading) {
    return (
      <div className="container mx-auto px-4 py-8">
        <div className="text-center py-12">
          <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
          <p className="text-gray-600 mt-4">Carregando painel...</p>
        </div>
      </div>
    )
  }
  
  return (
    <div className="container mx-auto px-4 py-8">
      {/* Header */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-800">Painel da Prefeitura</h1>
        <p className="text-gray-600 mt-2">
          Visão geral das ocorrências urbanas
        </p>
      </div>
      
      {/* Erro */}
      {error && (
        <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg flex items-start space-x-2">
          <FiAlertCircle className="w-5 h-5 text-red-600 flex-shrink-0 mt-0.5" />
          <p className="text-sm text-red-600">{error}</p>
        </div>
      )}
      
      {/* Cards de Estatísticas */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        {cards.map((card, index) => {
          const Icon = card.icon
          return (
            <div key={index} className="card hover:shadow-xl transition-shadow">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-600 mb-1">
                    {card.title}
                  </p>
                  <p className="text-3xl font-bold text-gray-800">
                    {card.value}
                  </p>
                </div>
                <div className={`${card.bgColor} p-4 rounded-lg`}>
                  <Icon className="w-8 h-8 text-white" />
                </div>
              </div>
            </div>
          )
        })}
      </div>
      
      {/* Gráfico de Pizza Simples */}
      {estatisticas && estatisticas.total > 0 && (
        <div className="card mb-8">
          <h2 className="text-xl font-semibold text-gray-800 mb-6">
            Distribuição por Status
          </h2>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
            {/* Barras de Progresso */}
            <div className="space-y-4">
              <div>
                <div className="flex justify-between mb-2">
                  <span className="text-sm font-medium text-gray-700">Abertas</span>
                  <span className="text-sm font-medium text-gray-700">
                    {((estatisticas.abertas / estatisticas.total) * 100).toFixed(1)}%
                  </span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-3">
                  <div 
                    className="bg-yellow-500 h-3 rounded-full transition-all duration-500"
                    style={{ width: `${(estatisticas.abertas / estatisticas.total) * 100}%` }}
                  ></div>
                </div>
              </div>
              
              <div>
                <div className="flex justify-between mb-2">
                  <span className="text-sm font-medium text-gray-700">Em Andamento</span>
                  <span className="text-sm font-medium text-gray-700">
                    {((estatisticas.emAndamento / estatisticas.total) * 100).toFixed(1)}%
                  </span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-3">
                  <div 
                    className="bg-blue-600 h-3 rounded-full transition-all duration-500"
                    style={{ width: `${(estatisticas.emAndamento / estatisticas.total) * 100}%` }}
                  ></div>
                </div>
              </div>
              
              <div>
                <div className="flex justify-between mb-2">
                  <span className="text-sm font-medium text-gray-700">Resolvidas</span>
                  <span className="text-sm font-medium text-gray-700">
                    {((estatisticas.resolvidas / estatisticas.total) * 100).toFixed(1)}%
                  </span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-3">
                  <div 
                    className="bg-green-500 h-3 rounded-full transition-all duration-500"
                    style={{ width: `${(estatisticas.resolvidas / estatisticas.total) * 100}%` }}
                  ></div>
                </div>
              </div>
              
              <div>
                <div className="flex justify-between mb-2">
                  <span className="text-sm font-medium text-gray-700">Fechadas</span>
                  <span className="text-sm font-medium text-gray-700">
                    {((estatisticas.fechadas / estatisticas.total) * 100).toFixed(1)}%
                  </span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-3">
                  <div 
                    className="bg-gray-500 h-3 rounded-full transition-all duration-500"
                    style={{ width: `${(estatisticas.fechadas / estatisticas.total) * 100}%` }}
                  ></div>
                </div>
              </div>
            </div>
            
            {/* Legenda */}
            <div className="flex flex-col justify-center space-y-3">
              <div className="flex items-center space-x-3">
                <div className="w-4 h-4 bg-yellow-500 rounded"></div>
                <span className="text-sm text-gray-700">
                  Abertas: <strong>{estatisticas.abertas}</strong>
                </span>
              </div>
              <div className="flex items-center space-x-3">
                <div className="w-4 h-4 bg-blue-600 rounded"></div>
                <span className="text-sm text-gray-700">
                  Em Andamento: <strong>{estatisticas.emAndamento}</strong>
                </span>
              </div>
              <div className="flex items-center space-x-3">
                <div className="w-4 h-4 bg-green-500 rounded"></div>
                <span className="text-sm text-gray-700">
                  Resolvidas: <strong>{estatisticas.resolvidas}</strong>
                </span>
              </div>
              <div className="flex items-center space-x-3">
                <div className="w-4 h-4 bg-gray-500 rounded"></div>
                <span className="text-sm text-gray-700">
                  Fechadas: <strong>{estatisticas.fechadas}</strong>
                </span>
              </div>
            </div>
          </div>
        </div>
      )}
      
      {/* Ocorrências Recentes */}
      <div className="card">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-xl font-semibold text-gray-800">
            Ocorrências Recentes
          </h2>
          <Link to="/ocorrencias" className="text-primary-600 hover:text-primary-700 text-sm font-medium">
            Ver todas →
          </Link>
        </div>
        
        {ocorrenciasRecentes.length === 0 ? (
          <p className="text-gray-500 text-center py-8">
            Nenhuma ocorrência registrada ainda
          </p>
        ) : (
          <div className="space-y-4">
            {ocorrenciasRecentes.map(ocorrencia => (
              <Link
                key={ocorrencia.id}
                to={`/ocorrencias/${ocorrencia.id}`}
                className="block p-4 bg-gray-50 rounded-lg hover:bg-gray-100 transition-colors"
              >
                <div className="flex justify-between items-start mb-2">
                  <h3 className="font-semibold text-gray-800 flex-1">
                    {ocorrencia.titulo}
                  </h3>
                  <span className={`px-3 py-1 rounded-full text-xs font-semibold ${getStatusColor(ocorrencia.status)}`}>
                    {ocorrencia.status.replace('_', ' ')}
                  </span>
                </div>
                <p className="text-sm text-gray-600 mb-2 line-clamp-1">
                  {ocorrencia.descricao}
                </p>
                <div className="flex items-center text-xs text-gray-500 space-x-4">
                  <span>{ocorrencia.localizacao}</span>
                  <span>•</span>
                  <span>{ocorrencia.tipo}</span>
                </div>
              </Link>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}

export default PainelPrefeitura
