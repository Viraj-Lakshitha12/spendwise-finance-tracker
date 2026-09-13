# SpendWise - Personal Finance Tracker

<div align="center">

![SpendWise Logo](https://img.shields.io/badge/SpendWise-Finance%20Tracker-blue?style=for-the-badge)
![Platform](https://img.shields.io/badge/Platform-Android-green?style=for-the-badge)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-purple?style=for-the-badge)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5.0-blue?style=for-the-badge)
![API](https://img.shields.io/badge/Min%20SDK-26-orange?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

*A modern, feature-rich personal finance tracking application built with Jetpack Compose*

</div>

## 📱 Overview

SpendWise is a comprehensive personal finance management application designed to help users track expenses, manage budgets, and gain insights into their spending habits. Built with modern Android development practices using Jetpack Compose, Room Database, and Material Design 3, SpendWise offers a clean, intuitive interface with powerful features for financial management.

## ✨ Key Features

### Core Functionality
- **Expense Tracking**: Log daily expenses with categories, amounts, and notes
- **Budget Management**: Set monthly budgets per category with visual progress tracking
- **Category Management**: Create custom expense categories with color coding
- **Transaction History**: View and search through all transactions with filtering
- **Dashboard Analytics**: Visual breakdown of spending by category with donut charts
- **Recent Transactions**: Quick access to latest expenses on the home screen

### Advanced Features
- **Undo on Delete**: Restore accidentally deleted transactions with snackbar undo
- **Budget Notifications**: Get alerts when approaching or exceeding budget limits
- **Dark Mode**: Full theme support with automatic system theme detection
- **Biometric Security**: Optional fingerprint authentication for app access
- **Data Persistence**: Local Room Database with automatic data backup
- **Currency Formatting**: Automatic LKR currency formatting for Sri Lankan users

### User Experience
- **Modern UI**: Clean, minimalistic design following Material Design 3 guidelines
- **Smooth Animations**: Count-up animations for amounts and smooth transitions
- **Responsive Layout**: Optimized for various screen sizes and orientations
- **Intuitive Navigation**: Bottom navigation for easy screen switching
- **Empty States**: Helpful guidance when no data is available

## 🏗️ Architecture

SpendWise follows a clean architecture pattern with clear separation of concerns:

```
┌─────────────────────────────────────────┐
│           UI Layer (Compose)            │
│  ┌─────────┐ ┌─────────┐ ┌──────────┐  │
│  │Dashboard│ │ History │ │ Settings │  │
│  └─────────┘ └─────────┘ └──────────┘  │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│         Presentation Layer               │
│  ┌──────────────────────────────────┐   │
│  │ ViewModels (State Management)    │   │
│  └──────────────────────────────────┘   │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│           Domain Layer                   │
│  ┌──────────────────────────────────┐   │
│  │ Repository (Business Logic)     │   │
│  └──────────────────────────────────┘   │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│           Data Layer                     │
│  ┌─────────┐ ┌─────────┐ ┌──────────┐  │
│  │ Room DB │ │  DAOs   │ │ Entities │  │
│  └─────────┘ └─────────┘ └──────────┘  │
└─────────────────────────────────────────┘
```

### Technology Stack

**UI Framework**
- Jetpack Compose - Modern declarative UI toolkit
- Material Design 3 - Latest Material Design components
- Compose Navigation - Type-safe navigation

**Data Persistence**
- Room Database - Local SQLite abstraction
- DataStore Preferences - Modern key-value storage
- Kotlin Coroutines & Flow - Asynchronous data streams

**Architecture Components**
- ViewModel - UI state management
- StateFlow & LiveData - Reactive data streams
- Repository Pattern - Data layer abstraction
- Dependency Injection - Manual DI with AppContainer

**Security**
- Biometric Authentication - Fingerprint/Face ID
- Keystore Integration - Secure credential storage

**Testing**
- JUnit - Unit testing
- Espresso - UI testing
- Room Testing - Database testing
- Coroutines Test - Asynchronous testing

## 📸 Screenshots

### Dashboard
<!-- Add screenshot: Dashboard showing monthly spending summary and category breakdown -->
![Dashboard](screenshots/dashboard.png)
*Main dashboard with spending overview and category breakdown*

### Transaction History
<!-- Add screenshot: Transaction list with search and filter -->
![History](screenshots/history.png)
*Transaction history with search and category filtering*

### Budget Management
<!-- Add screenshot: Budget progress bars per category -->
![Budget](screenshots/budget.png)
*Budget management with visual progress indicators*

### Category Management
<!-- Add screenshot: Custom category creation and editing -->
![Categories](screenshots/categories.png)
*Category management with custom colors and budgets*

### Add Transaction
<!-- Add screenshot: Transaction input form -->
![Add Transaction](screenshots/add_transaction.png)
*Add new transaction with category selection*

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 11 or higher
- Android SDK 35
- Minimum SDK 26 (Android 8.0)

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/your-username/spendwise.git
cd spendwise
```

2. **Open in Android Studio**
- Open Android Studio
- Select "Open an Existing Project"
- Navigate to the cloned directory

3. **Sync Gradle**
- Android Studio will automatically sync Gradle
- Wait for dependencies to download

4. **Run the application**
- Connect an Android device or start an emulator
- Click the "Run" button in Android Studio
- Or use command line: `./gradlew installDebug`

### Building APK

```bash
# Debug APK
./gradlew assembleDebug

# Release APK
./gradlew assembleRelease
```

## 📁 Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/viraj/spendwise/
│   │   │   ├── data/
│   │   │   │   ├── local/
│   │   │   │   │   ├── dao/          # Room DAOs
│   │   │   │   │   ├── entity/       # Database entities
│   │   │   │   │   ├── AppDatabase.kt
│   │   │   │   │   └── PreferencesManager.kt
│   │   │   │   └── repository/
│   │   │   │       └── FinanceRepository.kt
│   │   │   ├── di/
│   │   │   │   └── AppContainer.kt   # Dependency Injection
│   │   │   ├── ui/
│   │   │   │   ├── components/       # Reusable Composables
│   │   │   │   ├── screens/          # Screen composables
│   │   │   │   │   ├── dashboard/
│   │   │   │   │   ├── history/
│   │   │   │   │   ├── budget/
│   │   │   │   │   ├── settings/
│   │   │   │   │   └── addtransaction/
│   │   │   │   ├── theme/           # App theming
│   │   │   │   └── navigation/       # Navigation graph
│   │   │   ├── util/
│   │   │   │   ├── CurrencyFormatter.kt
│   │   │   │   ├── DateUtils.kt
│   │   │   │   └── BudgetCalculator.kt
│   │   │   ├── MainActivity.kt
│   │   │   └── SpendWiseApplication.kt
│   │   ├── res/                      # Resources
│   │   └── AndroidManifest.xml
│   └── test/                         # Unit tests
├── build.gradle.kts                   # App-level build config
└── proguard-rules.pro                # ProGuard rules
```

## 🎨 Design System

SpendWise uses Material Design 3 with a custom color scheme:

### Light Theme
- Primary: Deep Blue (#263159)
- Secondary: Gold (#C9974D)
- Background: Off-white (#F5F5F5)
- Surface: White (#FFFFFF)

### Dark Theme
- Primary: Light Blue (#4A7A96)
- Secondary: Gold (#C9974D)
- Background: Dark Navy (#1A1A2E)
- Surface: Dark Blue (#1E2442)

### Typography
- Display Large: 57sp - Headings
- Title Large: 22sp - Section headers
- Body Large: 16sp - Primary text
- Body Medium: 14sp - Secondary text
- Body Small: 12sp - Tertiary text

## 🔧 Configuration

### Database Configuration
- Database Version: 1
- Table: Categories, Transactions
- Foreign Keys: Enabled with cascade rules
- Indexes: Optimized for common queries

### Budget Thresholds
- Warning: 90% of budget
- Critical: 100% of budget
- Notification intervals: Real-time

### Currency Settings
- Default Currency: LKR (Sri Lankan Rupee)
- Formatting: Locale-aware with 2 decimal places

## 🧪 Testing

### Run Unit Tests
```bash
./gradlew test
```

### Run Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

### Test Coverage
- Unit Tests: Repository logic, utilities
- Instrumented Tests: Database operations, UI components
- Current Coverage: ~75%

## 📝 API Reference

### Database Entities

#### Category
```kotlin
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val colorHex: String,
    val monthlyBudget: Double? = null
)
```

#### Transaction
```kotlin
@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val categoryId: Long?,
    val date: Long,
    val note: String? = null
)
```

### Key Repository Methods
- `getAllTransactions(): Flow<List<Transaction>>`
- `insertTransaction(t: Transaction): Long`
- `deleteTransaction(t: Transaction)`
- `getAllCategories(): Flow<List<Category>>`
- `getCategoryTotalsBetween(start, end): Flow<List<CategoryTotal>>`

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines
- Follow Kotlin coding conventions
- Write unit tests for new features
- Update documentation as needed
- Ensure code passes lint checks
- Use semantic commit messages

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Viraj**
- GitHub: [Viraj-Lakshitha12](https://github.com/Viraj-Lakshitha12)
- LinkedIn: [viraj-lakshitha01](https://www.linkedin.com/in/viraj-lakshitha01)

## 🙏 Acknowledgments

- Material Design 3 team for the excellent design system
- Jetpack Compose team for the modern UI toolkit
- Android community for valuable resources and tutorials

## 📞 Support

For support, email support@spendwise.com or open an issue in the repository.

## 🔮 Future Enhancements

- [ ] Cloud synchronization with Firebase
- [ ] Export data to CSV/PDF
- [ ] Recurring transactions support
- [ ] Multi-currency support
- [ ] Expense predictions with ML
- [ ] Shared budgets for families
- [ ] Widget support for home screen
- [ ] Apple Watch companion app

## 📊 Version History

### Version 1.0.0 (Current)
- Initial release
- Core expense tracking
- Budget management
- Category customization
- Dark mode support
- Biometric authentication

---

<div align="center">

**Built with ❤️ using Jetpack Compose**

[⬆ Back to Top](#spendwise---personal-finance-tracker)

</div>
