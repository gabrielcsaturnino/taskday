import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { useAuth } from '../contexts/AuthContext';

const RegisterContainer = styled.div`
  max-width: 500px;
  margin: 2rem auto;
  padding: 2rem;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(10px);
`;

const Title = styled.h2`
  text-align: center;
  color: #333;
  margin-bottom: 2rem;
  font-size: 2rem;
`;

const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
`;

const Input = styled.input`
  padding: 1rem;
  border: 2px solid #e1e5e9;
  border-radius: 10px;
  font-size: 1rem;
  transition: border-color 0.3s ease;

  &:focus {
    outline: none;
    border-color: #667eea;
  }
`;

const Select = styled.select`
  padding: 1rem;
  border: 2px solid #e1e5e9;
  border-radius: 10px;
  font-size: 1rem;
  background: white;
  transition: border-color 0.3s ease;

  &:focus {
    outline: none;
    border-color: #667eea;
  }
`;

const Button = styled.button`
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 1rem;
  border-radius: 10px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.3s ease;

  &:hover {
    transform: translateY(-2px);
  }

  &:disabled {
    opacity: 0.6;
    cursor: not-allowed;
    transform: none;
  }
`;

const LinkText = styled.p`
  text-align: center;
  margin-top: 1rem;
  color: #666;

  a {
    color: #667eea;
    text-decoration: none;
    font-weight: 600;

    &:hover {
      text-decoration: underline;
    }
  }
`;

const ErrorMessage = styled.div`
  background: #fee;
  color: #c33;
  padding: 1rem;
  border-radius: 10px;
  border: 1px solid #fcc;
`;

const Register: React.FC = () => {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    password: '',
    confirmPassword: '',
    cpf: '',
    rgDocument: '',
    dateOfBirthday: '',
    description: '',
    type: 'client' as 'client' | 'contractor',
    address: {
      street: '',
      number: '',
      neighborhood: '',
      city: '',
      state: '',
      zipCode: ''
    }
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { register } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    
    if (name.startsWith('address.')) {
      const addressField = name.split('.')[1];
      setFormData({
        ...formData,
        address: {
          ...formData.address,
          [addressField]: value
        }
      });
    } else {
      setFormData({
        ...formData,
        [name]: value
      });
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    if (formData.password !== formData.confirmPassword) {
      setError('As senhas não coincidem');
      return;
    }

    if (formData.password.length < 8) {
      setError('A senha deve ter pelo menos 8 caracteres');
      return;
    }

    setLoading(true);

    try {
      const userData = {
        firstName: formData.firstName,
        lastName: formData.lastName,
        email: formData.email,
        phone: formData.phone,
        password: formData.password,
        cpf: formData.cpf,
        rgDocument: formData.rgDocument,
        dateOfBirthday: formData.dateOfBirthday,
        ...(formData.type === 'contractor' && { description: formData.description }),
        createAddressRequestDTO: formData.address
      };

      await register(userData);
      navigate('/dashboard');
    } catch (err) {
      setError('Erro ao criar conta. Tente novamente.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <RegisterContainer>
      <Title>Cadastrar</Title>
      {error && <ErrorMessage>{error}</ErrorMessage>}
      <Form onSubmit={handleSubmit}>
        <Input
          type="text"
          name="firstName"
          placeholder="Nome"
          value={formData.firstName}
          onChange={handleChange}
          required
        />
        <Input
          type="text"
          name="lastName"
          placeholder="Sobrenome"
          value={formData.lastName}
          onChange={handleChange}
          required
        />
        <Input
          type="email"
          name="email"
          placeholder="Email"
          value={formData.email}
          onChange={handleChange}
          required
        />
        <Input
          type="tel"
          name="phone"
          placeholder="Telefone"
          value={formData.phone}
          onChange={handleChange}
          required
        />
        <Input
          type="text"
          name="cpf"
          placeholder="CPF"
          value={formData.cpf}
          onChange={handleChange}
          required
        />
        <Input
          type="text"
          name="rgDocument"
          placeholder="RG"
          value={formData.rgDocument}
          onChange={handleChange}
          required
        />
        <Input
          type="date"
          name="dateOfBirthday"
          placeholder="Data de Nascimento"
          value={formData.dateOfBirthday}
          onChange={handleChange}
          required
        />
        <Select
          name="type"
          value={formData.type}
          onChange={handleChange}
          required
        >
          <option value="client">Cliente</option>
          <option value="contractor">Freelancer</option>
        </Select>
        {formData.type === 'contractor' && (
          <Input
            type="text"
            name="description"
            placeholder="Descrição do seu trabalho"
            value={formData.description}
            onChange={handleChange}
            required
          />
        )}
        <Input
          type="password"
          name="password"
          placeholder="Senha"
          value={formData.password}
          onChange={handleChange}
          required
        />
        <Input
          type="password"
          name="confirmPassword"
          placeholder="Confirmar Senha"
          value={formData.confirmPassword}
          onChange={handleChange}
          required
        />
        
        <h3 style={{ color: 'white', marginTop: '20px', marginBottom: '10px' }}>Endereço</h3>
        <Input
          type="text"
          name="address.street"
          placeholder="Rua"
          value={formData.address.street}
          onChange={handleChange}
          required
        />
        <Input
          type="text"
          name="address.number"
          placeholder="Número"
          value={formData.address.number}
          onChange={handleChange}
          required
        />
        <Input
          type="text"
          name="address.neighborhood"
          placeholder="Bairro"
          value={formData.address.neighborhood}
          onChange={handleChange}
          required
        />
        <Input
          type="text"
          name="address.city"
          placeholder="Cidade"
          value={formData.address.city}
          onChange={handleChange}
          required
        />
        <Input
          type="text"
          name="address.state"
          placeholder="Estado"
          value={formData.address.state}
          onChange={handleChange}
          required
        />
        <Input
          type="text"
          name="address.zipCode"
          placeholder="CEP"
          value={formData.address.zipCode}
          onChange={handleChange}
          required
        />
        
        <Button type="submit" disabled={loading}>
          {loading ? 'Criando conta...' : 'Cadastrar'}
        </Button>
      </Form>
      <LinkText>
        Já tem uma conta? <Link to="/login">Entrar</Link>
      </LinkText>
    </RegisterContainer>
  );
};

export default Register;

