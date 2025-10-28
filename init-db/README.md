# Scripts de Inicialização do Banco de Dados - Jooby

Este diretório contém os scripts SQL que são executados automaticamente quando o container PostgreSQL é iniciado pela primeira vez.

## 📁 Arquivos

### `01-init.sql`
Script principal que cria toda a estrutura do banco de dados Jooby, incluindo:
- ✅ Tipos ENUM personalizados
- ✅ Todas as tabelas principais
- ✅ Relacionamentos e constraints
- ✅ Índices para performance
- ✅ Triggers para timestamps automáticos
- ✅ Dados de exemplo para testes

## 🔄 Como Funciona

1. **Primeira execução**: O PostgreSQL executa automaticamente todos os scripts `.sql` na pasta `/docker-entrypoint-initdb.d/`
2. **Scripts numerados**: São executados em ordem alfabética (01-, 02-, etc.)
3. **Execução única**: Os scripts só são executados se o banco estiver vazio

## 🚀 Inicialização Automática

O banco é inicializado automaticamente quando você executa:

```bash
# Iniciar todo o ecossistema
./scripts/start-all.sh

# Ou apenas o banco
docker-compose up postgres -d
```

## 🛠️ Adicionando Novos Scripts

Para adicionar novos scripts de inicialização:

1. Crie um novo arquivo `.sql` com prefixo numérico:
   ```bash
   touch init-db/02-new-feature.sql
   ```

2. Escreva seu SQL no arquivo

3. Reinicie o container para aplicar:
   ```bash
   docker-compose down
   docker-compose up postgres -d
   ```

## ⚠️ Importante

- **Backup**: Sempre faça backup antes de modificar scripts
- **Ordem**: Use numeração sequencial para garantir ordem de execução
- **Testes**: Teste scripts em ambiente de desenvolvimento primeiro
- **Idempotência**: Scripts devem poder ser executados múltiplas vezes sem erro

## 🔧 Troubleshooting

### Script não executou
```bash
# Verificar logs do PostgreSQL
docker-compose logs postgres

# Verificar se o banco foi inicializado
docker exec jooby-postgres psql -U gabriel -d joobydb -c "\dt"
```

### Erro de sintaxe
```bash
# Testar script manualmente
docker exec -i jooby-postgres psql -U gabriel -d joobydb < init-db/01-init.sql
```

### Resetar banco
```bash
# Parar e remover volumes (CUIDADO: apaga todos os dados!)
docker-compose down -v
docker-compose up postgres -d
```

## 📊 Estrutura do Banco

O banco `joobydb` contém as seguintes tabelas principais:
- `contractor` - Dados dos freelancers
- `client` - Dados dos clientes
- `client_jobs` - Jobs publicados
- `client_job_applications` - Candidaturas
- `client_job_execution` - Execução dos jobs
- `address` - Endereços (unificado)
- `chat_rooms` - Salas de chat
- `messages` - Mensagens do chat
