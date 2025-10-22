# 📱 TaskDay Mobile - Guia Completo

## 🚀 **React Native com TypeScript**

### **Estrutura do Projeto Mobile:**
```
📁 mobile/
├── 📱 src/
│   ├── 📱 screens/          # Telas da aplicação
│   ├── 🔧 services/         # Integração com API
│   ├── 📋 types/            # Tipos TypeScript
│   ├── 🧩 components/       # Componentes reutilizáveis
│   ├── 🎣 hooks/            # Custom hooks
│   └── 🛠️ utils/            # Utilitários
├── 📱 android/              # Código Android nativo
├── 📱 ios/                  # Código iOS nativo
├── 📱 package.json          # Dependências
└── 📱 tsconfig.json         # Configuração TypeScript
```

---

## 🛠️ **Setup e Configuração**

### **1. 📋 Pré-requisitos**

#### **Sistema Operacional:**
- **Linux/Windows/macOS** - Para desenvolvimento
- **Node.js 18+** - Runtime JavaScript
- **NPM/Yarn** - Gerenciador de pacotes

#### **Para Android:**
- **Android Studio** - IDE e SDK
- **Java 17** - JDK
- **Android SDK** - API Level 33+
- **Emulador Android** - Para testes

#### **Para iOS (macOS apenas):**
- **Xcode 14+** - IDE Apple
- **CocoaPods** - Gerenciador de dependências iOS
- **Simulador iOS** - Para testes

### **2. 🚀 Setup Inicial**

```bash
# 1. Setup completo do ambiente
./scripts/mobile-dev.sh setup

# 2. Instalar dependências
cd mobile
npm install

# 3. Configurar ambiente
cp .env.example .env
```

### **3. 🔧 Configuração do Ambiente**

#### **Arquivo `.env`:**
```bash
# TaskDay Mobile - Configurações
API_BASE_URL=http://10.0.2.2:8080/api/v1  # Android Emulator
# API_BASE_URL=http://localhost:8080/api/v1  # iOS Simulator
API_TIMEOUT=10000
DEBUG_MODE=true
```

---

## 🏗️ **Desenvolvimento**

### **1. 🌿 Fluxo de Desenvolvimento**

#### **Criar Nova Feature:**
```bash
# 1. Criar branch para feature mobile
./scripts/create-feature.sh mobile-user-profile

# 2. Desenvolver no mobile
cd mobile
# ... código ...

# 3. Testar
./scripts/mobile-dev.sh test

# 4. Commit e push
git add .
git commit -m "feat: implementa perfil de usuário mobile"
git push origin feature/mobile-user-profile
```

#### **Correção de Bug:**
```bash
# 1. Criar branch para bugfix
./scripts/create-bugfix.sh mobile-login-error

# 2. Corrigir
# ... código ...

# 3. Testar
./scripts/mobile-dev.sh test

# 4. Merge
./scripts/merge-to-develop.sh bugfix/mobile-login-error
```

### **2. 🧪 Testes**

#### **Testes Unitários:**
```bash
# Executar testes
./scripts/mobile-dev.sh test

# Testes específicos
cd mobile
npm test -- --testNamePattern="LoginScreen"
```

#### **Testes de Integração:**
```bash
# Testes com backend
cd mobile
npm run test:integration
```

#### **Testes E2E:**
```bash
# Testes end-to-end
cd mobile
npm run test:e2e
```

### **3. 📱 Execução**

#### **Android:**
```bash
# Emulador
./scripts/mobile-dev.sh android

# Dispositivo físico
adb devices
./scripts/mobile-dev.sh android
```

#### **iOS:**
```bash
# Simulador
./scripts/mobile-dev.sh ios

# Dispositivo físico
./scripts/mobile-dev.sh ios --device
```

---

## 🔗 **Integração com Backend**

### **1. 📡 API Service**

#### **Configuração:**
```typescript
// src/services/api.ts
class ApiService {
  private baseURL: string;
  
  constructor() {
    this.baseURL = __DEV__ 
      ? 'http://10.0.2.2:8080/api/v1'  // Android
      : 'https://api.taskday.com/v1';   // Produção
  }
}
```

