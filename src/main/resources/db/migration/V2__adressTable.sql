DROP TABLE address_client;
DROP TABLE address_contractor;

CREATE TABLE address (
    id_address SERIAL PRIMARY KEY,
    street VARCHAR(100) NOT NULL,
    number VARCHAR(10) NOT NULL,
    neighborhood VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50) NOT NULL,
    zip_code VARCHAR(10) NOT NULL,
    -- Campos adicionais de endereço se necessário (complemento, ponto de referência, etc.)

    -- Coluna para identificar o tipo de "dono" do endereço
    owner_type VARCHAR(30) NOT NULL, -- Ex: 'CLIENT', 'CONTRACTOR'

    id_client INT REFERENCES client(id_client) ON DELETE CASCADE ON UPDATE CASCADE NULL,
    id_contractor INT REFERENCES contractor(id_contractor) ON DELETE CASCADE ON UPDATE CASCADE NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Constraint para garantir que apenas uma FK de "dono" seja preenchida e corresponda ao owner_type
    CONSTRAINT chk_address_owner CHECK (
        (owner_type = 'CLIENT' AND id_client IS NOT NULL AND id_contractor IS NULL) OR
        (owner_type = 'CONTRACTOR' AND id_contractor IS NOT NULL AND id_client IS NULL)
        -- Adicionar outras condições conforme novos owner_types são adicionados
    ),

    
    CONSTRAINT uq_owner_address_details UNIQUE (owner_type, id_client, id_contractor, street, number, zip_code)
);

-- Índices para as chaves estrangeiras e owner_type melhoram a performance
CREATE INDEX idx_address_id_client ON address(id_client) WHERE id_client IS NOT NULL;
CREATE INDEX idx_address_id_contractor ON address(id_contractor) WHERE id_contractor IS NOT NULL;
CREATE INDEX idx_address_owner_type ON address(owner_type);