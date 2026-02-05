
import java.util.*;

public class ByteBurgers {

    static final Scanner scanner = new Scanner(System.in);

    // La carta de preus (Constants)
    static Map<String, Double> CARTA = new HashMap<>();

    static {
        CARTA.put("Hamburguesa", 8.50);
        CARTA.put("Patates", 3.00);
        CARTA.put("Refresc", 2.50);
        CARTA.put("Aigua", 1.50);
    }
//    static Map<String, Integer> STOCK = inicialitzaStock();

    static Map<String, Integer> COMANDA = new HashMap<>();

    public static void main(String[] args) {
        Map<String, Integer> comanda = new HashMap<>();
        Map<String, Integer> stock;
        Map<String, Integer> vendes = new HashMap<>();
        Map<String, Integer> unitatsVenudes = new HashMap<>();
        boolean botigaOberta = true;
        stock = inicialitzaStock();
        double totalImpostos = 0;

        System.out.println("=== BENVINGUT A BYTEBURGERS ===");

        while (botigaOberta) {
            mostrarMenuPrincipal();
            int opcio = llegirOpcio();

            switch (opcio) {
                case 1:
                    mostrarCarta();
                    break;
                case 2:
                    afegirProductes(comanda, stock);
                    break;
                case 3:
                    totalImpostos = mostrarCistella(comanda);
                    break;
                case 4:
                    // totalImpostos = mostrarCistella(comanda); alternativa sin declarar variable dins de processarPagament
                    processarPagament(comanda, unitatsVenudes);
                    break;
                case 5:
                    menuAdministrador(stock, unitatsVenudes);
                    break;
                case 6:
                    System.out.println("Tancant la caixa... Fins dema!");
                    botigaOberta = false;
                    break;
                default:
                    System.out.println("Opcio no valida.");
            }
        }
    }

    public static int llegirOpcio() {
        Scanner sc = new Scanner(System.in);

        int opcio = sc.nextInt();
        return opcio;
    }

    public static void mostrarCarta() {
        System.out.println("==== LA NOSTRA CARTA ====");

        for (Map.Entry<String, Double> producte : CARTA.entrySet()) {
            System.out.println(producte.getKey() + " --> " + producte.getValue());
        }
    }

    public static void mostrarMenuPrincipal() {
        System.out.println("==== MENU PRINCIPAL ====");
        System.out.println("1. Veure la carta");
        System.out.println("2. Afegeix producte a la comanda");
        System.out.println("3. Veure cistella");
        System.out.println("4. Finalitzar i pagar");
        System.out.println("5. Menu d'administrador");
        System.out.println("6. Sortir");
        System.out.print("Tria una opcio (1-6):");

    }

    public static Map inicialitzaStock() {
        Map<String, Integer> stock = new HashMap<>();

        stock.put("Hamburguesa", 500);
        stock.put("Patates", 1000);
        stock.put("Refresc", 1000);
        stock.put("Aigua", 2000);
        return stock;

    }

    //Declarar que String,Integer... en diciconarios todo el metodo.
    public static void afegirProductes(Map<String, Integer> comanda, Map<String, Integer> stock) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Quin producte vols afegir? ");
        String producte = sc.nextLine();

        if (!CARTA.containsKey(producte)) {
            System.out.println("Aquest producte no existeix a la carta.");

        }

        System.out.print("Quina quantitat vols? ");

        if (!sc.hasNextInt()) {
            System.out.println("Si us plau, introdueix un nombre vàlid.");
            sc.nextLine();

        }

        int quantitat = sc.nextInt();
        sc.nextLine();

        int stockDispo = stock.get(producte);

        if (quantitat > stockDispo) {
            System.out.println("Stock insuficient. Disponible: " + stockDispo);

        }

        //afegeix a la comanda
        comanda.put(producte, comanda.getOrDefault(producte, 0) + quantitat);

        //fa la resta del stock
        stock.put(producte, stockDispo - quantitat);

