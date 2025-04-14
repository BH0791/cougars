-- Ajout des contraintes sur la table 'teams'
ALTER TABLE teams
ADD CONSTRAINT name_unique UNIQUE (name);
-- Ajout des contraintes sur la table 'players'
ALTER TABLE players
ADD CONSTRAINT firstname_unique UNIQUE (firstName);
