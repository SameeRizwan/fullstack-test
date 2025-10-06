# Fullstack Developer Test - Spring Boot + HTMX + Web Awesome

This project demonstrates a fullstack application built with the specified tech stack for AI customer service app development.

## 🚀 Tech Stack

- **Backend**: Spring Boot 3.3.0 with Kotlin
- **Database**: PostgreSQL with Flyway migrations
- **Database Access**: JdbcClient (Spring 6+)
- **Frontend**: Thymeleaf server-side rendering + HTMX
- **UI Components**: Web Awesome design system
- **Build Tool**: Gradle with Kotlin DSL
- **Scheduling**: @Scheduled for background jobs
- **JVM**: Java 21 (latest LTS)

## 📋 Features

### Core Functionality
- ✅ **Product Management**: CRUD operations for products
- ✅ **External API Integration**: Fetches products from famme.no/products.json
- ✅ **Scheduled Jobs**: Auto-populates database on startup
- ✅ **Real-time Updates**: HTMX for seamless UI updates without page reloads
- ✅ **Search**: Live search functionality as you type
- ✅ **Modern UI**: Web Awesome components with responsive design

### Technical Features
- ✅ **Database Migrations**: Flyway for schema management
- ✅ **JdbcClient**: Modern Spring database access
- ✅ **JSONB Support**: Stores product variants as JSON
- ✅ **RESTful API**: Clean controller design
- ✅ **Error Handling**: Graceful error management
- ✅ **Responsive Design**: Mobile-friendly interface

## 🛠️ Setup Instructions

### Prerequisites
- **Java 21** (LTS version) - Required for compilation and runtime
- **Docker Desktop** (for PostgreSQL)
- **IntelliJ IDEA Ultimate** (EAP program for free access)

> ✅ **Note**: This project uses Java 21 LTS with Gradle 9.1.0+ for optimal compatibility and performance.

### Running the Application

#### Step 1: Start PostgreSQL Database
```bash
# Start the PostgreSQL container
docker-compose up -d postgres

# Verify the database is running
docker-compose ps
```

#### Step 2: Configure IntelliJ IDEA Ultimate
1. **Open the project** in IntelliJ IDEA Ultimate
2. **Configure database connection**:
   - Host: localhost
   - Port: 5432
   - Database: fullstack
   - Username: postgres
   - Password: postgres

#### Step 3: Run the Application
```bash
# Build and run the application
./gradlew bootRun
```

#### Step 4: Access the Application
- **Visit**: http://localhost:8080
- **Features**: Load products, add new products, search, edit, delete

#### Step 5: Stop Services
```bash
# Stop the application (Ctrl+C in terminal)

# Stop the database
docker-compose down
```

## 🎯 Test Requirements Fulfilled

### Simple Frontend + Backend Integration
- ✅ **Load Products Button**: Fetches products from database
- ✅ **Product Table**: Displays products with image, title, price, description
- ✅ **Add Product Form**: Creates new products with HTMX updates
- ✅ **Scheduled Job**: Fetches from famme.no/products.json (50 products max)
- ✅ **Database Design**: Handles product variants with JSONB
- ✅ **Web Awesome Styling**: Modern, responsive UI components

### AI Integration Features (Ready for Implementation)
- 🔄 **Search Page**: Live search with HTMX
- 🔄 **Edit Product**: Update product details
- 🔄 **Delete Product**: With confirmation dialog
- 🔄 **Custom Feature**: TBD (ready for AI implementation)

## 🌟 Key Technical Decisions

1. **JdbcClient over JPA**: Using Spring's new JdbcClient for better performance and control
2. **JSONB for Variants**: Flexible storage for product variants without complex joins
3. **HTMX over React**: Server-side rendering with modern interactivity
4. **Web Awesome**: Consistent design system with modern components
5. **Flyway Migrations**: Version-controlled database schema changes
6. **Kotlin DSL**: Type-safe Gradle configuration

## 🚀 Next Steps for AI Integration

The project is ready for AI-assisted feature development. The following features can be implemented using AI tools:

1. **Search Enhancement**: Advanced filtering and sorting
2. **Product Analytics**: Usage statistics and insights
3. **Bulk Operations**: Multi-select and batch actions
4. **Export/Import**: CSV/JSON data exchange
5. **API Documentation**: OpenAPI/Swagger integration

## 📊 Performance Considerations

- **Database Indexing**: Optimized queries with proper indexes
- **HTMX Efficiency**: Minimal data transfer with targeted updates
- **Connection Pooling**: Efficient database connection management
- **Caching Strategy**: Ready for Redis integration if needed

## 🔒 Security Features

- **SQL Injection Prevention**: Parameterized queries with JdbcClient
- **XSS Protection**: Thymeleaf auto-escaping
- **CSRF Protection**: Spring Security ready
- **Input Validation**: Server-side validation

---

**Built with ❤️ using Spring Boot, Kotlin, HTMX, and Web Awesome**
