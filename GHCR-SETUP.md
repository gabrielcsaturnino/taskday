# 🐳 GitHub Container Registry (GHCR) - Setup Completo

## ❌ **Problema Atual**
```
ERROR: failed to build: denied: installation not allowed to Create organization package
```

## 🔧 **Soluções**

### **Solução 1: Usar Usuário Pessoal (Recomendado)**

#### **1. Configurar GitHub Actions**

O arquivo `.github/workflows/ci-cd.yml` já foi corrigido para usar:
```yaml
env:
  REGISTRY: ghcr.io
  IMAGE_NAME: gabrielcsaturnino/taskday  # Seu usuário pessoal
```

#### **2. Configurar Permissões**

Adicione as permissões necessárias:
```yaml
permissions:
  contents: read
  packages: write
```

#### **3. Testar Localmente**

```bash
# Execute o script de teste
./test-docker-push.sh
```

### **Solução 2: Configurar Organização (Avançado)**

#### **1. Criar Organização no GitHub**
1. Vá para: https://github.com/organizations/new
2. Crie uma organização (ex: `taskday-org`)
3. Convide seu usuário como owner

#### **2. Configurar Permissões da Organização**
1. Vá para: Settings → Actions → General
2. Em "Workflow permissions", selecione "Read and write permissions"
3. Marque "Allow GitHub Actions to create and approve pull requests"

#### **3. Configurar Package Permissions**
1. Vá para: Settings → Packages
2. Selecione "Allow all actions to use packages"
3. Ou configure permissões específicas

### **Solução 3: Usar Docker Hub (Alternativa)**

#### **1. Configurar Docker Hub**
```yaml
env:
  REGISTRY: docker.io
  IMAGE_NAME: gabrielcsaturnino/taskday
```

#### **2. Adicionar Secrets**
1. Vá para: Settings → Secrets and variables → Actions
2. Adicione:
   - `DOCKER_USERNAME`: seu usuário do Docker Hub
   - `DOCKER_PASSWORD`: sua senha do Docker Hub

---

## 🚀 **Teste Rápido**

### **1. Testar Build Local**
```bash
# Build da imagem
docker build -t ghcr.io/gabrielcsaturnino/taskday:latest .

# Testar a imagem
docker run -p 8080:8080 ghcr.io/gabrielcsaturnino/taskday:latest
```

### **2. Testar Push Local**
```bash
# Login no GHCR
echo "SEU_GITHUB_TOKEN" | docker login ghcr.io -u gabrielcsaturnino --password-stdin

# Push da imagem
docker push ghcr.io/gabrielcsaturnino/taskday:latest
```

### **3. Verificar no GitHub**
1. Vá para: https://github.com/gabrielcsaturnino/taskday/pkgs
2. Verifique se a imagem aparece

---

## 🔑 **Configuração de Token GitHub**

### **1. Criar Personal Access Token**
1. Vá para: https://github.com/settings/tokens
2. Clique em "Generate new token (classic)"
3. Selecione scopes:
   - ✅ `write:packages`
   - ✅ `read:packages`
   - ✅ `delete:packages`

### **2. Configurar no GitHub Actions**
O token `GITHUB_TOKEN` é automaticamente fornecido pelo GitHub Actions.

---

## 📋 **Checklist de Verificação**

### **✅ Configuração Básica**
- [ ] Repositório público ou privado com acesso
- [ ] GitHub Actions habilitado
- [ ] Dockerfile funcionando
- [ ] Build local funcionando

### **✅ Permissões**
- [ ] `contents: read` configurado
- [ ] `packages: write` configurado
- [ ] Token com permissões corretas

### **✅ Testes**
- [ ] Build local funcionando
- [ ] Push local funcionando
- [ ] GitHub Actions funcionando

---

## 🐛 **Troubleshooting**

### **Erro: "denied: installation not allowed"**
**Causa:** Tentativa de criar pacote de organização sem permissão
**Solução:** Use usuário pessoal ou configure permissões da organização

### **Erro: "authentication required"**
**Causa:** Não está logado no GHCR
**Solução:** Execute `docker login ghcr.io`

### **Erro: "permission denied"**
**Causa:** Token sem permissões suficientes
**Solução:** Crie novo token com `write:packages`

### **Erro: "repository not found"**
**Causa:** Nome da imagem incorreto
**Solução:** Verifique se `IMAGE_NAME` está correto

---

## 🎯 **Próximos Passos**

1. **Teste local:** Execute `./test-docker-push.sh`
2. **Commit e push:** Faça commit das alterações
3. **Verifique Actions:** Vá para a aba Actions no GitHub
4. **Deploy:** Configure deploy automático

---

## 📚 **Recursos Úteis**

- [GitHub Container Registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-container-registry)
- [Docker Login](https://docs.docker.com/engine/reference/commandline/login/)
- [GitHub Actions](https://docs.github.com/en/actions)

---

**TaskDay** - Deploy automatizado com sucesso! 🚀🐳
