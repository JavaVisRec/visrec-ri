/**
 * Visual Recognition API for Java, JSR381
 * Copyright (C) 2020  Zoran Sevarac, Frank Greco
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package javax.visrec.ri.util;

import deepnetts.data.MLDataItem;
import deepnetts.data.TabularDataSet;
import deepnetts.util.DeepNettsException;

import java.io.*;

public class DataSets {

    private DataSets() {
        // Prevent instantiation
    }

    /**
     * Creates and returns data set from specified CSV file. Empty lines are
     * skipped
     *
     * @param csvFile CSV file
     * @param numInputs number of input values in a row
     * @param numOutputs number of output values in a row
     * @param hasColumnNames true if first row contains column names
     * @param delimiter delimiter used to separate values
     * @return instance of data set with values loaded from file
     *
     * @throws FileNotFoundException if file was not found
     * @throws IOException if there was an error reading file
     *
     * TODO: Detect if there are labels in the first line, if there are no
     * labels, set class1, class2, class3 in classifier evaluation! and detect
     * type of attributes Move this method to some factory class or something?
     * or as a default method in data set?
     *
     *  TODO: should I wrap IO with DeepNetts Exception?
     * Autodetetect delimiter; header and column type
     *
     */
    public static TabularDataSet<MLDataItem> readCsv(File csvFile, int numInputs, int numOutputs, boolean hasColumnNames, String delimiter) throws FileNotFoundException, IOException {
        TabularDataSet<MLDataItem> dataSet = new TabularDataSet<>(numInputs, numOutputs);
        BufferedReader br = new BufferedReader(new FileReader(csvFile));
        String line=null;
        // auto detect column names - ako sadrzi slova onda ima imena. Sta ako su atributi nominalni? U ovoj fazi se pretpostavlja d anisu...
        // i ako u redovima ispod takodje ima stringova u istoj koloni - detect header
        if (hasColumnNames) {    // get col names from the first line
            line = br.readLine().trim();
            String[] colNames = line.split(delimiter);
            // todo checsk number of col names
            dataSet.setColumnNames(colNames);
        } else {
            String[] colNames = new String[numInputs+numOutputs];
            for(int i=0; i<numInputs;i++)
                colNames[i] = "in"+(i+1);

            for(int j=0; j<numOutputs;j++)
                colNames[numInputs+j] = "out"+(j+1);

            dataSet.setColumnNames(colNames);
        }

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                continue; // skip empty lines
            }
            String[] values = line.split(delimiter);
            if (values.length != (numInputs + numOutputs)) {
                throw new DeepNettsException("Wrong number of values in the row " + (dataSet.size() + 1) + ": found " + values.length + " expected " + (numInputs + numOutputs));
            }
            float[] in = new float[numInputs];
            float[] out = new float[numOutputs];

            try {
                // these methods could be extracted into parse float vectors
                for (int i = 0; i < numInputs; i++) { //parse inputs
                    in[i] = Float.parseFloat(values[i]);
                }

                for (int j = 0; j < numOutputs; j++) { // parse outputs
                    out[j] = Float.parseFloat(values[numInputs + j]);
                }
            } catch (NumberFormatException nex) {
                throw new DeepNettsException("Error parsing csv, number expected line in " + (dataSet.size() + 1) + ": " + nex.getMessage(), nex);
            }
            dataSet.add(new TabularDataSet.Item(in, out));
        }

        return dataSet;
    }
}
