CREATE TABLE tweet_hashtags (
    tweet_id INT NOT NULL REFERENCES tweets(id) ON DELETE CASCADE,
    hashtag_id INT NOT NULL REFERENCES hashtags(id) ON DELETE CASCADE,
    PRIMARY KEY (tweet_id, hashtag_id)
);