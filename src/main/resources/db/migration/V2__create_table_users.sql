CREATE TYPE user_status AS ENUM (
    'ACTIVE',
    'DELETED'
);

CREATE TABLE users (
    id                    BIGSERIAL PRIMARY KEY,
    first_name            VARCHAR(150)       NOT NULL,
    last_name             VARCHAR(150),
    email                 VARCHAR(150)       NOT NULL UNIQUE,
    password              VARCHAR(255)       NOT NULL,
    nickname              VARCHAR(100),
    city_region           VARCHAR(150),
    bio                   TEXT,
    preferred_categories  recipe_category[]  NOT NULL DEFAULT '{}',
    status                user_status        NOT NULL DEFAULT 'ACTIVE',
    created_at            TIMESTAMP          NOT NULL DEFAULT now(),
    updated_at            TIMESTAMP,
    deleted_at            TIMESTAMP
);

CREATE UNIQUE INDEX uk_users_nickname ON users (nickname) WHERE nickname IS NOT NULL;
