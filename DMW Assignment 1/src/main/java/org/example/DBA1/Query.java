package org.example.DBA1;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Query implements IQuery {


    public String primaryField;
    public int primaryFieldIndex;

    @Override
    public void runQuery(String query){

        Pattern pattern = Pattern.compile("[cC][rR][eE][aA][tT][eE]\\s[tT][aA][bB][lL][eE]\\s(([a-zA-Z_])+)\\s\\((([a-zA-Z_,\\s\\d])+)\\)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);
        boolean matchFound = matcher.find();
        if (matchFound){
            createQuery(matcher.group(1), matcher.group(3));
            return;
        }

        pattern = Pattern.compile("[sS][eE][lL][eE][cC][tT]\\s(\\*|([a-zA-Z,])+)\\s[fF][rR][oO][mM]\\s(([a-zA-Z_])+)(\\s([wW][hH][eE][rR][eE])\\s([a-zA-Z=\\d\\s]+))*", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(query);
        matchFound = matcher.find();
        if (matchFound){

            selectQuery(matcher.group(3), matcher.group(1), matcher.groupCount() < 5? null :matcher.group(7));
        }

        pattern = Pattern.compile("[iI][nN][sS][eE][rR][tT]\\s[iI][nN][tT][oO]\\s([a-zA-Z\\d_]+)\\s[vV][aA][lL][uU][eE][sS]\\s\\(([a-zA-Z\\d_,]+)\\)", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(query);
        matchFound = matcher.find();
        if (matchFound){
            insertQuery(matcher.group(1), matcher.group(2));
        }

        pattern = Pattern.compile("[uU][pP][dD][aA][tT][eE]\\s([a-zA-Z_]+)\\s[sS][eE][tT]\\s([a-zA-Z=\\d,]+)(\\s([wW][hH][eE][rR][eE])\\s([a-zA-Z=\\d\\s]+))*", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(query);
        matchFound = matcher.find();
        if (matchFound){
            updateQuery(matcher.group(1), matcher.group(2), matcher.group(5));
        }

        pattern = Pattern.compile("[dD][eE][lL][eE][tT][eE]\\s[fF][rR][oO][mM]\\s(([a-zA-Z_])+)(\\s([wW][hH][eE][rR][eE])\\s([a-zA-Z=\\d\\s]+))*", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(query);
        matchFound = matcher.find();
        if (matchFound){
            deleteQuery(matcher.group(1), matcher.group(5));
        }
    }



    public boolean validatePrimaryKeyValues(String column, String tableName, String value) throws IOException {
        File table = new File(Utils.getTableFileName(tableName));

        try {
            Map<String, String> fieldEntry = getMetaData(tableName);

            BufferedReader br = new BufferedReader(new FileReader(table));
            String st;
            while ((st = br.readLine()) != null) {
                List<String> rowValues = List.of(st.split("\\|"));

                List<Map.Entry<String, String>> indexFieldMapping = new ArrayList<Map.Entry<String, String>>(fieldEntry.entrySet());
                Map<String, String> row = new LinkedHashMap<>();

                for(int i = 0; i < rowValues.size(); i++){
                    row.put(indexFieldMapping.get(i).getKey(), rowValues.get(i));
                }

                boolean status = checkValue(column, value, row);
                if(status) return false;

            }
        }
        catch (FileNotFoundException e){
            System.out.println("File for the table " + tableName + " does not exist");
            return false;
        }
        return true;
    }

    public boolean checkValue(String column, String value, Map<String, String> row){

        return row.get(column).equals(value);
    }


    @Override
    public void createQuery(String tableName, String columns){
        File tableFile = new File(Utils.getTableFileName(tableName));
        if(tableFile.exists()) {
            System.out.println("Table with this name already exist");
            return;
        }

        try {
            tableFile.createNewFile();

            File tableMetadataFile = new File(Utils.getTableMetadataFileName(tableName));

            tableMetadataFile.createNewFile();

            List<String> fields = List.of(columns.split(","));

            FileWriter fileWriter = new FileWriter(tableMetadataFile);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            for(String field: fields){
                field = field.trim();
                field = field.replace(' ', '|');
                printWriter.println(field);
            }

            printWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    public Map<String, String> getMetaData(String tableName) throws IOException {
        File tableMetadata = new File(Utils.getTableMetadataFileName(tableName));

        Map<String, String> fieldMapping = new LinkedHashMap<>();

        BufferedReader br = new BufferedReader(new FileReader(tableMetadata));

        String st;
        int i = 0;
        while ((st = br.readLine()) != null) {
            List<String> field = List.of(st.split("\\|"));
            fieldMapping.put(field.get(0), field.get(1));

            if(field.size() > 2 && field.get(2).equals("primarykey")){
                primaryField = field.get(0);
                primaryFieldIndex = i;
            }

            i++;
        }
        return fieldMapping;
    }
    public boolean validateSelectFields(String fields, Map<String, String> columns){

        if(fields.equals("*")) return true;
        List<String> selectFields = List.of(fields.split(","));

        for(String field: selectFields){

            if(!columns.containsKey(field.trim())){
                System.out.println(field + " does not exist in the table");
                return false;
            }
        }

        return true;
    }

    public void print(String fields, Map<String, String> row){

        List<String> selectFields = List.of(fields.split(","));
        StringBuilder output = new StringBuilder();
        if(fields.equals("*")){
            for (Map.Entry<String, String> entry : row.entrySet()) {
                String key = entry.getKey();
                String rowValue = entry.getValue();
                output.append(rowValue + " | ");
            }

            output.delete(output.length() - 3, output.length());

            System.out.println(output);

            return;
        }

        for(String field: selectFields){

            String rowValue = row.get(field.trim());
            output.append(rowValue + " | ");

        }
        output.delete(output.length() - 3, output.length());
        System.out.println(output);

    }

    @Override
    public void selectQuery(String tableName, String fields, String conditions){

        File table = new File(Utils.getTableFileName(tableName));

        try {
            Map<String, String> fieldEntry = getMetaData(tableName);

            if(!validateSelectFields(fields, fieldEntry)) return;

            BufferedReader br = new BufferedReader(new FileReader(table));
            String st;
            while ((st = br.readLine()) != null) {
                List<String> rowValues = List.of(st.split("\\|"));

                List<Map.Entry<String, String>> indexFieldMapping = new ArrayList<Map.Entry<String, String>>(fieldEntry.entrySet());
                Map<String, String> row = new LinkedHashMap<>();

                for(int i = 0; i < rowValues.size(); i++){
                    row.put(indexFieldMapping.get(i).getKey(), rowValues.get(i));
                }
                boolean conditionStatus = handleConditions(row, conditions);

                if(conditionStatus){
                    print(fields, row);
                }

            }

        }
        catch (FileNotFoundException e){
            System.out.println("File for the table " + tableName + " does not exist");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void insertQuery(String tableName, String values) {
        File tableFile = new File(Utils.getTableFileName(tableName));
        if(!tableFile.exists()) {
            System.out.println("Table with this name not exist");
            return;
        }
        try {
            File tableMetadataFile = new File(Utils.getTableMetadataFileName(tableName));
            if(!tableMetadataFile.exists()) {
                System.out.println("Table with this name not exist");
                return;
            }

            List<String> valuesList = List.of(values.split(","));
            Map<String, String> fieldEntry = getMetaData(tableName);

            if(fieldEntry.size() != valuesList.size()) {
                System.out.println("Given insert query is invalid. Requires " + fieldEntry.size() + " values instead of "
                        + valuesList.size());
                return;
            }

            if(!validateInsertValuesWithFieldType(fieldEntry, valuesList)) {
                System.out.println("Insert values contains invalid datatype");
                return;
            }

            FileWriter fileWriter = new FileWriter(Utils.getTableFileName(tableName), true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);

            if(primaryField != null &&
                    !validatePrimaryKeyValues(primaryField, tableName, valuesList.get(primaryFieldIndex).trim())) {
                System.out.println("Value having primary key already exist");
                return;
            }

            printWriter.println(values.replace(',', '|'));
            printWriter.close();
        } catch (Exception e) {
            System.out.println("Something went wrong!");
        }
    }

    @Override
    public void updateQuery(String tableName, String values, String conditions){
        File tableFile = new File(Utils.getTableFileName(tableName));
        if(!tableFile.exists()) {
            System.out.println("Table with this name not exist");
            return;
        }
        try {
            File tableMetadataFile = new File(Utils.getTableMetadataFileName(tableName));
            if(!tableMetadataFile.exists()) {
                System.out.println("Table with this name not exist");
                return;
            }

            List<String> valuesList = List.of(values.split(","));
            Map<String, String> fieldEntry = getMetaData(tableName);

            List<String> columnValues = List.of(values.split(","));

            Map<String, String> columnValueMap = new HashMap<>();

            String primaryKeyNewValue = null;

            for(String columnValue: columnValues){
                String[] keyValue = columnValue.split("=");
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();

                if(key.equals(primaryField)) primaryKeyNewValue = value;

                columnValueMap.put(key, value);
            }

            if(primaryKeyNewValue != null){
                if(!validatePrimaryKeyValues(primaryField, tableName, primaryKeyNewValue)){
                    System.out.println("Primary key can not contain duplicate values");
                }
            }

            FileWriter fileWriter = new FileWriter(Utils.getTableFileName(tableName + "-temp"));
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);


            BufferedReader br = new BufferedReader(new FileReader(tableFile));

            String st;
            while ((st = br.readLine()) != null) {
                List<String> rowValues = List.of(st.split("\\|"));

                List<Map.Entry<String, String>> indexFieldMapping = new ArrayList<Map.Entry<String, String>>(fieldEntry.entrySet());
                Map<String, String> row = new LinkedHashMap<>();

                for(int i = 0; i < rowValues.size(); i++){
                    row.put(indexFieldMapping.get(i).getKey(), rowValues.get(i));
                }
                boolean conditionStatus = handleConditions(row, conditions);

                StringBuilder output = new StringBuilder(st);
                if(conditionStatus){
                    for (Map.Entry<String, String> entry : columnValueMap.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        row.put(key, value);
                    }

                    StringBuilder temp = new StringBuilder();
                    for (Map.Entry<String, String> entry : row.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        temp.append(value + "|");
                    }

                    temp.delete(temp.length() - 1, temp.length());

                    output = temp;
                }

                printWriter.println(output);

            }

            printWriter.close();

            copyFromTemporaryData(tableName);
        } catch (Exception e) {
            System.out.println("Something went wrong!");
        }

    }

    @Override
    public void deleteQuery(String tableName, String conditions){
        File tableFile = new File(Utils.getTableFileName(tableName));
        if(!tableFile.exists()) {
            System.out.println("Table with this name not exist");
            return;
        }
        try {
            File tableMetadataFile = new File(Utils.getTableMetadataFileName(tableName));
            if(!tableMetadataFile.exists()) {
                System.out.println("Table with this name not exist");
                return;
            }

            Map<String, String> fieldEntry = getMetaData(tableName);

            FileWriter fileWriter = new FileWriter(Utils.getTableFileName(tableName + "-temp"));
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);


            BufferedReader br = new BufferedReader(new FileReader(tableFile));

            String st;
            while ((st = br.readLine()) != null) {
                List<String> rowValues = List.of(st.split("\\|"));

                List<Map.Entry<String, String>> indexFieldMapping = new ArrayList<Map.Entry<String, String>>(fieldEntry.entrySet());
                Map<String, String> row = new LinkedHashMap<>();

                for(int i = 0; i < rowValues.size(); i++){
                    row.put(indexFieldMapping.get(i).getKey(), rowValues.get(i));
                }
                boolean conditionStatus = handleConditions(row, conditions);

                if(!conditionStatus){
                    printWriter.println(st);
                }


            }

            printWriter.close();

            copyFromTemporaryData(tableName);
        } catch (Exception e) {
            System.out.println("Something went wrong!");
        }
    }

    public void copyFromTemporaryData(String tableName) throws IOException {

        FileWriter fileWriter = new FileWriter(Utils.getTableFileName(tableName));
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        PrintWriter printWriter = new PrintWriter(bufferedWriter);

        File tableFile = new File(Utils.getTableFileName(tableName + "-temp"));
        BufferedReader br = new BufferedReader(new FileReader(tableFile));

        String st;
        while ((st = br.readLine()) != null) {

            printWriter.println(st);

        }

        printWriter.close();
        tableFile.delete();
    }

    public boolean validateInsertValuesWithFieldType(Map<String, String> fieldEntry, List<String> valuesList) {
        List<Map.Entry<String, String>> listOfEntries = new ArrayList<Map.Entry<String, String>>(fieldEntry.entrySet());

        for(int i = 0; i < listOfEntries.size(); i++) {
            String columnType = listOfEntries.get(i).getValue();

            if(!Utils.validateType(columnType, valuesList.get(i))) return false;
        }
        return true;
    }
    public boolean handleCondition(Map<String, String> row, String field, String value){

        for (Map.Entry<String, String> entry : row.entrySet()) {
            String key = entry.getKey();
            String rowValue = entry.getValue();
            if(key.equals(field) && rowValue.equals(value)){
                return true;
            }
        }

        return false;

    }

    public boolean handleConditions(Map<String, String> row, String conditions){

        if(conditions == null){
            return true;
        }
        if(conditions.contains("and")){

            String condition1 = conditions.split("and")[0].trim();
            String condition2 = conditions.split("and")[1].trim();

            return handleCondition(row, condition1.split("=")[0].trim(), condition1.split("=")[1].trim()) &&
                    handleCondition(row, condition2.split("=")[0].trim(), condition2.split("=")[1].trim());
        }
        if(conditions.contains("or")){

            String condition1 = conditions.split("or")[0].trim();
            String condition2 = conditions.split("or")[1].trim();

            return handleCondition(row, condition1.split("=")[0].trim(), condition1.split("=")[1].trim()) ||
                    handleCondition(row, condition2.split("=")[0].trim(), condition2.split("=")[1].trim());
        }

        return handleCondition(row, conditions.split("=")[0].trim(), conditions.split("=")[1].trim());

    }
}
