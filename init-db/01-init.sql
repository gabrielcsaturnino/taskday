-- TaskDay Database Initialization Script
-- This script creates the complete database schema based on all Flyway migrations

-- Create custom types
CREATE TYPE execution_status_enum AS ENUM ('pending', 'in_progress', 'completed', 'cancelled');
CREATE TYPE application_status_enum AS ENUM ('submitted', 'viewed', 'shortlisted', 'accepted', 'rejected');
CREATE TYPE job_status_enum AS ENUM ('active', 'inactive');
CREATE TYPE chat_room_status_enum AS ENUM ('active', 'inactive');
CREATE TYPE type_message_enum AS ENUM ('text', 'image');

-- Create contractor table
CREATE TABLE contractor (
    id_contractor SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    rg_doc VARCHAR(10) NOT NULL UNIQUE,
    date_of_birth DATE NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(11) NOT NULL UNIQUE,
    avarage_rating NUMERIC(3,2) NOT NULL DEFAULT 0.00, 
    hash_password VARCHAR(255) NOT NULL,
    status_account BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create contractor experience table
CREATE TABLE contractor_experience (
    id_experience SERIAL PRIMARY KEY,
    id_contractor INT NOT NULL REFERENCES contractor(id_contractor) ON DELETE CASCADE ON UPDATE CASCADE,
    job_title VARCHAR(100) NOT NULL,
    job_description TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create contractor skill table
CREATE TABLE contractor_skill (
    id_skill SERIAL PRIMARY KEY,
    id_contractor INT NOT NULL REFERENCES contractor(id_contractor) ON DELETE CASCADE ON UPDATE CASCADE,
    skill_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create client table
CREATE TABLE client (
    id_client SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    rg_doc VARCHAR(10) NOT NULL UNIQUE,
    last_name VARCHAR(50) NOT NULL,
    date_of_birth DATE NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(11) NOT NULL UNIQUE,
    hash_password VARCHAR(255) NOT NULL,
    status_account BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create client jobs table
CREATE TABLE client_jobs (
    id_job SERIAL PRIMARY KEY,
    id_client INT NOT NULL REFERENCES client(id_client) ON DELETE CASCADE ON UPDATE CASCADE,
    job_title VARCHAR(100) NOT NULL,
    job_description TEXT NOT NULL,
    job_salary NUMERIC(10,2) NOT NULL,
    job_status job_status_enum NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create client job applications table
CREATE TABLE client_job_applications (
    id_application SERIAL PRIMARY KEY,
    id_job INT NOT NULL REFERENCES client_jobs(id_job) ON DELETE CASCADE ON UPDATE CASCADE,
    id_contractor INT NOT NULL REFERENCES contractor(id_contractor) ON DELETE CASCADE ON UPDATE CASCADE,
    status_application application_status_enum NOT NULL DEFAULT 'submitted', 
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_job_contractor_application UNIQUE (id_job, id_contractor) 
);

-- Create client job execution table
CREATE TABLE client_job_execution (
    id_execution SERIAL PRIMARY KEY,
    id_job INT NOT NULL REFERENCES client_jobs(id_job) ON DELETE CASCADE ON UPDATE CASCADE,
    id_contractor INT NOT NULL REFERENCES contractor(id_contractor) ON DELETE CASCADE ON UPDATE CASCADE,
    status execution_status_enum NOT NULL DEFAULT 'pending',
    avarage_rating_job NUMERIC(3,2) NOT NULL DEFAULT 0.00,
    status_execution BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_job_execution UNIQUE (id_job) 
);

-- Create address table (unified for clients, contractors, and jobs)
CREATE TABLE address (
    id_address SERIAL PRIMARY KEY,
    street VARCHAR(100) NOT NULL,
    number VARCHAR(10) NOT NULL,
    neighborhood VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50) NOT NULL,
    zip_code VARCHAR(10) NOT NULL,
    
    -- Column to identify the type of "owner" of the address
    owner_type VARCHAR(30) NOT NULL, -- Ex: 'CLIENT', 'CONTRACTOR', 'JOB'
    
    id_client INT REFERENCES client(id_client) ON DELETE CASCADE ON UPDATE CASCADE NULL,
    id_contractor INT REFERENCES contractor(id_contractor) ON DELETE CASCADE ON UPDATE CASCADE NULL,
    id_job INT REFERENCES client_jobs(id_job) ON DELETE CASCADE ON UPDATE CASCADE NULL,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraint to ensure only one FK "owner" is filled and matches the owner_type
    CONSTRAINT chk_address_owner CHECK (
        (owner_type = 'CLIENT' AND id_client IS NOT NULL AND id_contractor IS NULL AND id_job IS NULL) OR
        (owner_type = 'CONTRACTOR' AND id_contractor IS NOT NULL AND id_client IS NULL AND id_job IS NULL) OR
        (owner_type = 'JOB' AND id_job IS NOT NULL AND id_client IS NULL AND id_contractor IS NULL)
    ),
    
    CONSTRAINT uq_owner_address_details UNIQUE (owner_type, id_client, id_contractor, id_job, street, number, zip_code)
);

-- Create chat rooms table
CREATE TABLE chat_rooms (
    id_chat_room SERIAL PRIMARY KEY,
    id_client INT NOT NULL REFERENCES client(id_client) ON DELETE CASCADE ON UPDATE CASCADE,
    id_contractor INT NOT NULL REFERENCES contractor(id_contractor) ON DELETE CASCADE ON UPDATE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status_chat chat_room_status_enum NOT NULL DEFAULT 'inactive',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create messages table
CREATE TABLE messages (
    id_message SERIAL PRIMARY KEY,
    id_chat_room INT NOT NULL REFERENCES chat_rooms(id_chat_room) ON DELETE CASCADE ON UPDATE CASCADE,
    id_client INT NOT NULL REFERENCES client(id_client) ON DELETE CASCADE ON UPDATE CASCADE,
    id_contractor INT NOT NULL REFERENCES contractor(id_contractor) ON DELETE CASCADE ON UPDATE CASCADE,
    content_message TEXT NOT NULL,
    type_message type_message_enum NOT NULL DEFAULT 'text',    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX idx_address_id_client ON address(id_client) WHERE id_client IS NOT NULL;
CREATE INDEX idx_address_id_contractor ON address(id_contractor) WHERE id_contractor IS NOT NULL;
CREATE INDEX idx_address_id_job ON address(id_job) WHERE id_job IS NOT NULL;
CREATE INDEX idx_address_owner_type ON address(owner_type);

-- Create trigger function for updating timestamps
CREATE OR REPLACE FUNCTION trigger_set_timestamp()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create triggers for automatic timestamp updates
CREATE TRIGGER set_timestamp_contractor
    BEFORE UPDATE ON contractor
    FOR EACH ROW
    EXECUTE FUNCTION trigger_set_timestamp();

CREATE TRIGGER set_timestamp_contractor_experience
    BEFORE UPDATE ON contractor_experience
    FOR EACH ROW
    EXECUTE FUNCTION trigger_set_timestamp();

CREATE TRIGGER set_timestamp_contractor_skill
    BEFORE UPDATE ON contractor_skill
    FOR EACH ROW
    EXECUTE FUNCTION trigger_set_timestamp();

CREATE TRIGGER set_timestamp_client
    BEFORE UPDATE ON client
    FOR EACH ROW
    EXECUTE FUNCTION trigger_set_timestamp();

CREATE TRIGGER set_timestamp_client_jobs
    BEFORE UPDATE ON client_jobs
    FOR EACH ROW
    EXECUTE FUNCTION trigger_set_timestamp();

CREATE TRIGGER set_timestamp_client_job_applications
    BEFORE UPDATE ON client_job_applications
    FOR EACH ROW
    EXECUTE FUNCTION trigger_set_timestamp();

CREATE TRIGGER set_timestamp_client_job_execution
    BEFORE UPDATE ON client_job_execution
    FOR EACH ROW
    EXECUTE FUNCTION trigger_set_timestamp();

CREATE TRIGGER set_timestamp_address
    BEFORE UPDATE ON address
    FOR EACH ROW
    EXECUTE FUNCTION trigger_set_timestamp();

CREATE TRIGGER set_timestamp_chat_rooms
    BEFORE UPDATE ON chat_rooms
    FOR EACH ROW
    EXECUTE FUNCTION trigger_set_timestamp();

CREATE TRIGGER set_timestamp_messages
    BEFORE UPDATE ON messages
    FOR EACH ROW
    EXECUTE FUNCTION trigger_set_timestamp();

-- Insert some sample data for testing
INSERT INTO client (first_name, last_name, cpf, rg_doc, date_of_birth, email, phone, hash_password, status_account) 
VALUES 
('João', 'Silva', '123.456.789-01', '1234567890', '1990-01-01', 'joao@example.com', '11999999999', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKyVhXPqUe6KjQjQjQjQjQjQjQjQj', true),
('Maria', 'Santos', '987.654.321-01', '0987654321', '1985-05-15', 'maria@example.com', '11888888888', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKyVhXPqUe6KjQjQjQjQjQjQjQjQj', true);

INSERT INTO contractor (first_name, last_name, cpf, rg_doc, date_of_birth, email, phone, hash_password, status_account) 
VALUES 
('Pedro', 'Oliveira', '111.222.333-44', '1111111111', '1988-03-20', 'pedro@example.com', '11777777777', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKyVhXPqUe6KjQjQjQjQjQjQjQjQj', true),
('Ana', 'Costa', '555.666.777-88', '2222222222', '1992-07-10', 'ana@example.com', '11666666666', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKyVhXPqUe6KjQjQjQjQjQjQjQjQj', true);

-- Insert sample jobs
INSERT INTO client_jobs (id_client, job_title, job_description, job_salary, job_status) 
VALUES 
(1, 'Desenvolvimento de App Mobile', 'Preciso de um desenvolvedor para criar um aplicativo mobile em React Native', 5000.00, 'active'),
(2, 'Design de Interface', 'Busco um designer para criar interfaces modernas para meu site', 3000.00, 'active');

-- Insert sample addresses
INSERT INTO address (street, number, neighborhood, city, state, zip_code, owner_type, id_client) 
VALUES 
('Rua das Flores', '123', 'Centro', 'São Paulo', 'SP', '01234-567', 'CLIENT', 1),
('Avenida Paulista', '456', 'Bela Vista', 'São Paulo', 'SP', '01310-100', 'CLIENT', 2);

INSERT INTO address (street, number, neighborhood, city, state, zip_code, owner_type, id_contractor) 
VALUES 
('Rua Augusta', '789', 'Consolação', 'São Paulo', 'SP', '01305-100', 'CONTRACTOR', 1),
('Rua Oscar Freire', '321', 'Jardins', 'São Paulo', 'SP', '01426-001', 'CONTRACTOR', 2);

COMMENT ON DATABASE taskdaydb IS 'TaskDay - Plataforma de Freelancers Database';

