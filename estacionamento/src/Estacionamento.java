import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.table.DefaultTableModel;

public class Estacionamento extends JFrame{
    private JPanel panel1;
    private JButton btgrid;
    private JButton btEditar;
    private JButton btcarros;
    private JTextField tfmodelo;
    private JTextField tfcor;
    private JTextField tfplaca;
    private JTextField tfvaga;
    private JTextField tfcliente;
    private JTextField tftelefone;
    private JButton btadicionar;
    private JButton btcancelar;
    private JPanel panelCards;
    private JPanel panelAddCarro;
    private JPanel panelGrid;
    private JButton btRemover;
    private JTextField tfNome;
    private JTable tablegrid;
    private JButton btatualizar;
    private JScrollPane scrollpanel;
    private JButton btModificar;

    ListaDeCarros listaDeCarros = new ListaDeCarros();
    ListaDeClientes listaDeClientes = new ListaDeClientes();
    ListaDeVagas listaDeVagas = new ListaDeVagas();

    public Estacionamento() {
        tela();
        configurarTabela();
        adicionar_carro();
        DefaultTableModel modeloTabela = new DefaultTableModel(new String[]{"Placa", "Modelo", "Cor", "Vaga", "Proprietário", "Entrada"}, 0);
        tablegrid.setModel(modeloTabela);
        carregarDados(modeloTabela);
        configurarBotaoEditar();
        configurarBotaoRemover();
        configurarBotaoModificar();
        botaoAtualizar();
        mostrar_telefone();
    }

