/*
 * Copyright 2013 National Bank of Belgium
 * 
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved 
 * by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and 
 * limitations under the Licence.
 */
package be.nbb.demetra.dfm.output;

import com.google.common.base.Optional;
import ec.nbdemetra.ui.DemetraUI;
import ec.tss.Ts;
import ec.tss.TsCollection;
import ec.tss.TsFactory;
import ec.tss.datatransfer.TsDragRenderer;
import ec.tss.datatransfer.TssTransferSupport;
import ec.tss.dfm.DfmResults;
import ec.tss.dfm.DfmSeriesDescriptor;
import ec.tss.tsproviders.utils.Formatters;
import ec.ui.chart.TsXYDatasets;
import ec.util.chart.ColorScheme;
import ec.util.chart.ObsFunction;
import ec.util.chart.SeriesFunction;
import ec.util.chart.TimeSeriesChart;
import ec.util.chart.swing.ColorSchemeIcon;
import static ec.util.chart.swing.JTimeSeriesChartCommand.applyColorSchemeSupport;
import static ec.util.chart.swing.JTimeSeriesChartCommand.applyLineThickness;
import static ec.util.chart.swing.JTimeSeriesChartCommand.copyImage;
import static ec.util.chart.swing.JTimeSeriesChartCommand.printImage;
import static ec.util.chart.swing.JTimeSeriesChartCommand.saveImage;
import ec.util.chart.swing.SwingColorSchemeSupport;
import ec.util.various.swing.JCommand;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Transferable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import static javax.swing.TransferHandler.COPY;
import javax.swing.TransferHandler.TransferSupport;

/**
 *
 * @author Mats Maggi
 */
final class FitSignalDataView extends javax.swing.JPanel {

    public static final String DFM_RESULTS_PROPERTY = "dfmResults";

    private Optional<DfmResults> dfmResults;
    private TsCollection collection;
    
    private final DemetraUI demetraUI;
    private Formatters.Formatter<Number> formatter;
    private CustomSwingColorSchemeSupport defaultColorSchemeSupport;

