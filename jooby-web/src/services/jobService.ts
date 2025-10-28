import { api } from './api';

export interface Job {
  id: number;
  title: string;
  description: string;
  pricePerHour: number;
  jobStatus: 'active' | 'inactive';
  client: {
    id: number;
    firstName: string;
    lastName: string;
  };
  address?: {
    street: string;
    number: string;
    neighborhood: string;
    city: string;
    state: string;
    zipCode: string;
  };
  createdAt: string;
  updatedAt: string;
}

export interface JobSearchParams {
  title?: string;
  location?: string;
  minPrice?: number;
  maxPrice?: number;
  status?: string;
}

export const jobService = {
  // Buscar todos os jobs
  async getAllJobs(): Promise<Job[]> {
    const response = await api.get('/api/v1/jobs/active');
    return response.data;
  },

  // Buscar job por ID
  async getJobById(id: number): Promise<Job> {
    const response = await api.get(`/api/v1/jobs/${id}`);
    return response.data;
  },

  // Buscar jobs com filtros
  async searchJobs(params: JobSearchParams): Promise<Job[]> {
    const response = await api.get('/api/v1/jobs/search', { params });
    return response.data;
  },

  // Criar novo job
  async createJob(jobData: Partial<Job>): Promise<Job> {
    const response = await api.post('/api/v1/jobs', jobData);
    return response.data;
  },

  // Atualizar job
  async updateJob(id: number, jobData: Partial<Job>): Promise<Job> {
    const response = await api.put(`/api/v1/jobs/${id}`, jobData);
    return response.data;
  },

  // Deletar job
  async deleteJob(id: number): Promise<void> {
    await api.delete(`/api/v1/jobs/${id}`);
  },

  // Fechar job
  async closeJob(id: number): Promise<void> {
    await api.put(`/api/v1/jobs/${id}/close`);
  }
};


