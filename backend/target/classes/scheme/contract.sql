create sequence contract_seq start 1 increment 1

CREATE TABLE public.contract (
	id bigint NOT NULL,
	CONSTRAINT contract_pk PRIMARY KEY (id),
	"name" varchar(100) NOT NULL,
	beneficiary varchar(100) NOT NULL,
	created_at timestamp(0) NULL,

);
