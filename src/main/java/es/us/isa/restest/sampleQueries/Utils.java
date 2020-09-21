package es.us.isa.restest.sampleQueries;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import io.swagger.v3.oas.models.parameters.Parameter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static es.us.isa.restest.sampleQueries.GenerateSPARQLFilters.generateSPARQLFilters;

public class Utils {

    // Execute a Query
    // TODO: Remove duplicates after filtering (datatype)
    public static void executeSPARQLQuery(String szQuery, String szEndpoint)
            throws Exception
    {
        // Create a Query with the given String
        Query query = QueryFactory.create(szQuery);

        // Create the Execution Factory using the given Endpoint
        QueryExecution qexec = QueryExecutionFactory.sparqlService(
                szEndpoint, query);

        // Set Timeout
        ((QueryEngineHTTP)qexec).addParam("timeout", "10000");


        // Execute Query
        int iCount = 0;
        ResultSet rs = qexec.execSelect();
        while (rs.hasNext()) {
            // Get Result
            QuerySolution qs = rs.next();

            // Get Variable Names
            Iterator<String> itVars = qs.varNames();

            // Count
            iCount++;
            System.out.println("\nResult " + iCount + ": ");

            // Display Result
            while (itVars.hasNext()) {
                String szVar = itVars.next();

                // Gets an RDF node
                RDFNode szVal = qs.get(szVar);
                String szValString = "";

                if(szVal.isURIResource()){
                    szValString = szVal.asResource().getLocalName().replace("_", " ").trim();

                }else{
                    szValString = szVal.asLiteral().getString();
                }

                System.out.println("[" + szVar + "]: " + szValString);
            }
        }
    }

    // Generate the Query given two lists of Strings
//    public static String generateQuery(List<String> requiredParameters, List<String> optionalParameters){
//        String queryString = "";
//
//        // Add prefixes
//        queryString = queryString + "PREFIX dbo: <http://dbpedia.org/ontology/> \n";
//        queryString = queryString + "PREFIX dbp: <http://dbpedia.org/property/> \n";
//        queryString = queryString + "PREFIX dbr: <http://dbpedia.org/resource/> \n";
//
//        List<String> allParameters = new ArrayList<>();
//        allParameters.addAll(requiredParameters);
//        allParameters.addAll(optionalParameters);
//
//        String randomString = generateRandomString(allParameters);
//        String parametersString = generateParametersString(allParameters);
//
//        // First line
//        queryString = queryString + "Select distinct " + parametersString +"  where { \n\n";
//
//        // Required parameters
//        int requiredSize = requiredParameters.size();
//        if(requiredSize>0){
//
//            queryString = queryString + "?" + randomString;
//
//            for(int i=0; i<= (requiredSize-2); i++){
//                String currentParameter = requiredParameters.get(i);
//                queryString = queryString + "\tdbo:" + currentParameter + "|dbp:" + currentParameter + " ?" + currentParameter + " ; \n";
//            }
//            String lastParameter = requiredParameters.get(requiredSize - 1);
//            queryString = queryString + "\tdbo:" + lastParameter + "|dbp:" + lastParameter + " ?" + lastParameter + " . \n\n";
//        }
//
//        // Optional parameters
//        int optionalSize = optionalParameters.size();
//        if(optionalSize >0){
//            for(int i = 0; i<= (optionalSize-1); i++){
//                String currentParameter = optionalParameters.get(i);
//                queryString = queryString + "\tOPTIONAL { ?" + randomString + " " + "dbo:" + currentParameter +"|dbp:" + currentParameter + " ?" + currentParameter + " }\n";
//
//            }
//
//        }
//
//
//        // Close query
//        queryString = queryString + "} LIMIT 20 \n";
//
//        return queryString;
//
//    }

//    // Generate the Query given a list of Parameters
//    public static String generateQuery(List<Parameter> parameters) {
//        String queryString = "";
//        String filters = "";
//
//        // Add prefixes
//        queryString = queryString + "PREFIX dbo: <http://dbpedia.org/ontology/> \n";
//        queryString = queryString + "PREFIX dbp: <http://dbpedia.org/property/> \n";
//        queryString = queryString + "PREFIX dbr: <http://dbpedia.org/resource/> \n";
//
//        List<Parameter> requiredParameters = new ArrayList<>();
//        List<Parameter> optionalParameters = new ArrayList<>();
//        List<String> allParametersName = new ArrayList<>();
//
//        for (Parameter p : parameters) {
//            allParametersName.add(p.getName());
//            if (p.getRequired()) {
//                requiredParameters.add(p);
//            } else {
//                optionalParameters.add(p);
//            }
//        }
//
//        // Random String and parameter string
//        String randomString = generateRandomString(allParametersName);
//        String parametersString = generateParametersString(allParametersName);
//
//        // First line
//        queryString = queryString + "Select distinct " + parametersString + "  where { \n\n";
//
//        // Required parameters
//        int requiredSize = requiredParameters.size();
//        if (requiredSize > 0) {
//
//            queryString = queryString + "?" + randomString;
//
//            for (int i = 0; i <= (requiredSize - 2); i++) {
//                // Add required parameter to query
//                Parameter currentParameter = requiredParameters.get(i);
//                String currentParameterName = currentParameter.getName();
//                queryString = queryString + "\tdbo:" + currentParameterName + "|dbp:" +
//                        currentParameterName + " ?" + currentParameterName + " ; \n";
//
//                // TODO: Test with multiple parameters
//                filters = filters + generateSPARQLFilters(currentParameter);
//            }
//            // TODO: Test with multiple parameters
//            Parameter lastParameter = requiredParameters.get(requiredSize - 1);
//            String lastParameterName = lastParameter.getName();
//            queryString = queryString + "\tdbo:" + lastParameterName + "|dbp:" +
//                    lastParameterName + " ?" + lastParameterName + " . \n\n";
//
//            filters = filters + generateSPARQLFilters(lastParameter);
//        }
//
//        // Optional parameters
//        int optionalSize = optionalParameters.size();
//        if (optionalSize > 0) {
//            for (int i = 0; i <= (optionalSize - 1); i++) {
//                // Add optional parameter to query
//                Parameter currentParameter = optionalParameters.get(i);
//                String currentParameterName = currentParameter.getName();
//
//                // Filters of optional parameter
//                String optionalFilter = generateSPARQLFilters(currentParameter);
//
//                // TODO: Test with multiple optional parameters
//                queryString = queryString + "\tOPTIONAL { ?" + randomString + " " + "dbo:" + currentParameterName
//                        + "|dbp:" + currentParameterName + " ?" + currentParameterName +  " . " + optionalFilter + " }\n";
//
//            }
//
//        }
//
//        // Add filters
//        queryString = queryString + "\n" + filters;
//
//
//        // Close query
//        queryString = queryString + "\n}  \n";
//        return queryString;
//
//    }


