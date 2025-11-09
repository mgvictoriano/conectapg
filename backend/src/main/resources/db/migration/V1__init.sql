-- Criação da tabela de usuários
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    tipo VARCHAR(50) NOT NULL DEFAULT 'CIDADAO',
    ativo BOOLEAN NOT NULL DEFAULT true,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_tipo_usuario CHECK (tipo IN ('CIDADAO', 'FUNCIONARIO', 'ADMIN'))
);

-- Criação da tabela de ocorrências
CREATE TABLE ocorrencias (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT NOT NULL,
    localizacao VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'ABERTA',
    tipo VARCHAR(50) NOT NULL,
    usuario_id BIGINT NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT chk_status CHECK (status IN ('ABERTA', 'EM_ANDAMENTO', 'RESOLVIDA', 'FECHADA')),
    CONSTRAINT chk_tipo CHECK (tipo IN ('ILUMINACAO', 'BURACO', 'LIXO', 'VANDALISMO', 'OUTROS'))
);

-- Índices para melhorar performance
CREATE INDEX idx_ocorrencias_usuario_id ON ocorrencias(usuario_id);
CREATE INDEX idx_ocorrencias_status ON ocorrencias(status);
CREATE INDEX idx_ocorrencias_data_criacao ON ocorrencias(data_criacao DESC);
CREATE INDEX idx_usuarios_email ON usuarios(email);

-- Dados de exemplo (opcional)
INSERT INTO usuarios (nome, email, senha, tipo) VALUES 
('Admin Sistema', 'admin@conectapg.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN'),
('João Silva', 'joao@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'CIDADAO');

INSERT INTO ocorrencias (titulo, descricao, localizacao, tipo, usuario_id) VALUES
('Poste queimado', 'Poste de iluminação queimado na esquina', 'Rua das Flores, 123', 'ILUMINACAO', 2),
('Buraco na via', 'Grande buraco prejudicando o trânsito', 'Av. Principal, 456', 'BURACO', 2);
