import axios from 'axios';
import { API_BASE_URL } from '../config/api';
import humps from 'humps'; // Importe o humps

export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  // Adicione transformadores aqui
  transformResponse: [
    ...(axios.defaults.transformResponse as any || []),
    (data) => humps.camelizeKeys(data), // Converte respostas snake_case para camelCase
  ],
  transformRequest: [
    (data) => humps.decamelizeKeys(data), // Converte requisições camelCase para snake_case
    ...(axios.defaults.transformRequest as any || []),
  ],
});

// Interceptor para adicionar token automaticamente
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('auth_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Interceptor para lidar com respostas de erro
api.interceptors.response.use(
  (response) => response,
  (error) => {
    // Cameliza o erro também, se houver dados
    if (error.response?.data) {
      error.response.data = humps.camelizeKeys(error.response.data);
    }
    
    if (error.response?.status === 401) {
      localStorage.removeItem('auth_token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;