    /**
     * Creates new form ShocksDecompositionView
     */
    public FitSignalDataView() {
        initComponents();

        this.dfmResults = Optional.absent();
        
        demetraUI = DemetraUI.getInstance();
        formatter = demetraUI.getDataFormat().numberFormatter();
        defaultColorSchemeSupport = new CustomSwingColorSchemeSupport() {
            @Override
            public ColorScheme getColorScheme() {
                return demetraUI.getColorScheme();
            }
        };

        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateChart();
            }
        });

        chart.setValueFormat(new DecimalFormat("#.###"));
        chart.setSeriesRenderer(new SeriesFunction<TimeSeriesChart.RendererType>() {
            @Override
            public TimeSeriesChart.RendererType apply(int series) {
                switch (getType(series)) {
                    case ACTUAL:
                    case SIGNAL:
                        return TimeSeriesChart.RendererType.LINE;
                    default:
                        return TimeSeriesChart.RendererType.STACKED_COLUMN;
                }
            }
        });
        chart.setSeriesFormatter(new SeriesFunction<String>() {
            @Override
            public String apply(int series) {
                return chart.getDataset().getSeriesKey(series).toString();
            }
        });
        chart.setObsFormatter(new ObsFunction<String>() {
            @Override
            public String apply(int series, int obs) {
                return chart.getSeriesFormatter().apply(series)
                        + " - " + chart.getPeriodFormat().format(chart.getDataset().getX(series, obs))
                        + "\n" + formatter.format(chart.getDataset().getY(series, obs));
            }
        });
        chart.setPopupMenu(createChartMenu().getPopupMenu());
        chart.setColorSchemeSupport(defaultColorSchemeSupport);
        chart.setNoDataMessage("No data produced");
        chart.setMouseWheelEnabled(true);
        
        chart.setTransferHandler(new TsCollectionTransferHandler());

        addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                switch (evt.getPropertyName()) {
                    case DFM_RESULTS_PROPERTY:
                        updateComboBox();
                        updateChart();
                        break;
                }
            }
        });

        updateComboBox();
        updateChart();
        
        demetraUI.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                switch (evt.getPropertyName()) {
                    case DemetraUI.DATA_FORMAT_PROPERTY:
                        onDataFormatChanged();
                        break;
                    case DemetraUI.COLOR_SCHEME_NAME_PROPERTY:
                        onColorSchemeChanged();
                        break;
                }
            }
        });
    }
    
    private void onDataFormatChanged() {
        formatter = demetraUI.getDataFormat().numberFormatter();
    }

    private void onColorSchemeChanged() {
        defaultColorSchemeSupport = new CustomSwingColorSchemeSupport() {
            @Override
            public ColorScheme getColorScheme() {
                return demetraUI.getColorScheme();
            }
        };
        chart.setColorSchemeSupport(defaultColorSchemeSupport);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        comboBox = new javax.swing.JComboBox();
        chart = new ec.util.chart.swing.JTimeSeriesChart();

        setLayout(new java.awt.BorderLayout());

        add(comboBox, java.awt.BorderLayout.PAGE_START);
        add(chart, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ec.util.chart.swing.JTimeSeriesChart chart;
    private javax.swing.JComboBox comboBox;
    // End of variables declaration//GEN-END:variables

    //<editor-fold defaultstate="collapsed" desc="Getters/Setters">
    public Optional<DfmResults> getDfmResults() {
        return dfmResults;
    }

    public void setDfmResults(Optional<DfmResults> dfmResults) {
        Optional<DfmResults> old = this.dfmResults;
        this.dfmResults = dfmResults != null ? dfmResults : Optional.<DfmResults>absent();
        firePropertyChange(DFM_RESULTS_PROPERTY, old, this.dfmResults);
    }
    //</editor-fold>

    private void updateComboBox() {
        if (dfmResults.isPresent()) {
            comboBox.setModel(toComboBoxModel(dfmResults.get().getDescriptions()));
            comboBox.setEnabled(true);
        } else {
            comboBox.setModel(new DefaultComboBoxModel());
            comboBox.setEnabled(false);
        }
    }

    private void updateChart() {
        if (dfmResults.isPresent() && comboBox.getSelectedIndex() != -1) {
            TsXYDatasets.Builder b = TsXYDatasets.builder();
            collection = toCollection(dfmResults.get());
            for (Ts o : collection) {
                b.add(o.getName(), o.getTsData());
            }
            chart.setDataset(b.build());
        } else {
            chart.setDataset(null);
        }
    }

    private enum SeriesType {

        ACTUAL, SIGNAL
    }

    private SeriesType getType(int series) {
        String key = (String) chart.getDataset().getSeriesKey(series);
        switch (key) {
            case "Actual (+mean)":
                return SeriesType.ACTUAL;
            case "Signal (+mean)":
                return SeriesType.SIGNAL;
            default:
                throw new RuntimeException();
        }
    }

    private TsCollection toCollection(DfmResults dfmResults) {
        Objects.requireNonNull(dfmResults);

        TsCollection result = TsFactory.instance.createTsCollection();

        int selectedIndex = comboBox.getSelectedIndex();

        double selectedMean = dfmResults.getDescriptions()[selectedIndex].mean;
        result.quietAdd(TsFactory.instance.createTs("Actual (+mean)", null, dfmResults.getTheData()[selectedIndex].plus(selectedMean)));
        result.quietAdd(TsFactory.instance.createTs("Signal (+mean)", null, dfmResults.getSignalProjections()[selectedIndex]));

        return result;
    }

    private abstract class CustomSwingColorSchemeSupport extends SwingColorSchemeSupport {

        @Override
        public Color getLineColor(int series) {
            switch (getType(series)) {
                case ACTUAL:
                    return getLineColor(ColorScheme.KnownColor.RED);
                case SIGNAL:
                    return getLineColor(ColorScheme.KnownColor.BLUE);
                default:
                    throw new RuntimeException();
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Menus">
    private JMenu newColorSchemeMenu() {
        JMenu item = new JMenu("Color scheme");
        item.add(new JCheckBoxMenuItem(applyColorSchemeSupport(defaultColorSchemeSupport).toAction(chart))).setText("Default");
        item.addSeparator();
        for (final ColorScheme o : DemetraUI.getInstance().getColorSchemes()) {
            final CustomSwingColorSchemeSupport colorSchemeSupport = new CustomSwingColorSchemeSupport() {
                @Override
                public ColorScheme getColorScheme() {
                    return o;
                }
            };
            JMenuItem subItem = item.add(new JCheckBoxMenuItem(applyColorSchemeSupport(colorSchemeSupport).toAction(chart)));
            subItem.setText(o.getDisplayName());
            subItem.setIcon(new ColorSchemeIcon(o));
        }
        return item;
    }

    private JMenu newLineThicknessMenu() {
        JMenu item = new JMenu("Line thickness");
        item.add(new JCheckBoxMenuItem(applyLineThickness(1f).toAction(chart))).setText("Thin");
        item.add(new JCheckBoxMenuItem(applyLineThickness(2f).toAction(chart))).setText("Thick");
        return item;
    }

    private JMenu createChartMenu() {
        JMenu menu = new JMenu();
        JMenuItem item;

        item = menu.add(CopyCommand.INSTANCE.toAction(this));
        item.setText("Copy");

        menu.addSeparator();
        menu.add(newColorSchemeMenu());
        menu.add(newLineThicknessMenu());
        menu.addSeparator();
        menu.add(newExportMenu());

        return menu;
    }

    private JMenu newExportMenu() {
        JMenu menu = new JMenu("Export image to");
        menu.add(printImage().toAction(chart)).setText("Printer...");
        menu.add(copyImage().toAction(chart)).setText("Clipboard");
        menu.add(saveImage().toAction(chart)).setText("File...");
        return menu;
    }
        //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Commands">
    private static final class CopyCommand extends JCommand<FitSignalDataView> {

        public static final CopyCommand INSTANCE = new CopyCommand();

        @Override
        public void execute(FitSignalDataView c) throws Exception {
            Optional<DfmResults> dfmResults = c.getDfmResults();
            if (dfmResults.isPresent()) {
                Transferable t = TssTransferSupport.getInstance().fromTsCollection(c.toCollection(dfmResults.get()));
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(t, null);
            }
        }

        @Override
        public boolean isEnabled(FitSignalDataView c) {
            return c.getDfmResults().isPresent();
        }

        @Override
        public JCommand.ActionAdapter toAction(FitSignalDataView c) {
            return super.toAction(c).withWeakPropertyChangeListener(c, DFM_RESULTS_PROPERTY);
        }
    }
    //</editor-fold>

    private static DefaultComboBoxModel toComboBoxModel(DfmSeriesDescriptor[] desc) {
        DefaultComboBoxModel result = new DefaultComboBoxModel(desc);
        return result;
    }
    
    private TsCollection dragSelection = null;

    protected Transferable transferableOnSelection() {
        TsCollection col = TsFactory.instance.createTsCollection();

        ListSelectionModel model = chart.getSeriesSelectionModel();
        if (!model.isSelectionEmpty()) {
            for (int i = model.getMinSelectionIndex(); i <= model.getMaxSelectionIndex(); i++) {
                if (model.isSelectedIndex(i)) {
                    col.quietAdd(collection.get(i));
                }
            }
        }
        dragSelection = col;
        return TssTransferSupport.getInstance().fromTsCollection(dragSelection);
    }
    
    public class TsCollectionTransferHandler extends TransferHandler {

        @Override
        public int getSourceActions(JComponent c) {
            transferableOnSelection();
            TsDragRenderer r = dragSelection.getCount() < 10 ? TsDragRenderer.asChart() : TsDragRenderer.asCount();
            Image image = r.getTsDragRendererImage(Arrays.asList(dragSelection.toArray()));
            setDragImage(image);
            return COPY;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            return transferableOnSelection();
        }

        @Override
        public boolean canImport(TransferSupport support) {
            return false;
        }
    }
}
