import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useForm } from 'react-hook-form'
import { useAuthStore } from '../context/authStore'
import { ocorrenciaService } from '../services/ocorrenciaService'
import { FiArrowLeft, FiAlertCircle, FiCheckCircle } from 'react-icons/fi'

const CriarOcorrencia = () => {
  const navigate = useNavigate()
  const { user } = useAuthStore()
  const { register, handleSubmit, formState: { errors } } = useForm()
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState(false)
  
  const tiposOcorrencia = [
    { value: 'ILUMINACAO', label: 'Iluminação Pública' },
    { value: 'BURACO', label: 'Buraco na Via' },
    { value: 'LIXO', label: 'Lixo/Entulho' },
    { value: 'VANDALISMO', label: 'Vandalismo' },
    { value: 'OUTROS', label: 'Outros' },
  ]
  
  const onSubmit = async (data) => {
    setLoading(true)
    setError('')
    setSuccess(false)
    
    try {
      const ocorrenciaData = {
        ...data,
        usuarioId: user.id,
      }
      
      await ocorrenciaService.criar(ocorrenciaData)
      setSuccess(true)
      
      // Redireciona após 2 segundos
      setTimeout(() => {
        navigate('/ocorrencias')
      }, 2000)
    } catch (err) {
      setError(err.message || 'Erro ao criar ocorrência')
    } finally {
      setLoading(false)
    }
  }
  
  return (
    <div className="container mx-auto px-4 py-8">
      <div className="max-w-3xl mx-auto">
        {/* Header */}
        <div className="mb-8">
          <button
            onClick={() => navigate(-1)}
            className="flex items-center space-x-2 text-gray-600 hover:text-gray-800 mb-4"
          >
            <FiArrowLeft className="w-5 h-5" />
            <span>Voltar</span>
          </button>
          
          <h1 className="text-3xl font-bold text-gray-800">Nova Ocorrência</h1>
          <p className="text-gray-600 mt-2">
            Relate um problema urbano para que a prefeitura possa resolvê-lo
          </p>
        </div>
        
        {/* Alertas */}
        {error && (
          <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg flex items-start space-x-2">
            <FiAlertCircle className="w-5 h-5 text-red-600 flex-shrink-0 mt-0.5" />
            <p className="text-sm text-red-600">{error}</p>
          </div>
        )}
        
        {success && (
          <div className="mb-6 p-4 bg-green-50 border border-green-200 rounded-lg flex items-start space-x-2">
            <FiCheckCircle className="w-5 h-5 text-green-600 flex-shrink-0 mt-0.5" />
            <p className="text-sm text-green-600">
              Ocorrência criada com sucesso! Redirecionando...
            </p>
          </div>
        )}
        
        {/* Formulário */}
        <div className="card">
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
            {/* Título */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Título *
              </label>
              <input
                type="text"
                {...register('titulo', { 
                  required: 'Título é obrigatório',
                  minLength: {
                    value: 5,
                    message: 'Título deve ter no mínimo 5 caracteres'
                  }
                })}
                className="input-field"
                placeholder="Ex: Poste queimado na Rua das Flores"
              />
              {errors.titulo && (
                <p className="mt-1 text-sm text-red-600">{errors.titulo.message}</p>
              )}
            </div>
            
            {/* Tipo */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Tipo de Ocorrência *
              </label>
              <select
                {...register('tipo', { required: 'Tipo é obrigatório' })}
                className="input-field"
              >
                <option value="">Selecione o tipo</option>
                {tiposOcorrencia.map(tipo => (
                  <option key={tipo.value} value={tipo.value}>
                    {tipo.label}
                  </option>
                ))}
              </select>
              {errors.tipo && (
                <p className="mt-1 text-sm text-red-600">{errors.tipo.message}</p>
              )}
            </div>
            
            {/* Descrição */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Descrição *
              </label>
              <textarea
                {...register('descricao', { 
                  required: 'Descrição é obrigatória',
                  minLength: {
                    value: 10,
                    message: 'Descrição deve ter no mínimo 10 caracteres'
                  }
                })}
                rows="5"
                className="input-field resize-none"
                placeholder="Descreva o problema com detalhes..."
              />
              {errors.descricao && (
                <p className="mt-1 text-sm text-red-600">{errors.descricao.message}</p>
              )}
            </div>
            
            {/* Localização */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Localização *
              </label>
              <input
                type="text"
                {...register('localizacao', { 
                  required: 'Localização é obrigatória',
                  minLength: {
                    value: 5,
                    message: 'Localização deve ter no mínimo 5 caracteres'
                  }
                })}
                className="input-field"
                placeholder="Ex: Rua das Flores, 123 - Centro"
              />
              {errors.localizacao && (
                <p className="mt-1 text-sm text-red-600">{errors.localizacao.message}</p>
              )}
              <p className="mt-1 text-xs text-gray-500">
                Informe o endereço ou ponto de referência
              </p>
            </div>
            
            {/* Botões */}
            <div className="flex space-x-4 pt-4">
              <button
                type="button"
                onClick={() => navigate(-1)}
                className="btn-secondary flex-1"
                disabled={loading}
              >
                Cancelar
              </button>
              <button
                type="submit"
                className="btn-primary flex-1"
                disabled={loading || success}
              >
                {loading ? 'Criando...' : 'Criar Ocorrência'}
              </button>
            </div>
          </form>
        </div>
        
        {/* Informação adicional */}
        <div className="mt-6 p-4 bg-blue-50 border border-blue-200 rounded-lg">
          <p className="text-sm text-blue-800">
            <strong>Dica:</strong> Seja o mais específico possível na descrição e localização 
            para que a equipe da prefeitura possa resolver o problema rapidamente.
          </p>
        </div>
      </div>
    </div>
  )
}

export default CriarOcorrencia
