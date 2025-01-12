import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    // inserir no banco
    public boolean adicionarCliente(Cliente cliente) {
        String sql = "INSERT INTO Clientes (nome, id, telefone) VALUES (?, ?, ?)";

        try (Connection conn = Connect.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Configurando os parâmetros da consulta
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getId());
            stmt.setString(3, cliente.getTelefone());


            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Cliente> listarClientes() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT nome, id, telefone FROM clientes";

        try (Connection conn = Connect.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("nome");
                String nome = rs.getString("id");
                String telefone = rs.getString("telefone");
                lista.add(new Cliente(id, nome, telefone));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }


    public boolean atualizarTelefonePorPlaca(String placa, String novoTelefone) {
        String sql = "UPDATE clientes c " +
                "JOIN carros ca ON c.id = ca.id_dono " +
                "SET c.telefone = ? WHERE ca.placa = ?";
        try (Connection conn = Connect.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novoTelefone);
            stmt.setString(2, placa);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
