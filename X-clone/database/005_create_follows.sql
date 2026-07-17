CREATE TABLE follows
(
    follower_id  INTEGER NOT NULL,
    following_id INTEGER NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (follower_id, following_id),

    FOREIGN KEY (follower_id) REFERENCES users(id) ON DELETE CASCADE,

    FOREIGN KEY (following_id) REFERENCES users(id) ON DELETE CASCADE,

    CHECK (follower_id <> following_id)
);