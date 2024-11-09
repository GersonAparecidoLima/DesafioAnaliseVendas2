import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Solicita o caminho do arquivo ao usuário
        System.out.print("Entre o caminho do arquivo: ");
        String filePath = scanner.nextLine();

        // Lista de vendas
        List<Sale> sales = new ArrayList<>();

        // Leitura do arquivo CSV
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Ignora a primeira linha (cabeçalho)
                if (line.startsWith("month")) continue;

                // Divide a linha do CSV em partes
                String[] values = line.split(",");
                if (values.length < 5) {
                    System.out.println("Linha inválida: " + line);
                    continue;
                }

                try {
                    Integer month = Integer.valueOf(values[0].trim());
                    Integer year = Integer.valueOf(values[1].trim());
                    String seller = values[2].trim();
                    Integer quantity = Integer.valueOf(values[3].trim());
                    Double total = Double.valueOf(values[4].trim());

                    // Cria um objeto Sale e adiciona à lista
                    sales.add(new Sale(month, year, seller, quantity, total));
                } catch (NumberFormatException e) {
                    System.out.println("Erro ao processar linha: " + line);
                    continue;
                }
            }

            // Passo 1: Gerar vendedores únicos usando Set
            Set<String> sellers = sales.stream()
                    .map(Sale::getSeller)  // Extrai o nome dos vendedores
                    .collect(Collectors.toSet());  // Coleta em um Set para garantir nomes únicos

            // Passo 2: Calcular o total vendido por cada vendedor
            System.out.println("\nTotal de vendas por vendedor:");
            sellers.forEach(seller -> {
                double totalVendido = sales.stream()
                        .filter(s -> s.getSeller().equals(seller))  // Filtra pelas vendas do vendedor
                        .mapToDouble(Sale::getTotal)  // Mapeia para o total de cada venda
                        .sum();  // Soma os totais vendidos pelo vendedor
                System.out.printf("%s: %.2f\n", seller, totalVendido);  // Exibe o total vendido
            });

        } catch (FileNotFoundException e) {
            System.out.println("Erro: " + filePath + " (O sistema não pode encontrar o arquivo especificado)");
           // System.out.println("Verifique o caminho e tente novamente.");
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + filePath);
            e.printStackTrace();
        }
    }
}
