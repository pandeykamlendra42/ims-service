
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

--- Seed Dummy Data
INSERT INTO supplier ("name", "contact_information") VALUES
('supplier 2', 'contact'),
('supplier 3', 'contact'),
('supplier 4', 'contact');


INSERT INTO product ("id", "name", "supplier_id", "price", "stock_quantity", "images") VALUES
('47cb49df-df6e-4231-b6c2-e7916c267efc', 'Product 1', 1, 14500.00, 15, '{image1,image1}'),
('57bb49df-df6e-4231-b6c2-e7916c267efc', 'Product 2', 3, 1500.00, 1650, '{image2,image3}'),
('67bb49df-df6e-4231-b6c2-e7916c267efc', 'Product 3', 2, 180.00, 150, '{image3,image4}'),
('27bb49df-df6e-4231-b6c2-e7916c267efe', 'Product 4', 2, 2510.00, 190, '{image5,image5}');