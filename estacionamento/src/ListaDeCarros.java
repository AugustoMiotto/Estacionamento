import java.util.ArrayList;
import java.util.List;

public class ListaDeCarros {
    private List<Carro> carros = new ArrayList<>(); // Lista em memória

    public ListaDeCarros() {
        carregarCarrosDoBanco();
    }

    public List<Carro> get(){
        return  carros;
    }

    public void carregarCarrosDoBanco() {
        CarroDAO carroDAO = new CarroDAO();
        List<Carro> carrosCarregados = carroDAO.listarCarros();
        carros.addAll(carrosCarregados);  // Adiciona os carros carregados à lista
    }


    public boolean adicionarCarro(Carro carro) {
        carros.add(carro);
        CarroDAO carroDAO = new CarroDAO();
        return carroDAO.inserirCarro(carro);
    }

    public boolean removerCarro(String placa, int vaga) {
        Carro carroRemover = null;
        for (Carro carro : carros) {
            if (carro.getPlaca().equalsIgnoreCase(placa) || carro.getVaga() == vaga) {
                carroRemover = carro;
                break;
            }
        }

        if (carroRemover == null) {
            System.out.println("Carro não encontrado na lista!");
            return false;
        }

        // Remove o carro do banco de dados
        CarroDAO carroDAO = new CarroDAO();
        boolean carroRemovido = carroDAO.removerCarro(placa);

        // Libera a vaga no banco de dados
        VagaDAO vagaDAO = new VagaDAO();
        boolean vagaLiberada = vagaDAO.marcarVagaComoLivre(vaga);

        // Remove da lista se as operações no banco funcionarem
        if (carroRemovido && vagaLiberada) {
            carros.remove(carroRemover);
            return true;
        }

        return false;
    }

    public Carro buscarCarroPorPlaca(String placa) {
        for (Carro carro : carros) {
            if (carro.getPlaca().equalsIgnoreCase(placa)) {
                return carro;
            }
        }
        return null;
    }

    public int buscarVagaPorPlaca(String placa) {
        for (Carro carro : carros) {
            if (carro.getPlaca().equalsIgnoreCase(placa)) {
                return carro.getVaga();
            }
        }
        return -1;
    }

    public boolean atualizarVagaPorPlaca(String placa, int novaVaga) {
        Carro carro = buscarCarroPorPlaca(placa);
        if (carro != null) {
            carro.setVaga(novaVaga);
            CarroDAO carroDAO = new CarroDAO();
            return carroDAO.atualizarVagaPorPlaca(placa, novaVaga);
        } else {
            System.out.println("Carro com a placa " + placa + " não encontrado na lista.");
            return false;
        }
    }
}
