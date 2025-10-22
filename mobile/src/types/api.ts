// TaskDay Mobile - API Types
// Tipos TypeScript para integração com o backend Spring Boot

export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  statusAccount: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface Client extends User {
  addresses: Address[];
}

export interface Contractor extends User {
  addresses: Address[];
  rating: number;
  specialties: string[];
}

export interface Address {
  id: number;
  street: string;
  number: string;
  neighborhood: string;
  city: string;
  state: string;
  zipCode: string;
}

export interface Job {
  id: number;
  title: string;
  description: string;
  pricePerHour: number;
  status: JobStatus;
  address: Address;
  client: Client;
  createdAt: string;
  updatedAt: string;
}

export enum JobStatus {
  OPEN = 'OPEN',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED'
}

export interface JobApplication {
  id: number;
  job: Job;
  contractor: Contractor;
  status: ApplicationStatus;
  appliedAt: string;
}

export enum ApplicationStatus {
  SUBMITTED = 'SUBMITTED',
  ACCEPTED = 'ACCEPTED',
  REJECTED = 'REJECTED'
}

export interface ChatRoom {
  id: number;
  client: Client;
  contractor: Contractor;
  job: Job;
  status: ChatRoomStatus;
  createdAt: string;
}

export enum ChatRoomStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  ARCHIVED = 'ARCHIVED'
}

export interface Message {
  id: number;
  chatRoom: ChatRoom;
  content: string;
  type: MessageType;
  owner: MessageOwner;
  createdAt: string;
}

export enum MessageType {
  TEXT = 'TEXT',
  IMAGE = 'IMAGE',
  FILE = 'FILE'
}

export enum MessageOwner {
  CLIENT = 'CLIENT',
  CONTRACTOR = 'CONTRACTOR'
}

// API Response Types
export interface ApiResponse<T> {
  data: T;
  message: string;
  success: boolean;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

// Authentication Types
export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  user: User;
  expiresIn: number;
}

export interface RegisterRequest {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  password: string;
  userType: 'CLIENT' | 'CONTRACTOR';
}

// Search Types
export interface JobSearchParams {
  title?: string;
  location?: string;
  minPrice?: number;
  maxPrice?: number;
  status?: JobStatus;
  page?: number;
  size?: number;
}

export interface ContractorSearchParams {
  name?: string;
  location?: string;
  minRating?: number;
  specialties?: string[];
  page?: number;
  size?: number;
}
