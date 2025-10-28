import React from 'react';
import styled from 'styled-components';
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const HeroSection = styled.section`
  text-align: center;
  padding: 4rem 0;
  color: white;
`;

const HeroTitle = styled.h1`
  font-size: 3.5rem;
  font-weight: bold;
  margin-bottom: 1rem;
  background: linear-gradient(135deg, #fff 0%, #f0f0f0 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
`;

const HeroSubtitle = styled.p`
  font-size: 1.25rem;
  margin-bottom: 2rem;
  opacity: 0.9;
`;

const CTAButton = styled(Link)`
  display: inline-block;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  padding: 1rem 2rem;
  border-radius: 30px;
  text-decoration: none;
  font-weight: 600;
  font-size: 1.1rem;
  border: 2px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);

  &:hover {
    background: rgba(255, 255, 255, 0.3);
    transform: translateY(-2px);
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  }
`;

const FeaturesSection = styled.section`
  padding: 4rem 0;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  margin: 2rem 0;
  border-radius: 20px;
`;

const FeaturesGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
`;

const FeatureCard = styled.div`
  background: rgba(255, 255, 255, 0.1);
  padding: 2rem;
  border-radius: 15px;
  text-align: center;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
`;

const FeatureIcon = styled.div`
  font-size: 3rem;
  margin-bottom: 1rem;
`;

const FeatureTitle = styled.h3`
  font-size: 1.5rem;
  margin-bottom: 1rem;
  color: white;
`;

const FeatureDescription = styled.p`
  color: rgba(255, 255, 255, 0.8);
  line-height: 1.6;
`;

const Home: React.FC = () => {
  const { user } = useAuth();

  return (
    <>
      <HeroSection>
        <HeroTitle>Jooby</HeroTitle>
        <HeroSubtitle>
          Conecte-se com os melhores freelancers ou encontre oportunidades incríveis
        </HeroSubtitle>
        {user ? (
          <CTAButton to="/dashboard">Ir para Dashboard</CTAButton>
        ) : (
          <CTAButton to="/register">Começar Agora</CTAButton>
        )}
      </HeroSection>

      <FeaturesSection>
        <FeaturesGrid>
          <FeatureCard>
            <FeatureIcon>💼</FeatureIcon>
            <FeatureTitle>Para Clientes</FeatureTitle>
            <FeatureDescription>
              Publique seus projetos e encontre freelancers qualificados para executá-los
              com excelência e dentro do prazo.
            </FeatureDescription>
          </FeatureCard>

          <FeatureCard>
            <FeatureIcon>🚀</FeatureIcon>
            <FeatureTitle>Para Freelancers</FeatureTitle>
            <FeatureDescription>
              Encontre oportunidades que combinam com suas habilidades e construa
              uma carreira de sucesso como freelancer.
            </FeatureDescription>
          </FeatureCard>

          <FeatureCard>
            <FeatureIcon>🤝</FeatureIcon>
            <FeatureTitle>Comunidade</FeatureTitle>
            <FeatureDescription>
              Faça parte de uma comunidade vibrante de profissionais talentosos
              e clientes que valorizam qualidade.
            </FeatureDescription>
          </FeatureCard>
        </FeaturesGrid>
      </FeaturesSection>
    </>
  );
};

export default Home;


