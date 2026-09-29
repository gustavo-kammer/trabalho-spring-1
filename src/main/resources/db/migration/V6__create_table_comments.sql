CREATE TABLE comments (
    id          BIGSERIAL PRIMARY KEY,
    post_id     BIGINT     NOT NULL,
    user_id     BIGINT     NOT NULL,
    content     TEXT       NOT NULL,
    created_at  TIMESTAMP  NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP,
    deleted_at  TIMESTAMP,

    CONSTRAINT fk_comments_posts FOREIGN KEY (post_id)
        REFERENCES posts (id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_users FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_comments_post_id ON comments (post_id);
CREATE INDEX idx_comments_user_id ON comments (user_id);
