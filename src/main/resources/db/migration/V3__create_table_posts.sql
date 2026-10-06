CREATE TYPE post_status AS ENUM (
    'ACTIVE',
    'ARCHIVED',
    'DELETED'
);

CREATE TABLE posts (
    id             BIGSERIAL PRIMARY KEY,
    title          VARCHAR(200)     NOT NULL,
    user_id        BIGINT           NOT NULL,
    photo_url      VARCHAR(500),
    description    TEXT,
    ingredients    TEXT             NOT NULL,
    instructions   TEXT             NOT NULL,
    categories  recipe_category[]  NOT NULL DEFAULT '{}',
    status         post_status      NOT NULL DEFAULT 'ACTIVE',
    created_at     TIMESTAMP        NOT NULL DEFAULT now(),
    updated_at     TIMESTAMP,
    deleted_at     TIMESTAMP,

    CONSTRAINT fk_posts_users FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_posts_user_id ON posts (user_id);
CREATE INDEX idx_posts_categories ON posts USING GIN (categories);
CREATE INDEX idx_posts_status ON posts (status);