#### **Autenticação:**
```typescript
// Login
const response = await apiService.login({
  email: 'user@example.com',
  password: 'password123'
});

// Token automático
apiService.setAuthToken(response.token);
```

#### **Requisições:**
```typescript
// GET
const jobs = await apiService.getJobs();

// POST
const newJob = await apiService.createJob({
  title: 'Desenvolvedor React Native',
  description: 'Desenvolvimento de app mobile',
  pricePerHour: 50
});

// PUT
const updatedJob = await apiService.updateJob(jobId, {
  title: 'Novo título'
});
```

### **2. 🔄 Sincronização de Dados**

#### **Cache Local:**
```typescript
// AsyncStorage para cache
import AsyncStorage from '@react-native-async-storage/async-storage';

// Salvar dados
await AsyncStorage.setItem('user_data', JSON.stringify(user));

// Recuperar dados
const userData = await AsyncStorage.getItem('user_data');
const user = JSON.parse(userData);
```

#### **Offline Support:**
```typescript
// Verificar conectividade
import NetInfo from '@react-native-community/netinfo';

const isConnected = await NetInfo.fetch().then(state => state.isConnected);

if (!isConnected) {
  // Usar dados em cache
  const cachedJobs = await AsyncStorage.getItem('jobs_cache');
  return JSON.parse(cachedJobs);
}
```

---

## 🎨 **Interface e UX**

### **1. 🧩 Componentes**

#### **Componente Base:**
```typescript
// src/components/Button.tsx
import React from 'react';
import { TouchableOpacity, Text, StyleSheet } from 'react-native';

interface ButtonProps {
  title: string;
  onPress: () => void;
  variant?: 'primary' | 'secondary';
  disabled?: boolean;
}

const Button: React.FC<ButtonProps> = ({ 
  title, 
  onPress, 
  variant = 'primary',
  disabled = false 
}) => {
  return (
    <TouchableOpacity 
      style={[styles.button, styles[variant], disabled && styles.disabled]}
      onPress={onPress}
      disabled={disabled}
    >
      <Text style={[styles.text, styles[`${variant}Text`]]}>
        {title}
      </Text>
    </TouchableOpacity>
  );
};
```

#### **Hook Personalizado:**
```typescript
// src/hooks/useApi.ts
import { useState, useEffect } from 'react';
import { apiService } from '../services/api';

export const useApi = <T>(apiCall: () => Promise<T>) => {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const result = await apiCall();
        setData(result);
      } catch (err: any) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  return { data, loading, error, refetch: fetchData };
};
```

### **2. 🎨 Estilização**

#### **Tema Global:**
```typescript
// src/styles/theme.ts
export const theme = {
  colors: {
    primary: '#3498db',
    secondary: '#2c3e50',
    success: '#27ae60',
    warning: '#f39c12',
    error: '#e74c3c',
    background: '#f5f5f5',
    surface: '#ffffff',
    text: '#2c3e50',
    textSecondary: '#7f8c8d',
  },
  spacing: {
    xs: 4,
    sm: 8,
    md: 16,
    lg: 24,
    xl: 32,
  },
  typography: {
    h1: { fontSize: 32, fontWeight: 'bold' },
    h2: { fontSize: 24, fontWeight: 'bold' },
    body: { fontSize: 16 },
    caption: { fontSize: 12 },
  },
};
```

#### **Estilos Responsivos:**
```typescript
// src/styles/responsive.ts
import { Dimensions } from 'react-native';

const { width, height } = Dimensions.get('window');

export const isTablet = width >= 768;
export const isSmallScreen = width < 375;

export const responsive = {
  fontSize: (size: number) => isSmallScreen ? size * 0.9 : size,
  padding: (size: number) => isTablet ? size * 1.5 : size,
};
```

---

## 🚀 **Build e Deploy**

### **1. 📦 Build para Desenvolvimento**

