const Footer = () => {
  return (
    <footer className="bg-gray-800 text-white mt-auto">
      <div className="container mx-auto px-4 py-6">
        <div className="flex flex-col md:flex-row justify-between items-center">
          <div className="mb-4 md:mb-0">
            <p className="text-sm">
              © {new Date().getFullYear()} ConectaPG - Sistema de Ocorrências Urbanas
            </p>
            <p className="text-xs text-gray-400 mt-1">
              Prefeitura de Praia Grande - SP
            </p>
          </div>
          
          <div className="flex space-x-6 text-sm">
            <a href="#" className="hover:text-primary-400 transition-colors">
              Sobre
            </a>
            <a href="#" className="hover:text-primary-400 transition-colors">
              Ajuda
            </a>
            <a href="#" className="hover:text-primary-400 transition-colors">
              Contato
            </a>
          </div>
        </div>
      </div>
    </footer>
  )
}

export default Footer
