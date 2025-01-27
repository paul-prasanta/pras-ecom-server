--
-- PostgreSQL database dump
--

-- Dumped from database version 17.2
-- Dumped by pg_dump version 17.2

-- Started on 2025-01-26 08:34:16

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 6 (class 2615 OID 16476)
-- Name: ecom; Type: SCHEMA; Schema: -; Owner: fiddle
--

CREATE SCHEMA ecom;


ALTER SCHEMA ecom OWNER TO fiddle;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 221 (class 1259 OID 16400)
-- Name: customer; Type: TABLE; Schema: ecom; Owner: fiddle
--

CREATE TABLE ecom.customer (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    gender integer,
    mobile_number character varying(20) NOT NULL,
    email character varying(50),
    update_time timestamp with time zone NOT NULL,
    creation_time timestamp with time zone NOT NULL
);


ALTER TABLE ecom.customer OWNER TO fiddle;

--
-- TOC entry 220 (class 1259 OID 16399)
-- Name: customer_id_seq; Type: SEQUENCE; Schema: ecom; Owner: fiddle
--

CREATE SEQUENCE ecom.customer_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE ecom.customer_id_seq OWNER TO fiddle;

--
-- TOC entry 4837 (class 0 OID 0)
-- Dependencies: 220
-- Name: customer_id_seq; Type: SEQUENCE OWNED BY; Schema: ecom; Owner: fiddle
--

ALTER SEQUENCE ecom.customer_id_seq OWNED BY ecom.customer.id;


--
-- TOC entry 223 (class 1259 OID 16407)
-- Name: product; Type: TABLE; Schema: ecom; Owner: fiddle
--

CREATE TABLE ecom.product (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    brand character varying(50) NOT NULL,
    category character varying(50),
    description text,
    price double precision NOT NULL,
    update_time timestamp with time zone NOT NULL,
    creation_time time without time zone NOT NULL
);


ALTER TABLE ecom.product OWNER TO fiddle;

--
-- TOC entry 222 (class 1259 OID 16406)
-- Name: product_id_seq; Type: SEQUENCE; Schema: ecom; Owner: fiddle
--

CREATE SEQUENCE ecom.product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE ecom.product_id_seq OWNER TO fiddle;

--
-- TOC entry 4838 (class 0 OID 0)
-- Dependencies: 222
-- Name: product_id_seq; Type: SEQUENCE OWNED BY; Schema: ecom; Owner: fiddle
--

ALTER SEQUENCE ecom.product_id_seq OWNED BY ecom.product.id;


--
-- TOC entry 225 (class 1259 OID 16416)
-- Name: sale; Type: TABLE; Schema: ecom; Owner: fiddle
--

CREATE TABLE ecom.sale (
    id integer NOT NULL,
    customer_id integer NOT NULL,
    total_price double precision NOT NULL,
    total_count integer NOT NULL,
    status integer DEFAULT 0 NOT NULL,
    note text,
    update_time timestamp with time zone NOT NULL,
    creation_time timestamp with time zone NOT NULL
);


ALTER TABLE ecom.sale OWNER TO fiddle;

--
-- TOC entry 224 (class 1259 OID 16415)
-- Name: sale_id_seq; Type: SEQUENCE; Schema: ecom; Owner: fiddle
--

CREATE SEQUENCE ecom.sale_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE ecom.sale_id_seq OWNER TO fiddle;

--
-- TOC entry 4839 (class 0 OID 0)
-- Dependencies: 224
-- Name: sale_id_seq; Type: SEQUENCE OWNED BY; Schema: ecom; Owner: fiddle
--

ALTER SEQUENCE ecom.sale_id_seq OWNED BY ecom.sale.id;


--
-- TOC entry 227 (class 1259 OID 16426)
-- Name: sale_item; Type: TABLE; Schema: ecom; Owner: fiddle
--

CREATE TABLE ecom.sale_item (
    id integer NOT NULL,
    sale_id integer NOT NULL,
    product_id integer NOT NULL,
    count integer,
    price double precision,
    update_time timestamp with time zone NOT NULL,
    creation_time timestamp with time zone NOT NULL
);


ALTER TABLE ecom.sale_item OWNER TO fiddle;

--
-- TOC entry 226 (class 1259 OID 16425)
-- Name: sale_item_id_seq; Type: SEQUENCE; Schema: ecom; Owner: fiddle
--

CREATE SEQUENCE ecom.sale_item_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE ecom.sale_item_id_seq OWNER TO fiddle;

--
-- TOC entry 4840 (class 0 OID 0)
-- Dependencies: 226
-- Name: sale_item_id_seq; Type: SEQUENCE OWNED BY; Schema: ecom; Owner: fiddle
--

ALTER SEQUENCE ecom.sale_item_id_seq OWNED BY ecom.sale_item.id;


--
-- TOC entry 219 (class 1259 OID 16390)
-- Name: user; Type: TABLE; Schema: ecom; Owner: fiddle
--

CREATE TABLE ecom."user" (
    id integer NOT NULL,
    user_name character varying(50) NOT NULL,
    password text NOT NULL,
    customer_id integer,
    role integer NOT NULL,
    status integer DEFAULT 0 NOT NULL,
    token text,
    update_time timestamp with time zone,
    creation_time timestamp with time zone
);


ALTER TABLE ecom."user" OWNER TO fiddle;

--
-- TOC entry 218 (class 1259 OID 16389)
-- Name: user_id_seq; Type: SEQUENCE; Schema: ecom; Owner: fiddle
--

