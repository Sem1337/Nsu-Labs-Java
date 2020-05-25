package main.factory;

import main.factory.details.Accessory;
import main.factory.details.Body;
import main.factory.details.Engine;

import javax.swing.*;
import java.awt.*;

class FactoryGUI extends JFrame {

    FactoryGUI(Factory factory) {
        this.factory = factory;

        setSize(900, 900);
        setMinimumSize(new Dimension(450, 450));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Factory");
        this.setPreferredSize(new Dimension(900, 900));


        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        add(panel);

        configureFields();
        configureSliders();

        setResizable(false);
        setVisible(true);
    }

    private void configureSliders() {
        JSlider supplierDelaySlider = new JSlider(100,5100,factory.getSupplierDelay());
        JSlider dealerDelaySlider = new JSlider(100,5100,factory.getDealerDelay());

        supplierDelaySlider.setPaintLabels(true);
        supplierDelaySlider.setMajorTickSpacing(1000);
        dealerDelaySlider.setPaintLabels(true);
        dealerDelaySlider.setMajorTickSpacing(1000);
        supplierDelaySlider.addChangeListener(changeEvent -> factory.setSupplierDelay(((JSlider)changeEvent.getSource()).getValue()));
        dealerDelaySlider.addChangeListener(changeEvent -> factory.setDealerDelay(((JSlider)changeEvent.getSource()).getValue()));

        supplierDelaySliderPanel.setLayout(new FlowLayout());
        dealerDelaySliderPanel.setLayout(new FlowLayout());

        JLabel dealerSliderLabel = new JLabel("dealer delay");
        JLabel supplierSliderLabel = new JLabel("supplier delay");
        supplierDelaySliderPanel.add(supplierSliderLabel);
        supplierDelaySliderPanel.add(supplierDelaySlider);
        dealerDelaySliderPanel.add(dealerSliderLabel);
        dealerDelaySliderPanel.add(dealerDelaySlider);

        panel.add(supplierDelaySliderPanel);
        panel.add(dealerDelaySliderPanel);

    }


    private void configureFields() {
        carStorageField = new JTextArea(11,55);
        accessoryStorageField = new JTextArea(11,55);
        engineStorageField = new JTextArea(11,55);
        bodyStorageField = new JTextArea(11,55);
        carStorageField.setEditable(false);
        accessoryStorageField.setEditable(false);
        engineStorageField.setEditable(false);
        bodyStorageField.setEditable(false);
        JScrollPane carFieldScrollPane = new JScrollPane(carStorageField);
        JScrollPane accessoryFieldScrollPane = new JScrollPane(accessoryStorageField);
        JScrollPane engineFieldScrollPane = new JScrollPane(engineStorageField);
        JScrollPane bodyFieldScrollPane = new JScrollPane(bodyStorageField);


        panel.add(carFieldScrollPane);
        panel.add(accessoryFieldScrollPane);
        panel.add(engineFieldScrollPane);
        panel.add(bodyFieldScrollPane);
    }

    void updateData() {
        carStorageField.setText("");
        accessoryStorageField.setText("");
        engineStorageField.setText("");
        bodyStorageField.setText("");

        carStorageField.append("total cars produced: " + factory.getTotalCarsProduced().toString() + "\n");
        for (Car car: factory.getCarStorage().getStorage()) {
            carStorageField.append(car.getDescription() + "\n");
        }

        for(Body body: factory.getBodyStorage().getStorage()){
            bodyStorageField.append(body.getDescription() + "\n");
        }

        for(Engine engine: factory.getEngineStorage().getStorage()) {
            engineStorageField.append(engine.getDescription() + "\n");
        }

        for(Accessory accessory: factory.getAccessoryStorage().getStorage()) {
            accessoryStorageField.append(accessory.getDescription() + "\n");
        }

    }



    private JPanel supplierDelaySliderPanel = new JPanel();
    private JPanel dealerDelaySliderPanel = new JPanel();
    private JPanel panel;
    private JTextArea carStorageField;
    private JTextArea accessoryStorageField;
    private JTextArea engineStorageField;
    private JTextArea bodyStorageField;
    private Factory factory;
}
