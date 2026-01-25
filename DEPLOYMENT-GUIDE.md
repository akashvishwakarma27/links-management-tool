# Railway Deployment Guide for GSK Smart Link Finder

## 🚀 Step 1: Prepare Your Backend

### 1.1 Create Railway Account
1. Go to [railway.app](https://railway.app)
2. Sign up with GitHub/GitLab/Email
3. Get $5 free credit (enough for months of hosting)

### 1.2 Update application.properties
Edit `backend/src/main/resources/application.properties`:

```properties
# Railway Database Configuration
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USER}
spring.datasource.password=${DATABASE_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# Server Configuration
server.port=${PORT:8080}
server.servlet.context-path=/api

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# JWT Configuration
jwt.secret=smartLinkFinderSecretKey2024!@#$%^&*()
jwt.expiration=86400000

# CORS Configuration
spring.web.cors.allowed-origins=*
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.web.cors.allowed-headers=*
spring.web.cors.allow-credentials=true
```

### 1.3 Add PostgreSQL Dependency
Update `backend/pom.xml`:

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

### 1.4 Create Procfile
Create `backend/Procfile`:

```
web: java -jar target/smart-link-finder-1.0.0.jar
```

### 1.5 Update CORS in Controllers
In all your controller files, change:
```java
@CrossOrigin(origins = "*")
```

## 🚀 Step 2: Deploy to Railway

### 2.1 Using Railway CLI
```bash
# Install Railway CLI
npm install -g @railway/cli

# Login to Railway
railway login

# Initialize project
cd backend
railway init

# Create environment variables
railway variables set PORT=8080
railway variables set JAVA_VERSION=17

# Deploy
railway up
```

### 2.2 Using Railway Dashboard (Easier)
1. Go to [railway.app](https://railway.app)
2. Click "New Project" → "Deploy from GitHub"
3. Connect your GitHub repository
4. Select your backend folder
5. Railway will automatically detect Spring Boot
6. Click "Deploy"

## 🚀 Step 3: Configure Frontend

Update your frontend files to use the Railway URL:

### In admin-side-gsk.html and index.html:
```javascript
const API_BASE_URL = 'https://your-app-name.railway.app/api';
```

## 🚀 Step 4: Deploy Frontend

### Option 1: Vercel (Free)
```bash
# Install Vercel
npm install -g vercel

# Deploy frontend
vercel --prod
```

### Option 2: Netlify (Free)
1. Drag and drop HTML files to [netlify.com](https://netlify.com)
2. Get your URL and update API_BASE_URL

## 🎯 Free Tier Limits:

### Railway:
- ✅ $5 free credit
- ✅ 500 hours/month
- ✅ PostgreSQL database included
- ✅ Custom domains

### Vercel/Netlify:
- ✅ Unlimited static hosting
- ✅ Custom domains
- ✅ HTTPS certificates
- ✅ CDN included

## 🔧 Troubleshooting:

### Common Issues:
1. **Database Connection**: Use PostgreSQL instead of MySQL
2. **Port Issues**: Railway uses PORT environment variable
3. **CORS Issues**: Allow all origins during development
4. **Build Issues**: Ensure Maven builds successfully

### Environment Variables:
Railway automatically provides:
- `DATABASE_URL` - PostgreSQL connection string
- `PORT` - Server port
- `JAVA_VERSION` - Java version

## 🌟 Final Result:
- **Backend**: `https://your-app-name.railway.app`
- **Frontend**: `https://your-app.vercel.app` or Netlify
- **Database**: Managed PostgreSQL by Railway
- **Cost**: $0 (free tier)

## 📞 Support:
- Railway docs: [docs.railway.app](https://docs.railway.app)
- Vercel docs: [vercel.com/docs](https://vercel.com/docs)
- Netlify docs: [docs.netlify.com](https://docs.netlify.com)
