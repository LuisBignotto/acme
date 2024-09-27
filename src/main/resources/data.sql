use `acme-airlines`

INSERT INTO roles (role_name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (role_name) VALUES ('ROLE_USER');
INSERT INTO roles (role_name) VALUES ('ROLE_BAGGAGE_MANAGER');
INSERT INTO roles (role_name) VALUES ('ROLE_SUPPORT');

INSERT INTO baggage_status (status) VALUES ('DESPACHADA');
INSERT INTO baggage_status (status) VALUES ('EM_ANALISE_DE_SEGURANCA');
INSERT INTO baggage_status (status) VALUES ('REPROVADA_PELA_ANALISE_DE_SEGURANCA');
INSERT INTO baggage_status (status) VALUES ('APROVADA_PELA_ANALISE_DE_SEGURANCA');
INSERT INTO baggage_status (status) VALUES ('NA_AERONAVE');
INSERT INTO baggage_status (status) VALUES ('EM_VOO');
INSERT INTO baggage_status (status) VALUES ('DESTINO_INCERTO');
INSERT INTO baggage_status (status) VALUES ('EXTRAVIADA');
INSERT INTO baggage_status (status) VALUES ('AGUARDANDO_RECOLETA');
INSERT INTO baggage_status (status) VALUES ('COLETADA');

commit;