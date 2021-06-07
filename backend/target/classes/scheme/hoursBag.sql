create sequence hours_bag_seq start 1 increment 1

CREATE TABLE public.hours_bag (
	id bigint NOT NULL,
	CONSTRAINT hours_bag_pk PRIMARY KEY (id),
	init_date timestamp(0) NULL,
	end_date timestamp(0) NULL,
	hours bigint NULL,
	created_at timestamp(0) NULL,
	contract_id bigint NULL,
	CONSTRAINT hours_bag_fk FOREIGN KEY (contract_id) REFERENCES public.contract(id)
	ON DELETE CASCADE
);