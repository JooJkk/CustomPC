package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.componentes.Componente;
import model.componentes.*;
import negocio.CarrinhoService;

public class CatalogoController {

    @FXML private TableView<Componente> tabelaComponentes;
    @FXML private TableColumn<Componente, String> colunaNome;
    @FXML private TableColumn<Componente, String> colunaMarca;
    @FXML private TableColumn<Componente, Double> colunaPreco;
    @FXML private Button btnAdicionar;

    // IMPORTANTE: Pegando a instância única do carrinho para integração
    private CarrinhoService carrinhoService = CarrinhoService.getInstance();

    @FXML
    public void initialize() {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colunaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));

        ObservableList<Componente> pecas = FXCollections.observableArrayList();

        // --- ADICIONANDO 20 ITENS ---
        pecas.add(new Processador("Ryzen 5 5600", "AMD", 1000.0, 0.5, 10, 65, "AM4", 65));
        pecas.add(new Processador("Intel i7-12700K", "Intel", 2100.0, 0.6, 5, 125, "LGA1700", 125));
        pecas.add(new Processador("Ryzen 9 7950X", "AMD", 3500.0, 0.5, 3, 170, "AM5", 170));
        pecas.add(new PlacaMae("B550M Aorus", "Gigabyte", 950.0, 1.0, 8, 30, "AM4", 4, "DDR4", "M-ATX"));
        pecas.add(new PlacaMae("X670E ASUS ROG", "ASUS", 2800.0, 1.2, 4, 40, "AM5", 4, "DDR5", "ATX"));
        pecas.add(new MemoriaRam("16GB Corsair Vengeance", "Corsair", 350.0, 0.1, 30, 5, "DDR4", 16, 3200));
        pecas.add(new MemoriaRam("32GB G.Skill Trident", "G.Skill", 1200.0, 0.2, 10, 10, "DDR5", 32, 6000));
        pecas.add(new Fonte("650W Corsair", "Corsair", 450.0, 2.0, 15, 0, 650, "80 Plus Gold"));
        pecas.add(new Fonte("850W EVGA", "EVGA", 800.0, 2.5, 10, 0, 850, "80 Plus Gold"));
        pecas.add(new PlacaMae("H610M-E", "MSI", 600.0, 0.8, 12, 25, "LGA1700", 2, "DDR4", "M-ATX"));
        pecas.add(new Processador("Core i3-12100F", "Intel", 600.0, 0.4, 20, 58, "LGA1700", 60));
        pecas.add(new MemoriaRam("8GB Kingston Fury", "Kingston", 190.0, 0.1, 50, 5, "DDR4", 8, 2666));
        pecas.add(new Fonte("500W Redragon", "Redragon", 280.0, 1.5, 20, 0, 500, "80 Plus Bronze"));
        pecas.add(new Processador("Ryzen 7 5700X", "AMD", 1300.0, 0.5, 15, 65, "AM4", 65));
        pecas.add(new MemoriaRam("16GB XPG Spectrix", "XPG", 420.0, 0.1, 25, 8, "DDR4", 16, 3600));
        pecas.add(new PlacaMae("B660M Phantom", "ASRock", 850.0, 0.9, 10, 30, "LGA1700", 4, "DDR4", "M-ATX"));
        pecas.add(new Processador("Core i5-13600K", "Intel", 1900.0, 0.6, 8, 125, "LGA1700", 125));
        pecas.add(new Fonte("1000W Seasonic", "Seasonic", 1500.0, 3.0, 5, 0, 1000, "80 Plus Platinum"));
        pecas.add(new MemoriaRam("64GB Corsair Dominator", "Corsair", 2500.0, 0.3, 5, 15, "DDR5", 64, 5600));
        pecas.add(new PlacaMae("A520M-K", "ASUS", 450.0, 0.7, 15, 20, "AM4", 2, "DDR4", "M-ATX"));

        tabelaComponentes.setItems(pecas);

        btnAdicionar.setOnAction(event -> {
            Componente selecionado = tabelaComponentes.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                // Adiciona ao carrinho único
                carrinhoService.adicionarItem(selecionado, 1);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Carrinho");
                alert.setHeaderText(null);
                alert.setContentText("✅ " + selecionado.getNome() + " adicionado!\nTotal no Checkout: R$ " + carrinhoService.calcularTotal());
                alert.showAndWait();
            }
        });
    }
}