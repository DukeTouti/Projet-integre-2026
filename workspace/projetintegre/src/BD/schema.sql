CREATE DATABASE IF NOT EXISTS projetintegre
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE projetintegre;
SET NAMES utf8mb4;
SET lower_case_table_names = 0;

CREATE TABLE utilisateur (
    idUtilisateur   INT AUTO_INCREMENT PRIMARY KEY,
    nom             VARCHAR(50)     NOT NULL,
    prenom          VARCHAR(50)     NOT NULL,
    email           VARCHAR(100)    NOT NULL UNIQUE,
    motDePasse      VARCHAR(255)    NOT NULL,
    role            VARCHAR(20)     NOT NULL,
    actif           BOOLEAN         NOT NULL DEFAULT TRUE
);

CREATE TABLE psh (
    idUtilisateur       INT             PRIMARY KEY,
    typeHandicap        VARCHAR(100)    NOT NULL,
    numeroEtudiant      VARCHAR(20)     NOT NULL UNIQUE,
    dateInscription     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_psh_utilisateur
        FOREIGN KEY (idUtilisateur) REFERENCES utilisateur(idUtilisateur)
        ON DELETE CASCADE
);

CREATE TABLE administrateur (
    idUtilisateur   INT             PRIMARY KEY,
    matricule       VARCHAR(20)     NOT NULL UNIQUE,
    departement     VARCHAR(100)    NOT NULL DEFAULT '',

    CONSTRAINT fk_administrateur_utilisateur
        FOREIGN KEY (idUtilisateur) REFERENCES utilisateur(idUtilisateur)
        ON DELETE CASCADE
);

CREATE TABLE dossier (
    idDossier       INT AUTO_INCREMENT PRIMARY KEY,
    description     TEXT            NOT NULL,
    dateCreation    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    dateMaj         DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    statut          VARCHAR(20)     NOT NULL DEFAULT 'EN_ATTENTE',
    typeDossier     VARCHAR(20)     NOT NULL,
    archive         BOOLEAN         NOT NULL DEFAULT FALSE,
    dateArchivage   DATETIME        NULL DEFAULT NULL,
    idAuteur        INT             NOT NULL,

    CONSTRAINT fk_dossier_auteur
        FOREIGN KEY (idAuteur) REFERENCES utilisateur(idUtilisateur)
        ON DELETE CASCADE
);

CREATE TABLE demande (
    idDossier       INT             PRIMARY KEY,
    typeDemande     VARCHAR(30)     NOT NULL,
    commentaire     TEXT            NOT NULL DEFAULT '',

    CONSTRAINT fk_demande_dossier
        FOREIGN KEY (idDossier) REFERENCES dossier(idDossier)
        ON DELETE CASCADE
);

CREATE TABLE reclamation (
    idDossier       INT             PRIMARY KEY,
    motif           TEXT            NOT NULL,
    reponse         TEXT            NOT NULL DEFAULT '',
    idDemande       INT             NOT NULL,

    CONSTRAINT fk_reclamation_dossier
        FOREIGN KEY (idDossier) REFERENCES dossier(idDossier)
        ON DELETE CASCADE,

    CONSTRAINT fk_reclamation_demande
        FOREIGN KEY (idDemande) REFERENCES demande(idDossier)
        ON DELETE CASCADE
);

CREATE TABLE pieceJustificative (
    idPiece         INT AUTO_INCREMENT PRIMARY KEY,
    nomFichier      VARCHAR(255)    NOT NULL,
    cheminFichier   VARCHAR(500)    NOT NULL,
    dateAjout       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    typeFichier     VARCHAR(50)     NOT NULL,
    idDemande       INT             NOT NULL,

    CONSTRAINT fk_piece_demande
        FOREIGN KEY (idDemande) REFERENCES demande(idDossier)
        ON DELETE CASCADE
);