    private void tela() {
        setContentPane(panel1);
        CardLayout layoutManager = ((CardLayout) panelCards.getLayout());
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(600,400);
        setResizable(false);
        panelCards.add(panelAddCarro, "panelAddCarro");
        panelCards.add(panelGrid,"panelGrid");

        /**
         * Para acionar o painel de adicionar veículo, clica-se no icone de carro.
         * Para acionar o painel do grid de dados, clica-se no icone de tabela.
         */
        btcarros.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                layoutManager.show(panelCards,"panelAddCarro");
                }
        });
        btgrid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                layoutManager.show(panelCards,"panelGrid");
            }
        });
    }

    private void adicionar_carro() {
        btadicionar.addActionListener(e -> {
            boolean validarcampos = true;
            try {
                // dados dos campos de texto
                String placa = tfplaca.getText().trim();
                String modelo = tfmodelo.getText().trim();
                String cor = tfcor.getText().trim();
                String idDono = tfcliente.getText().trim();
                String vagaTexto = tfvaga.getText().trim();
                int vagac = Integer.parseInt(vagaTexto);
                LocalDateTime horarioEntrada = LocalDateTime.now(); // Hora atual
                String telefone = tftelefone.getText().trim();
                String nome = tfNome.getText().trim();


                // Validação dos campos
                if (placa.isEmpty() || modelo.isEmpty() || cor.isEmpty() || idDono.isEmpty() || telefone.isEmpty() || nome.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios!", "Erro", JOptionPane.ERROR_MESSAGE);
                    validarcampos = false;
                    return;
                }

                //ADICIONAR CLIENTE
                // Verificar se o cliente já existe no banco de dados
                Cliente clienteExistente = listaDeClientes.buscarClientePorId(idDono);
                boolean verifica_cliente = false;
                if (clienteExistente == null) {
                    Cliente novoCliente = new Cliente(nome, idDono, telefone);
                    boolean clienteAdicionado = listaDeClientes.adicionarCliente(novoCliente);
                    if (!clienteAdicionado) {
                        JOptionPane.showMessageDialog(this, "Erro ao adicionar o cliente. Verifique os dados.", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    // Cliente já existe
                    JOptionPane.showMessageDialog(this, "Cliente já cadastrado. O carro foi adicionado com sucesso", "Cliente Existente", JOptionPane.INFORMATION_MESSAGE);
                    verifica_cliente = true;
                }

                //verifica se há vaga livre
                int vagaLivre = listaDeVagas.buscarVagaLivre();
                if (vagaLivre == -1) {
                    JOptionPane.showMessageDialog(this, "Nenhuma vaga disponível!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                //verifica se a vaga existe e se está livre
                Vaga vagabusca = listaDeVagas.buscarVagaPorNumero(vagac);
                if (vagabusca == null) {
                    JOptionPane.showMessageDialog(this, "A vaga número " + vagac + " não existe!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!vagabusca.getStatus().equals("livre")) {
                    JOptionPane.showMessageDialog(this, "A vaga número " + vagac + " já está ocupada!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                //atualiza o status da vaga
                boolean vagaOcupada = listaDeVagas.atualizarStatusVaga(vagac,"ocupada");

                if (vagaOcupada) {
                    JOptionPane.showMessageDialog(this, "Vaga ocupada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao ocupar a vaga.", "Erro", JOptionPane.ERROR_MESSAGE);
                }

                //ADICIONAR CARRO
                Carro carro = new Carro(placa, modelo, cor, idDono, vagac, horarioEntrada);
                boolean carroAdicionado = listaDeCarros.adicionarCarro(carro);

                if (!carroAdicionado) {
                    JOptionPane.showMessageDialog(this, "Erro ao adicionar o carro. Verifique os dados.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!verifica_cliente) {
                    JOptionPane.showMessageDialog(this, "Carro adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }
                // Limpa os campos
                tfplaca.setText("");
                tfmodelo.setText("");
                tfcor.setText("");
                tfcliente.setText("");
                tfvaga.setText("");
                tfNome.setText("");
                tftelefone.setText("");

            } catch (NumberFormatException ex) {
                if (!validarcampos) {
                    JOptionPane.showMessageDialog(this, "Vaga deve ser um número válido!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
                }
        });

        //Botão de cancelar
        btcancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tfplaca.setText("");
                tfmodelo.setText("");
                tfcor.setText("");
                tfcliente.setText("");
                tfvaga.setText("");
                tfNome.setText("");
                tftelefone.setText("");

            }
        });

    }

    private void carregarDados(DefaultTableModel modeloTabela) {

        if (listaDeCarros.get().isEmpty()) {
            System.out.println("Nenhum carro foi carregado.");
        } else {
            System.out.println("Carros carregados: " +listaDeCarros.get().size());
        }

        modeloTabela.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Adicionar os carros à tabela
        for (Carro carro : listaDeCarros.get()) {
            modeloTabela.addRow(new Object[] {
                    carro.getPlaca(),
                    carro.getModelo(),
                    carro.getCor(),
                    carro.getVaga(),
                    carro.getIdDono(),
                    carro.getHorarioEntrada().toLocalTime().format(formatter)
            });
        }
    }

    private void configurarTabela() {
        String[] colunas = {"Placa", "Modelo", "Cor", "Vaga", "Proprietário", "Entrada"};

        // Criar o modelo da tabela
        DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // As células não são editáveis
            }
        };

        // Configurar o JTable com o modelo
        tablegrid.setModel(modeloTabela);

        // Carregar os dados da lista na tabela
        carregarDados(modeloTabela);

        // Configurar o cabeçalho da tabela
        tablegrid.setTableHeader(tablegrid.getTableHeader());

        // Desmarcar a linha ao clicar fora
        JScrollPane scrollPane = (JScrollPane) tablegrid.getParent().getParent();
        scrollPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Verifica se o clique ocorreu fora da tabela
                Point point = e.getPoint();
                int row = tablegrid.rowAtPoint(SwingUtilities.convertPoint(scrollPane, point, tablegrid));
                if (row == -1) {
                    tablegrid.clearSelection();
                }
            }
        });
    }

    /**
     * A edição de dados permite alterar a vaga do carro e o telefone do cliente, ao selecionar um carro no grid e clicar no botão editar
     o programa ira redirecionar o usuário aos campos de texto, então é possível digitar a nova vaga e/ou o novo telefone e clicar no botão "Modificar".
     */
    private void configurarBotaoEditar() {
        btEditar.addActionListener(e -> {
            // Verifica se uma linha foi selecionada
            int selectedRow = tablegrid.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Selecione uma linha para editar.", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(null,"Digite a nova vaga e/ou novo telefone do cliente.","info",JOptionPane.INFORMATION_MESSAGE);

            try {
                // Obtém os dados da linha selecionada
                DefaultTableModel model = (DefaultTableModel) tablegrid.getModel();
                String placa = (String) model.getValueAt(selectedRow, 0);  // Pega a placa do carro
                String idDono = (String) model.getValueAt(selectedRow, 4);  // Pega o ID do dono

                // Busca os dados do carro e do cliente na lista
                Carro carro = listaDeCarros.buscarCarroPorPlaca(placa);
                Cliente cliente = listaDeClientes.buscarClientePorId(idDono);

                if (carro != null && cliente != null) {
                    // Preenche os campos de edição com os dados
                    tfplaca.setText(carro.getPlaca());
                    tfmodelo.setText(carro.getModelo());
                    tfcor.setText(carro.getCor());
                    tfcliente.setText(carro.getIdDono());
                    tftelefone.setText(cliente.getTelefone());
                    tfvaga.setText(String.valueOf(carro.getVaga()));
                    tfNome.setText(cliente.getNome());

                    // Navega para o painel de edição
                    CardLayout layoutManager = (CardLayout) panelCards.getLayout();
                    layoutManager.show(panelCards, "panelAddCarro");
                } else {
                    JOptionPane.showMessageDialog(this, "Carro ou cliente não encontrados.", "Erro", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar os dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
    }

    private void configurarBotaoModificar(){
        btModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String placa = tfplaca.getText().trim();
                String modelo = tfmodelo.getText().trim();
                String cor = tfcor.getText().trim();
                String idDono = tfcliente.getText().trim();
                String vagaTexto = tfvaga.getText().trim();
                int vagac = Integer.parseInt(vagaTexto);
                String telefone = tftelefone.getText().trim();
                String nome = tfNome.getText().trim();

                //ADICIONAR NOVA VAGA AO BANCO
                if (!vagaTexto.isEmpty() && placa.isEmpty() && modelo.isEmpty() && cor.isEmpty() && idDono.isEmpty() && telefone.isEmpty() && nome.isEmpty()) {
                    Vaga vaga = new Vaga(vagac);

                    boolean vagaAdicionada = false;

                    vagaAdicionada = listaDeVagas.adicionarVaga(vaga);

                    if (vagaAdicionada) {
                        JOptionPane.showMessageDialog(null, "Vaga adicionada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao adicionar vaga. Verifique os dados.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }

                    // Limpa o campo de texto
                    tfvaga.setText("");
                    return;
                }
                try {
                    // Validação dos campos
                    if (telefone.isEmpty() || vagaTexto.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios (telefone e vaga).", "Erro", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    // Converte a vaga para inteiro
                    int novaVaga;
                    try {
                        novaVaga = Integer.parseInt(vagaTexto);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Vaga deve ser um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (placa.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Erro: Não há carro selecionado para modificar.", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Buscar informações da vaga antiga
                    int vagaAntiga = listaDeCarros.buscarVagaPorPlaca(placa);

                    // Atualizar telefone do cliente no banco de dados
                    boolean clienteAtualizado = listaDeClientes.atualizarTelefonePorPlaca(placa, telefone);

                    // Marcar a vaga antiga como livre
                    boolean vagaAntigaLiberada = listaDeVagas.marcarVagaComoLivre(vagaAntiga);

                    // Atualizar vaga do carro no banco de dados
                    boolean carroAtualizado = listaDeCarros.atualizarVagaPorPlaca(placa, novaVaga);

                    // Marcar a nova vaga como ocupada
                    boolean novaVagaOcupada = listaDeVagas.marcarVagaComoOcupada(novaVaga,placa);

                    if (clienteAtualizado && carroAtualizado && vagaAntigaLiberada && novaVagaOcupada) {
                        JOptionPane.showMessageDialog(null, "Dados atualizados com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                        tfplaca.setText("");
                        tfmodelo.setText("");
                        tfcor.setText("");
                        tfcliente.setText("");
                        tftelefone.setText("");
                        tfvaga.setText("");
                        tfNome.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao atualizar os dados. Verifique as informações.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

    }

    private void configurarBotaoRemover() {
        btRemover.addActionListener(e -> {
            try {
                // REMOVE VAGA
                String vagaTexto = tfvaga.getText().trim();

                // Verifica se apenas o campo da vaga foi preenchido
                if (!vagaTexto.isEmpty() && tfplaca.getText().isEmpty() && tfmodelo.getText().isEmpty() &&
                        tfcor.getText().isEmpty() && tfcliente.getText().isEmpty() &&
                        tftelefone.getText().isEmpty() && tfNome.getText().isEmpty()) {

                    int numero = Integer.parseInt(vagaTexto);

                    // Remove a vaga do banco
                    boolean vagaRemovida = listaDeVagas.removerVaga(numero);
                    if (vagaRemovida) {
                        JOptionPane.showMessageDialog(null, "Vaga removida com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Erro ao remover a vaga. Verifique se a vaga existe.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }

                    tfvaga.setText("");
                    return;
                }

                // REMOVE CARRO e CLIENTE
                // Verifica se a tabela está vazia
                if (tablegrid.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "A tabela está vazia.", "Erro", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Verifica se uma linha foi selecionada
                int selectedRow = tablegrid.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Selecione um carro para remover.", "Erro", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                DefaultTableModel model = (DefaultTableModel) tablegrid.getModel();
                String placa = (String) model.getValueAt(selectedRow, 0);
                int vaga = (int) model.getValueAt(selectedRow, 3);

                // Buscar o carro na lista
                Carro carro = listaDeCarros.buscarCarroPorPlaca(placa);
                if (carro == null) {
                    JOptionPane.showMessageDialog(this, "Carro não encontrado na lista!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // CALCULA CUSTO DO VEÍCULO
                LocalDateTime horarioEntrada = carro.getHorarioEntrada();
                LocalDateTime horarioSaida = LocalDateTime.now();
                double custo = carro.calcularCustoNoturno(horarioEntrada, horarioSaida);

                // Exibe o tempo de permanência e o custo
                Duration duracao = Duration.between(horarioEntrada, horarioSaida);
                long horas = duracao.toHours();
                long minutos = duracao.toMinutes() % 60;

                String mensagem = String.format(
                        "O veículo com placa %s ficou estacionado por %02d horas e %02d minutos.\nCusto total: R$ %.2f", placa, horas, minutos, custo);
                JOptionPane.showMessageDialog(this, mensagem, "Resumo", JOptionPane.INFORMATION_MESSAGE);

                // Remove da lista
                boolean sucessoRemover = listaDeCarros.removerCarro(placa,vaga);
                if (!sucessoRemover) {
                    JOptionPane.showMessageDialog(this, "Erro ao remover o carro do banco de dados!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                    // Remove da tabela
                    model.removeRow(selectedRow);
                    listaDeVagas.atualizarStatusVagaNaLista(vaga, "livre");
                    JOptionPane.showMessageDialog(this, "Carro removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void botaoAtualizar() {
        btatualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    DefaultTableModel modeloTabela = (DefaultTableModel) tablegrid.getModel();
                    modeloTabela.setRowCount(0);  // Limpa os dados existentes da tabela

                    // Recarrega os dados da tabela a partir da lista de carros
                    carregarDados(modeloTabela);

                    // Verifica se existem carros para adicionar
                    if (modeloTabela.getRowCount() == 0) {
                        System.out.println("Nenhum carro encontrado.");  // Log caso não existam carros
                        JOptionPane.showMessageDialog(Estacionamento.this, "Nenhum carro encontrado!", "Aviso", JOptionPane.WARNING_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(Estacionamento.this, "Erro ao atualizar a tabela.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Para mostrar o telefone, basta clicar no campo do id do cliente no grid de dados.
     */
    private void mostrar_telefone(){
        tablegrid.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tablegrid.rowAtPoint(e.getPoint());
                int col = tablegrid.columnAtPoint(e.getPoint());

                int colunaProprietario = 4;
                if (col == colunaProprietario && row >= 0) {
                    Object idProprietario = tablegrid.getValueAt(row, colunaProprietario);
                    if (idProprietario != null) {

                        String telefone = listaDeClientes.buscarTelefoneNaLista(idProprietario.toString());
                        String nome = listaDeClientes.buscarNomeNaLista(idProprietario.toString());
                        if (telefone != null) {
                            JOptionPane.showMessageDialog(null, "O telefone do proprietário "+ nome +" é: " + telefone, "Informação", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Telefone não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
    }

}


