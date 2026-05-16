#!/bin/bash

# 1. On se place à la racine du projet
cd ~/Documents/Projet-integre-2026/

# 2. On recrée le dossier lib
mkdir -p lib
cd lib

echo "🚀 Téléchargement des bibliothèques dans $(pwd)..."

# 3. Téléchargement des 5 JARs (avec User-Agent pour éviter les blocages)
UA="Mozilla/5.0"

echo "📥 Téléchargement de Vosk (0.3.45)..."
wget -q --no-check-certificate --user-agent="$UA" https://repo1.maven.org/maven2/com/alphacephei/vosk/0.3.45/vosk-0.3.45.jar

echo "📥 Téléchargement de JNA (5.13.0)..."
wget -q --no-check-certificate --user-agent="$UA" https://repo1.maven.org/maven2/net/java/dev/jna/jna/5.13.0/jna-5.13.0.jar

echo "📥 Téléchargement de JNA Platform (5.13.0)..."
wget -q --no-check-certificate --user-agent="$UA" https://repo1.maven.org/maven2/net/java/dev/jna/jna-platform/5.13.0/jna-platform-5.13.0.jar

echo "📥 Téléchargement de JLayer (1.0.1)..."
wget -q --no-check-certificate --user-agent="$UA" https://repo1.maven.org/maven2/javazoom/jlayer/1.0.1/jlayer-1.0.1.jar

echo "📥 Téléchargement de JSON-Simple (1.1.1)..."
wget -q --no-check-certificate --user-agent="$UA" https://repo1.maven.org/maven2/com/googlecode/json-simple/json-simple/1.1.1/json-simple-1.1.1.jar

echo "✅ Terminé ! Voici le contenu de ton dossier lib :"
ls -lh
