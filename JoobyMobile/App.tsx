// Jooby Mobile - Main App Component
import React, { useState, useEffect } from 'react';
import { View, StyleSheet, ActivityIndicator } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { User } from './src/types/api';
import LoginScreen from './src/screens/LoginScreen';
import RegisterScreen from './src/screens/RegisterScreen';
import HomeScreen from './src/screens/HomeScreen';

type Screen = 'login' | 'register' | 'home';

const App: React.FC = () => {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);
  const [currentScreen, setCurrentScreen] = useState<Screen>('login');

  useEffect(() => {
    checkAuthStatus();
  }, []);

  const checkAuthStatus = async () => {
    try {
      const userData = await AsyncStorage.getItem('user_data');
      if (userData) {
        setUser(JSON.parse(userData));
        setCurrentScreen('home');
      }
    } catch (error) {
      console.error('Erro ao verificar status de autenticação:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleLoginSuccess = (userData: User) => {
    setUser(userData);
    setCurrentScreen('home');
  };

  const handleRegisterSuccess = (userData: User) => {
    setUser(userData);
    setCurrentScreen('home');
  };

  const handleLogout = async () => {
    try {
      await AsyncStorage.removeItem('user_data');
      await AsyncStorage.removeItem('auth_token');
      setUser(null);
      setCurrentScreen('login');
    } catch (error) {
      console.error('Erro ao fazer logout:', error);
    }
  };

  const navigateToRegister = () => {
    setCurrentScreen('register');
  };

  const navigateToLogin = () => {
    setCurrentScreen('login');
  };

  if (loading) {
    return (
      <View style={styles.loadingContainer}>
        <ActivityIndicator size="large" color="#3498db" />
      </View>
    );
  }

  return (
    <View style={styles.container}>
      {user ? (
        <HomeScreen user={user} onLogout={handleLogout} />
      ) : currentScreen === 'login' ? (
        <LoginScreen 
          onLoginSuccess={handleLoginSuccess} 
          onNavigateToRegister={navigateToRegister}
        />
      ) : (
        <RegisterScreen 
          onRegisterSuccess={handleRegisterSuccess}
          onBackToLogin={navigateToLogin}
        />
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#f5f5f5',
  },
});

export default App;