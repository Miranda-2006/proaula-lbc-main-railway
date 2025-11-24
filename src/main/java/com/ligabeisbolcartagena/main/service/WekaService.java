package com.ligabeisbolcartagena.main.service;

import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.core.SerializationHelper;

@Service
public class WekaService {

    private Instances structure;
    private Classifier model;

    public WekaService() throws Exception {
        structure = ConverterUtils.DataSource.read("src/main/resources/lbc-dataset.arff");
        structure.setClassIndex(structure.numAttributes() - 1);
        model = (Classifier) SerializationHelper.read("src/main/resources/lbcmodeltree.model");
        System.out.println("✔ Modelo WEKA cargado correctamente.");
    }

    public Prediccion predecir(String localVisitante,
                               double rachaUlt3,
                               double promFavor,
                               double promContra,
                               String rivalFuerza) throws Exception {

        Instance inst = new DenseInstance(structure.numAttributes());
        inst.setDataset(structure);

        inst.setValue(0, localVisitante);
        inst.setValue(1, rachaUlt3);
        inst.setValue(2, promFavor);
        inst.setValue(3, promContra);
        inst.setValue(4, rivalFuerza);

        // Probabilidades de cada clase
        double[] dist = model.distributionForInstance(inst);

        // Predicción final (clase con mayor probabilidad)
        int idx = 0;
        for (int i = 1; i < dist.length; i++) {
            if (dist[i] > dist[idx]) idx = i;
        }
        String clase = structure.classAttribute().value(idx);
        double confianza = dist[idx] * 100; // porcentaje

        // Devolvemos un objeto con toda la info
        return new Prediccion(clase, confianza, localVisitante, rachaUlt3, promFavor, promContra, rivalFuerza);
    }

    // Clase interna para encapsular la predicción
    public static class Prediccion {
        private String clase;
        private double confianza;
        private String localVisitante;
        private double rachaUlt3;
        private double promFavor;
        private double promContra;
        private String rivalFuerza;

        public Prediccion(String clase, double confianza,
                          String localVisitante, double rachaUlt3,
                          double promFavor, double promContra,
                          String rivalFuerza) {
            this.clase = clase;
            this.confianza = confianza;
            this.localVisitante = localVisitante;
            this.rachaUlt3 = rachaUlt3;
            this.promFavor = promFavor;
            this.promContra = promContra;
            this.rivalFuerza = rivalFuerza;
        }

        // Getters
        public String getClase() { return clase; }
        public double getConfianza() { return confianza; }
        public String getLocalVisitante() { return localVisitante; }
        public double getRachaUlt3() { return rachaUlt3; }
        public double getPromFavor() { return promFavor; }
        public double getPromContra() { return promContra; }
        public String getRivalFuerza() { return rivalFuerza; }
    }
}