        System.out.println("Producte afegit correctament: " + quantitat + " unitats de " + producte);

    }

    public static double mostrarCistella(Map<String, Integer> comanda) {
        if (comanda.isEmpty()) {
            System.out.println("No hi ha cap producte a la cistella.");
            return 0;
        }

        System.out.println("==== CISTELLA ACTUAL ====");

        double total = 0.0;
        double totalImpostos = 0.0;

        for (Map.Entry<String, Integer> producte : comanda.entrySet()) {

            String nom = producte.getKey();
            int unitats = producte.getValue();
            double preu = CARTA.get(nom);

            double totalProducte = preu * unitats;
            total += totalProducte;
            total = Math.round(total * 100.0) / 100.0;

            double totalProducteImpostos = (totalProducte * 0.21) + totalProducte;
            totalImpostos += totalProducteImpostos;
            totalImpostos = Math.round(totalImpostos * 100.0) / 100.0;

            System.out.println(nom + " x" + unitats + " --> " + Math.round(totalProducte * 100.00) / 100.0 + " euros");

        }

        System.out.println("TOTAL COMANDA: " + total + " euros.");
        System.out.println("TOTAL COMANDA AMB IMPOSTOS: " + totalImpostos + " euros.");
        return totalImpostos;

    }

    public static void processarPagament(Map<String, Integer> comanda, Map<String, Integer> unitatsVenudes) {
        Scanner sc = new Scanner(System.in);
        double totalImpostos = mostrarCistella(comanda);

        if (comanda.isEmpty()) {
            System.out.println("No hi ha productes a la comanda.");

        } else {

            System.out.println("==== BYTE BURGERS ====");
            System.out.println("TOTAL COMANDA AMB IMPOSTOS: " + totalImpostos);

            System.out.println("Tens codi de descomopte (S/N): ");
            String resposta = sc.nextLine().toLowerCase();

            double totalDescompte = Math.round((totalImpostos * 0.10) * 100.0) / 100.0;

            if (resposta.equals("si")) {
                System.out.print("Introdueix codi: ");
                String codi = sc.nextLine();
                if (codi.equals("DAM2025")) {
                    double preuFinalDescompte = Math.round((totalImpostos - totalDescompte) * 100.0) / 100.0;
                    System.out.println("Preu final: " + preuFinalDescompte);
                    System.out.print("Amb quant d'efectiu pagaras? ");
                    double efectiu = sc.nextDouble();
                    efectiu = Math.round(efectiu * 100.0) / 100.0;
                    if ((efectiu - preuFinalDescompte) < 0) {
                        System.out.println("Error de pagament");
                    } else {
                        System.out.println("COMANDA PAGADA CORRECTAMENT");
                        for (Map.Entry<String, Integer> entry : comanda.entrySet()) {
                            String producte = entry.getKey();
                            int quantitat = entry.getValue();
                            unitatsVenudes.put(producte, unitatsVenudes.getOrDefault(producte, 0) + quantitat);
                        }
                        double canvi = Math.round((efectiu - preuFinalDescompte) * 100.0) / 100.0;
                        System.out.println("Canvi: " + canvi);
                        System.out.println("==== GRACIES PER LA SEVA VISITA ====");
                        System.out.println("Comanda netejada correctament.");
                        comanda.clear();  //Netejar la llista pel següent client.
                    }

                } else {
                    System.out.println("Error de codi.");

                }
            } else if (resposta.equals("no")) {
                System.out.println("Preu final comanda: " + totalImpostos);
                System.out.print("Amb quant d'efectiu pagaras? ");
                double efectiu = sc.nextDouble();
                efectiu = Math.round(efectiu * 100.0) / 100.0;
                if ((efectiu - totalImpostos) < 0) {
                    System.out.println("Error de pagament");
                } else {
                    System.out.println("COMANDA PAGADA CORRECTAMENT");

                    //al finalizar afegeix el productes a unitatsVenudes de la comanda.
                    for (Map.Entry<String, Integer> entry : comanda.entrySet()) {
                        String producte = entry.getKey();
                        int quantitat = entry.getValue();
                        unitatsVenudes.put(producte, unitatsVenudes.getOrDefault(producte, 0) + quantitat);
                    }
                    double canvi = Math.round((efectiu - totalImpostos) * 100.0) / 100.0;
                    System.out.println("Canvi: " + canvi);
                    System.out.println("==== GRACIES PER LA SEVA VISITA ====");
                    System.out.println("Comanda netejada correctament.");
                    comanda.clear();       //Netejar la llista pel següent client.
                }
            } else {
                System.out.print("Introdueix (S/N): ");
            }

            // System.out.println(comanda);
        }

    }

    public static void menuAdministrador(Map<String, Integer> stock, Map<String, Integer> unitatsVenudes) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Introdueix usuari: ");
        String usuari = sc.nextLine();

        System.out.println("Introdueix contrasenya: ");
        String contrasenya = sc.nextLine();

        if (!usuari.equals("AdamPartida") || !contrasenya.equals("dam123")) {
            System.out.println("Entrada incorrecta.");
//           
        }

        System.out.println("Inici de sessio correcte.");

        int opcioAdmin;
        do {
            System.out.println("==== MENU ADMINISTRADOR ====");
            System.out.println("1. Afegir stock d'un producte");
            System.out.println("2. Mostrar stock de productes");
            System.out.println("3. Mostrar vendes");
            System.out.println("4. Afegir un nou producte");
            System.out.println("5. Tornar al menu principal");
            System.out.println("6. Tancar caixa (Sortir).");
            System.out.print("Tria una opcio (1-6):");
            opcioAdmin = sc.nextInt();
            sc.nextLine();

            switch (opcioAdmin) {
                case 1:
                    afegirStockProducte(stock);
                    break;
                case 2:
                    mostrarStockProducte(stock);
                    break;
                case 3:
                    mostrarVendes(unitatsVenudes);
                    break;
                case 4:
                    afegirNouProducte(stock);
                    break;
                case 5:
                    System.out.println("Tornant al menu principal...");
                    break;
                default:
                    System.out.println("Opcio no valida.");
            }

        } while (opcioAdmin != 5);

    }

    public static void afegirStockProducte(Map<String, Integer> stock) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Introdueix producte: ");
        String producte = sc.nextLine();

        if (!stock.containsKey(producte)) {
            System.out.println("Aquest producte no existeix");

        }

        System.out.print("Quant vols afegir? ");
        int quantitat = sc.nextInt();

        stock.put(producte, stock.get(producte) + quantitat);

        System.out.println("Stock actualizat correctament.");

    }

    public static void mostrarStockProducte(Map<String, Integer> stock) {

        System.out.println("==== STOCK ====");

        for (Map.Entry<String, Integer> article : stock.entrySet()) {
            System.out.println("Producte: " + article.getKey() + " ---> " + " Unitats: " + article.getValue());
        }

    }

    public static void mostrarVendes(Map<String, Integer> unitatsVenudes) {
        System.out.println("==== VENDES ====");

        if (unitatsVenudes.isEmpty()) {
            System.out.println("De moment no hi ha cap venta.");
            return;
        }
        for (Map.Entry<String, Integer> article : unitatsVenudes.entrySet()) {
            System.out.println("Producte venut: " + article.getKey() + " ----> " + " Unitats venudes: " + article.getValue());
        }

    }

    public static void afegirNouProducte(Map<String, Integer> stock) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Introdueix producte nou: ");
        String producteNou = sc.nextLine();

        if (CARTA.containsKey(producteNou)) {
            System.out.println("Aquest producte ja esta a la carta.");

        }
        System.out.println("Indica el preu del producte: ");
        double preuProducteNou = sc.nextDouble();

        System.out.println("Introdueix stock inicial: ");
        int stockProducteNou = sc.nextInt();

        CARTA.put(producteNou, preuProducteNou);    //afegueix a la carta principal
        stock.put(producteNou, stockProducteNou);   //afegeix al stock inicial del nou producte

        System.out.println("Producte nou afegit correctament.");

    }

}
