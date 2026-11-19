# KatalogProduk (JavaFX + SQLite)

Aplikasi desktop katalog produk berbasis **JavaFX** dan **SQLite** yang mendukung operasi CRUD (**Create**, **Read**, **Update**, **Delete**) untuk data produk (`nama`, `harga`, dan `foto`).

## Fitur Utama
- **Tambah Produk (Create)**: Menambahkan data produk baru ke database SQLite (`data/katalog.db`) melalui form input.
- **Lihat Katalog (Read)**: Menampilkan daftar produk dalam bentuk kartu pada `TilePane` beserta total jumlah produk.
- **Ubah Produk (Update)**: Memilih data produk untuk diedit kembali ke form input lalu memperbaruinya di database.
- **Hapus Produk (Delete)**: Menghapus data produk dari database setelah melewati dialog konfirmasi.

## Struktur Proyek
- `src/main/java/main/Main.java` & `MainApp.java` — Titik masuk aplikasi JavaFX dan inisialisasi window/database.
- `src/main/java/db/Database.java` — Koneksi SQLite (`jdbc:sqlite:data/katalog.db`) dan pembuatan tabel `produk`.
- `src/main/java/model/Produk.java` — Model data produk beserta validasi nilai.
- `src/main/java/data/ProdukRepository.java` — Operasi query SQL (`SELECT`, `INSERT`, `UPDATE`, `DELETE`).
- `src/main/java/controller/KatalogController.java` — Pengatur logika UI, event tombol, dan pembaruan tampilan katalog.
- `src/main/resources/view/katalog.fxml` — Tata letak antarmuka pengguna (FXML).
- `src/main/resources/image/` — Aset gambar contoh produk (`roti.png`, `susu.png`, `kopi.png`).
- `gambar/` — Folder dokumentasi *screenshot* antarmuka aplikasi.

## Cara Menjalankan Aplikasi
1. Pastikan Anda sudah mengunduh/meng-clone repository ini ke komputer lokal.
2. Pastikan **JDK** dan **Apache Maven** (atau **Apache NetBeans** dengan dukungan JavaFX & Maven) sudah terpasang.
3. Buka folder proyek **KatalogProduk** di NetBeans atau terminal.
4. Jalankan *Clean and Build* agar dependensi dan resource (`fxml`, `image`, `css`) ter-compile dengan baik.
5. Jalankan class `main.Main` (atau `main.MainApp`). Database SQLite (`data/katalog.db`) akan otomatis dibuat dan diinisialisasi saat aplikasi dijalankan.

## Screenshot Aplikasi

### Tampilan Awal (Kosong)
![Tampilan Awal (Kosong)](gambar/Tampilan%20Awal%20(Kosong).png)

### Tampilan Awal (2 Item)
![Tampilan Awal (2 Item)](gambar/Tampilan%20Awal(2%20Item).png)

### Tampilan Konfirmasi Hapus
![Tampilan Konfirmasi Hapus](gambar/Tampilan%20Konfirmasi%20Hapus.png)
