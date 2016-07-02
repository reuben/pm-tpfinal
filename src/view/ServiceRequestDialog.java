package view;

import controllers.AppController;
import controllers.ServiceRequestController;
import controllers.TechnicianController;
import model.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class ServiceRequestDialog extends JDialog {
    class MaterialsTableModel extends DefaultTableModel {
        public MaterialsTableModel() {
            super(new String[]{"Qtde.", "Material", "Valor"}, 0);
            this.editable = true;
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return editable;
        }

        public void setEditable(boolean editable) {
            this.editable = editable;
        }

        private boolean editable;

        public boolean isEditable() {
            return editable;
        }
    }

    private void createUIComponents() {
        DecimalFormat format = (DecimalFormat)DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setCurrencySymbol("R$ ");
        symbols.setGroupingSeparator('.');
        symbols.setMonetaryDecimalSeparator(',');
        format.setDecimalFormatSymbols(symbols);
        format.setMaximumFractionDigits(2);
        NumberFormatter formatter = new NumberFormatter(format);
        this.valueTextField = new JFormattedTextField(formatter);
        this.valueTextField.setValue(0.0);
        this.hourlyRateTextField = new JFormattedTextField(formatter);
        this.hourlyRateTextField.setValue(0.0);
        currencyFormat = format;
    }

    private NumberFormat currencyFormat;

    private ServiceRequestDialog(ServiceRequest serviceRequest) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setSize(600, 400);
        setMinimumSize(new Dimension(600, 400));
        setPreferredSize(new Dimension(600, 400));
        setTitle("Requisição de serviço");

        this.serviceRequest = serviceRequest;

        materialsTableModel = new MaterialsTableModel();
        materialsTable.setModel(materialsTableModel);
        materialsTable.setPreferredScrollableViewportSize(materialsTable.getPreferredSize());
        materialsTable.setFillsViewportHeight(true);
        // Create new row on double click
        materialsTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (materialsTableModel.isEditable() && e.getClickCount() == 2) {
                    materialsTableModel.setRowCount(materialsTableModel.getRowCount() + 1);
                    materialsTable.editCellAt(materialsTableModel.getRowCount() - 1, 0);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        resetUI();

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void resetUI() {
        for (ActionListener al : saveEstimateBtn.getActionListeners()) {
            saveEstimateBtn.removeActionListener(al);
        }

        for (ActionListener al : createEstimateBtn.getActionListeners()) {
            createEstimateBtn.removeActionListener(al);
        }

        for (ActionListener al : technicianComboBox.getActionListeners()) {
            technicianComboBox.removeActionListener(al);
        }

        statusLabel.setText(serviceRequest.getStatus().toString());
        clientLabel.setText("<html>" +
                serviceRequest.getRequester().getName() + "<br>" +
                serviceRequest.getRequester().getId() + "<br>" +
                serviceRequest.getRequester().getCPF() + "<br>" +
                serviceRequest.getRequester().getAddress() + "<br>" +
                serviceRequest.getRequester().getPhone() + "<br>" +
                serviceRequest.getRequester().getEmail() +
                "</html>");
        if (serviceRequest.getTechnician() != null) {
            technicianLabel.setText(serviceRequest.getTechnician().getName());
        }

        TaskType[] taskTypesForServiceRequest = ServiceRequestController.getTaskTypesForServiceRequest(serviceRequest);

        taskTypesList.setListData(Arrays.stream(taskTypesForServiceRequest)
                .map(v -> new TaskTypeCheckBox(v, true, false))
                .toArray(TaskTypeCheckBox[]::new));

        descriptionTextArea.setText(serviceRequest.getDescription());

        if (serviceRequest.getEstimate() != null) {
            // Enable estimate tab
            tabbedPane1.setEnabledAt(1, true);

            // Disable editing of fields
            technicianComboBox.setEnabled(false);
            serviceHoursTextField.setEditable(false);
            hourlyRateTextField.setEditable(false);
            materialsTableModel.setEditable(false);

            // Set estimate data
            technicianComboBox.removeAllItems();
            technicianComboBox.addItem(serviceRequest.getTechnician());
            technicianComboBox.setSelectedItem(serviceRequest.getTechnician());

            final ServiceEstimate estimate = serviceRequest.getEstimate();
            serviceHoursTextField.setText(String.valueOf(estimate.getServiceHours()));
            hourlyRateTextField.setValue(estimate.getHourlyRate());

            while (materialsTableModel.getRowCount() > 0) {
                materialsTableModel.removeRow(0);
            }

            for (ServiceEstimate.MaterialEntry material : estimate.getMaterials()) {
                materialsTableModel.addRow(new Object[] {
                        material.get(0),
                        material.get(1),
                        material.get(2)
                });
            }

            valueTextField.setValue(estimate.getValue());
            taxValueLabel.setText("" + estimate.getServiceTax()*100 + "%");
            totalValueLabel.setText(currencyFormat.format(estimate.getFinalValue()));
        }

        if (serviceRequest.getStatus() == ServiceRequest.Status.Registered) {
            this.tempEstimate = new ServiceEstimate(serviceRequest);

            for (Technician technician : TechnicianController.getTechniciansForTaskTypes(Arrays.asList(taskTypesForServiceRequest))) {
                technicianComboBox.addItem(technician);
            }
            technicianComboBox.addActionListener(e -> {
                serviceRequest.setStatus(ServiceRequest.Status.AwaitingEstimate);
                serviceRequest.setTechnician((Technician)technicianComboBox.getSelectedItem());
                ServiceRequestController.updateServiceRequest(serviceRequest);
            });
            valueTextField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    recalculateTaxAndFinalValue();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    recalculateTaxAndFinalValue();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    recalculateTaxAndFinalValue();
                }

                private void recalculateTaxAndFinalValue() {
                    tempEstimate.setValue(((Number)valueTextField.getValue()).doubleValue());
                    tempEstimate.applyTax();
                    taxValueLabel.setText("" + tempEstimate.getServiceTax()*100 + "%");
                    totalValueLabel.setText(currencyFormat.format(tempEstimate.getFinalValue()));
                }
            });
            computeValueBtn.addActionListener(e -> {
                double hours = 0.0;
                if (!serviceHoursTextField.getText().isEmpty()) {
                    hours = Double.valueOf(serviceHoursTextField.getText());
                }
                tempEstimate.setServiceHours(hours);

                double hourlyRate = 0.0;
                if (!hourlyRateTextField.getText().isEmpty()) {
                    hourlyRate = ((Number)hourlyRateTextField.getValue()).doubleValue();
                }
                tempEstimate.setHourlyRate(hourlyRate);

                double totalCost = hours * hourlyRate;

                for (int i = 0; i < materialsTableModel.getRowCount(); ++i) {
                    if (((String)materialsTableModel.getValueAt(i, 0)).isEmpty()) {
                        continue;
                    }
                    long quantity = Long.valueOf((String)materialsTableModel.getValueAt(i, 0));
                    double unitCost = Double.valueOf((String)materialsTableModel.getValueAt(i, 2));
                    totalCost += quantity * unitCost;
                }

                valueTextField.setValue(totalCost);
                tempEstimate.setValue(totalCost);
                tempEstimate.applyTax();
                taxValueLabel.setText("" + tempEstimate.getServiceTax()*100 + "%");
                totalValueLabel.setText(currencyFormat.format(tempEstimate.getFinalValue()));
            });
            createEstimateBtn.addActionListener(e -> {
                tabbedPane1.setEnabledAt(1, true);
                tabbedPane1.setSelectedIndex(1);
            });
            saveEstimateBtn.addActionListener(e -> {
                String formattedValue = currencyFormat.format(tempEstimate.getFinalValue());
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Tem certeza que deseja salvar o orçamento no valor de " + formattedValue + "?",
                        "Confirmar orçamento",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (confirm != JOptionPane.YES_OPTION) {
                    serviceRequest.setTechnician(null);
                    ServiceRequestController.updateServiceRequest(serviceRequest);
                    resetUI();
                    tabbedPane1.setSelectedIndex(0);
                    return;
                }

                ArrayList<ServiceEstimate.MaterialEntry> materials = new ArrayList<>();
                for (int i = 0; i < materialsTableModel.getRowCount(); ++i) {
                    materials.add(new ServiceEstimate.MaterialEntry(
                            Long.valueOf((String)materialsTableModel.getValueAt(i, 0)),
                            (String)materialsTableModel.getValueAt(i, 1),
                            Double.valueOf((String)materialsTableModel.getValueAt(0, 2))));
                }
                tempEstimate.setMaterials(materials);
                tempEstimate.setValue(((Number)valueTextField.getValue()).doubleValue());
                tempEstimate.applyTax();
                ServiceRequestController.saveNewEstimate(tempEstimate);
                serviceRequest.setTechnician((Technician)technicianComboBox.getSelectedItem());
                serviceRequest.setEstimate(tempEstimate);
                serviceRequest.setStatus(ServiceRequest.Status.AwaitingApproval);
                ServiceRequestController.updateServiceRequest(serviceRequest);
                resetUI();
                tabbedPane1.setSelectedIndex(0);
            });
        } else if (serviceRequest.getStatus() == ServiceRequest.Status.AwaitingApproval) {
            saveEstimateBtn.setText("Aprovar orçamento");
            createEstimateBtn.setText("Aprovar orçamento");
            createEstimateBtn.addActionListener(e -> {
                String formattedValue = currencyFormat.format(serviceRequest.getEstimate().getFinalValue());
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Tem certeza que deseja aprovar o orçamento no valor de " + formattedValue + "?",
                        "Aprovar orçamento",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    serviceRequest.setStatus(ServiceRequest.Status.Approved);
                    ServiceRequestController.updateServiceRequest(serviceRequest);
                }
                resetUI();
            });
        } else if (serviceRequest.getStatus() == ServiceRequest.Status.Approved) {
            saveEstimateBtn.setText("Executar serviço");
            createEstimateBtn.setText("Executar serviço");
            createEstimateBtn.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Tem certeza que deseja iniciar a execução do serviço?",
                        "Confirmar execução",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                serviceRequest.setStatus(ServiceRequest.Status.InProgress);
                ServiceRequestController.updateServiceRequest(serviceRequest);
                resetUI();
            });
        } else if (serviceRequest.getStatus() == ServiceRequest.Status.InProgress) {
            saveEstimateBtn.setText("Concluir serviço");
            createEstimateBtn.setText("Concluir serviço");
            createEstimateBtn.addActionListener(e -> {
                serviceRequest.setStatus(ServiceRequest.Status.Finished);
                serviceRequest.setServiceConcludedTime(LocalDateTime.now());
                ServiceRequestController.updateServiceRequest(serviceRequest);
                resetUI();
            });
        } else if (serviceRequest.getStatus() == ServiceRequest.Status.Finished) {
            saveEstimateBtn.setText("Emitir fatura");
            createEstimateBtn.setText("Emitir fatura");
            createEstimateBtn.addActionListener(e -> {
                StringBuilder receipt = new StringBuilder();
                receipt.append("Fatura\n")
                        .append("\n")
                        .append("Cliente:\n")
                        .append(serviceRequest.getRequester().getName()).append("\n")
                        .append(serviceRequest.getRequester().getId()).append("\n")
                        .append(serviceRequest.getRequester().getCPF()).append("\n")
                        .append(serviceRequest.getRequester().getAddress()).append("\n")
                        .append(serviceRequest.getRequester().getPhone()).append("\n")
                        .append(serviceRequest.getRequester().getEmail()).append("\n")
                        .append("\n")
                        .append("Serviço executado por:\n")
                        .append(serviceRequest.getTechnician().getName()).append("\n")
                        .append(serviceRequest.getTechnician().getPhone()).append("\n")
                        .append(serviceRequest.getTechnician().getEmail()).append("\n")
                        .append("\n")
                        .append("Serviço executado em " + serviceRequest.getServiceConcludedTime().format(AppController.BRAZILIAN_LOCAL_DATETIME)).append("\n")
                        .append("\n")
                        .append("Orçamento aprovado:\n")
                        .append("Horas: ")
                            .append(serviceRequest.getEstimate().getServiceHours())
                            .append(" x ")
                            .append(currencyFormat.format(serviceRequest.getEstimate().getHourlyRate())).append("/hora\n")
                        .append("\n")
                        .append("Materiais:\n");
                for (ServiceEstimate.MaterialEntry material : serviceRequest.getEstimate().getMaterials()) {
                    receipt.append(material.get(1))
                        .append("\t")
                        .append(material.get(0))
                        .append(" x ")
                        .append(currencyFormat.format(material.get(2)))
                        .append("\n");
                }
                receipt.append("\n")
                    .append("Valor: ").append(currencyFormat.format(serviceRequest.getEstimate().getValue())).append("\n")
                    .append("Imposto Sobre Serviços: ").append(serviceRequest.getEstimate().getServiceTax()*100).append("%\n")
                    .append("\n")
                    .append("Total: ").append(currencyFormat.format(serviceRequest.getEstimate().getFinalValue()));
                JOptionPane.showMessageDialog(this, receipt.toString(), "Fatura de serviço", JOptionPane.INFORMATION_MESSAGE);
                serviceRequest.setStatus(ServiceRequest.Status.AwaitingPayment);
                ServiceRequestController.updateServiceRequest(serviceRequest);
                resetUI();
            });
        } else if (serviceRequest.getStatus() == ServiceRequest.Status.AwaitingPayment) {
            saveEstimateBtn.setText("Confirmar pagamento");
            createEstimateBtn.setText("Confirmar pagamento");
            createEstimateBtn.addActionListener(e -> {
                Payment payment = PaymentDialog.create(serviceRequest);
                if (payment != null) {
                    ServiceRequestController.saveNewPayment(payment);
                    serviceRequest.setPayment(payment);
                    serviceRequest.setStatus(ServiceRequest.Status.Closed);
                    ServiceRequestController.updateServiceRequest(serviceRequest);
                    resetUI();
                }
            });
        }
    }

    private void onOK() {
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public static void create(ServiceRequest serviceRequest) {
        ServiceRequestDialog dialog = new ServiceRequestDialog(serviceRequest);
        dialog.pack();
        dialog.setLocationRelativeTo(AppController.getAppFrame());
        dialog.setVisible(true);
    }

    private ServiceRequest serviceRequest;
    private ServiceEstimate tempEstimate;

    private final MaterialsTableModel materialsTableModel;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel clientLabel;
    private CheckBoxList<TaskTypeCheckBox> taskTypesList;
    private JTextArea descriptionTextArea;
    private JLabel statusLabel;
    private JComboBox<Technician> technicianComboBox;
    private JTabbedPane tabbedPane1;
    private JTextField serviceHoursTextField;
    private JTable materialsTable;
    private JFormattedTextField hourlyRateTextField;
    private JFormattedTextField valueTextField;
    private JButton computeValueBtn;
    private JButton saveEstimateBtn;
    private JButton createEstimateBtn;
    private JLabel technicianLabel;
    private JLabel taxValueLabel;
    private JLabel totalValueLabel;
}
