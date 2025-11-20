import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import { useAuthStore } from './context/authStore'
import Navbar from './components/Navbar'
import Footer from './components/Footer'
import Login from './pages/Login'
import CriarOcorrencia from './pages/CriarOcorrencia'
import ListaOcorrencias from './pages/ListaOcorrencias'
import DetalheOcorrencia from './pages/DetalheOcorrencia'
import PainelPrefeitura from './pages/PainelPrefeitura'

// Componente para rotas protegidas
const ProtectedRoute = ({ children, requireAdmin = false }) => {
  const { user, isAuthenticated } = useAuthStore()
  
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />
  }
  
  if (requireAdmin && user?.tipo !== 'ADMIN' && user?.tipo !== 'GESTOR') {
    return <Navigate to="/" replace />
  }
  
  return children
}

function App() {
  const { isAuthenticated } = useAuthStore()

  return (
    <Router>
      <div className="min-h-screen flex flex-col">
        {isAuthenticated && <Navbar />}
        
        <main className="flex-grow">
          <Routes>
            <Route path="/login" element={<Login />} />
            
            <Route 
              path="/" 
              element={
                <ProtectedRoute>
                  <ListaOcorrencias />
                </ProtectedRoute>
              } 
            />
            
            <Route 
              path="/ocorrencias" 
              element={
                <ProtectedRoute>
                  <ListaOcorrencias />
                </ProtectedRoute>
              } 
            />
            
            <Route 
              path="/ocorrencias/nova" 
              element={
                <ProtectedRoute>
                  <CriarOcorrencia />
                </ProtectedRoute>
              } 
            />
            
            <Route 
              path="/ocorrencias/:id" 
              element={
                <ProtectedRoute>
                  <DetalheOcorrencia />
                </ProtectedRoute>
              } 
            />
            
            <Route 
              path="/painel" 
              element={
                <ProtectedRoute requireAdmin={true}>
                  <PainelPrefeitura />
                </ProtectedRoute>
              } 
            />
            
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </main>
        
        {isAuthenticated && <Footer />}
      </div>
    </Router>
  )
}

export default App
