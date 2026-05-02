# 🌡️ TechTerm — ThermVaccine

> **Plataforma Inteligente para Monitoramento e Otimização de Infraestruturas Sustentáveis**  
> Monitoramento Ativo e Predição de Riscos na Logística de Termolábeis

---

## 📋 Sobre o Projeto

O transporte inadequado de termolábeis resulta na perda de até **50% dos insumos globais** e **25% dos produtos biológicos no Brasil**. O **TechTerm** é uma plataforma desenvolvida para combater esse problema por meio do monitoramento contínuo da temperatura durante o transporte logístico de vacinas.

O sistema captura dados gerados por DataLoggers via nuvem, detecta variações de temperatura e recalcula automaticamente o tempo de vida útil restante do medicamento — alertando a equipe de qualidade sempre que necessário.

---

## 🎯 Objetivos

- Monitorar a temperatura das vacinas cadastradas no sistema em tempo real
- Calcular o tempo de vida útil inicial do medicamento
- Recalcular o tempo de vida restante em caso de variação de temperatura
- Alertar a equipe de qualidade para acionamento do protocolo de predição

---

## 👥 Atores do Sistema

| Ator | Responsabilidades |
|---|---|
| **Operador de Logística** | Cadastrar lotes, emitir comandas e vincular DataLoggers à carga |
| **Analista de Qualidade** | Gerenciar DataLoggers, monitorar temperaturas e gerar relatórios |

---

## 🗂️ Funcionalidades

### Operador de Logística
- Login e autenticação por cargo
- Cadastro de lote e quantidade
- Emissão de comanda de transporte
- Vinculação de DataLogger à carga

### Analista de Qualidade
- Gerenciamento de DataLoggers
- Monitoramento de temperaturas em tempo real
- Cálculo automático de vida útil restante
- Visualização e geração de relatórios
- Consulta de histórico de viagens

---

## 🏗️ Arquitetura do Projeto

```
thermvaccine/
├── src/
│   ├── main/
│   │   ├── java/com/thermvaccine/
│   │   │   ├── controller/       # Entrada de dados e interação com usuário
│   │   │   ├── model/            # Entidades do domínio (Vacina, Lote, Transporte...)
│   │   │   ├── repository/       # Acesso ao banco de dados
│   │   │   ├── service/          # Regras de negócio
│   │   │   └── Main.java
│   │   └── resources/
│   │       └── META-INF/
│   │           └── persistence.xml   # Configuração JPA/Hibernate
│   └── test/
│       └── java/
└── pom.xml
```

---

## 🗄️ Modelo de Dados

O sistema é composto pelas seguintes entidades:

| Entidade | Descrição |
|---|---|
| `Vac` | Armazena os dados das vacinas (nome, temperatura mín/máx) |
| `Lote` | Representa um pedido de vacina com quantidade e status |
| `Comanda` | Ordem de transporte emitida pelo operador |
| `Transporte` | Registro do veículo e dados da viagem |
| `Caixa` | Unidade física de transporte dentro do veículo |
| `Datalog` | DataLogger vinculado à carga |
| `Regi_Datalog` | Registros de temperatura capturados pelo DataLogger |
| `Usuarios` | Usuários do sistema com cargo definido |
| `Empresas` | Empresa à qual o usuário pertence |

---

## 🛠️ Tecnologias

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 17 | Linguagem principal |
| Maven | 3.x | Gerenciamento de dependências |
| Hibernate | 6.4.4 | Implementação JPA / ORM |
| PostgreSQL Driver | 42.7.3 | Conexão com banco de dados |
| Lombok | 1.18.32 | Redução de boilerplate |
| JUnit Jupiter | 5.10.2 | Testes unitários |
| Supabase | — | Banco de dados PostgreSQL em nuvem |

---

## ⚙️ Como Configurar

### Pré-requisitos
- Java 17+
- Maven 3.x
- Conta no [Supabase](https://supabase.com)

### Configuração do Banco de Dados

Edite o arquivo `src/main/resources/META-INF/persistence.xml` e preencha com suas credenciais do Supabase (disponíveis em *Project Settings → Database*):

```xml
<property name="jakarta.persistence.jdbc.url"      value="jdbc:postgresql://SEU_HOST.supabase.co:5432/postgres"/>
<property name="jakarta.persistence.jdbc.user"     value="SEU_USUARIO"/>
<property name="jakarta.persistence.jdbc.password" value="SUA_SENHA"/>
```

### Executando o Projeto

```bash
# Clonar o repositório
git clone https://github.com/seu-usuario/ThermVaccine_PI.git

# Entrar na pasta do projeto
cd ThermVaccine_PI/thermvaccine

# Compilar
mvn compile

# Executar
mvn exec:java -Dexec.mainClass="com.thermvaccine.Main"
```

---

## 👨‍💻 Equipe

| RA | Nome |
|---|---|
| 119221 | Debora Ayumi Tanowe Morel Aguiar |
| 118406 | João Victor de Lima |
| 118030 | Kayky Fernando Garcia |
| 119531 | Luis Felipe Penteado |
| 118954 | Luis Otávio Bonometti Santana |
| 118413 | Vinicius Gustavo Perez |

---

## 🏫 Instituição

**Fundação Hermínio Ometto — FHO**  
Sistemas de Informação — Projeto Interdisciplinar I / 3ºA / 2026
