package controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import data.ProdukRepository;
import model.Produk;

public class KatalogController {

    @FXML
    private TextField namaField;
    @FXML
    private TextField hargaField;
    @FXML
    private ComboBox<String> fotoBox;
    @FXML
    private Label pesanLabel;
    @FXML
    private Label jumlahLabel;
    @FXML
    private TilePane katalogPane;

    private final ProdukRepository repository = new ProdukRepository();
    private Long editingId = null;

    @FXML
    private void initialize() {
        fotoBox.getItems().add("roti.png");
        fotoBox.getItems().add("susu.png");
        fotoBox.getItems().add("kopi.png");
        fotoBox.setValue("roti.png");
        try {
            showData();
        } catch (SQLException e) {
            pesanLabel.setText("Gagal membaca database. Lihat Output.");
            e.printStackTrace();
        }
    }

    private void showData() throws SQLException {
        ArrayList<Produk> daftar = repository.getAll();
        katalogPane.getChildren().clear();
        for (Produk produk : daftar) {
            VBox kartu = createProductCard(produk);
            katalogPane.getChildren().add(kartu);
        }
        jumlahLabel.setText("Jumlah produk: " + daftar.size());
    }

    private VBox createProductCard(Produk produk) {
        ImageView fotoView = new ImageView();
        fotoView.setFitWidth(170);
        fotoView.setFitHeight(110);
        fotoView.setPreserveRatio(true);
        StackPane bingkai = new StackPane();
        bingkai.setPrefSize(180, 120);
        bingkai.setMinSize(180, 120);
        bingkai.setMaxSize(180, 120);

        URL lokasi = getClass().getResource("/image/" + produk.getFoto());
        if (lokasi != null) {
            Image gambar = new Image(lokasi.toExternalForm(),
                    170, 110, true, true);
            if (!gambar.isError()) {
                fotoView.setImage(gambar);
                bingkai.getChildren().add(fotoView);
            } else {
                bingkai.getChildren().add(new Label("Gambar tidak terbaca"));
            }
        } else {
            bingkai.getChildren().add(new Label("Gambar tidak tersedia"));
        }

        Label namaLabel = new Label(produk.getNama());
        namaLabel.setWrapText(true);
        namaLabel.getStyleClass().add("nama-produk");
        Label hargaLabel = new Label("Rp" + produk.getHarga());
        Label idLabel = new Label("ID: " + produk.getId());
        idLabel.getStyleClass().add("muted");

        Button btnEdit = new Button("Edit");
        btnEdit.setOnAction(e -> startEdit(produk));

        Button btnHapus = new Button("Hapus");
        btnHapus.setOnAction(e -> deleteProduct(produk));

        HBox aksiBox = new HBox(8, btnEdit, btnHapus);

        VBox kartu = new VBox();
        kartu.setSpacing(8);
        kartu.setMinWidth(220);
        kartu.setPrefWidth(220);
        kartu.setMaxWidth(220);
        kartu.getStyleClass().add("kartu-produk");
        kartu.getChildren().add(bingkai);
        kartu.getChildren().add(namaLabel);
        kartu.getChildren().add(hargaLabel);
        kartu.getChildren().add(idLabel);
        kartu.getChildren().add(aksiBox);
        return kartu;
    }

    private void startEdit(Produk produk) {
        editingId = produk.getId();
        namaField.setText(produk.getNama());
        hargaField.setText(String.valueOf(produk.getHarga()));
        fotoBox.setValue(produk.getFoto());
        pesanLabel.setText("Mode edit ID " + produk.getId() + ". Ubah data lalu klik Simpan.");
    }

    private void deleteProduct(Produk produk) {
        Alert konfirmasi = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Hapus produk \"" + produk.getNama() + "\" (ID: " + produk.getId() + ")?",
                ButtonType.YES,
                ButtonType.NO
        );
        konfirmasi.setHeaderText("Konfirmasi Hapus");
        konfirmasi.showAndWait().ifPresent(respon -> {
            if (respon == ButtonType.YES) {
                try {
                    repository.delete(produk.getId());
                    if (editingId != null && editingId == produk.getId()) {
                        resetForm();
                    }
                    showData();
                    pesanLabel.setText("Produk berhasil dihapus.");
                } catch (SQLException e) {
                    pesanLabel.setText("Gagal menghapus produk. Lihat Output.");
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void cancelEdit() {
        resetForm();
        pesanLabel.setText("Edit dibatalkan.");
    }

    private void resetForm() {
        editingId = null;
        namaField.clear();
        hargaField.clear();
        fotoBox.setValue("roti.png");
    }

    @FXML
    private void saveProduct() {
        String nama;
        long harga;
        String foto;
        try {
            nama = namaField.getText();
            harga = Long.parseLong(hargaField.getText().trim());
            foto = fotoBox.getValue();
            new Produk(nama, harga, foto);
        } catch (NumberFormatException e) {
            pesanLabel.setText("Harga harus angka bulat, contoh 12000.");
            return;
        } catch (IllegalArgumentException e) {
            pesanLabel.setText(e.getMessage());
            return;
        }

        try {
            if (editingId == null) {
                repository.add(new Produk(nama, harga, foto));
                pesanLabel.setText("Produk berhasil disimpan.");
            } else {
                repository.update(new Produk(editingId, nama, harga, foto));
                pesanLabel.setText("Produk berhasil diperbarui.");
            }
        } catch (SQLException e) {
            pesanLabel.setText("Gagal menyimpan ke database. Lihat Output.");
            e.printStackTrace();
            return;
        }

        resetForm();
        try {
            showData();
        } catch (SQLException e) {
            pesanLabel.setText("Data tersimpan, tetapi katalog gagal dimuat ulang.");
            e.printStackTrace();
        }
    }
}