import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VagaDAO {
    /**
     *
     * @param vaga número da vaga
     * Para a inserção de uma vaga que não existe no banco (o banco vem com 50 vagas disponíveis),
     basta digitar o número da vaga no TextField vaga e clicar no botão "modificar" que aparece no canto inferior esquerdo.
     */
    public boolean adicionarVaga(Vaga vaga) {
        String sql = "INSERT INTO vagas (numero, status) VALUES (?, 'livre')";

        try (Connection conn = Connect.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vaga.getNumero());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param numero numero da vaga
     * Para a remoção da vaga no banco, digita-se apenas a vaga no textField
     da vaga e clica no botão de remover que está na parte do grid.
     */
    public boolean removerVaga(int numero) {
        String sql = "DELETE FROM vagas WHERE numero = ?";
        try (Connection conn = Connect.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, numero);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean atualizarStatusVaga(int numeroVaga, String status) {
        String sql = "UPDATE vagas SET status = ? WHERE numero = ?";
        try (Connection conn = Connect.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, numeroVaga);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean marcarVagaComoLivre(int vaga) {
        String sql = "UPDATE vagas SET placa_carro = NULL, status = 'livre' WHERE numero = ?";
        try (Connection conn = Connect.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vaga);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean marcarVagaComoOcupada(int vaga, String placa) {
        String sql = "UPDATE vagas SET placa_carro = ?, status = 'ocupada' WHERE numero = ?";
        try (Connection conn = Connect.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, placa);
            stmt.setInt(2, vaga);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    public List<Vaga> listarVagas() {
        String sql = "SELECT * FROM vagas";
        List<Vaga> vagas = new ArrayList<>();

        try (Connection conn = Connect.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int numero = rs.getInt("numero");
                String status = rs.getString("status");
                String placaCarro = rs.getString("placa_carro");

                vagas.add(new Vaga(numero, status, placaCarro));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vagas;
    }

}
