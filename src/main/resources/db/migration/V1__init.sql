
CREATE TYPE execution_status_enum AS ENUM ('pending', 'in_progress', 'completed', 'cancelled');
CREATE TYPE application_status_enum AS ENUM ('submitted', 'viewed', 'shortlisted', 'accepted', 'rejected');
CREATE TYPE job_status_enum AS ENUM ('active', 'inactive');
CREATE TYPE chat_room_status_enum AS ENUM ('active', 'inactive');
create type type_message_enum as enum('text', 'image');


CREATE TABLE contractor (
    id_contractor SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    cpf_contractor VARCHAR(11) NOT NULL UNIQUE,
    rg_contractor VARCHAR(10) NOT NULL UNIQUE,
    date_of_birth DATE NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(11) NOT NULL UNIQUE,
    avarage_rating NUMERIC(3,2) NOT NULL DEFAULT 0.00, 
    hash_password VARCHAR(255) NOT NULL,
    status_account BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE contractor_experience (
    id_experience SERIAL PRIMARY KEY,
    id_contractor INT NOT NULL REFERENCES contractor(id_contractor) ON DELETE CASCADE ON UPDATE CASCADE,
    job_title VARCHAR(100) NOT NULL,
    job_description TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE contractor_skill (
    id_skill SERIAL PRIMARY KEY,
    id_contractor INT NOT NULL REFERENCES contractor(id_contractor) ON DELETE CASCADE ON UPDATE CASCADE,
    skill_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

create table client(
    id_client serial primary key,
    first_name varchar(50) not null,
    cpf_client varchar(11) not null unique,
    rg_client varchar(10) not null unique,
    last_name varchar(50) not null,
    email varchar(100) not null unique,
    phone varchar(11) not null unique,
    hash_password varchar(255) not null,
    status_account boolean not null default false,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp
);



CREATE TABLE client_jobs (
    id_job SERIAL PRIMARY KEY,
    id_client INT NOT NULL REFERENCES client(id_client) ON DELETE CASCADE ON UPDATE CASCADE,
    job_title VARCHAR(100) NOT NULL,
    job_description TEXT NOT NULL,
    job_location VARCHAR(100) NOT NULL,
    job_salary NUMERIC(10,2) NOT NULL,
    job_status job_status_enum NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE client_job_applications (
    id_application SERIAL PRIMARY KEY,
    id_job INT NOT NULL REFERENCES client_jobs(id_job) ON DELETE CASCADE ON UPDATE CASCADE,
    id_contractor INT NOT NULL REFERENCES contractor(id_contractor) ON DELETE CASCADE ON UPDATE CASCADE,
    status_application application_status_enum NOT NULL DEFAULT 'submitted', 
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_job_contractor_application UNIQUE (id_job, id_contractor) 
);

CREATE TABLE client_job_execution (
    id_execution SERIAL PRIMARY KEY,
    id_job INT NOT NULL REFERENCES client_jobs(id_job) ON DELETE CASCADE ON UPDATE CASCADE,
    id_contractor INT NOT NULL REFERENCES contractor(id_contractor) ON DELETE CASCADE ON UPDATE CASCADE,
    status execution_status_enum NOT NULL DEFAULT 'pending',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_job_execution UNIQUE (id_job) 
);



create table address_client(
    id_address_client serial primary key,
    id_client int not null references client(id_client) on delete cascade on update cascade,
    street varchar(100) not null,
    number varchar(10) not null,
    neighborhood varchar(50) not null,
    city varchar(50) not null,
    state varchar(50) not null,
    zip_code varchar(10) not null unique,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp
);

create table address_contractor(
    id_address_contractor serial primary key,
    id_contractor int not null references contractor(id_contractor) on delete cascade on update cascade,
    street varchar(100) not null,
    number varchar(10) not null,
    neighborhood varchar(50) not null,
    city varchar(50) not null,
    state varchar(50) not null,
    zip_code varchar(10) not null unique,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp
);

create table chat_rooms(
    id_chat_room serial primary key,
    id_client int not null references client(id_client) on delete cascade on update cascade,
    id_contractor int not null references contractor(id_contractor) on delete cascade on update cascade,
    created_at timestamp not null default current_timestamp,
    status_chat chat_room_status_enum not null default 'inactive',
    updated_at timestamp not null default current_timestamp
);

create table messages(
    id_message serial primary key,
    id_chat_room int not null references chat_rooms(id_chat_room) on delete cascade on update cascade,
    id_client int not null references client(id_client) on delete cascade on update cascade,
    id_contractor int not null references contractor(id_contractor) on delete cascade on update cascade,
    content_message text not null,
    type_message type_message_enum not null default 'text',    
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp
);


CREATE OR REPLACE FUNCTION trigger_set_timestamp()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

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
