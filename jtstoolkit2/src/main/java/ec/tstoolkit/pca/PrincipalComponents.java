/*
 * Copyright 2013 National Bank of Belgium
 * 
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved 
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
package ec.tstoolkit.pca;

import ec.tstoolkit.data.DataBlock;
import ec.tstoolkit.maths.matrices.Matrix;
import ec.tstoolkit.maths.matrices.SingularValueDecomposition;

/**
 *
 * @author Jean Palate
 */
public class PrincipalComponents {

    private Matrix data_;
    private SingularValueDecomposition svd_;
    private double scaling_;

    public boolean process(Matrix data) {
        clear();
        data_ = data;
        svd_=new SingularValueDecomposition();
       
        Matrix ndata;
        if (data.getColumnsCount() == 1){
            ndata=data;
            scaling_=1;
        }
        else{
            scaling_=1/Math.sqrt(data.getColumnsCount()-1);
            ndata=data.times(scaling_);
        }
        svd_.decompose(ndata);
        return svd_.isFullRank();
    }

    private void clear() {
        svd_=null;
        data_=null;
        scaling_=1;
    }
    
    public double getScaling(){
        return scaling_;
    }

    public Matrix getData() {
        return data_;
    }
    
    public SingularValueDecomposition getSvd(){
        return svd_;
    }
    
    public double[] getSingularValues(){
        return svd_.getSingularValues();
    }
    
    public Matrix getEigenVectors(){
        return svd_.V();
    }
    
    public DataBlock getEigenVector(int pos){
        return svd_.V().column(pos);
    }
    
    public DataBlock getFactor(int pos){
        DataBlock u=svd_.U().column(pos).deepClone();
        u.mul(svd_.getSingularValues()[pos]);
        return u;
    }
}