```bash
# Build Android
cd mobile
npx react-native run-android --mode=debug

# Build iOS
npx react-native run-ios --mode=debug
```

### **2. 🏭 Build para Produção**

#### **Android:**
```bash
# Gerar APK
cd mobile/android
./gradlew assembleRelease

# Gerar AAB (Google Play)
./gradlew bundleRelease
```

#### **iOS:**
```bash
# Build para App Store
cd mobile/ios
xcodebuild -workspace TaskDayMobile.xcworkspace -scheme TaskDayMobile -configuration Release
```

### **3. 📱 Deploy**

#### **Google Play Store:**
```bash
# 1. Build AAB
./gradlew bundleRelease

# 2. Upload via Google Play Console
# 3. Configurar release notes
# 4. Publicar
```

#### **Apple App Store:**
```bash
# 1. Build via Xcode
# 2. Upload via Xcode Organizer
# 3. Configurar no App Store Connect
# 4. Submeter para review
```

---

## 🔧 **Scripts Disponíveis**

### **📱 Desenvolvimento Mobile:**
```bash
# Setup inicial
./scripts/mobile-dev.sh setup

# Iniciar ambiente
./scripts/mobile-dev.sh start

# Parar ambiente
./scripts/mobile-dev.sh stop

# Executar no Android
./scripts/mobile-dev.sh android

# Executar no iOS
./scripts/mobile-dev.sh ios

# Executar testes
./scripts/mobile-dev.sh test

# Build para produção
./scripts/mobile-dev.sh build

# Limpeza
./scripts/mobile-dev.sh clean
```

### **🌿 Gerenciamento de Branches:**
```bash
# Nova feature mobile
./scripts/create-feature.sh mobile-new-feature

# Correção de bug mobile
./scripts/create-bugfix.sh mobile-bug-fix

# Merge seguro
./scripts/merge-to-develop.sh feature/mobile-new-feature
```

### **🐳 Docker:**
```bash
# Ambiente completo
docker-compose -f docker-compose.mobile.yml up -d

# Apenas backend
docker-compose -f docker-compose.mobile.yml up -d backend postgres

# Logs
docker-compose -f docker-compose.mobile.yml logs -f
```

---

## 🐛 **Troubleshooting**

### **1. ❌ Problemas Comuns**

#### **Metro bundler não inicia:**
```bash
# Limpar cache
cd mobile
npx react-native clean
npm start --reset-cache
```

#### **Android build falha:**
```bash
# Limpar gradle
cd mobile/android
./gradlew clean

# Reinstalar dependências
cd ..
rm -rf node_modules
npm install
```

#### **iOS build falha:**
```bash
# Limpar pods
cd mobile/ios
rm -rf Pods
pod install

# Limpar Xcode
rm -rf ~/Library/Developer/Xcode/DerivedData
```

### **2. 🔍 Debug**

#### **Logs do React Native:**
```bash
# Android
npx react-native log-android

# iOS
npx react-native log-ios
```

#### **Debug do JavaScript:**
```bash
# Abrir debugger
# Shake device -> Debug -> Open Debugger
```

#### **Inspect Network:**
```bash
# Flipper para debug de rede
# https://fbflipper.com/
```

---

## 📚 **Recursos Adicionais**

### **📖 Documentação:**
- [React Native](https://reactnative.dev/)
- [TypeScript](https://www.typescriptlang.org/)
- [React Navigation](https://reactnavigation.org/)
- [AsyncStorage](https://react-native-async-storage.github.io/async-storage/)

### **🛠️ Ferramentas:**
- [Flipper](https://fbflipper.com/) - Debug
- [React Native Debugger](https://github.com/jhen0409/react-native-debugger)
- [Reactotron](https://github.com/infinitered/reactotron)

### **📱 Testes:**
- [Jest](https://jestjs.io/) - Testes unitários
- [Detox](https://github.com/wix/Detox) - Testes E2E
- [Maestro](https://maestro.mobile.dev/) - Testes de UI

---

**TaskDay Mobile** - Desenvolvimento nativo com TypeScript! 📱🚀
