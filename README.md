# Java Room Reservation System

### Prerequisites
- Java 8 or higher
- SQLite JDBC driver (included in `lib/`)

### Run the Application
```bash
# Compile
javac -cp "lib/*" -d . *.java controller/*.java model/*.java view/*.java

# Run
java -cp ".;lib/*" Main
```

### Default Admin Account
- **Email**: admin@example.com
- **Password**: admin
- **Role**: Administrator

##  Project Structure
```
├── Main.java                 # Application entry point
├── controller/MainController.java    # Main business logic
├── model/                   # Data models
├── view/                    # Swing UI components
└── lib/                     # SQLite JDBC driver
```

##  Database
- SQLite database (`reservation.db`) is created automatically
- Session data saved in `session.txt`

##  Features
- User authentication (Admin/Student roles)
- Room and user management
- Reservation requests with conflict validation
- Reservation approval/rejection workflow
- Real-time date validation
- Modern Swing UI with calendar integration