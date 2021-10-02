CREATE DATABASE testprojdb;

\c testprojdb;

CREATE TABLE IF NOT EXISTS public.department(
    dept_no integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    budget double precision NOT NULL,
    CONSTRAINT "DEPARTMENT_pkey" PRIMARY KEY (dept_no)
);

CREATE TABLE IF NOT EXISTS public.employee(
    emp_no integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    dept_no integer NOT NULL,
    first_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    last_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    hire_date date NOT NULL,
    salary double precision NOT NULL,
    CONSTRAINT "EMPLOYEE_pkey" PRIMARY KEY (emp_no),
    CONSTRAINT "FK_DEPARTMENT_EMPLOYEE" FOREIGN KEY (dept_no)
        REFERENCES public.department (dept_no) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);