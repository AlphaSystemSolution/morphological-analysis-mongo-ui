package com.alphasystem.morphologicalanalysis.treebank.ui.panels;

import com.alphasystem.morphologicalanalysis.treebank.ui.workers.UpdateCanvasWorker;
import com.alphasystem.ui.AbstractComponentAction;
import com.jidesoft.dialog.ButtonPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.util.ResourceBundle;

import static com.alphasystem.ui.BaseDialog.BUTTON_TYPE_KEY;
import static com.alphasystem.ui.util.SpringUtilities.makeCompactGrid;
import static com.jidesoft.dialog.ButtonPanel.AFFIRMATIVE_BUTTON;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.PAGE_END;
import static java.awt.Font.PLAIN;
import static java.awt.event.KeyEvent.*;
import static java.lang.String.format;
import static javax.swing.BorderFactory.*;

/**
 * @author sali
 */
public class PropertiesPanel extends JPanel {

    private static final long serialVersionUID = -7791692247630178509L;

    private class PropertiesPanelAction extends AbstractComponentAction{

        private static final long serialVersionUID = 6324933265915979383L;

        public PropertiesPanelAction(){
            super("Update", "updateCanvas");
            setKeyStroke(KeyStroke.getKeyStroke(VK_U, InputEvent.CTRL_DOWN_MASK + InputEvent.ALT_DOWN_MASK));
            setMnemonic(VK_U);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            width = (Integer) totalWidthSpinner.getValue();
            height = (Integer) totalHeightSpinner.getValue();
            contentPanel.getTreeBankData().getMetaData().withWidth(width)
                    .withHeight(height).withShowGridLines(showGridLines.isSelected())
                    .withShowOutline(showOutlines.isSelected()).withDebugMode(debugMode.isSelected());
            contentPanel.updateCursor(true);
            UpdateCanvasWorker worker = new UpdateCanvasWorker(contentPanel);
            worker.execute();
        }
    }

    private static final Font DEFAULT_FONT = new Font("Georgia", PLAIN, 12);

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
            .getBundle(format("%s", PropertiesPanel.class.getName()));

    private final MATBContentPanel contentPanel;
    private int width;
    private int height;
    private JSpinner totalWidthSpinner;
    private JSpinner totalHeightSpinner;
    private JCheckBox showGridLines;
    private JCheckBox showOutlines;
    private JCheckBox debugMode;

    public PropertiesPanel(final MATBContentPanel contentPanel, int width, int height){
        super();

        this.contentPanel = contentPanel;
        this.width = width;
        this.height = height;

        setBorder(createCompoundBorder(createEmptyBorder(0, 6, 0, 6), createTitledBorder("Properties")));
        JPanel mainPanel = new JPanel(new SpringLayout());

        int rows = 0;
        int cols = 1;

        mainPanel.add(createGlobalProperties());
        rows++;

        makeCompactGrid(mainPanel, rows, cols);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(mainPanel, CENTER);
        panel.add(createButtonPanel(), PAGE_END);

        add(panel);
    }

    private JSpinner createSpinnerForWidthAndHeight(int initialValue){
        SpinnerNumberModel model = new SpinnerNumberModel(initialValue, 0, 1000, 20);
        JSpinner jSpinner = new JSpinner(model);
        jSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner source = (JSpinner) e.getSource();
                System.out.println(source.getValue());
            }
        });
        return jSpinner;
    }

    private JLabel createJLabel(String labelKey, int key, int index){
        JLabel label = new JLabel(RESOURCE_BUNDLE.getString(labelKey));
        label.setFont(DEFAULT_FONT);
        label.setDisplayedMnemonic(key);
        label.setDisplayedMnemonicIndex(index);
        return label;
    }

    private ButtonPanel createButtonPanel(){
        ButtonPanel buttonPanel = new ButtonPanel();

        JButton updateButton = new JButton(new PropertiesPanelAction());
        updateButton.setFont(DEFAULT_FONT);
        updateButton.putClientProperty(BUTTON_TYPE_KEY, AFFIRMATIVE_BUTTON);

        buttonPanel.add(updateButton, AFFIRMATIVE_BUTTON);
        return buttonPanel;
    }

    private JPanel createGlobalProperties(){
        JPanel panel = new JPanel(new SpringLayout());

        int rows = 0;
        int cols = 2;

        JLabel label = createJLabel("totalWidth.label", VK_W, 0);
        totalWidthSpinner = createSpinnerForWidthAndHeight(width);
        totalWidthSpinner.setFont(DEFAULT_FONT);
        label.setLabelFor(totalWidthSpinner);
        panel.add(label);
        panel.add(totalWidthSpinner);
        rows++;

        label = createJLabel("totalHeight.label", VK_H, 0);
        totalHeightSpinner = createSpinnerForWidthAndHeight(height);
        totalHeightSpinner.setFont(DEFAULT_FONT);
        label.setLabelFor(totalHeightSpinner);
        panel.add(label);
        panel.add(totalHeightSpinner);
        rows++;

        label = createJLabel("drawGridLines.label", VK_G, 0);
        showGridLines = new JCheckBox();
        showGridLines.addActionListener(new AbstractComponentAction() {

            private static final long serialVersionUID = -8647161298760890079L;

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean selected = showGridLines.isSelected();
                showOutlines.setSelected(selected);
                showOutlines.setEnabled(!selected);
            }
        });
        label.setLabelFor(showGridLines);
        panel.add(label);
        panel.add(showGridLines);
        rows++;

        label = createJLabel("useOutlineOnly.label", VK_O, 0);
        showOutlines = new JCheckBox();
        label.setLabelFor(showOutlines);
        panel.add(label);
        panel.add(showOutlines);
        rows++;

        label = createJLabel("debugMode.label", VK_D, 0);
        debugMode = new JCheckBox();
        label.setLabelFor(debugMode);
        panel.add(label);
        panel.add(debugMode);
        rows++;

        makeCompactGrid(panel, rows, cols);
        return panel;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
