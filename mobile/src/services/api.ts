// TaskDay Mobile - API Service
// Serviço para integração com o backend Spring Boot

import axios, { AxiosInstance, AxiosResponse } from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
import {
  User,
  Client,
  Contractor,
  Job,
  JobApplication,
  ChatRoom,
  Message,
  LoginRequest,
  LoginResponse,
  RegisterRequest,
  JobSearchParams,
  ContractorSearchParams,
  ApiResponse,
  PaginatedResponse
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
          // Token expirado, limpar storage e redirecionar para login
          await AsyncStorage.removeItem('auth_token');
          await AsyncStorage.removeItem('user_data');
          // Aqui você pode implementar navegação para tela de login
        }
        return Promise.reject(error);
      }
    );
  }

  // Authentication Methods
  async login(credentials: LoginRequest): Promise<LoginResponse> {
    const response: AxiosResponse<LoginResponse> = await this.api.post('/auth/login', credentials);
    
    // Salvar token e dados do usuário
    await AsyncStorage.setItem('auth_token', response.data.token);
    await AsyncStorage.setItem('user_data', JSON.stringify(response.data.user));
    
    return response.data;
  }

  async register(userData: RegisterRequest): Promise<ApiResponse<User>> {
    const response: AxiosResponse<ApiResponse<User>> = await this.api.post('/auth/register', userData);
    return response.data;
  }

  async logout(): Promise<void> {
    await AsyncStorage.removeItem('auth_token');
    await AsyncStorage.removeItem('user_data');
  }

  // User Methods
  async getCurrentUser(): Promise<User> {
    const response: AxiosResponse<User> = await this.api.get('/auth/me');
    return response.data;
  }

  async updateProfile(userData: Partial<User>): Promise<User> {
    const response: AxiosResponse<User> = await this.api.put('/auth/profile', userData);
    return response.data;
  }

  async changePassword(currentPassword: string, newPassword: string): Promise<void> {
    await this.api.put('/auth/password', {
      currentPassword,
      newPassword
    });
  }

  // Client Methods
  async getClient(id: number): Promise<Client> {
    const response: AxiosResponse<Client> = await this.api.get(`/clients/${id}`);
    return response.data;
  }

  async updateClient(clientData: Partial<Client>): Promise<Client> {
    const response: AxiosResponse<Client> = await this.api.put('/clients/profile', clientData);
    return response.data;
  }

  // Contractor Methods
  async getContractor(id: number): Promise<Contractor> {
    const response: AxiosResponse<Contractor> = await this.api.get(`/contractors/${id}`);
    return response.data;
  }

  async searchContractors(params: ContractorSearchParams): Promise<PaginatedResponse<Contractor>> {
    const response: AxiosResponse<PaginatedResponse<Contractor>> = await this.api.get('/contractors/search', { params });
    return response.data;
  }

  async updateContractor(contractorData: Partial<Contractor>): Promise<Contractor> {
    const response: AxiosResponse<Contractor> = await this.api.put('/contractors/profile', contractorData);
    return response.data;
  }

  // Job Methods
  async getJobs(): Promise<Job[]> {
    const response: AxiosResponse<Job[]> = await this.api.get('/jobs');
    return response.data;
  }

  async getJob(id: number): Promise<Job> {
    const response: AxiosResponse<Job> = await this.api.get(`/jobs/${id}`);
    return response.data;
  }

  async searchJobs(params: JobSearchParams): Promise<PaginatedResponse<Job>> {
    const response: AxiosResponse<PaginatedResponse<Job>> = await this.api.get('/jobs/search', { params });
    return response.data;
  }

  async createJob(jobData: Partial<Job>): Promise<Job> {
    const response: AxiosResponse<Job> = await this.api.post('/jobs', jobData);
    return response.data;
  }

  async updateJob(id: number, jobData: Partial<Job>): Promise<Job> {
    const response: AxiosResponse<Job> = await this.api.put(`/jobs/${id}`, jobData);
    return response.data;
  }

  async deleteJob(id: number): Promise<void> {
    await this.api.delete(`/jobs/${id}`);
  }

  async closeJob(id: number): Promise<Job> {
    const response: AxiosResponse<Job> = await this.api.put(`/jobs/${id}/close`);
    return response.data;
  }

  // Job Application Methods
  async applyToJob(jobId: number): Promise<JobApplication> {
    const response: AxiosResponse<JobApplication> = await this.api.post(`/job-applications/apply/${jobId}`);
    return response.data;
  }

  async getJobApplication(id: number): Promise<JobApplication> {
    const response: AxiosResponse<JobApplication> = await this.api.get(`/job-applications/${id}`);
    return response.data;
  }

  async updateApplicationStatus(id: number, status: string): Promise<JobApplication> {
    const response: AxiosResponse<JobApplication> = await this.api.put(`/job-applications/${id}/status`, { status });
    return response.data;
  }

  // Chat Methods
  async getChatRooms(): Promise<ChatRoom[]> {
    const response: AxiosResponse<ChatRoom[]> = await this.api.get('/chat-rooms/my-chats');
    return response.data;
  }

  async getChatRoom(id: number): Promise<ChatRoom> {
    const response: AxiosResponse<ChatRoom> = await this.api.get(`/chat-rooms/${id}`);
    return response.data;
  }

  async getMessages(chatRoomId: number): Promise<Message[]> {
    const response: AxiosResponse<Message[]> = await this.api.get(`/chat-rooms/${chatRoomId}/messages`);
    return response.data;
  }

  async sendMessage(chatRoomId: number, content: string, type: string = 'TEXT'): Promise<Message> {
    const response: AxiosResponse<Message> = await this.api.post('/chat-rooms/messages', {
      chatRoomId,
      content,
      type
    });
    return response.data;
  }

  // File Upload Methods
  async uploadFile(file: any): Promise<string> {
    const formData = new FormData();
    formData.append('file', file);
    
    const response: AxiosResponse<{ filename: string }> = await this.api.post('/files/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    
    return response.data.filename;
  }

  // Health Check
  async healthCheck(): Promise<boolean> {
    try {
      const response = await this.api.get('/health');
      return response.status === 200;
    } catch {
      return false;
    }
  }
}

// Export singleton instance
export const apiService = new ApiService();
export default apiService;
