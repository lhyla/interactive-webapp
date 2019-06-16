CREATE TABLE if not exists public."data" (
	id int8 NOT NULL,
	engineering_unit varchar(255) NOT NULL,
	measurement_date timestamp NULL,
	quality varchar(255) NOT NULL,
	value numeric(19,2) NULL,
	CONSTRAINT data_pkey PRIMARY KEY (id)
);

CREATE SEQUENCE if not exists public.data_seq
	INCREMENT BY 50
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 2351
	CACHE 1
	NO CYCLE;