CREATE SEQUENCE ecom.user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE ecom.user_id_seq OWNER TO fiddle;

--
-- TOC entry 4841 (class 0 OID 0)
-- Dependencies: 218
-- Name: user_id_seq; Type: SEQUENCE OWNED BY; Schema: ecom; Owner: fiddle
--

ALTER SEQUENCE ecom.user_id_seq OWNED BY ecom."user".id;


--
-- TOC entry 4664 (class 2604 OID 16403)
-- Name: customer id; Type: DEFAULT; Schema: ecom; Owner: fiddle
--

ALTER TABLE ONLY ecom.customer ALTER COLUMN id SET DEFAULT nextval('ecom.customer_id_seq'::regclass);


--
-- TOC entry 4665 (class 2604 OID 16410)
-- Name: product id; Type: DEFAULT; Schema: ecom; Owner: fiddle
--

ALTER TABLE ONLY ecom.product ALTER COLUMN id SET DEFAULT nextval('ecom.product_id_seq'::regclass);


--
-- TOC entry 4666 (class 2604 OID 16419)
-- Name: sale id; Type: DEFAULT; Schema: ecom; Owner: fiddle
--

ALTER TABLE ONLY ecom.sale ALTER COLUMN id SET DEFAULT nextval('ecom.sale_id_seq'::regclass);


--
-- TOC entry 4668 (class 2604 OID 16429)
-- Name: sale_item id; Type: DEFAULT; Schema: ecom; Owner: fiddle
--

ALTER TABLE ONLY ecom.sale_item ALTER COLUMN id SET DEFAULT nextval('ecom.sale_item_id_seq'::regclass);


--
-- TOC entry 4662 (class 2604 OID 16393)
-- Name: user id; Type: DEFAULT; Schema: ecom; Owner: fiddle
--

ALTER TABLE ONLY ecom."user" ALTER COLUMN id SET DEFAULT nextval('ecom.user_id_seq'::regclass);


--
-- TOC entry 4674 (class 2606 OID 16405)
-- Name: customer customer_pkey; Type: CONSTRAINT; Schema: ecom; Owner: fiddle
--

ALTER TABLE ONLY ecom.customer
    ADD CONSTRAINT customer_pkey PRIMARY KEY (id);


--
-- TOC entry 4678 (class 2606 OID 16414)
-- Name: product product_pkey; Type: CONSTRAINT; Schema: ecom; Owner: fiddle
--

ALTER TABLE ONLY ecom.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);


--
-- TOC entry 4682 (class 2606 OID 16431)
-- Name: sale_item sale_item_pkey; Type: CONSTRAINT; Schema: ecom; Owner: fiddle
--

ALTER TABLE ONLY ecom.sale_item
    ADD CONSTRAINT sale_item_pkey PRIMARY KEY (id);


--
-- TOC entry 4680 (class 2606 OID 16424)
-- Name: sale sale_pkey; Type: CONSTRAINT; Schema: ecom; Owner: fiddle
--

ALTER TABLE ONLY ecom.sale
    ADD CONSTRAINT sale_pkey PRIMARY KEY (id);


--
-- TOC entry 4676 (class 2606 OID 16577)
-- Name: customer unique_mobilenumber; Type: CONSTRAINT; Schema: ecom; Owner: fiddle
--

ALTER TABLE ONLY ecom.customer
    ADD CONSTRAINT unique_mobilenumber UNIQUE (mobile_number);


--
-- TOC entry 4670 (class 2606 OID 16579)
-- Name: user unique_username; Type: CONSTRAINT; Schema: ecom; Owner: fiddle
--

ALTER TABLE ONLY ecom."user"
    ADD CONSTRAINT unique_username UNIQUE (user_name);


--
-- TOC entry 4672 (class 2606 OID 16398)
-- Name: user user_pkey; Type: CONSTRAINT; Schema: ecom; Owner: fiddle
--

ALTER TABLE ONLY ecom."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- TOC entry 4684 (class 2606 OID 16571)
-- Name: sale sale_customer_id; Type: FK CONSTRAINT; Schema: ecom; Owner: fiddle
--

ALTER TABLE ONLY ecom.sale
    ADD CONSTRAINT sale_customer_id FOREIGN KEY (customer_id) REFERENCES ecom.customer(id) NOT VALID;


--
-- TOC entry 4685 (class 2606 OID 16585)
-- Name: sale_item sale_item_product_id; Type: FK CONSTRAINT; Schema: ecom; Owner: fiddle
--

ALTER TABLE ONLY ecom.sale_item
    ADD CONSTRAINT sale_item_product_id FOREIGN KEY (product_id) REFERENCES ecom.product(id) NOT VALID;


--
-- TOC entry 4686 (class 2606 OID 16580)
-- Name: sale_item sale_item_sale_id; Type: FK CONSTRAINT; Schema: ecom; Owner: fiddle
--

ALTER TABLE ONLY ecom.sale_item
    ADD CONSTRAINT sale_item_sale_id FOREIGN KEY (sale_id) REFERENCES ecom.sale(id) NOT VALID;


--
-- TOC entry 4683 (class 2606 OID 16566)
-- Name: user user_customer_id; Type: FK CONSTRAINT; Schema: ecom; Owner: fiddle
--

ALTER TABLE ONLY ecom."user"
    ADD CONSTRAINT user_customer_id FOREIGN KEY (customer_id) REFERENCES ecom.customer(id) NOT VALID;


-- Completed on 2025-01-26 08:34:16

--
-- PostgreSQL database dump complete
--

