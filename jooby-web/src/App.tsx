import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import styled from 'styled-components';
import Header from './components/Header';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import Jobs from './pages/Jobs';
import Profile from './pages/Profile';
import { AuthProvider } from './contexts/AuthContext';

const queryClient = new QueryClient();

const AppContainer = styled.div`
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
`;

const MainContent = styled.main`
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
`;

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <Router>
          <AppContainer>
            <Header />
            <MainContent>
              <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/dashboard" element={<Dashboard />} />
                <Route path="/jobs" element={<Jobs />} />
                <Route path="/profile" element={<Profile />} />
              </Routes>
            </MainContent>
          </AppContainer>
        </Router>
      </AuthProvider>
    </QueryClientProvider>
  );
}

export default App;