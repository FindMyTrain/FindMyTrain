package iitb.mtp.aurobindo.findmytrain;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;

public class Classify {

    /****** svmPredict takes in the feature vector and returns the results vector*******/
    public double[] svmPredict(double[][] xtest) {

        double[] yPred = new double[xtest.length];

        /**** Get svm model from static model saved before ******/
        svm_model model = SvmModel.model;

        for(int k = 0; k < xtest.length; k++){
            double[] fVector = xtest[k];
            svm_node[] nodes = new svm_node[fVector.length];
            for (int i = 0; i < fVector.length; i++)    {
                svm_node node = new svm_node();
                node.index = i;
                node.value = fVector[i];
                nodes[i] = node;
            }

            int totalClasses = 2;
            int[] labels = new int[totalClasses];

            svm.svm_get_labels(model, labels);

            double[] prob_estimates = new double[totalClasses];
            yPred[k] = svm.svm_predict_probability(model, nodes, prob_estimates);
        }
        /**** returns the results ****/
        return yPred;
    }


}


