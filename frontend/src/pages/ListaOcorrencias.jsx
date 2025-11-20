import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { ocorrenciaService } from '../services/ocorrenciaService'
import CardOcorrencia from '../components/CardOcorrencia'
import { FiPlusCircle, FiFilter, FiAlertCircle } from 'react-icons/fi'

const ListaOcorrencias = () => {
  const [ocorrencias, setOcorrencias] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [filtros, setFiltros] = useState({
    status: '',
    tipo: '',
  })
  
  const statusOptions = [
    { value: '', label: 'Todos os Status' },
    { value: 'ABERTA', label: 'Abertas' },
    { value: 'EM_ANDAMENTO', label: 'Em Andamento' },
    { value: 'RESOLVIDA', label: 'Resolvidas' },
    { value: 'FECHADA', label: 'Fechadas' },
  ]
  
  const tipoOptions = [
    { value: '', label: 'Todos os Tipos' },
    { value: 'ILUMINACAO', label: 'Iluminação' },
    { value: 'BURACO', label: 'Buraco' },
    { value: 'LIXO', label: 'Lixo' },
    { value: 'VANDALISMO', label: 'Vandalismo' },
    { value: 'OUTROS', label: 'Outros' },
  ]
  
  useEffect(() => {
    carregarOcorrencias()
  }, [filtros])
  
  const carregarOcorrencias = async () => {
    setLoading(true)
    setError('')
    
    try {
      const data = await ocorrenciaService.listarTodas(filtros)
      setOcorrencias(data)
    } catch (err) {
      setError(err.message || 'Erro ao carregar ocorrências')
    } finally {
      setLoading(false)
    }
  }
  
  const handleFiltroChange = (campo, valor) => {
    setFiltros(prev => ({
      ...prev,
      [campo]: valor
    }))
  }
  
  const limparFiltros = () => {
    setFiltros({ status: '', tipo: '' })
  }
  
  return (
    <div className="container mx-auto px-4 py-8">
      {/* Header */}
      <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-8 space-y-4 md:space-y-0">
        <div>
          <h1 className="text-3xl font-bold text-gray-800">Ocorrências</h1>
          <p className="text-gray-600 mt-2">
            {ocorrencias.length} ocorrência{ocorrencias.length !== 1 ? 's' : ''} encontrada{ocorrencias.length !== 1 ? 's' : ''}
          </p>
        </div>
        
        <Link to="/ocorrencias/nova" className="btn-primary flex items-center space-x-2">
          <FiPlusCircle className="w-5 h-5" />
          <span>Nova Ocorrência</span>
        </Link>
      </div>
      
      {/* Filtros */}
      <div className="card mb-8">
        <div className="flex items-center space-x-2 mb-4">
          <FiFilter className="w-5 h-5 text-gray-600" />
          <h2 className="text-lg font-semibold text-gray-800">Filtros</h2>
        </div>
        
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Status
            </label>
            <select
              value={filtros.status}
              onChange={(e) => handleFiltroChange('status', e.target.value)}
              className="input-field"
            >
              {statusOptions.map(option => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
          </div>
          
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Tipo
            </label>
            <select
              value={filtros.tipo}
              onChange={(e) => handleFiltroChange('tipo', e.target.value)}
              className="input-field"
            >
              {tipoOptions.map(option => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
          </div>
          
          <div className="flex items-end">
            <button
              onClick={limparFiltros}
              className="btn-secondary w-full"
            >
              Limpar Filtros
            </button>
          </div>
        </div>
      </div>
      
      {/* Erro */}
      {error && (
        <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg flex items-start space-x-2">
          <FiAlertCircle className="w-5 h-5 text-red-600 flex-shrink-0 mt-0.5" />
          <p className="text-sm text-red-600">{error}</p>
        </div>
      )}
      
      {/* Loading */}
      {loading && (
        <div className="text-center py-12">
          <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
          <p className="text-gray-600 mt-4">Carregando ocorrências...</p>
        </div>
      )}
      
      {/* Lista de Ocorrências */}
      {!loading && ocorrencias.length === 0 && (
        <div className="text-center py-12">
          <div className="inline-block w-24 h-24 bg-gray-100 rounded-full flex items-center justify-center mb-4">
            <FiAlertCircle className="w-12 h-12 text-gray-400" />
          </div>
          <h3 className="text-xl font-semibold text-gray-800 mb-2">
            Nenhuma ocorrência encontrada
          </h3>
          <p className="text-gray-600 mb-6">
            {filtros.status || filtros.tipo 
              ? 'Tente ajustar os filtros ou criar uma nova ocorrência.'
              : 'Seja o primeiro a reportar um problema!'}
          </p>
          <Link to="/ocorrencias/nova" className="btn-primary inline-flex items-center space-x-2">
            <FiPlusCircle className="w-5 h-5" />
            <span>Criar Primeira Ocorrência</span>
          </Link>
        </div>
      )}
      
      {!loading && ocorrencias.length > 0 && (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {ocorrencias.map(ocorrencia => (
            <CardOcorrencia key={ocorrencia.id} ocorrencia={ocorrencia} />
          ))}
        </div>
      )}
    </div>
  )
}

export default ListaOcorrencias
