CREATE TABLE teams(
    id INT NOT NULL,
    name IN VARCHAR(128),
    CONSTRAINT name_length CHECK(LENGTH(name) >= 3 OR name IS NULL,
    firstName IN VARCHAR(128),
    CONSTRAINT firstName_length CHECK(LENGTH(firstName) >= 3 OR name IS NULL,
    PRIMARY KEY (id))
);

CREATE INDEX name_and_id_index ON teams(id, name);

CREATE TABLE playes(
    id INT NOT NULL,
    firstName IN VARCHAR(128),
    CONSTRAINT firstName_length CHECK(LENGTH(firstName) >= 3 OR name IS NULL,
    teamId IN VARCHAR(128),
    CONSTRAINT teamId_length CHECK(LENGTH(teamId) >= 3 OR name IS NULL,
    PRIMARY KEY (id))
)