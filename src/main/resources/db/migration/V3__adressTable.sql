ALTER TABLE address
ADD COLUMN id_job INT REFERENCES client_jobs(id_job) ON DELETE CASCADE ON UPDATE CASCADE NULL;

ALTER TABLE address
DROP CONSTRAINT chk_address_owner;

ALTER TABLE address
ADD CONSTRAINT chk_address_owner CHECK (
    (owner_type = 'CLIENT' AND id_client IS NOT NULL AND id_contractor IS NULL) OR
    (owner_type = 'CONTRACTOR' AND id_contractor IS NOT NULL AND id_client IS NULL) OR
    (owner_type = 'JOB' AND id_job IS NOT NULL AND id_client IS NULL AND id_contractor IS NULL)
);


ALTER TABLE address
DROP CONSTRAINT uq_owner_address_details;

ALTER TABLE address
ADD CONSTRAINT uq_owner_address_details UNIQUE (owner_type, id_client, id_contractor, id_job, street, number, zip_code);


CREATE INDEX idx_address_id_job ON address(id_job) WHERE id_job IS NOT NULL;