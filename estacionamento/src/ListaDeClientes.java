import java.util.ArrayList;
import java.util.List;

public class ListaDeClientes {
    private List<Cliente> clientes = new ArrayList<>();
    ListaDeCarros listaDeCarros = new ListaDeCarros();

    public ListaDeClientes() {
        carregarClientesDoBanco();
    }

    public void carregarClientesDoBanco() {
        if (clientes == null) {
            clientes = new ArrayList<>();
        }
        clientes.clear();

        ClienteDAO clienteDAO = new ClienteDAO();
        List<Cliente> listaClientesBanco = clienteDAO.listarClientes();

        if (listaClientesBanco != null && !listaClientesBanco.isEmpty()) {
            clientes.addAll(listaClientesBanco);
        } else {
            System.out.println("Nenhum cliente encontrado no banco.");
        }
    }

    public boolean adicionarCliente(Cliente cliente) {
        clientes.add(cliente);
        ClienteDAO clienteDAO = new ClienteDAO();
        return clienteDAO.adicionarCliente(cliente);
    }

    public boolean atualizarTelefonePorPlaca(String placa, String novoTelefone) {
        Carro carro = listaDeCarros.buscarCarroPorPlaca(placa);
        if (carro != null) {
            String idDono = carro.getIdDono();
            ClienteDAO clienteDAO = new ClienteDAO();
            boolean atualizado = clienteDAO.atualizarTelefonePorPlaca(placa, novoTelefone);
            if (atualizado) {
                Cliente cliente = buscarClientePorId(idDono);
                if (cliente != null) {
                    cliente.setTelefone(novoTelefone);
                }
            }
            return atualizado;
        }
        return false;
    }

    public Cliente buscarClientePorId(String idCliente) {
        for (Cliente cliente : clientes) {
            if (cliente.getId().equalsIgnoreCase(idCliente)) {
                return cliente;
            }
        }
        return null;
    }

    public String buscarNomeNaLista(String idProprietario) {
        for (Cliente cliente : clientes) {
            if (cliente.getId().equals(idProprietario)) {
                return cliente.getNome();
            }
        }
        return null;
    }

    public String buscarTelefoneNaLista(String idProprietario) {
        for (Cliente cliente : clientes) {
            if (cliente.getId().equalsIgnoreCase(idProprietario)) {
                return cliente.getTelefone();
            }
        }
        return null;
    }
}

