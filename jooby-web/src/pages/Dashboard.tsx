import React from 'react';
import styled from 'styled-components';
import { useAuth } from '../contexts/AuthContext';
import { useQuery } from '@tanstack/react-query';
import { jobService } from '../services/jobService';
import { userService } from '../services/userService';

const DashboardContainer = styled.div`
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
`;

const WelcomeCard = styled.div`
  background: rgba(255, 255, 255, 0.95);
  padding: 2rem;
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  margin-bottom: 2rem;
  text-align: center;
`;

const WelcomeTitle = styled.h1`
  color: #333;
  margin-bottom: 1rem;
  font-size: 2.5rem;
`;

const WelcomeSubtitle = styled.p`
  color: #666;
  font-size: 1.2rem;
`;

const StatsGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
  margin-bottom: 2rem;
`;

const StatCard = styled.div`
  background: rgba(255, 255, 255, 0.95);
  padding: 2rem;
  border-radius: 15px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  text-align: center;
`;

const StatNumber = styled.div`
  font-size: 3rem;
  font-weight: bold;
  color: #667eea;
  margin-bottom: 0.5rem;
`;

const StatLabel = styled.div`
  color: #666;
  font-size: 1.1rem;
`;

const QuickActions = styled.div`
  background: rgba(255, 255, 255, 0.95);
  padding: 2rem;
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
`;

const ActionsTitle = styled.h2`
  color: #333;
  margin-bottom: 1.5rem;
  text-align: center;
`;

const ActionsGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
`;

const ActionButton = styled.button`
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 1.5rem;
  border-radius: 15px;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;

  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 10px 25px rgba(102, 126, 234, 0.3);
  }
`;

const Dashboard: React.FC = () => {
  const { user } = useAuth();

  // Buscar dados do dashboard
  const { data: jobs = [] } = useQuery({
    queryKey: ['jobs', 'dashboard'],
    queryFn: () => jobService.getAllJobs()
  });

  // Removido userProfile não utilizado

  // Calcular estatísticas
  const activeJobs = jobs.filter(job => job.jobStatus === 'active').length;
  const totalJobs = jobs.length;

  return (
    <DashboardContainer>
      <WelcomeCard>
        <WelcomeTitle>
          Bem-vindo ao Jooby, {user?.firstName}!
        </WelcomeTitle>
        <WelcomeSubtitle>
          {user?.type === 'client' 
            ? 'Gerencie seus projetos e encontre freelancers talentosos'
            : 'Encontre oportunidades incríveis e construa sua carreira'
          }
        </WelcomeSubtitle>
      </WelcomeCard>

      <StatsGrid>
        <StatCard>
          <StatNumber>{activeJobs}</StatNumber>
          <StatLabel>Projetos Ativos</StatLabel>
        </StatCard>
        <StatCard>
          <StatNumber>{totalJobs}</StatNumber>
          <StatLabel>Total de Projetos</StatLabel>
        </StatCard>
        <StatCard>
          <StatNumber>0</StatNumber>
          <StatLabel>Candidaturas</StatLabel>
        </StatCard>
        <StatCard>
          <StatNumber>0</StatNumber>
          <StatLabel>Avaliações</StatLabel>
        </StatCard>
      </StatsGrid>

      <QuickActions>
        <ActionsTitle>Ações Rápidas</ActionsTitle>
        <ActionsGrid>
          {user?.type === 'client' ? (
            <>
              <ActionButton>Publicar Projeto</ActionButton>
              <ActionButton>Ver Candidatos</ActionButton>
              <ActionButton>Gerenciar Projetos</ActionButton>
              <ActionButton>Ver Mensagens</ActionButton>
            </>
          ) : (
            <>
              <ActionButton>Buscar Projetos</ActionButton>
              <ActionButton>Minhas Candidaturas</ActionButton>
              <ActionButton>Atualizar Perfil</ActionButton>
              <ActionButton>Ver Mensagens</ActionButton>
            </>
          )}
        </ActionsGrid>
      </QuickActions>
    </DashboardContainer>
  );
};

export default Dashboard;

