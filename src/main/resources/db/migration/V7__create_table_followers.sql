CREATE TABLE followers (
    id                BIGSERIAL  PRIMARY KEY,
    user_id           BIGINT     NOT NULL,
    user_followed_id  BIGINT     NOT NULL,
    created_at        TIMESTAMP  NOT NULL DEFAULT now(),
    unfollowed_at     TIMESTAMP,

    CONSTRAINT uk_followers_user_followed UNIQUE (user_id, user_followed_id),
    CONSTRAINT fk_followers_user FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_followers_user_followed FOREIGN KEY (user_followed_id)
        REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT ck_followers_no_self_follow CHECK (user_id <> user_followed_id)
);

CREATE INDEX idx_followers_user_followed_id ON followers (user_followed_id);
