-- TABLES

CREATE TABLE public.contractor (
    id_contractor integer NOT NULL,
    first_name character varying(50) NOT NULL,
    last_name character varying(50) NOT NULL,
    cpf character varying(11) NOT NULL,
    rg_doc character varying(10) NOT NULL,
    description text NOT NULL,
    date_of_birth date NOT NULL,
    email character varying(100) NOT NULL,
    phone character varying(11) NOT NULL,
    avarage_rating numeric(3,2) DEFAULT 0.00 NOT NULL,
    hash_password character varying(255) NOT NULL,
    status_account boolean DEFAULT false NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE public.client (
    id_client integer NOT NULL,
    first_name character varying(50) NOT NULL,
    cpf character varying(11) NOT NULL,
    rg_doc character varying(10) NOT NULL,
    last_name character varying(50) NOT NULL,
    email character varying(100) NOT NULL,
    phone character varying(11) NOT NULL,
    hash_password character varying(255) NOT NULL,
    status_account boolean DEFAULT false NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_of_birth date NOT NULL
);

CREATE TABLE public.client_jobs (
    id_job integer NOT NULL,
    id_client integer NOT NULL,
    job_title character varying(100) NOT NULL,
    job_description text NOT NULL,
    job_salary numeric(10,2) NOT NULL,
    job_status VARCHAR(255) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE public.address (
    id_address integer NOT NULL,
    street character varying(100) NOT NULL,
    number character varying(10) NOT NULL,
    neighborhood character varying(50) NOT NULL,
    city character varying(50) NOT NULL,
    state character varying(50) NOT NULL,
    zip_code character varying(10) NOT NULL,
    owner_type character varying(30) NOT NULL,
    id_client integer,
    id_contractor integer,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    id_job integer,
    CONSTRAINT chk_address_owner CHECK ((((owner_type) = 'CLIENT') AND (id_client IS NOT NULL) AND (id_contractor IS NULL)) AND (id_job IS NULL) OR (((owner_type) = 'CONTRACTOR') AND (id_contractor IS NOT NULL) AND (id_client IS NULL)) AND (id_job IS NULL) OR (((owner_type) = 'JOB') AND (id_job IS NOT NULL) AND (id_client IS NULL) AND (id_contractor IS NULL)))
);

CREATE TABLE public.chat_rooms (
    id_chat_room integer NOT NULL,
    id_client integer NOT NULL,
    id_contractor integer NOT NULL,
    id_job integer NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    status_chat VARCHAR(255) DEFAULT 'inactive' NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE public.client_job_applications (
    id_application integer NOT NULL,
    id_job integer NOT NULL,
    id_contractor integer NOT NULL,
    status_application VARCHAR(255) DEFAULT 'submitted' NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE public.client_job_execution (
    id_execution integer NOT NULL,
    id_job integer NOT NULL,
    id_contractor_leader integer NOT NULL,
    status VARCHAR(255) DEFAULT 'pending' NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    avarage_rating_job numeric(3,2) DEFAULT 0.00 NOT NULL,
    status_execution boolean DEFAULT false NOT NULL,
    in_progress_at timestamp without time zone NULL,
    completed_at timestamp without time zone NULL,
    total_time BIGINT DEFAULT NULL
);

CREATE TABLE public.job_execution_contractor (
    job_execution_id INTEGER NOT NULL,
    contractor_id INTEGER NOT NULL
);




CREATE TABLE public.messages (
    id_message integer NOT NULL,
    id_chat_room INTEGER NOT NULL,
    content_message TEXT NOT NULL,
    message_owner_send VARCHAR(255) NOT NULL, -- Esta é a única fonte da verdade sobre o remetente
    type_message VARCHAR(255) DEFAULT 'TEXT' NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,

    CONSTRAINT chk_message_owner_send CHECK (message_owner_send IN ('CLIENT', 'CONTRACTOR'))
);


-- SEQUENCES AND DEFAULTS FOR PRIMARY KEYS

CREATE SEQUENCE public.address_id_address_seq AS integer START WITH 1 INCREMENT BY 1;
ALTER TABLE public.address ALTER COLUMN id_address SET DEFAULT nextval('public.address_id_address_seq');

CREATE SEQUENCE public.chat_rooms_id_chat_room_seq AS integer START WITH 1 INCREMENT BY 1;
ALTER TABLE public.chat_rooms ALTER COLUMN id_chat_room SET DEFAULT nextval('public.chat_rooms_id_chat_room_seq');

CREATE SEQUENCE public.client_id_client_seq AS integer START WITH 1 INCREMENT BY 1;
ALTER TABLE public.client ALTER COLUMN id_client SET DEFAULT nextval('public.client_id_client_seq');

CREATE SEQUENCE public.client_job_applications_id_application_seq AS integer START WITH 1 INCREMENT BY 1;
ALTER TABLE public.client_job_applications ALTER COLUMN id_application SET DEFAULT nextval('public.client_job_applications_id_application_seq');

CREATE SEQUENCE public.client_job_execution_id_execution_seq AS integer START WITH 1 INCREMENT BY 1;
ALTER TABLE public.client_job_execution ALTER COLUMN id_execution SET DEFAULT nextval('public.client_job_execution_id_execution_seq');

CREATE SEQUENCE public.client_jobs_id_job_seq AS integer START WITH 1 INCREMENT BY 1;
ALTER TABLE public.client_jobs ALTER COLUMN id_job SET DEFAULT nextval('public.client_jobs_id_job_seq');

CREATE SEQUENCE public.contractor_id_contractor_seq AS integer START WITH 1 INCREMENT BY 1;
ALTER TABLE public.contractor ALTER COLUMN id_contractor SET DEFAULT nextval('public.contractor_id_contractor_seq');

CREATE SEQUENCE public.messages_id_message_seq AS integer START WITH 1 INCREMENT BY 1;
ALTER TABLE public.messages ALTER COLUMN id_message SET DEFAULT nextval('public.messages_id_message_seq');


-- PRIMARY AND UNIQUE CONSTRAINTS

ALTER TABLE public.address ADD CONSTRAINT address_pkey PRIMARY KEY (id_address);
ALTER TABLE public.chat_rooms ADD CONSTRAINT chat_rooms_pkey PRIMARY KEY (id_chat_room);
ALTER TABLE public.client ADD CONSTRAINT client_pkey PRIMARY KEY (id_client);
ALTER TABLE public.client ADD CONSTRAINT client_cpf_client_key UNIQUE (cpf);
ALTER TABLE public.client ADD CONSTRAINT client_email_key UNIQUE (email);
ALTER TABLE public.client ADD CONSTRAINT client_phone_key UNIQUE (phone);
ALTER TABLE public.client ADD CONSTRAINT client_rg_client_key UNIQUE (rg_doc);
ALTER TABLE public.client_job_applications ADD CONSTRAINT client_job_applications_pkey PRIMARY KEY (id_application);
ALTER TABLE public.client_job_execution ADD CONSTRAINT client_job_execution_pkey PRIMARY KEY (id_execution);
ALTER TABLE public.client_jobs ADD CONSTRAINT client_jobs_pkey PRIMARY KEY (id_job);
ALTER TABLE public.contractor ADD CONSTRAINT contractor_pkey PRIMARY KEY (id_contractor);
ALTER TABLE public.contractor ADD CONSTRAINT contractor_cpf_contractor_key UNIQUE (cpf);
ALTER TABLE public.contractor ADD CONSTRAINT contractor_email_key UNIQUE (email);
ALTER TABLE public.contractor ADD CONSTRAINT contractor_phone_key UNIQUE (phone);
ALTER TABLE public.contractor ADD CONSTRAINT contractor_rg_contractor_key UNIQUE (rg_doc);
ALTER TABLE public.messages ADD CONSTRAINT messages_pkey PRIMARY KEY (id_message);
ALTER TABLE public.client_job_applications ADD CONSTRAINT uq_job_contractor_application UNIQUE (id_job, id_contractor);
ALTER TABLE public.client_job_execution ADD CONSTRAINT uq_job_execution UNIQUE (id_job);
ALTER TABLE public.address ADD CONSTRAINT uq_owner_address_details UNIQUE (owner_type, id_client, id_contractor, id_job, street, number, zip_code);
ALTER TABLE public.job_execution_contractor ADD CONSTRAINT pk_job_execution_contractor PRIMARY KEY (job_execution_id, contractor_id);


-- FOREIGN KEY CONSTRAINTS

ALTER TABLE public.address ADD CONSTRAINT address_id_client_fkey FOREIGN KEY (id_client) REFERENCES public.client(id_client) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE public.address ADD CONSTRAINT address_id_contractor_fkey FOREIGN KEY (id_contractor) REFERENCES public.contractor(id_contractor) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE public.address ADD CONSTRAINT address_id_job_fkey FOREIGN KEY (id_job) REFERENCES public.client_jobs(id_job) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE public.chat_rooms ADD CONSTRAINT chat_rooms_id_client_fkey FOREIGN KEY (id_client) REFERENCES public.client(id_client) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE public.chat_rooms ADD CONSTRAINT chat_rooms_id_contractor_fkey FOREIGN KEY (id_contractor) REFERENCES public.contractor(id_contractor) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE public.chat_rooms ADD CONSTRAINT chat_rooms_id_job_fkey FOREIGN KEY (id_job) REFERENCES public.client_jobs(id_job) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE public.client_job_applications ADD CONSTRAINT client_job_applications_id_contractor_fkey FOREIGN KEY (id_contractor) REFERENCES public.contractor(id_contractor) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE public.client_job_applications ADD CONSTRAINT client_job_applications_id_job_fkey FOREIGN KEY (id_job) REFERENCES public.client_jobs(id_job) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE public.client_job_execution ADD CONSTRAINT client_job_execution_id_job_fkey FOREIGN KEY (id_job) REFERENCES public.client_jobs(id_job) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE public.client_job_execution ADD CONSTRAINT client_job_execution_id_contractor_leader_fkey FOREIGN KEY (id_contractor_leader) REFERENCES public.contractor(id_contractor) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE public.client_jobs ADD CONSTRAINT client_jobs_id_client_fkey FOREIGN KEY (id_client) REFERENCES public.client(id_client) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE public.messages ADD CONSTRAINT messages_id_chat_room_fkey FOREIGN KEY (id_chat_room) REFERENCES public.chat_rooms(id_chat_room) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE public.job_execution_contractor ADD CONSTRAINT fk_job_exec_contractor_exec FOREIGN KEY (job_execution_id) REFERENCES public.client_job_execution(id_execution) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE public.job_execution_contractor ADD CONSTRAINT fk_job_exec_contractor_contractor FOREIGN KEY (contractor_id) REFERENCES public.contractor(id_contractor) ON UPDATE CASCADE ON DELETE CASCADE;