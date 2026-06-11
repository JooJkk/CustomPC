import model.Cliente;
import model.componentes.Fonte;
import model.componentes.MemoriaRam;
import model.componentes.PlacaMae;
import model.componentes.Processador;
import negocio.ClienteService;
import negocio.ComponenteService;
import negocio.RelatoriosService;

public class Main {

     public static void main(String[] args) {
          App.main(args);

          //ClienteService clienteService = ClienteService.getInstance();
          //clienteService.cadastrar(new Cliente("Nome qualquer", "email@teste.com", "123"));

          /*USADO PARA COLOCAR OS COMPONENTES NO JSON
          ComponenteService componenteService = ComponenteService.getInstance();
          componenteService.cadastrar(new Processador("Ryzen 5 5600", "AMD", 1000.0, 0.5, 0.3, 10, 65, "AM4", 65));
          componenteService.cadastrar(new Processador("Intel i7-12700K", "Intel", 2100.0, 0.6, 0.4, 5, 125, "LGA1700", 125));
          componenteService.cadastrar(new Processador("Ryzen 9 7950X", "AMD", 3500.0, 0.5, 0.5, 3, 170, "AM5", 170));
          componenteService.cadastrar(new Processador("Core i5-13600K", "Intel", 1900.0, 0.6, 0.4, 8, 125, "LGA1700", 125));
          componenteService.cadastrar(new Processador("Core i3-12100F", "Intel", 650.0, 0.4, 0.3, 20, 58, "LGA1700", 60));

          componenteService.cadastrar(new PlacaMae("B550M Aorus Elite", "Gigabyte", 950.0, 1.0, 0.6, 8, 30, "AM4", 4, "DDR4", "Micro-ATX"));
          componenteService.cadastrar(new PlacaMae("X670E ASUS ROG", "ASUS", 2800.0, 1.2, 0.8, 4, 40, "AM5", 4, "DDR5", "ATX"));
          componenteService.cadastrar(new PlacaMae("H610M-E", "MSI", 620.0, 0.8, 0.5, 12, 25, "LGA1700", 2, "DDR4", "Micro-ATX"));
          componenteService.cadastrar(new PlacaMae("A520M-K", "ASUS", 480.0, 0.7, 0.4, 15, 20, "AM4", 2, "DDR4", "Micro-ATX"));
          componenteService.cadastrar(new PlacaMae("Z790 MSI Pro", "MSI", 2100.0, 1.1, 0.7, 6, 45, "LGA1700", 4, "DDR5", "ATX"));

          componenteService.cadastrar(new MemoriaRam("16GB Corsair Vengeance", "Corsair", 380.0, 0.1, 0.1, 30, 5, "DDR4", 16, 3200));
          componenteService.cadastrar(new MemoriaRam("32GB G.Skill Trident", "G.Skill", 1250.0, 0.2, 0.15, 10, 10, "DDR5", 32, 6000));
          componenteService.cadastrar(new MemoriaRam("8GB Kingston Fury", "Kingston", 210.0, 0.1, 0.08, 50, 5, "DDR4", 8, 2666));
          componenteService.cadastrar(new MemoriaRam("16GB XPG Spectrix", "XPG", 440.0, 0.1, 0.1, 25, 8, "DDR4", 16, 3600));

          componenteService.cadastrar(new Fonte("650W Corsair CV", "Corsair", 460.0, 2.0, 1.5, 15, 0, 650, "80 Plus Bronze"));
          componenteService.cadastrar(new Fonte("850W EVGA SuperNova", "EVGA", 850.0, 2.5, 2.0, 10, 0, 850, "80 Plus Gold"));
          componenteService.cadastrar(new Fonte("500W Redragon RGPS", "Redragon", 290.0, 1.5, 1.2, 22, 0, 500, "80 Plus Bronze"));
          componenteService.cadastrar(new Fonte("1000W Seasonic Prime", "Seasonic", 1600.0, 3.0, 2.5, 5, 0, 1000, "80 Plus Platinum"));

          componenteService.cadastrar(new Processador("RTX 4060 Ti", "NVIDIA", 2600.0, 1.2, 2.8, 7, 0, "PCIe", 160));
          componenteService.cadastrar(new Processador("RX 6750 XT", "AMD", 2300.0, 1.3, 3.0, 5, 0, "PCIe", 250));
          componenteService.cadastrar(new Processador("RTX 3060", "MSI", 1850.0, 1.1, 2.5, 12, 0, "PCIe", 170));
*/

     }
}