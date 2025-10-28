// TaskDay Mobile - API Service
import axios, { AxiosInstance, AxiosResponse } from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
import {
  User,
  LoginRequest,
  LoginResponse,
  RegisterRequest,
  RegisterResponse,
  Job,
  ApiResponse
} from '../types/api';

class ApiService {
  private api: AxiosInstance;
  private baseURL: string;

  constructor() {
    // Configuração da URL base - ajuste conforme seu ambiente
    this.baseURL = __DEV__ 
      ? 'http://10.0.2.2:8080/api/v1'  // Android Emulator
      : 'https://your-production-url.com/api/v1';  // Produção
    
    this.api = axios.create({
      baseURL: this.baseURL,
      timeout: 10000,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    this.setupInterceptors();
  }

  private setupInterceptors() {
    // Request interceptor para adicionar token
    this.api.interceptors.request.use(
      async (config) => {
        const token = await AsyncStorage.getItem('auth_token');
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );

    // Response interceptor para tratar erros
    this.api.interceptors.response.use(
      (response) => response,
      async (error) => {
        if (error.response?.status === 401) {
          // Token expirado, limpar storage
          await AsyncStorage.removeItem('auth_token');
          await AsyncStorage.removeItem('user_data');
        }
        return Promise.reject(error);
      }
    );
  }

  // Autenticação
  async login(loginData: LoginRequest): Promise<LoginResponse> {
    const response = await this.api.post<LoginResponse>('/auth/login', loginData);
    
    // Salvar token e dados do usuário
    await AsyncStorage.setItem('auth_token', response.data.token);
    await AsyncStorage.setItem('user_data', JSON.stringify(response.data.user));
    
    return response.data;
  }

  async register(registerData: RegisterRequest): Promise<RegisterResponse> {
    const response = await this.api.post<RegisterResponse>('/auth/register', registerData);
    return response.data;
  }

  async logout(): Promise<void> {
    await AsyncStorage.removeItem('auth_token');
    await AsyncStorage.removeItem('user_data');
  }

  // Jobs
  async getJobs(): Promise<Job[]> {
    const response = await this.api.get<Job[]>('/jobs/active');
    return response.data;
  }

  async getJobById(id: number): Promise<Job> {
    const response = await this.api.get<Job>(`/jobs/${id}`);
    return response.data;
  }

  // Usuários
  async getCurrentUser(): Promise<User> {
    const response = await this.api.get<User>('/auth/me');
    return response.data;
  }

  async updateProfile(userData: Partial<User>): Promise<User> {
    const response = await this.api.put<User>('/auth/profile', userData);
    return response.data;
  }
}

export const apiService = new ApiService();

