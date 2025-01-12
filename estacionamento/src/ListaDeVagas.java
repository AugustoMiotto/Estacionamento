import java.util.ArrayList;
import java.util.List;

public class ListaDeVagas {
    private List<Vaga> vagas = new ArrayList<>();
    VagaDAO vagaDAO = new VagaDAO();

    public ListaDeVagas() {
        carregarVagasDoBanco();
    }

    public void carregarVagasDoBanco() {
        VagaDAO vagaDAO = new VagaDAO();
        List<Vaga> vagasCarregadas = vagaDAO.listarVagas();
        if (vagasCarregadas != null) {
            vagas.clear();
            vagas.addAll(vagasCarregadas);
        }
    }

    public boolean atualizarStatusVaga(int numeroVaga, String novoStatus) {

        Vaga vaga = buscarVagaPorNumero(numeroVaga);
        if (vaga != null) {
            vaga.setStatus(novoStatus);
            VagaDAO vagaDAO = new VagaDAO();
            return vagaDAO.atualizarStatusVaga(numeroVaga, novoStatus);
        }
        return false;
    }

    public Vaga buscarVagaPorNumero(int numero) {
        for (Vaga vaga : vagas) {
            if (vaga.getNumero() == numero) {
                return vaga;
            }
        }
        return null;
    }

    public int buscarVagaLivre() {
        for (Vaga vaga : vagas) {
            if ("livre".equalsIgnoreCase(vaga.getStatus())) {
                return vaga.getNumero();
            }
        }
        return -1;
    }

    public boolean marcarVagaComoLivre(int numero) {
        Vaga vaga = buscarVagaPorNumero(numero);
        if (vaga != null) {
            vaga.setStatus("livre");
            vaga.setPlacaCarro(null);
            vagaDAO.marcarVagaComoLivre(numero);
            return true;
        }
        return false;
    }
    public void atualizarStatusVagaNaLista(int numeroVaga, String novoStatus) {
        for (Vaga vaga : vagas) {
            if (vaga.getNumero() == numeroVaga) {
                vaga.setStatus(novoStatus);
                return;
            }
        }
    }
    public boolean marcarVagaComoOcupada(int numero, String placa) {
        Vaga vaga = buscarVagaPorNumero(numero);
        if (vaga != null) {
            vaga.setStatus("ocupada");
            vaga.setPlacaCarro(placa);
            vagaDAO.marcarVagaComoOcupada(numero,placa);
            return true;
        }
        return false;
    }
    public boolean adicionarVaga(Vaga vaga) {
        if (buscarVagaPorNumero(vaga.getNumero()) == null) {
            vagas.add(vaga);
            VagaDAO vagaDAO = new VagaDAO();
            return vagaDAO.adicionarVaga(vaga);
        }
        return false;
    }
    public boolean removerVaga(int numero) {
        Vaga vaga = buscarVagaPorNumero(numero);
        if (vaga != null) {
            vagas.remove(vaga);
            VagaDAO vagaDAO = new VagaDAO();
            return vagaDAO.removerVaga(numero);
        }
        return false;
    }
}

