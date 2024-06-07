
CREATE SEQUENCE IF NOT EXISTS supplier_id_seq;
CREATE TABLE IF NOT EXISTS supplier (
    id                             BIGINT NOT NULL DEFAULT nextval('supplier_id_seq'),
    created_at                     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    modified_at                    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    is_deleted                     BOOLEAN NOT NULL DEFAULT FALSE,
    name                           TEXT NOT NULL,
    contact_information            TEXT,
    PRIMARY KEY(id)
);


CREATE TABLE IF NOT EXISTS product (
    id                             UUID NOT NULL,
    created_at                     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    modified_at                    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    is_deleted                     BOOLEAN NOT NULL DEFAULT FALSE,
    name                           TEXT NOT NULL,
    supplier_id                    BIGINT NOT NULL,
    price                          NUMERIC(10, 2) NOT NULL,
    stock_quantity                 BIGINT NOT NULL,
    images                         TEXT[],
    PRIMARY KEY(id),
    CONSTRAINT images_length CHECK (array_length(images, 1) <= 10),
    CONSTRAINT fk_supplier
          FOREIGN KEY(supplier_id)
    	  REFERENCES supplier(id)
    	  ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS product_supplier_id_index ON product (supplier_id);