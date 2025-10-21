# Scripts de Inicialização do Banco de Dados

Este diretório contém os scripts SQL que são executados automaticamente quando o container PostgreSQL é iniciado pela primeira vez.

## 📁 Arquivos

### `01-init.sql`
Script principal que cria toda a estrutura do banco de dados TaskDay, incluindo:
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
docker exec taskday-postgres psql -U gabriel -d taskdaydb -c "\dt"
```

### Erro de sintaxe
```bash
# Testar script manualmente
docker exec -i taskday-postgres psql -U gabriel -d taskdaydb < init-db/01-init.sql
```

### Resetar banco
```bash
# Parar e remover volumes (CUIDADO: apaga todos os dados!)
docker-compose down -v
docker-compose up postgres -d
```
