CREATE TABLE likes (
    id          BIGSERIAL  PRIMARY KEY,
    post_id     BIGINT     NOT NULL,
    user_id     BIGINT     NOT NULL,
    created_at  TIMESTAMP  NOT NULL DEFAULT now(),
    deleted_at  TIMESTAMP,

    CONSTRAINT uk_likes_post_user UNIQUE (post_id, user_id),
    CONSTRAINT fk_likes_posts FOREIGN KEY (post_id)
        REFERENCES posts (id) ON DELETE CASCADE,
    CONSTRAINT fk_likes_users FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_likes_user_id ON likes (user_id);
