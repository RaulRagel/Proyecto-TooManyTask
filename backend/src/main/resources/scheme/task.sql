create sequence task_seq start 1 increment 1

CREATE TABLE public.task (
	id bigint NOT NULL,
	CONSTRAINT task_pk PRIMARY KEY (id),
	title varchar(100) NOT NULL,
	description varchar(100) NULL,
	created_at timestamp(0) NULL,
	finished_at timestamp(0) NULL,
	invested_time decimal(5,2) NULL,
    priority varchar(10) NOT NULL,
    state  varchar(10) NOT NULL,
	contract_id bigint NULL,
	pin boolean not null,
	CONSTRAINT task_fk FOREIGN KEY (contract_id) REFERENCES public.contract(id)
	ON DELETE CASCADE
);