import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { api } from '../services/api';
import { userService } from '../services/userService';

interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  type: 'client' | 'contractor';
  statusAccount: boolean;
  createdAt: string;
  updatedAt: string;
}

interface AuthContextType {
  user: User | null;
  login: (email: string, password: string) => Promise<void>;
  register: (userData: any) => Promise<void>;
  logout: () => void;
  loading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('auth_token');
    if (token) {
      // Verificar se o token ainda é válido
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      // Aqui você pode fazer uma chamada para verificar o token
      setLoading(false);
    } else {
      setLoading(false);
    }
  }, []);

  const login = async (email: string, password: string) => {
    try {
      const response = await api.post('/authenticate', { email, password });
      const token = response.data;
      
      localStorage.setItem('auth_token', token);
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      
      // Buscar dados do usuário
      const userData = await userService.getProfile();
      setUser(userData);
    } catch (error) {
      console.error('Erro no login:', error);
      throw error;
    }
  };

  const register = async (userData: any) => {
    try {
      // Determinar o endpoint baseado no tipo de usuário
      const endpoint = userData.type === 'contractor' ? '/api/v1/contractors' : '/api/v1/clients';
      
      await api.post(endpoint, userData);
      // Após registro, fazer login automaticamente
      await login(userData.email, userData.password);
    } catch (error) {
      console.error('Erro no registro:', error);
      throw error;
    }
  };

  const logout = () => {
    localStorage.removeItem('auth_token');
    delete api.defaults.headers.common['Authorization'];
    setUser(null);
  };

  const value = {
    user,
    login,
    register,
    logout,
    loading
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};
