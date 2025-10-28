import React, { useState } from 'react';
import styled from 'styled-components';
import { useAuth } from '../contexts/AuthContext';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { userService, UpdateUserData, ChangePasswordData } from '../services/userService';

const ProfileContainer = styled.div`
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
`;

const ProfileCard = styled.div`
  background: rgba(255, 255, 255, 0.95);
  padding: 2rem;
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  margin-bottom: 2rem;
`;

const Title = styled.h1`
  color: white;
  text-align: center;
  margin-bottom: 2rem;
  font-size: 2.5rem;
`;

const ProfileHeader = styled.div`
  text-align: center;
  margin-bottom: 2rem;
`;

const Avatar = styled.div`
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  margin: 0 auto 1rem;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 2rem;
  color: white;
  font-weight: bold;
`;

const UserName = styled.h2`
  color: #333;
  margin-bottom: 0.5rem;
`;

const UserEmail = styled.p`
  color: #666;
  margin-bottom: 1rem;
`;

const UserType = styled.span`
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 600;
`;

const ProfileSection = styled.div`
  margin-bottom: 2rem;
`;

const SectionTitle = styled.h3`
  color: #333;
  margin-bottom: 1rem;
  font-size: 1.3rem;
`;

const InfoGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
`;

const InfoItem = styled.div`
  padding: 1rem;
  background: #f8f9fa;
  border-radius: 10px;
`;

const InfoLabel = styled.div`
  color: #666;
  font-size: 0.9rem;
  margin-bottom: 0.5rem;
`;

const InfoValue = styled.div`
  color: #333;
  font-weight: 500;
`;

const EditButton = styled.button`
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 1rem 2rem;
  border-radius: 25px;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.3s ease;
  width: 100%;

  &:hover {
    transform: translateY(-2px);
  }
`;

const Profile: React.FC = () => {
  const { user } = useAuth();
  const queryClient = useQueryClient();
  const [isEditing, setIsEditing] = useState(false);
  const [editData, setEditData] = useState<UpdateUserData>({});
  const [passwordData, setPasswordData] = useState<ChangePasswordData>({
    currentPassword: '',
    newPassword: ''
  });

  // Buscar dados do perfil
  const { data: userProfile, isLoading } = useQuery({
    queryKey: ['user', 'profile'],
    queryFn: () => userService.getProfile(),
    enabled: !!user
  });

  // Mutação para atualizar perfil
  const updateProfileMutation = useMutation({
    mutationFn: (data: UpdateUserData) => userService.updateProfile(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['user', 'profile'] });
      setIsEditing(false);
      setEditData({});
    }
  });

  // Mutação para alterar senha
  const changePasswordMutation = useMutation({
    mutationFn: (data: ChangePasswordData) => userService.changePassword(data),
    onSuccess: () => {
      setPasswordData({ currentPassword: '', newPassword: '' });
      alert('Senha alterada com sucesso!');
    }
  });

  const handleEdit = () => {
    setEditData({
      firstName: userProfile?.firstName || '',
      lastName: userProfile?.lastName || '',
      email: userProfile?.email || '',
      phone: userProfile?.phone || ''
    });
    setIsEditing(true);
  };

  const handleSave = () => {
    updateProfileMutation.mutate(editData);
  };

  const handlePasswordChange = () => {
    if (passwordData.currentPassword && passwordData.newPassword) {
      changePasswordMutation.mutate(passwordData);
    }
  };

  if (isLoading) {
    return <div style={{ textAlign: 'center', color: 'white', padding: '2rem' }}>Carregando...</div>;
  }

  if (!userProfile) {
    return <div style={{ textAlign: 'center', color: 'white', padding: '2rem' }}>Erro ao carregar perfil</div>;
  }

  if (!user) {
    return <div style={{ textAlign: 'center', color: 'white', padding: '2rem' }}>Usuário não encontrado</div>;
  }

  return (
    <ProfileContainer>
      <Title>Meu Perfil</Title>
      
      <ProfileCard>
        <ProfileHeader>
          <Avatar>
            {userProfile.firstName.charAt(0)}{userProfile.lastName.charAt(0)}
          </Avatar>
          <UserName>{userProfile.firstName} {userProfile.lastName}</UserName>
          <UserEmail>{userProfile.email}</UserEmail>
          <UserType>
            {userProfile.type === 'client' ? 'Cliente' : 'Freelancer'}
          </UserType>
        </ProfileHeader>

        <ProfileSection>
          <SectionTitle>Informações Pessoais</SectionTitle>
          <InfoGrid>
            <InfoItem>
              <InfoLabel>Nome</InfoLabel>
              <InfoValue>{userProfile.firstName}</InfoValue>
            </InfoItem>
            <InfoItem>
              <InfoLabel>Sobrenome</InfoLabel>
              <InfoValue>{userProfile.lastName}</InfoValue>
            </InfoItem>
            <InfoItem>
              <InfoLabel>Email</InfoLabel>
              <InfoValue>{userProfile.email}</InfoValue>
            </InfoItem>
            <InfoItem>
              <InfoLabel>Telefone</InfoLabel>
              <InfoValue>{user.phone}</InfoValue>
            </InfoItem>
          </InfoGrid>
        </ProfileSection>

        <EditButton>Editar Perfil</EditButton>
      </ProfileCard>
    </ProfileContainer>
  );
};

export default Profile;

