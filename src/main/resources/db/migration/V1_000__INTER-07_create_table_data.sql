CREATE TABLE public."data" (
	id bigint NOT NULL,
	engineering_unit varchar(255) NOT NULL,
	measurement_date timestamp NULL,
	quality varchar(255) NOT NULL,
	value numeric(20,4) NULL,
	db_creation_date timestamp NULL DEFAULT statement_timestamp(),
	db_modify_date timestamp NULL DEFAULT statement_timestamp(),
	CONSTRAINT data_pkey PRIMARY KEY (id)
);

CREATE SEQUENCE if not exists public.data_seq
	INCREMENT BY 50
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 2351
	CACHE 1
	NO CYCLE;