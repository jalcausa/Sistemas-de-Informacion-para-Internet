CREATE TABLE VUELO (
    ID BIGINT NOT NULL,
    destino_iata VARCHAR,
    origen_iata VARCHAR,
    codigo_iata VARCHAR,
    fecha_llegada DATE,
    fecha_salida DATE,
    hora_llegada TIME,
    hora_salida TIME,
    PRIMARY KEY (ID))
CREATE TABLE PLAZA (
    ID BIGINT NOT NULL,
    FILA INTEGER,
    LETRA CHAR,
    TIPO VARCHAR,
    vuelo BIGINT,
    PRIMARY KEY (ID))
ALTER TABLE PLAZA ADD CONSTRAINT FK_PLAZA_vuelo FOREIGN KEY (vuelo) REFERENCES VUELO (ID)
