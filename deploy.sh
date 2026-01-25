#!/bin/bash

# GSK Smart Link Finder - Railway Deployment Script

echo "🚀 Starting Railway Deployment for GSK Smart Link Finder..."

# Step 1: Build the project
echo "📦 Building Spring Boot application..."
cd backend
./mvnw clean package -DskipTests

if [ $? -eq 0 ]; then
    echo "✅ Build successful!"
else
    echo "❌ Build failed!"
    exit 1
fi

# Step 2: Install Railway CLI (if not installed)
if ! command -v railway &> /dev/null; then
    echo "📦 Installing Railway CLI..."
    npm install -g @railway/cli
fi

# Step 3: Login to Railway
echo "🔐 Logging into Railway..."
railway login

# Step 4: Initialize Railway project
echo "🚀 Initializing Railway project..."
railway init

# Step 5: Set environment variables
echo "⚙️ Setting environment variables..."
railway variables set PORT=8080
railway variables set JAVA_VERSION=17

# Step 6: Deploy
echo "🚀 Deploying to Railway..."
railway up

echo "✅ Deployment complete!"
echo "🌐 Your backend will be available at: https://your-app-name.railway.app"
echo "📊 Check Railway dashboard for status and logs"
