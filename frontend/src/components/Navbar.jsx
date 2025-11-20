import { Link, useNavigate } from 'react-router-dom'
import { useAuthStore } from '../context/authStore'
import { FiHome, FiPlusCircle, FiBarChart2, FiLogOut, FiUser } from 'react-icons/fi'

const Navbar = () => {
  const { user, logout } = useAuthStore()
  const navigate = useNavigate()
  
  const handleLogout = () => {
    logout()
    navigate('/login')
  }
  
  const isAdmin = user?.tipo === 'ADMIN' || user?.tipo === 'GESTOR'
  
  return (
    <nav className="bg-white shadow-md">
      <div className="container mx-auto px-4">
        <div className="flex justify-between items-center h-16">
          {/* Logo */}
          <Link to="/" className="flex items-center space-x-2">
            <div className="w-10 h-10 bg-primary-600 rounded-lg flex items-center justify-center">
              <span className="text-white font-bold text-xl">CP</span>
            </div>
            <span className="text-xl font-bold text-gray-800">ConectaPG</span>
          </Link>
          
          {/* Menu */}
          <div className="flex items-center space-x-6">
            <Link 
              to="/ocorrencias" 
              className="flex items-center space-x-2 text-gray-700 hover:text-primary-600 transition-colors"
            >
              <FiHome className="w-5 h-5" />
              <span className="hidden md:inline">Ocorrências</span>
            </Link>
            
            <Link 
              to="/ocorrencias/nova" 
              className="flex items-center space-x-2 text-gray-700 hover:text-primary-600 transition-colors"
            >
              <FiPlusCircle className="w-5 h-5" />
              <span className="hidden md:inline">Nova Ocorrência</span>
            </Link>
            
            {isAdmin && (
              <Link 
                to="/painel" 
                className="flex items-center space-x-2 text-gray-700 hover:text-primary-600 transition-colors"
              >
                <FiBarChart2 className="w-5 h-5" />
                <span className="hidden md:inline">Painel</span>
              </Link>
            )}
            
            {/* User Info */}
            <div className="flex items-center space-x-4 border-l pl-4">
              <div className="hidden md:flex items-center space-x-2">
                <FiUser className="w-5 h-5 text-gray-600" />
                <div className="text-sm">
                  <p className="font-semibold text-gray-800">{user?.nome}</p>
                  <p className="text-gray-500 text-xs">{user?.tipo}</p>
                </div>
              </div>
              
              <button
                onClick={handleLogout}
                className="flex items-center space-x-2 text-red-600 hover:text-red-700 transition-colors"
                title="Sair"
              >
                <FiLogOut className="w-5 h-5" />
                <span className="hidden md:inline">Sair</span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </nav>
  )
}

export default Navbar
