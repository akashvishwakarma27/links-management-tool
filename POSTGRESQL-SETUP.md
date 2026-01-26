# PostgreSQL Setup Guide for Render

## 🚀 Step 1: Create PostgreSQL Database on Render

1. **Go to Render Dashboard**: https://dashboard.render.com
2. **Click "New +" → "PostgreSQL"**
3. **Configure Database**:
   - **Name**: `smartlinkfinder-db`
   - **Database Name**: `smartlinkfinder`
   - **User**: `smartlinkfinder_user`
   - **Region**: Choose closest to your users
   - **Plan**: Free (good for development)

4. **Click "Create Database"**

## 📋 Step 2: Get Database Credentials

After creation, Render will provide:
- **Database URL**: `postgresql://user:password@host:port/database`
- **Internal URL**: For Render services
- **External URL**: For external connections

## 🔧 Step 3: Update Backend Configuration

The backend is already configured to use Render environment variables:

```properties
# application.properties
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USER}
spring.datasource.password=${DATABASE_PASSWORD}
```

## 🚀 Step 4: Deploy Backend with Database

1. **Connect Backend to Database**:
   - Go to your backend service on Render
   - Click "Environment" tab
   - Add environment variables from Step 2:
     - `DATABASE_URL` = (your database URL)
     - `DATABASE_USER` = (your database user)
     - `DATABASE_PASSWORD` = (your database password)

2. **Redeploy Backend**:
   - Click "Manual Deploy" → "Deploy Latest Commit"

## ✅ Step 5: Verify Database Persistence

1. **Test the application**:
   - Add some links via admin panel
   - Wait for Render to restart (free tier ~15-30 minutes)
   - Check if data persists

2. **Check Database**:
   - Go to PostgreSQL dashboard on Render
   - Click "Query" tab
   - Run: `SELECT COUNT(*) FROM links;`

## 🔐 Step 6: Default Users

The system will automatically create:
- **Admin**: `admin/admin123`
- **Super Admin**: `akash_super_admin/akash123`

## 📊 Benefits of PostgreSQL over H2

✅ **True Persistence**: Data survives service restarts  
✅ **Production Ready**: Built for production workloads  
✅ **Scalability**: Handles concurrent connections  
✅ **Backup Support**: Render provides automatic backups  
✅ **Performance**: Optimized for production queries  

## 🚨 Troubleshooting

### If Backend Fails to Start:
1. Check environment variables are correct
2. Verify database is running on Render
3. Check Render logs for connection errors

### If Data Still Disappears:
1. Verify `DATABASE_URL` is set correctly
2. Check if using internal or external URL
3. Ensure PostgreSQL plan is active

### If Login Issues:
1. Clear browser cache and localStorage
2. Verify default users were created
3. Check database for users table

## 📞 Support

If you encounter issues:
1. Check Render service logs
2. Verify database connectivity
3. Test with the login-test.html file
