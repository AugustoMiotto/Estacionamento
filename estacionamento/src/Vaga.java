public class Vaga {
    private int numero;
    private String status;
    private String placaCarro;

    public Vaga(int numero) {
        this.numero = numero;

    }
    public Vaga(int numero, String status, String placaCarro) {
        this.numero = numero;
        this.status = status;
        this.placaCarro = placaCarro;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlacaCarro() {
        return placaCarro;
    }

    public void setPlacaCarro(String placaCarro) {
        this.placaCarro = placaCarro;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
}
