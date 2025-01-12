import java.time.LocalDateTime;
import java.time.Duration;
import java.time.LocalTime;

public class Carro {
    private String placa;
    private String modelo;
    private String cor;
    private String idDono; // ID do cliente
    private int vaga;
    private LocalDateTime horarioEntrada;
    private LocalDateTime horarioSaida;
    private Duration duracao;

    // Construtor
    public Carro(String placa, String modelo, String cor, String idDono, int vaga, LocalDateTime horarioEntrada) {
        this.placa = placa;
        this.modelo = modelo;
        this.cor = cor;
        this.idDono = idDono;
        this.vaga = vaga;
        this.horarioEntrada = horarioEntrada;
    }

    //Construtor DAO

    public Carro(String modelo, String placa, String cor, LocalDateTime horarioEntrada) {
        this.modelo = modelo;
        this.placa = placa;
        this.cor = cor;
        this.horarioEntrada = horarioEntrada;
    }

    // Getters e setters
    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getIdDono() {
        return idDono;
    }

    public void setIdDono(String idDono) {
        this.idDono = idDono;
    }

    public int getVaga() {
        return vaga;
    }

    public void setVaga(int vaga) {
        this.vaga = vaga;
    }

    public LocalDateTime getHorarioEntrada() {
        return horarioEntrada;
    }

    public void setHorarioEntrada(LocalDateTime horarioEntrada) {
        this.horarioEntrada = horarioEntrada;
    }

    public LocalDateTime getHorarioSaida() {
        return horarioSaida;
    }

    public void setHorarioSaida(LocalDateTime horarioSaida) {
        this.horarioSaida = horarioSaida;
        calcularDuracao();
    }

    public Duration getDuracao() {
        return duracao;
    }


    private void calcularDuracao() {
        if (horarioSaida != null && horarioEntrada != null) {
            this.duracao = Duration.between(horarioEntrada, horarioSaida);
        }
    }

    /**
     *
     * @param entrada Hora de entrada do veículo
     * @param saida Hora de Saída do Veículo
     * @return custo total.
     * As duas primeiras horas são 10 reais; Após isso são 6 reais por hora; Do periodo das 20:00 até às 6:00 é cobrado taxa fixa de 30 reais.
     */

    public static double calcularCustoNoturno(LocalDateTime entrada, LocalDateTime saida) {
        double custo = 0.0;
        LocalTime inicioNoturno = LocalTime.of(20, 0);
        LocalTime fimNoturno = LocalTime.of(6, 0);

        LocalDateTime inicioPeriodoNoturno = LocalDateTime.of(entrada.toLocalDate(), inicioNoturno);
        LocalDateTime fimPeriodoNoturno = LocalDateTime.of(saida.toLocalDate(), fimNoturno);

        if (fimPeriodoNoturno.isBefore(inicioPeriodoNoturno)) {
            fimPeriodoNoturno = fimPeriodoNoturno.plusDays(1);
        }

        if (saida.isBefore(inicioPeriodoNoturno) || entrada.isAfter(fimPeriodoNoturno)) {
            // Totalmente fora do período noturno
            custo = calcularTarifaPorPeriodo(entrada, saida);
        } else if (entrada.isBefore(inicioPeriodoNoturno) && saida.isAfter(fimPeriodoNoturno)) {
            // Entrada antes do período noturno e saída após
            custo += calcularTarifaPorPeriodo(entrada, inicioPeriodoNoturno);
            custo += calcularTarifaPorPeriodo(fimPeriodoNoturno, saida);
            custo += 30.0; // Período noturno
        } else if (entrada.isAfter(inicioPeriodoNoturno) && saida.isBefore(fimPeriodoNoturno)) {
            // Totalmente dentro do período noturno
            custo = 30.0;
        } else if (entrada.isBefore(inicioPeriodoNoturno)) {
            // Entrada no período diurno e saída no período noturno
            custo += calcularTarifaPorPeriodo(entrada, inicioPeriodoNoturno);
            custo += 30.0;
        } else if (saida.isAfter(fimPeriodoNoturno)) {
            // Entrada no período noturno e saída no período diurno
            custo += 30.0;
            custo += calcularTarifaPorPeriodo(fimPeriodoNoturno, saida);
        }

        return custo;
    }

    public static double calcularTarifaPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        long minutos = Duration.between(inicio, fim).toMinutes();
        double custo = 0.0;
        if (minutos <= 120) {
            // Primeiras 2 horas (10 reais por hora)
            custo = (minutos / 60) * 10.0 + ((minutos % 60 > 0) ? 10.0 : 0.0);
        } else {
            // Depois de 2 horas (6 reais por hora)
            custo = 20.0;
            minutos -= 120;
            custo += (minutos / 60) * 6.0 + ((minutos % 60 > 0) ? 6.0 : 0.0);
        }
        return custo;
    }
}

