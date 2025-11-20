import { Link } from 'react-router-dom'
import { FiMapPin, FiClock, FiUser } from 'react-icons/fi'
import { format } from 'date-fns'
import { ptBR } from 'date-fns/locale'

const CardOcorrencia = ({ ocorrencia }) => {
  const getStatusColor = (status) => {
    const colors = {
      ABERTA: 'bg-yellow-100 text-yellow-800 border-yellow-300',
      EM_ANDAMENTO: 'bg-blue-100 text-blue-800 border-blue-300',
      RESOLVIDA: 'bg-green-100 text-green-800 border-green-300',
      FECHADA: 'bg-gray-100 text-gray-800 border-gray-300',
    }
    return colors[status] || colors.ABERTA
  }
  
  const getTipoLabel = (tipo) => {
    const labels = {
      ILUMINACAO: 'Iluminação',
      BURACO: 'Buraco',
      LIXO: 'Lixo',
      VANDALISMO: 'Vandalismo',
      OUTROS: 'Outros',
    }
    return labels[tipo] || tipo
  }
  
  const formatDate = (dateString) => {
    try {
      return format(new Date(dateString), "dd/MM/yyyy 'às' HH:mm", { locale: ptBR })
    } catch {
      return dateString
    }
  }
  
  return (
    <Link to={`/ocorrencias/${ocorrencia.id}`}>
      <div className="card hover:scale-[1.02] transition-transform cursor-pointer">
        {/* Header */}
        <div className="flex justify-between items-start mb-3">
          <h3 className="text-lg font-semibold text-gray-800 line-clamp-2">
            {ocorrencia.titulo}
          </h3>
          <span className={`px-3 py-1 rounded-full text-xs font-semibold border ${getStatusColor(ocorrencia.status)}`}>
            {ocorrencia.status.replace('_', ' ')}
          </span>
        </div>
        
        {/* Descrição */}
        <p className="text-gray-600 text-sm mb-4 line-clamp-2">
          {ocorrencia.descricao}
        </p>
        
        {/* Informações */}
        <div className="space-y-2 text-sm text-gray-500">
          <div className="flex items-center space-x-2">
            <FiMapPin className="w-4 h-4" />
            <span>{ocorrencia.localizacao}</span>
          </div>
          
          <div className="flex items-center space-x-2">
            <FiClock className="w-4 h-4" />
            <span>{formatDate(ocorrencia.dataCriacao)}</span>
          </div>
          
          {ocorrencia.usuario && (
            <div className="flex items-center space-x-2">
              <FiUser className="w-4 h-4" />
              <span>{ocorrencia.usuario.nome}</span>
            </div>
          )}
        </div>
        
        {/* Footer */}
        <div className="mt-4 pt-4 border-t border-gray-200">
          <span className="inline-block px-3 py-1 bg-gray-100 text-gray-700 rounded-full text-xs font-medium">
            {getTipoLabel(ocorrencia.tipo)}
          </span>
        </div>
      </div>
    </Link>
  )
}

export default CardOcorrencia
