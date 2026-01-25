# GSK Smart Link Finder - Production Ready

## 🚀 Project Overview
A comprehensive smart link management system for GSK with brand and country detection, admin dashboard, and user-friendly search interface.

## 📁 Project Structure
```
Links_management_tool/
├── admin-side-gsk.html      # Admin Dashboard (Production Ready)
├── index.html               # User Search Interface (Production Ready)
├── admin-management.html    # Admin Management (Production Ready)
├── backend/                 # Spring Boot Backend
├── README.md               # This file
└── .vscode/               # Development settings
```

## ✨ Features

### Admin Dashboard (`admin-side-gsk.html`)
- ✅ **Authentication** - Secure login with JWT tokens
- ✅ **Dashboard Stats** - Total links, brands, countries, and admins
- ✅ **Link Management** - Add, edit, delete links with themed confirmations
- ✅ **Search & Filter** - Real-time search with pagination
- ✅ **Excel Upload** - Bulk import functionality
- ✅ **Themed Notifications** - #f36633 and #ffffff color scheme
- ✅ **Responsive Design** - Works on all devices

### User Interface (`index.html`)
- ✅ **Smart Search** - Search by reference code, brand, country, or URL
- ✅ **Brand/Country Detection** - Automatic extraction from URLs
- ✅ **Responsive Layout** - Mobile-friendly design
- ✅ **Direct Links** - One-click access to resources

### Admin Management (`admin-management.html`)
- ✅ **User Management** - Add, edit, delete admin users
- ✅ **Role Management** - Super Admin and Admin roles
- ✅ **Secure Operations** - Protected admin functions

## 🎨 Theme & Branding
- **Primary Color**: #f36633 (GSK Orange)
- **Secondary Color**: #ffffff (White)
- **Consistent Design**: All components follow GSK branding
- **Professional UI**: Clean, modern interface

## 🔧 Technical Stack

### Frontend
- **HTML5** - Semantic markup
- **CSS3** - Modern styling with animations
- **Vanilla JavaScript** - No dependencies, lightweight
- **Responsive Design** - Mobile-first approach

### Backend
- **Spring Boot 3.x** - Java framework
- **MySQL** - Database
- **JWT Authentication** - Secure token-based auth
- **RESTful APIs** - Standard endpoints

## 🚀 Deployment Instructions

### Prerequisites
- Java 21+ installed
- MySQL database running
- Web server (Apache/Nginx) or Spring Boot embedded

### Backend Setup
```bash
cd backend
./mvnw clean package
java -jar target/smart-link-finder-1.0.0.jar
```

### Frontend Deployment
1. Copy all `.html` files to web server root
2. Ensure backend API is accessible at `http://localhost:8080/api`
3. Configure CORS if needed

### Database Configuration
Update `backend/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/link_manager
spring.datasource.username=root
spring.datasource.password=your_password
```

## 📊 API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register-admin` - Register admin

### Links Management
- `GET /api/links` - Get all links (paginated)
- `POST /api/links` - Add new link
- `PUT /api/links/{id}` - Update link
- `DELETE /api/links/{id}` - Delete link
- `GET /api/links/search` - Search links

### Admin Management
- `GET /api/auth/admins` - Get all admins
- `POST /api/auth/register-admin` - Register admin
- `DELETE /api/auth/admins/{id}` - Delete admin

## 🔐 Security Features
- JWT token authentication
- Role-based access control
- CORS protection
- Input validation
- SQL injection prevention

## 🌐 Browser Compatibility
- ✅ Chrome 90+
- ✅ Firefox 88+
- ✅ Safari 14+
- ✅ Edge 90+

## 📱 Responsive Breakpoints
- **Mobile**: < 768px
- **Tablet**: 768px - 1024px
- **Desktop**: > 1024px

## 🎯 Key Features Highlight

### Smart Brand/Country Detection
- Automatically extracts brand names from URLs like `/pharma/pakistan/singrix/pi_33001.pdf`
- Supports complex URL patterns
- Fallback to reference code mapping

### Themed Delete Confirmation
- Beautiful modal with GSK branding
- Clear confirmation messages
- Smooth animations and transitions

### Real-time Notifications
- Success/error messages with theme colors
- Auto-dismiss after timeout
- Non-intrusive positioning

## 📈 Performance Optimizations
- Lazy loading for large datasets
- Efficient pagination
- Optimized API calls
- Minimal dependencies

## 🛠️ Maintenance
- Regular security updates
- Database optimization
- Performance monitoring
- Backup procedures

## 📞 Support
For technical issues or questions, contact the development team.

---

**Version**: 1.0.0  
**Last Updated**: January 2026  
**Status**: Production Ready ✅
