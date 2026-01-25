# 🔧 Smart Link Finder Tool

A secure, web-based application designed to centrally store, manage, and retrieve important links using short, human-readable reference codes (e.g., `PI-16282`).

## 🚀 Features

### Admin Panel (Full Access)
- ✅ Add, edit, and delete links
- ✅ Search and filter links
- ✅ Manage link categories and status
- ✅ View all links in sortable, paginated tables
- ✅ Role-based authentication with JWT

### User Panel (Read-Only Access)
- ✅ Search links using reference codes
- ✅ Copy links with one click
- ✅ Open links in new tabs
- ✅ Professional error messages for missing links

## 🛠 Technology Stack

### Backend
- **Java 17** with Spring Boot 3.2.0
- **Spring Security** with JWT authentication
- **Spring Data JPA** with Hibernate
- **MySQL 8.0** database
- **Maven** for dependency management

### Frontend
- **React 18** with modern hooks
- **React Router** for navigation
- **Axios** for API calls
- **Tailwind CSS** for styling
- **Lucide React** for icons

## 📋 Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Node.js 16 or higher
- npm or yarn

## 🛠 Setup Instructions

### 1. Database Setup

Create a MySQL database:

```sql
CREATE DATABASE smart_link_finder;
```

### 2. Backend Setup

1. Navigate to the backend directory:
```bash
cd backend
```

2. Update database credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/smart_link_finder?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
```

3. Build and run the application:
```bash
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### 3. Frontend Setup

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm start
```

The frontend will start on `http://localhost:3000`

## 🎯 Usage

### Default Admin Account
Create an admin account through the registration page or directly in the database:

```sql
INSERT INTO users (username, email, password, role, created_at, updated_at) 
VALUES ('admin', 'admin@example.com', '$2a$10$encrypted_password', 'ADMIN', NOW(), NOW());
```

### Access Points
- **User Search Interface**: `http://localhost:3000/search`
- **Admin Dashboard**: `http://localhost:3000/admin`
- **Login Page**: `http://localhost:3000/login`

## 📡 API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration

### Links (Public Access)
- `GET /api/links/reference/{referenceCode}` - Find link by reference code
- `GET /api/links/search?q={term}` - Search links

### Links (Admin Only)
- `GET /api/links` - Get all links (paginated)
- `POST /api/links` - Create new link
- `PUT /api/links/{id}` - Update link
- `DELETE /api/links/{id}` - Delete link

## 🔒 Security Features

- **JWT-based authentication** with configurable expiration
- **Role-based access control** (ADMIN/USER roles)
- **Password encryption** using BCrypt
- **CORS configuration** for cross-origin requests
- **Input validation** and sanitization
- **SQL injection protection** via JPA/Hibernate

## 📊 Database Schema

### Users Table
- `id` - Primary key
- `username` - Unique username
- `email` - Unique email
- `password` - Encrypted password
- `role` - ADMIN or USER
- `created_at` - Timestamp
- `updated_at` - Timestamp

### Links Table
- `id` - Primary key
- `reference_code` - Unique reference code (e.g., PI-16282)
- `full_url` - Complete URL
- `description` - Optional description
- `category` - Optional category
- `status` - ACTIVE or INACTIVE
- `added_by_id` - Foreign key to users table
- `created_at` - Timestamp
- `updated_at` - Timestamp

## 🎨 UI/UX Features

- **Responsive design** for mobile, tablet, and desktop
- **Modern, clean interface** with Tailwind CSS
- **Loading states** and error handling
- **One-click copy** functionality
- **Professional error messages**
- **Intuitive navigation** and user flow

## 🔧 Configuration

### JWT Settings
Update JWT secret and expiration in `application.properties`:
```properties
jwt.secret=your_secret_key_here
jwt.expiration=86400000  # 24 hours in milliseconds
```

### CORS Settings
Configure allowed origins in `application.properties`:
```properties
spring.web.cors.allowed-origins=http://localhost:3000
```

## 🚀 Deployment

### Backend Deployment
1. Build the JAR file:
```bash
mvn clean package
```

2. Run the JAR file:
```bash
java -jar target/smart-link-finder-1.0.0.jar
```

### Frontend Deployment
1. Build the React app:
```bash
npm run build
```

2. Deploy the `build` directory to your web server

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Verify MySQL is running
   - Check database credentials in `application.properties`
   - Ensure the database exists

2. **CORS Issues**
   - Verify frontend URL is in allowed origins
   - Check that backend is running on correct port

3. **JWT Token Issues**
   - Check JWT secret configuration
   - Verify token expiration time
   - Clear browser localStorage if needed

4. **Tailwind CSS Issues**
   - Run `npm install` to ensure all dependencies are installed
   - Restart the development server after installing Tailwind

## 📈 Future Enhancements

- [ ] Bulk upload of links via Excel/CSV
- [ ] Link usage analytics and reporting
- [ ] Auto-suggestions while typing reference codes
- [ ] QR code generation for each link
- [ ] Public REST API for third-party integrations
- [ ] Email notifications for missing links
- [ ] Version history for link updates

## 📄 License

This project is licensed under the MIT License.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📞 Support

For support and questions, please create an issue in the repository.
