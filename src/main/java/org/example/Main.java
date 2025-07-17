package org.example;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.*;
import org.example.entity.*;
import org.example.response.ProductOutput;
import org.example.response.AccountOutput;
import org.example.response.ProductDetailOutput;
import org.example.response.DetailOutput;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class Main {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final CardsInput tempCardInput = new CardsInput();


    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, ExecutionException, InterruptedException {

        File fileConfig = new File("/Users/natnichasirinipatkul/codingLogic/TestBBL2/Configuration.xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(fileConfig);
        NodeList thread = document.getElementsByTagName("Thread");
        NodeList mode = document.getElementsByTagName("Mode");

        Node threadNode = thread.item(0);
        Node modeNode = mode.item(0);


        ObjectMapper objectMapper = new ObjectMapper();
        File folder = new File("src/main/resources/input");
        File[] files = folder.listFiles();

        if (modeNode.getTextContent().equals("2")) {
            readCardsInputMode2(files);
            files = folder.listFiles();
        }
        List<Future<?>> futures = new ArrayList<>();
        if (files == null) {
            return;
        }
        ExecutorService pool = Executors.newFixedThreadPool(Integer.parseInt(threadNode.getTextContent()));


        for (File fileInput : files) {
            if (modeNode.getTextContent().equals("2") && !fileInput.getName().contains("aggregated-result_")) {
                if (!fileInput.delete()) {
                    System.out.println("File not deleted");
                } else {
                    System.out.println("File deleted");
                }


                continue;
            }
            Future<?> future = pool.submit(() -> {

                CardsInput input = null;
                try {

                    input = readCardsInputMode1(fileInput);

                    AccountOutput answer = new AccountOutput(0, new ArrayList<>());
                    MapWithAccountNumber mapAccounts = new MapWithAccountNumber(new HashMap<>());


                    for (CardDetailInput card : input.getCards()) {
                        mapAccounts.getAccountNumberMap().computeIfAbsent(card.getAccountNumber(), k -> new MapWithProductName(card.getAccountNumber(), new HashMap<>()));
                        mapAccounts.getAccountNumberMap().get(card.getAccountNumber()).getProductNameMap().computeIfAbsent(card.getProductName(), k -> new MapProductData(card.getProductName(), 0.00, new ArrayList<>()));
                        mapAccounts.getAccountNumberMap().get(card.getAccountNumber()).getProductNameMap().computeIfPresent(card.getProductName(), (k, value) -> {
                            value.setProductName(k);
                            value.setTotalBalance(Double.parseDouble(card.getBalance()) + value.getTotalBalance());
                            value.getDetails().add(new DetailOutput(card.getCardNumber(), String.format("%.2f", Double.parseDouble(card.getBalance())), formatDate(card.getExpireDate())));
                            return value;
                        });
                    }


                    for (MapWithProductName account : mapAccounts.getAccountNumberMap().values().stream().sorted(Comparator.comparing(MapWithProductName::getAccountNumber)).toList()) {
                        List<ProductDetailOutput> productList = new ArrayList<>();

                        for (MapProductData product : account.getProductNameMap().values().stream().sorted(Comparator.comparing(MapProductData::getProductName)).toList()) {
                            productList.add(new ProductDetailOutput(product.getProductName(), String.format("%.2f", product.getTotalBalance()), product.getDetails().stream().sorted(Comparator.comparing(DetailOutput::getCardNumber)).collect(Collectors.toList())));
                        }

                        answer.getAccounts().add(new ProductOutput(account.getAccountNumber(), productList));
                    }
                    answer.setAccountTotal(mapAccounts.getAccountNumberMap().size());

                    String jsonAnswer = null;

                    jsonAnswer = objectMapper.writeValueAsString(answer);
                    getFileWriter(fileInput, "src/main/resources/output/", jsonAnswer);


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (!fileInput.delete()) {
                    System.out.println("File not deleted");
                } else {
                    System.out.println("File deleted");
                }


            });

            futures.add(future);


        }
        for (Future<?> f : futures) {
            f.get();
        }
        pool.shutdown();

        tempCardInput.setCardTotal(0);
        tempCardInput.setCards(new ArrayList<>());


    }

    private static void getFileWriter(File fileInput, String path, String jsonAnswer) throws IOException {
        File fileJsonSampleOutput = new File(path + fileInput.getName());
        FileWriter fileWriter = null;
        fileWriter = new FileWriter(fileJsonSampleOutput);
        fileWriter.write(jsonAnswer);
        fileWriter.close();

    }


    private static void readCardsInputMode2(File[] files) throws IOException {
        for (File file : files) {
            CardsInput c = objectMapper.readValue(file, CardsInput.class);
            tempCardInput.getCards().addAll(c.getCards());
            tempCardInput.setCardTotal(tempCardInput.getCardTotal() + c.getCardTotal());

        }
        objectMapper.writeValue(new File("src/main/resources/input/aggregated-result_" + formateDateInFileNameJson()), tempCardInput);

    }

    private static CardsInput readCardsInputMode1(File fileInput) throws IOException {

        return objectMapper.readValue(fileInput, CardsInput.class);

    }

    private static String formatDate(String dateText) {
        String date = dateText.substring(0, 2);
        String month = dateText.substring(2, 4);
        String year = String.valueOf((Integer.parseInt(dateText.substring(4)) - 543));
        return year + "-" + month + "-" + date;

    }

    private static String formateDateInFileNameJson() {
        LocalDateTime now = LocalDateTime.now();
        String dateTime = "" + now.getYear() + now.getMonthValue() + now.getDayOfMonth() + "_" + now.getHour() + now.getMinute() + now.getSecond();

        return dateTime + ".json";
    }
}




