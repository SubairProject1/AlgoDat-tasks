package main.java.framework;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class ResultsContainer<ResultT extends Result> {

    private List<ResultContainer<ResultT>> resultContainers;
    private List<NameFieldPair> csvFields;

    public ResultsContainer(Class<ResultT> resultClass) {
        this.resultContainers = new ArrayList<ResultContainer<ResultT>>();
        this.csvFields = this.parseCsvFields(resultClass);
    }

    public void addResultContainer(ResultContainer<ResultT> resultContainer) {
        this.resultContainers.add(resultContainer);
    }

    public List<String> toCsv() {
        List<String> csv = new ArrayList<String>();

        List<String> names = this.csvFields.stream().map(nameFieldPair -> nameFieldPair.getName()).collect(Collectors.toList());
        String header = "group,number";
        for (String name : names) {
            header += "," + name;
        }
        csv.add(header);

        List<Field> requiredFields = this.csvFields.stream().map(nameFieldPair -> nameFieldPair.getField()).collect(Collectors.toList());
        for (ResultContainer<ResultT> resultContainer : this.resultContainers) {
            String line = "";
            String group = resultContainer.getGroupName();
            line += group;
            int number = resultContainer.getNumber();
            line += "," + number;

            List<String> values = resultContainer.getRequiredValues(requiredFields);
            for (String value : values) {
                line += "," + value;
            }
            csv.add(line);
        }
        return csv;
    }

    private List<NameFieldPair> parseCsvFields(Class<ResultT> resultClass) {
        List<NameFieldPair> csvFields = new ArrayList<NameFieldPair>();
        for (Field field : resultClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(PersistAs.class)) {
                PersistAs persistAsAnnotation = field.getAnnotation(PersistAs.class);
                csvFields.add(new NameFieldPair(persistAsAnnotation.value(), field));
            }
        }
        return csvFields;
    }

}
