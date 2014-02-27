/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nbb.demetra.dfm;

import ec.nbdemetra.ui.properties.OpenIdePropertySheetBeanEditor;
import ec.nbdemetra.ws.WorkspaceFactory;
import ec.nbdemetra.ws.WorkspaceItem;
import ec.nbdemetra.ws.ui.WorkspaceTopComponent;
import ec.tss.Dfm.DfmDocument;
import ec.tstoolkit.Parameter;
import ec.tstoolkit.dfm.MeasurementSpec;
import ec.tstoolkit.var.VarSpec;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//be.nbb.demetra.dfm//DfmModelSpecView//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "DfmModelSpecViewTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@Messages({
    "CTL_DfmModelSpecViewAction=DfmModelSpecView",
    "CTL_DfmModelSpecViewTopComponent=DfmModelSpecView Window",
    "HINT_DfmModelSpecViewTopComponent=This is a DfmModelSpecView window"
})
public final class DfmModelSpecViewTopComponent extends WorkspaceTopComponent<DfmDocument> implements MultiViewElement, MultiViewDescription {

 
    public DfmModelSpecViewTopComponent(WorkspaceItem<DfmDocument> document) {
        super(document);
        initComponents();
        setName(Bundle.CTL_DfmModelSpecViewTopComponent());
        setToolTipText(Bundle.HINT_DfmModelSpecViewTopComponent());
        if (document != null)
        dfmModelSpecView1.setModel(document.getElement());
    }

    public DfmModelSpecViewTopComponent() {
        this(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dfmModelSpecView1 = new be.nbb.demetra.dfm.DfmModelSpecView();

        setLayout(new java.awt.BorderLayout());
        add(dfmModelSpecView1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private be.nbb.demetra.dfm.DfmModelSpecView dfmModelSpecView1;
    // End of variables declaration//GEN-END:variables

    void writeProperties(java.util.Properties p) {
    }

    void readProperties(java.util.Properties p) {
    }

    //<editor-fold defaultstate="collapsed" desc="MultiViewElement">
    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
    }

    @Override
    public JComponent getVisualRepresentation() {
        return this;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        JToolBar toolBar = new JToolBar();
        toolBar.addSeparator();
        JButton editVarSpecButton = toolBar.add(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editVarSpec();
            }
        });
        VarSpec varSpec = getDocument().getElement().getSpecification().getModelSpec().getVarSpec();
        editVarSpecButton.setText("nvars=" + varSpec.getEquationsCount() + " nlags=" + varSpec.getLagsCount());
        return toolBar;
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback callback) {
    }

    @Override
    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }

    @Override
    public void componentActivated() {
        super.componentActivated();
    }

    @Override
    public void componentDeactivated() {
        super.componentDeactivated();
    }

    @Override
    public void componentHidden() {
        super.componentHidden();
    }

    @Override
    public void componentShowing() {
        super.componentShowing();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="MultiViewDescription">
    @Override
    public MultiViewElement createElement() {
        return this;
    }

    @Override
    public String preferredID() {
        return super.preferredID();
    }
    //</editor-fold>

    @Override
    protected String getContextPath() {
        return DfmDocumentManager.CONTEXTPATH;
    }

    private void editVarSpec() {
        VarSpec oldValue = getDocument().getElement().getSpecification().getModelSpec().getVarSpec();
        VarSpec newValue = oldValue.clone();
        if (OpenIdePropertySheetBeanEditor.editSheet(DfmSheets.onVarSpec(newValue), "Edit var spec", null)) {
            getDocument().getElement().getSpecification().getModelSpec().setVarSpec(newValue);
            int diff = newValue.getEquationsCount() - oldValue.getEquationsCount();
            if (diff != 0) {
                for (MeasurementSpec o : getDocument().getElement().getSpecification().getModelSpec().getMeasurements()) {
                    Parameter[] tmp = Arrays.copyOf(o.getCoefficients(), newValue.getEquationsCount());
                    if (diff > 0) {
                        for (int i = 0; i < diff; i++) {
                            tmp[oldValue.getEquationsCount() + i] = new Parameter();
                        }
                    }
                    o.setCoefficient(tmp);
                    dfmModelSpecView1.setModel(null);
                    if (getDocument() != null)
                    dfmModelSpecView1.setModel(getDocument().getElement());
                }
            }
        }
    }
}
