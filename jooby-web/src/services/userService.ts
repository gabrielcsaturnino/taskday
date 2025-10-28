import { api } from './api';

export interface User {
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

export interface UpdateUserData {
  firstName?: string;
  lastName?: string;
  email?: string;
  phone?: string;
}

export interface ChangePasswordData {
  currentPassword: string;
  newPassword: string;
}

export const userService = {
  // Buscar perfil do usuário logado
  async getProfile(): Promise<User> {
    // Primeiro tentar como cliente, se falhar tentar como freelancer
    try {
      const response = await api.get('/api/v1/clients/profile');
      return { ...response.data, type: 'client' };
    } catch (error) {
      const response = await api.get('/api/v1/contractors/profile');
      return { ...response.data, type: 'contractor' };
    }
  },

  // Atualizar perfil do usuário
  async updateProfile(userData: UpdateUserData): Promise<User> {
    // Primeiro tentar como cliente, se falhar tentar como freelancer
    try {
      const response = await api.put('/api/v1/clients/profile', userData);
      return { ...response.data, type: 'client' };
    } catch (error) {
      const response = await api.put('/api/v1/contractors/profile', userData);
      return { ...response.data, type: 'contractor' };
    }
  },

  // Alterar senha
  async changePassword(passwordData: ChangePasswordData): Promise<void> {
    // Primeiro tentar como cliente, se falhar tentar como freelancer
    try {
      await api.put('/api/v1/clients/password', passwordData);
    } catch (error) {
      await api.put('/api/v1/contractors/password', passwordData);
    }
  },

  // Buscar usuário por ID
  async getUserById(id: number): Promise<User> {
    const response = await api.get(`/api/v1/clients/${id}`);
    return response.data;
  },

  // Ativar conta
  async activateAccount(id: number): Promise<void> {
    await api.put(`/api/v1/clients/${id}/activate`);
  },

  // Desativar conta
  async deactivateAccount(id: number): Promise<void> {
    await api.put(`/api/v1/clients/${id}/deactivate`);
  }
};

