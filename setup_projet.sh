#!/bin/bash

# ============================================================
#  Script de configuration - Projet Intégré 2026
#  UIR / ESIN - 3A Cybersécurité
# ============================================================

set -e

REPO_URL="git@github.com:DukeTouti/Projet-integre-2026.git"
REPO_DIR="Projet-integre-2026"

echo "========================================"
echo "  Setup - Projet Intégré 2026"
echo "========================================"
echo ""

# --- 1. Vérification des prérequis ---
echo "[1/5] Vérification des prérequis..."

if ! command -v git &> /dev/null; then
    echo "  [ERREUR] Git n'est pas installé."
    echo "  Installe-le avec : sudo apt install git  (Linux/Ubuntu)"
    echo "                 ou : https://git-scm.com/downloads  (Windows/Mac)"
    exit 1
fi

if ! command -v java &> /dev/null; then
    echo "  [AVERTISSEMENT] Java non détecté. Assure-toi d'avoir le JDK installé."
fi

echo "  [OK] Git détecté : $(git --version)"
echo ""

# --- 2. Configuration Git personnelle ---
echo "[2/5] Configuration Git..."

GIT_NAME=$(git config --global user.name 2>/dev/null || echo "")
GIT_EMAIL=$(git config --global user.email 2>/dev/null || echo "")

if [ -z "$GIT_NAME" ]; then
    read -p "  Ton prénom et nom (ex: Yassine Alami) : " GIT_NAME
    git config --global user.name "$GIT_NAME"
fi

if [ -z "$GIT_EMAIL" ]; then
    read -p "  Ton email GitHub : " GIT_EMAIL
    git config --global user.email "$GIT_EMAIL"
fi

echo "  [OK] Configuré en tant que : $GIT_NAME <$GIT_EMAIL>"
echo ""

# --- 3. Vérification de la clé SSH ---
echo "[3/5] Vérification de la clé SSH..."

if [ ! -f "$HOME/.ssh/id_rsa.pub" ] && [ ! -f "$HOME/.ssh/id_ed25519.pub" ]; then
    echo "  [INFO] Aucune clé SSH trouvée. Génération d'une nouvelle clé..."
    ssh-keygen -t ed25519 -C "$GIT_EMAIL" -f "$HOME/.ssh/id_ed25519" -N ""
    echo ""
    echo "  ============================================================"
    echo "  IMPORTANT : Ajoute cette clé publique à ton compte GitHub"
    echo "  GitHub > Settings > SSH and GPG keys > New SSH key"
    echo "  ============================================================"
    echo ""
    cat "$HOME/.ssh/id_ed25519.pub"
    echo ""
    read -p "  Appuie sur [Entrée] une fois la clé ajoutée à GitHub..."
else
    echo "  [OK] Clé SSH existante trouvée."
fi

echo "  Test de la connexion SSH à GitHub..."
if ssh -T git@github.com 2>&1 | grep -q "successfully authenticated"; then
    echo "  [OK] Connexion SSH à GitHub réussie."
else
    echo "  [AVERTISSEMENT] Connexion SSH non confirmée (normal si première connexion)."
fi
echo ""

# --- 4. Clonage du dépôt ---
echo "[4/5] Clonage du dépôt..."

if [ -d "$REPO_DIR" ]; then
    echo "  [INFO] Le dossier '$REPO_DIR' existe déjà. Mise à jour..."
    cd "$REPO_DIR"
    git config --global --add safe.directory "$(pwd)" 2>/dev/null || true
    git fetch --all
    git pull origin master
else
    git clone "$REPO_URL"
    cd "$REPO_DIR"
    git config --global --add safe.directory "$(pwd)" 2>/dev/null || true
    git fetch --all
fi

echo "  [OK] Dépôt cloné avec succès."
echo ""
echo "  Branches disponibles :"
git branch -r | grep -v HEAD
echo ""

# --- 5. Création de la branche personnelle ---
echo "[5/5] Création de ta branche de travail..."

read -p "  Ton prénom en minuscules (ex: yassine) : " PRENOM
BRANCH_NAME="$PRENOM"

if git ls-remote --exit-code --heads origin "$BRANCH_NAME" &>/dev/null; then
    echo "  [INFO] La branche '$BRANCH_NAME' existe déjà, récupération..."
    git checkout -b "$BRANCH_NAME" --track "origin/$BRANCH_NAME"
else
    git checkout -b "$BRANCH_NAME"
    git push --set-upstream origin "$BRANCH_NAME"
    echo "  [OK] Branche '$BRANCH_NAME' créée et poussée sur GitHub."
fi

echo ""
echo "========================================"
echo "  Tout est prêt ! Récapitulatif :"
echo "========================================"
echo "  Dossier   : $(pwd)"
echo "  Branche   : $(git branch --show-current)"
echo "  Remote    : $REPO_URL"
echo ""
echo "  Commandes utiles :"
echo "    git status               -> voir les fichiers modifiés"
echo "    git add .                -> préparer tes modifications"
echo "    git commit -m 'message'  -> enregistrer tes modifications"
echo "    git push                 -> envoyer sur GitHub"
echo "    git pull origin master   -> récupérer les dernières modifs du master"
echo "========================================"
