# 🌐 ConectaPG

Sistema de reporte e acompanhamento de ocorrências urbanas desenvolvido para facilitar a comunicação entre **cidadãos** e **prefeitura** da cidade de **Praia Grande (SP)**.  
O objetivo é aumentar a **transparência**, a **agilidade na manutenção urbana** e o **engajamento social**, permitindo que qualquer cidadão registre problemas nas vias públicas (como buracos no asfalto), acompanhe o status do atendimento e receba notificações do andamento.

---

## ✨ Funcionalidades (MVP)

| Função | Descrição |
|-------|-----------|
| Registro de ocorrência | Envio de foto + descrição + endereço/geolocalização |
| Consulta de ocorrências | Cidadão acompanha o status em tempo real |
| Painel da prefeitura | Gestor visualiza, filtra e prioriza atendimentos |
| Atualização de status | Prefeitura avança etapas (em análise → execução → resolvido) |
| Notificações | Cidadão recebe alerta quando o status muda |

---

## 🧱 Arquitetura do Sistema

A solução foi modelada utilizando o **Modelo C4** com separação em múltiplos containers:

- **Frontend (React)** — Interface do cidadão e do gestor
- **Backend (Java 17 + Spring Boot 3)** — API REST, regras de negócio e integrações
- **Banco de Dados (PostgreSQL)** — Armazenamento relacional seguro
- **Storage S3/Compatível** — Armazenamento de imagens de forma eficiente
- **Swagger / OpenAPI** — Catálogo e teste da API

> Diagramas completos estão na pasta `/docs`.

---

## 📂 Estrutura do Repositório

