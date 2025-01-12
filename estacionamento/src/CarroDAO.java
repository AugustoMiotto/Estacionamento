import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CarroDAO {

    public boolean inserirCarro(Carro carro) {
        String sqlCarro = "INSERT INTO Carros (placa, modelo, cor, id_dono, vaga, horario_entrada) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlVaga = "UPDATE vagas SET placa_carro = ? WHERE numero = ?";

        try (Connection conn = Connect.conectar()) {
            conn.setAutoCommit(false); // Inicia transação

            try (PreparedStatement stmtCarro = conn.prepareStatement(sqlCarro)) {
                stmtCarro.setString(1, carro.getPlaca());
                stmtCarro.setString(2, carro.getModelo());
                stmtCarro.setString(3, carro.getCor());
                stmtCarro.setString(4, carro.getIdDono());
                stmtCarro.setInt(5, carro.getVaga());
                stmtCarro.setTimestamp(6, Timestamp.valueOf(carro.getHorarioEntrada()));

                stmtCarro.executeUpdate();
            }

            // Atualizar vaga
            try (PreparedStatement stmtVaga = conn.prepareStatement(sqlVaga)) {
                stmtVaga.setString(1, carro.getPlaca());
                stmtVaga.setInt(2, carro.getVaga());

                stmtVaga.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao adicionar o carro: " + e.getMessage());
            return false;
        }
    }

    public List<Carro> listarCarros() {
        List<Carro> carros = new ArrayList<>();
        String sql = "SELECT * FROM Carros";

        try (Connection conn = Connect.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String placa = rs.getString("placa");
                String modelo = rs.getString("modelo");
                String cor = rs.getString("cor");
                String idDono = rs.getString("id_dono");
                int vaga = rs.getInt("vaga");
                Timestamp horarioEntrada = rs.getTimestamp("horario_entrada");

                String horaEntrada = horarioEntrada.toLocalDateTime().toLocalTime().toString();

                // objeto Carro com os dados recuperados
                Carro carro = new Carro(placa, modelo, cor, idDono, vaga, horarioEntrada.toLocalDateTime());

                carros.add(carro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carros;
    }

    public boolean removerCarro(String placa) {
        String sqlRemoverVaga = "UPDATE vagas SET placa_carro = NULL WHERE placa_carro = ?";
        String sqlRemoverCarro = "DELETE FROM Carros WHERE placa = ?";

        try (Connection conn = Connect.conectar()) {
            conn.setAutoCommit(false);

            // desvincula a placa da vaga
            try (PreparedStatement stmtVaga = conn.prepareStatement(sqlRemoverVaga)) {
                stmtVaga.setString(1, placa);
                stmtVaga.executeUpdate();
            }

            // remove o carro
            try (PreparedStatement stmtCarro = conn.prepareStatement(sqlRemoverCarro)) {
                stmtCarro.setString(1, placa);
                int rowsAffected = stmtCarro.executeUpdate();

                if (rowsAffected > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean atualizarVagaPorPlaca(String placa, int novaVaga) {
        String sql = "UPDATE carros SET vaga = ? WHERE placa = ?";
        try (Connection conn = Connect.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, novaVaga);
            stmt.setString(2, placa);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

}



