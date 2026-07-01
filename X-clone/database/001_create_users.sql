CREATE TABLE users (

    id SERIAL PRIMARY KEY,

    username VARCHAR(30) UNIQUE NOT NULL,
    display_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,

    password_hash TEXT NOT NULL,

    bio TEXT,

    profile_image_url TEXT,
    banner_image_url TEXT,

    is_private BOOLEAN DEFAULT FALSE,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);