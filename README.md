# CatatHutang — Android Kotlin App

Aplikasi pencatatan hutang, cicilan, dan ide investasi untuk Android.
Dikonversi dari prototype HTML (`catathutang_full_v2.html`) ke proyek Android Kotlin penuh.

---

## Fitur

| Tab | Fitur |
|-----|-------|
| 📋 Catatan | Catat hutang & piutang, ringkasan saldo, kutipan motivasi |
| 💳 Cicilan | Tambah cicilan, progress bar, riwayat pembayaran, tandai lunas |
| 📈 Investasi | Harga emas/komoditas & saham IDX (data simulasi) |

---

## Tech Stack

- **Language**: Kotlin
- **Architecture**: MVVM (ViewModel + LiveData)
- **Database**: Room (SQLite)
- **Navigation**: Jetpack Navigation Component + Safe Args
- **UI**: Material 3, ViewBinding, RecyclerView
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 34 (Android 14)

---

## Struktur Proyek

```
app/src/main/java/com/catathutang/
├── MainActivity.kt
├── data/
│   ├── AppDatabase.kt
│   ├── HutangDao.kt
│   ├── CicilanDao.kt
│   ├── model/
│   │   ├── Hutang.kt
│   │   ├── Cicilan.kt          ← includes Room TypeConverter for paid list
│   │   └── Investasi.kt        ← Komoditas, Saham, InvestasiData
│   └── repository/
│       └── Repository.kt
├── ui/
│   ├── hutang/
│   │   ├── HutangFragment.kt
│   │   ├── HutangViewModel.kt
│   │   ├── HutangAdapter.kt
│   │   ├── AddHutangFragment.kt
│   │   └── HutangDetailFragment.kt
│   ├── cicilan/
│   │   ├── CicilanFragment.kt
│   │   ├── CicilanViewModel.kt
│   │   ├── CicilanAdapter.kt
│   │   ├── AddCicilanFragment.kt
│   │   ├── CicilanDetailFragment.kt
│   │   └── PaymentHistoryAdapter.kt
│   └── investasi/
│       ├── InvestasiFragment.kt
│       ├── KomoditasAdapter.kt
│       └── SahamAdapter.kt
└── utils/
    └── Utils.kt                ← Formatter, AvatarHelper, CategoryHelper, quotes
```

---

## Cara Membuka di Android Studio

1. **Clone / ekstrak** proyek ini
2. Buka **Android Studio** → *Open* → pilih folder `CatatHutang`
3. Tunggu Gradle sync selesai (butuh koneksi internet untuk download dependencies)
4. Jalankan di emulator atau device fisik (Android 8.0+)

### Persyaratan
- Android Studio Hedgehog (2023.1) atau lebih baru
- JDK 17
- Gradle 8.x (sudah dikonfigurasi di `gradle/libs.versions.toml`)

---

## Catatan Penting

- **Data investasi bersifat simulasi** — bukan harga real-time
- Data hutang & cicilan disimpan lokal menggunakan **Room database**
- Tidak ada koneksi internet yang diperlukan (fully offline)
- Dark mode didukung otomatis via `Theme.Material3.DayNight`

---

## Layar / Screens

| Screen | File Fragment | Layout XML |
|--------|--------------|------------|
| Daftar Hutang | `HutangFragment` | `fragment_hutang.xml` |
| Tambah Hutang | `AddHutangFragment` | `fragment_add_hutang.xml` |
| Detail Hutang | `HutangDetailFragment` | `fragment_hutang_detail.xml` |
| Daftar Cicilan | `CicilanFragment` | `fragment_cicilan.xml` |
| Tambah Cicilan | `AddCicilanFragment` | `fragment_add_cicilan.xml` |
| Detail Cicilan | `CicilanDetailFragment` | `fragment_cicilan_detail.xml` |
| Investasi | `InvestasiFragment` | `fragment_investasi.xml` |
