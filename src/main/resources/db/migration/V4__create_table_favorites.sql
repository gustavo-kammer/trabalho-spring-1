CREATE TABLE favorites (
    id          BIGSERIAL  PRIMARY KEY,
    post_id     BIGINT     NOT NULL,
    user_id     BIGINT     NOT NULL,
    created_at  TIMESTAMP  NOT NULL DEFAULT now(),
    deleted_at  TIMESTAMP,

    CONSTRAINT uk_favorites_post_user UNIQUE (post_id, user_id),
    CONSTRAINT fk_favorites_posts FOREIGN KEY (post_id)
        REFERENCES posts (id) ON DELETE CASCADE,
    CONSTRAINT fk_favorites_users FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_favorites_user_id ON favorites (user_id);
