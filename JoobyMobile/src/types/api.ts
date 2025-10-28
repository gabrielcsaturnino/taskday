// TaskDay Mobile - API Types
export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  userType: 'CLIENT' | 'CONTRACTOR';
  createdAt: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  user: User;
}

export interface RegisterRequest {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  password: string;
  userType: 'CLIENT' | 'CONTRACTOR';
}

export interface RegisterResponse {
  message: string;
  user: User;
}

export interface Job {
  id: number;
  title: string;
  description: string;
  pricePerHour: number;
  status: string;
  clientId: number;
  createdAt: string;
}

export interface ApiResponse<T> {
  data: T;
  message: string;
  success: boolean;
}

