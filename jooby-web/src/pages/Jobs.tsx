import React, { useState } from 'react';
import styled from 'styled-components';
import { useQuery } from '@tanstack/react-query';
import { jobService, JobSearchParams } from '../services/jobService';

const JobsContainer = styled.div`
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
`;

const Title = styled.h1`
  color: white;
  text-align: center;
  margin-bottom: 2rem;
  font-size: 2.5rem;
`;

const SearchSection = styled.div`
  background: rgba(255, 255, 255, 0.95);
  padding: 2rem;
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  margin-bottom: 2rem;
`;

const SearchForm = styled.form`
  display: grid;
  grid-template-columns: 2fr 1fr 1fr auto;
  gap: 1rem;
  align-items: end;
`;

const SearchInput = styled.input`
  padding: 1rem;
  border: 2px solid #e1e5e9;
  border-radius: 10px;
  font-size: 1rem;

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

  &:focus {
    outline: none;
    border-color: #667eea;
  }
`;

const SearchButton = styled.button`
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 1rem 2rem;
  border-radius: 10px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.3s ease;

  &:hover {
    transform: translateY(-2px);
  }
`;

const JobsGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 2rem;
`;

const JobCard = styled.div`
  background: rgba(255, 255, 255, 0.95);
  padding: 2rem;
  border-radius: 15px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  transition: transform 0.3s ease;

  &:hover {
    transform: translateY(-5px);
  }
`;

const JobTitle = styled.h3`
  color: #333;
  margin-bottom: 1rem;
  font-size: 1.3rem;
`;

const JobDescription = styled.p`
  color: #666;
  margin-bottom: 1rem;
  line-height: 1.6;
`;

const JobMeta = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
`;

const JobPrice = styled.span`
  font-size: 1.2rem;
  font-weight: bold;
  color: #667eea;
`;

const JobLocation = styled.span`
  color: #666;
`;

const ApplyButton = styled.button`
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 25px;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.3s ease;
  width: 100%;

  &:hover {
    transform: translateY(-2px);
  }
`;

const Jobs: React.FC = () => {
  const [searchParams, setSearchParams] = useState<JobSearchParams>({});
  const [searchTitle, setSearchTitle] = useState('');
  const [searchLocation, setSearchLocation] = useState('');
  const [searchCategory, setSearchCategory] = useState('');

  // Buscar jobs da API
  const { data: jobs = [], isLoading, error } = useQuery({
    queryKey: ['jobs', searchParams],
    queryFn: () => jobService.searchJobs(searchParams),
    enabled: Object.keys(searchParams).length > 0
  });

  // Buscar todos os jobs ativos se não há filtros
  const { data: allJobs = [] } = useQuery({
    queryKey: ['jobs', 'all'],
    queryFn: () => jobService.getAllJobs(),
    enabled: Object.keys(searchParams).length === 0
  });

  const handleSearch = () => {
    const params: JobSearchParams = {};
    if (searchTitle) params.title = searchTitle;
    if (searchLocation) params.location = searchLocation;
    if (searchCategory) params.status = searchCategory;
    
    setSearchParams(params);
  };

  const displayJobs = Object.keys(searchParams).length > 0 ? jobs : allJobs;

  return (
    <JobsContainer>
      <Title>Oportunidades</Title>
      
      <SearchSection>
        <SearchForm onSubmit={(e) => { e.preventDefault(); handleSearch(); }}>
          <SearchInput 
            placeholder="Buscar por título ou descrição..." 
            value={searchTitle}
            onChange={(e) => setSearchTitle(e.target.value)}
          />
          <Select 
            value={searchCategory}
            onChange={(e) => setSearchCategory(e.target.value)}
          >
            <option value="">Todas as categorias</option>
            <option value="development">Desenvolvimento</option>
            <option value="design">Design</option>
            <option value="marketing">Marketing</option>
          </Select>
          <SearchInput 
            placeholder="Localização..." 
            value={searchLocation}
            onChange={(e) => setSearchLocation(e.target.value)}
          />
          <SearchButton type="submit">Buscar</SearchButton>
        </SearchForm>
      </SearchSection>

      {isLoading && (
        <div style={{ textAlign: 'center', color: 'white', padding: '2rem' }}>
          Carregando oportunidades...
        </div>
      )}

      {error && (
        <div style={{ textAlign: 'center', color: '#ff6b6b', padding: '2rem' }}>
          Erro ao carregar oportunidades. Tente novamente.
        </div>
      )}

      <JobsGrid>
        {displayJobs.map((job) => (
          <JobCard key={job.id}>
            <JobTitle>{job.title}</JobTitle>
            <JobDescription>{job.description}</JobDescription>
            <JobMeta>
              <JobPrice>R$ {job.pricePerHour}/hora</JobPrice>
              <JobLocation>
                {job.address ? 
                  `${job.address.city}, ${job.address.state}` : 
                  'Localização não informada'
                }
              </JobLocation>
            </JobMeta>
            <ApplyButton>Candidatar-se</ApplyButton>
          </JobCard>
        ))}
      </JobsGrid>

      {displayJobs.length === 0 && !isLoading && (
        <div style={{ textAlign: 'center', color: 'white', padding: '2rem' }}>
          Nenhuma oportunidade encontrada.
        </div>
      )}
    </JobsContainer>
  );
};

export default Jobs;
