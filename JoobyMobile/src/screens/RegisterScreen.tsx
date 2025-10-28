// TaskDay Mobile - Register Screen
import React, { useState } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  Alert,
  ActivityIndicator,
  KeyboardAvoidingView,
  Platform,
  ScrollView,
} from 'react-native';
import { RegisterRequest } from '../types/api';
import { apiService } from '../services/api';

interface RegisterScreenProps {
  onRegisterSuccess: (user: any) => void;
  onBackToLogin: () => void;
}

const RegisterScreen: React.FC<RegisterScreenProps> = ({ onRegisterSuccess, onBackToLogin }) => {
  const [formData, setFormData] = useState<RegisterRequest>({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    password: '',
    userType: 'CLIENT'
  });
  const [confirmPassword, setConfirmPassword] = useState('');
  const [loading, setLoading] = useState(false);

  const handleInputChange = (field: keyof RegisterRequest, value: string) => {
    setFormData(prev => ({
      ...prev,
      [field]: value
    }));
  };

  const validateForm = (): boolean => {
    if (!formData.firstName.trim()) {
      Alert.alert('Erro', 'Nome é obrigatório');
      return false;
    }
    if (!formData.lastName.trim()) {
      Alert.alert('Erro', 'Sobrenome é obrigatório');
      return false;
    }
    if (!formData.email.trim()) {
      Alert.alert('Erro', 'Email é obrigatório');
      return false;
    }
    if (!formData.phone.trim()) {
      Alert.alert('Erro', 'Telefone é obrigatório');
      return false;
    }
    if (!formData.password.trim()) {
      Alert.alert('Erro', 'Senha é obrigatória');
      return false;
    }
    if (formData.password.length < 6) {
      Alert.alert('Erro', 'Senha deve ter pelo menos 6 caracteres');
      return false;
    }
    if (formData.password !== confirmPassword) {
      Alert.alert('Erro', 'Senhas não coincidem');
      return false;
    }
    return true;
  };

  const handleRegister = async () => {
    if (!validateForm()) return;

    setLoading(true);
    try {
      const response = await apiService.register(formData);
      Alert.alert('Sucesso', 'Conta criada com sucesso!', [
        { text: 'OK', onPress: () => onRegisterSuccess(response.user) }
      ]);
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || 'Erro ao criar conta';
      Alert.alert('Erro', errorMessage);
    } finally {
      setLoading(false);
    }
  };

  return (
    <KeyboardAvoidingView 
      style={styles.container} 
      behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
    >
      <ScrollView contentContainerStyle={styles.scrollContainer}>
        <View style={styles.content}>
          <Text style={styles.title}>Criar Conta</Text>
          <Text style={styles.subtitle}>Junte-se à comunidade TaskDay</Text>
          
          <View style={styles.form}>
            <View style={styles.nameRow}>
              <TextInput
                style={[styles.input, styles.halfInput]}
                placeholder="Nome"
                value={formData.firstName}
                onChangeText={(text) => handleInputChange('firstName', text)}
                autoCapitalize="words"
                autoCorrect={false}
              />
              <TextInput
                style={[styles.input, styles.halfInput]}
                placeholder="Sobrenome"
                value={formData.lastName}
                onChangeText={(text) => handleInputChange('lastName', text)}
                autoCapitalize="words"
                autoCorrect={false}
              />
            </View>
            
            <TextInput
              style={styles.input}
              placeholder="Email"
              value={formData.email}
              onChangeText={(text) => handleInputChange('email', text)}
              keyboardType="email-address"
              autoCapitalize="none"
              autoCorrect={false}
            />
            
            <TextInput
              style={styles.input}
              placeholder="Telefone"
              value={formData.phone}
              onChangeText={(text) => handleInputChange('phone', text)}
              keyboardType="phone-pad"
            />
            
            <View style={styles.userTypeContainer}>
              <Text style={styles.userTypeLabel}>Tipo de usuário:</Text>
              <View style={styles.userTypeButtons}>
                <TouchableOpacity
                  style={[
                    styles.userTypeButton,
                    formData.userType === 'CLIENT' && styles.userTypeButtonActive
                  ]}
                  onPress={() => handleInputChange('userType', 'CLIENT')}
                >
                  <Text style={[
                    styles.userTypeButtonText,
                    formData.userType === 'CLIENT' && styles.userTypeButtonTextActive
                  ]}>
                    Cliente
                  </Text>
                </TouchableOpacity>
                <TouchableOpacity
                  style={[
                    styles.userTypeButton,
                    formData.userType === 'CONTRACTOR' && styles.userTypeButtonActive
                  ]}
                  onPress={() => handleInputChange('userType', 'CONTRACTOR')}
                >
                  <Text style={[
                    styles.userTypeButtonText,
                    formData.userType === 'CONTRACTOR' && styles.userTypeButtonTextActive
                  ]}>
                    Prestador
                  </Text>
                </TouchableOpacity>
              </View>
            </View>
            
            <TextInput
              style={styles.input}
              placeholder="Senha"
              value={formData.password}
              onChangeText={(text) => handleInputChange('password', text)}
              secureTextEntry
              autoCapitalize="none"
            />
            
            <TextInput
              style={styles.input}
              placeholder="Confirmar senha"
              value={confirmPassword}
              onChangeText={setConfirmPassword}
              secureTextEntry
              autoCapitalize="none"
            />
            
            <TouchableOpacity 
              style={[styles.button, loading && styles.buttonDisabled]}
              onPress={handleRegister}
              disabled={loading}
            >
              {loading ? (
                <ActivityIndicator color="#fff" />
              ) : (
                <Text style={styles.buttonText}>Criar Conta</Text>
              )}
            </TouchableOpacity>
          </View>
          
          <TouchableOpacity style={styles.linkButton} onPress={onBackToLogin}>
            <Text style={styles.linkText}>Já tem conta? Faça login</Text>
          </TouchableOpacity>
        </View>
      </ScrollView>
    </KeyboardAvoidingView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  scrollContainer: {
    flexGrow: 1,
  },
  content: {
    flex: 1,
    justifyContent: 'center',
    paddingHorizontal: 20,
    paddingVertical: 40,
  },
  title: {
    fontSize: 32,
    fontWeight: 'bold',
    textAlign: 'center',
    color: '#2c3e50',
    marginBottom: 8,
  },
  subtitle: {
    fontSize: 16,
    textAlign: 'center',
    color: '#7f8c8d',
    marginBottom: 40,
  },
  form: {
    marginBottom: 20,
  },
  nameRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 16,
  },
  halfInput: {
    width: '48%',
  },
  input: {
    backgroundColor: '#fff',
    borderRadius: 8,
    paddingHorizontal: 16,
    paddingVertical: 12,
    marginBottom: 16,
    fontSize: 16,
    borderWidth: 1,
    borderColor: '#e0e0e0',
  },
  userTypeContainer: {
    marginBottom: 16,
  },
  userTypeLabel: {
    fontSize: 16,
    color: '#2c3e50',
    marginBottom: 8,
    fontWeight: '500',
  },
  userTypeButtons: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  userTypeButton: {
    flex: 1,
    paddingVertical: 12,
    paddingHorizontal: 16,
    borderRadius: 8,
    borderWidth: 1,
    borderColor: '#e0e0e0',
    backgroundColor: '#fff',
    marginHorizontal: 4,
    alignItems: 'center',
  },
  userTypeButtonActive: {
    backgroundColor: '#3498db',
    borderColor: '#3498db',
  },
  userTypeButtonText: {
    fontSize: 16,
    color: '#7f8c8d',
    fontWeight: '500',
  },
  userTypeButtonTextActive: {
    color: '#fff',
  },
  button: {
    backgroundColor: '#3498db',
    borderRadius: 8,
    paddingVertical: 16,
    alignItems: 'center',
    marginTop: 8,
  },
  buttonDisabled: {
    backgroundColor: '#bdc3c7',
  },
  buttonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
  linkButton: {
    alignItems: 'center',
    marginTop: 20,
  },
  linkText: {
    color: '#3498db',
    fontSize: 16,
  },
});

export default RegisterScreen;

