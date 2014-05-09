/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nbb.demetra.dfm;

import ec.nbdemetra.ui.DemetraUiIcon;
import ec.nbdemetra.ui.NbComponents;
import ec.nbdemetra.ui.properties.OpenIdePropertySheetBeanEditor;
import ec.nbdemetra.ws.WorkspaceItem;
import ec.nbdemetra.ws.ui.WorkspaceTopComponent;
import ec.tss.dfm.DfmDocument;
import ec.tss.dfm.VersionedDfmDocument;
import ec.tstoolkit.Parameter;
import ec.tstoolkit.dfm.DfmModelSpec;
import ec.tstoolkit.dfm.DfmSpec;
import ec.tstoolkit.dfm.MeasurementSpec;
import ec.tstoolkit.var.VarSpec;
import ec.util.various.swing.JCommand;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import static org.openide.util.ImageUtilities.createDisabledIcon;
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
public final class DfmModelSpecViewTopComponent extends WorkspaceTopComponent<VersionedDfmDocument> implements MultiViewElement, MultiViewDescription {

    private final DfmController controller;

    public DfmModelSpecViewTopComponent() {
        this(null, new DfmController());
    }

    DfmModelSpecViewTopComponent(WorkspaceItem<VersionedDfmDocument> document, DfmController controller) {
        super(document);
        initComponents();
        setName(Bundle.CTL_DfmModelSpecViewTopComponent());
        setToolTipText(Bundle.HINT_DfmModelSpecViewTopComponent());
        if (document != null) {
            dfmModelSpecView1.setModel(document.getElement().getCurrent());
        }
        
        this.controller = controller;
        controller.addPropertyChangeListener(DfmController.DFM_STATE_PROPERTY, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                // forward event
                firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            }
        });
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
        super.componentOpened();
    }

    @Override
    public void componentClosed() {
        super.componentClosed();
    }

    @Override
    public JComponent getVisualRepresentation() {
        return this;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        JToolBar toolBar = NbComponents.newInnerToolbar();
        toolBar.addSeparator();
        toolBar.add(Box.createRigidArea(new Dimension(5, 0)));

        JButton edit = toolBar.add(EditModelSpecCommand.INSTANCE.toAction(this));
        edit.setIcon(DemetraUiIcon.PREFERENCES);
        edit.setDisabledIcon(createDisabledIcon(edit.getIcon()));
        edit.setToolTipText("Varspec");

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

    //<editor-fold defaultstate="collapsed" desc="Commands">
    private static final class EditModelSpecCommand extends JCommand<DfmModelSpecViewTopComponent> {
        
        public static final EditModelSpecCommand INSTANCE = new EditModelSpecCommand();
        
        @Override
        public void execute(DfmModelSpecViewTopComponent c) throws Exception {
            DfmDocument doc = c.getDocument().getElement().getCurrent();
            DfmSpec oldspec = doc.getSpecification();
            DfmSpec newspec=oldspec.cloneDefinition();
            DfmModelSpec oldValue =oldspec.getModelSpec();
            DfmModelSpec newValue = newspec.getModelSpec();
            if (OpenIdePropertySheetBeanEditor.editSheet(DfmSheets.onModelSpec(newValue), "Edit var spec", null)) {
                int diff = newValue.getVarSpec().getEquationsCount() - oldValue.getVarSpec().getEquationsCount();
                if (diff != 0) {
                    for (MeasurementSpec o : newspec.getModelSpec().getMeasurements()) {
                        Parameter[] tmp = Arrays.copyOf(o.getCoefficients(), newValue.getVarSpec().getEquationsCount());
                        if (diff > 0) {
                            for (int i = 0; i < diff; i++) {
                                tmp[oldValue.getVarSpec().getEquationsCount() + i] = new Parameter();
                            }
                        }
                        o.setCoefficients(tmp);
                   }
                }
                doc.setSpecification(newspec);  
                c.dfmModelSpecView1.updateModel();
                c.controller.setDfmState(DfmController.DfmState.READY);
             }
        }
        
        @Override
        public boolean isEnabled(DfmModelSpecViewTopComponent c) {
            return c.controller.getDfmState() != DfmController.DfmState.STARTED;
        }
        
        @Override
        public ActionAdapter toAction(DfmModelSpecViewTopComponent c) {
            return super.toAction(c).withWeakPropertyChangeListener(c, DfmController.DFM_STATE_PROPERTY);
        }
    }
    //</editor-fold>
}