    // Generate the Query given a Map of parameters and their respective predicates
    public static String generateQuery(Map<Parameter, List<String>> parametersWithPredicates) {
        String queryString = "";
        String filters = "";

        // Add prefixes
        queryString = queryString + "PREFIX dbo: <http://dbpedia.org/ontology/> \n";
        queryString = queryString + "PREFIX dbp: <http://dbpedia.org/property/> \n";
        queryString = queryString + "PREFIX dbr: <http://dbpedia.org/resource/> \n";

        List<Parameter> requiredParameters = new ArrayList<>();
        List<Parameter> optionalParameters = new ArrayList<>();
        List<String> allParametersName = new ArrayList<>();

        for (Parameter p : parametersWithPredicates.keySet()) {
            allParametersName.add(p.getName());
            if (p.getRequired()) {
                requiredParameters.add(p);
            } else {
                optionalParameters.add(p);
            }
        }

        // Random String and parameter string
        String randomString = generateRandomString(allParametersName);
        String parametersString = generateParametersString(allParametersName);

        // First line
        queryString = queryString + "Select distinct " + parametersString + "  where { \n\n";

        // Required parameters
        int requiredSize = requiredParameters.size();
        if (requiredSize > 0) {

            queryString = queryString + "?" + randomString;

            for (int i = 0; i <= (requiredSize - 2); i++) {
                // Add required parameter to query
                Parameter currentParameter = requiredParameters.get(i);
                String currentParameterName = currentParameter.getName();

                // Predicates
                List<String> predicates = parametersWithPredicates.get(currentParameter);
                String predicatesString = getPredicatesString(predicates);

                queryString = queryString + "\t" + predicatesString + " ?" + currentParameterName + " ; \n";

                // TODO: Test with multiple parameters
                filters = filters + generateSPARQLFilters(currentParameter);
            }
            // TODO: Test with multiple parameters
            Parameter lastParameter = requiredParameters.get(requiredSize - 1);
            String lastParameterName = lastParameter.getName();

            // Predicates
            List<String> predicates = parametersWithPredicates.get(lastParameter);
            String predicatesString = getPredicatesString(predicates);

            queryString = queryString + "\t" + predicatesString + " ?" + lastParameterName + " . \n\n";

            filters = filters + generateSPARQLFilters(lastParameter);
        }

        // Optional parameters
        int optionalSize = optionalParameters.size();
        if (optionalSize > 0) {
            for (int i = 0; i <= (optionalSize - 1); i++) {
                // Add optional parameter to query
                Parameter currentParameter = optionalParameters.get(i);
                String currentParameterName = currentParameter.getName();

                // Filters of optional parameter
                String optionalFilter = generateSPARQLFilters(currentParameter);

                // TODO: Test with multiple optional parameters
                // Predicates
                List<String> predicates = parametersWithPredicates.get(currentParameter);
                String predicatesString = getPredicatesString(predicates);

                queryString = queryString + "\tOPTIONAL { ?" + randomString + " " + predicatesString + " ?" + currentParameterName +  " . " + optionalFilter + " }\n";

            }

        }

        // Add filters
        queryString = queryString + "\n" + filters;


        // Close query
        queryString = queryString + "\n}  \n";
        return queryString;

    }

    private static String generateRandomString(List<String> allParameters){
        String res = getAlphaNumericString();
        if(allParameters.contains(res)){
            return generateRandomString(allParameters);
        }else{
            return res;
        }
    }

    private static String getAlphaNumericString() {

        // chose a Character random from this String
        String AlphaNumericString = "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    private static String generateParametersString(List<String> allParameters){
        String res = "";
        for(int i=0; i <= allParameters.size()-1; i++){
            res = res + " ?" + allParameters.get(i);
        }

        return res;
    }

    private static  String getPredicatesString(List<String> predicates){
        String res = "<" + predicates.get(0) + ">";

        for(int i = 1; i<predicates.size(); i++){
            res = res + "|<" + predicates.get(i) + ">";
        }

        return res;
    }


}

