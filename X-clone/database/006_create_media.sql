CREATE TABLE media (

    id SERIAL PRIMARY KEY,

    tweet_id INTEGER NOT NULL REFERENCES tweets(id) ON DELETE CASCADE,

    media_url TEXT NOT NULL,

    media_type VARCHAR(20) NOT NULL,

    media_order INTEGER NOT NULL DEFAULT 0
);