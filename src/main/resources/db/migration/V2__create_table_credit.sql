CREATE TABLE Credit (
  id BIGINT AUTO_INCREMENT NOT NULL,
   credit_code UUID NOT NULL,
   credit_value DECIMAL NOT NULL,
   day_first_installment date NOT NULL,
   number_of_installments INT NOT NULL,
   status INT,
   customer_id BIGINT,
   CONSTRAINT pk_credit PRIMARY KEY (id)
);

ALTER TABLE Credit ADD CONSTRAINT uc_credit_creditcode UNIQUE (credit_code);

ALTER TABLE Credit ADD CONSTRAINT FK_CREDIT_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES Customer (id);