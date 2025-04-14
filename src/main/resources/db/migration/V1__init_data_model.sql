CREATE TABLE IF NOT EXISTS teams (
    id INT NOT NULL,
    name VARCHAR(128),
    CONSTRAINT name_length CHECK (LENGTH(name) >= 3 OR name IS NULL),
    firstName VARCHAR(128),
    CONSTRAINT firstName_length CHECK (LENGTH(firstName) >= 3 OR firstName IS NULL),
    PRIMARY KEY (id)
);

CREATE INDEX name_and_id_index ON teams (id, name);

CREATE TABLE IF NOT EXISTS players (
    id INT NOT NULL,
    firstName VARCHAR(128),
    CONSTRAINT firstName_length CHECK (LENGTH(firstName) >= 3 OR firstName IS NULL),
    teamId VARCHAR(128),
    CONSTRAINT teamId_length CHECK (LENGTH(teamId) >= 3 OR teamId IS NULL),
    PRIMARY KEY (id)
);