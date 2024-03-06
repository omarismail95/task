-- Dropping tables
DROP TABLE IF EXISTS authority;
DROP TABLE IF EXISTS users;

-- Start - Spring Security tables definition
---- Start - User table definition

CREATE TABLE IF NOT EXISTS users
(
    id                INT AUTO_INCREMENT          PRIMARY KEY,
    user_uuid         UUID UNIQUE                    NOT NULL,
    first_name        CHARACTER VARYING(100)         NOT NULL,
    last_name         CHARACTER VARYING(100)         NOT NULL,
    username          VARCHAR_IGNORECASE(255) UNIQUE NOT NULL,
    password          VARCHAR_IGNORECASE(255)        NOT NULL,
    enabled           BOOLEAN                        NOT NULL,
    refresh_token     UUID UNIQUE,
    token_expiry_date DATETIME
);
---- End - User table definition

---- Start - Authorities table definition
CREATE TABLE IF NOT EXISTS authority
(
    user_id   INT                     NOT NULL,
    authority ENUM ('ADMIN','NORMAL','CONTENT_WRITER') NOT NULL,

    CONSTRAINT authority_pk
        PRIMARY KEY (user_id, authority),

    CONSTRAINT authority_user_fk
        FOREIGN KEY (user_id)
            REFERENCES users (id)
);
---- End - Authorities table definition

-- End - Spring Security tables definition