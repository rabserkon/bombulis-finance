package com.bombulis.accounting.service.NasdaqApi;


import com.bombulis.accounting.service.AccountService.exception.Acc_ServerDataAssetsException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class YahooClient {
    private final Gson gson = new Gson();
    private final String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36";

    private String a1;
    private String a1s;
    private String a3;
    private String crumb;

    private HttpURLConnection client;
    private static final Logger logger = LoggerFactory.getLogger(YahooClient.class);
    public YahooClient() throws Acc_ServerDataAssetsException {
        this.a1 = "";
        this.a1s = "";
        this.a3 = "";
        this.crumb = null;
        try {
            client = (HttpURLConnection) new URL("https://finance.yahoo.com").openConnection();
            client.setRequestProperty("User-Agent", userAgent);

            Map<String, List<String>> headerFields = client.getHeaderFields();
            List<String> cookies = headerFields.get("Set-Cookie");

            if (cookies != null) {
                for (String cookie : cookies) {
                    if (cookie.startsWith("A1=")) {
                        a1 = cookie.split(";")[0];
                    } else if (cookie.startsWith("A1S=")) {
                        a1s = cookie.split(";")[0];
                    } else if (cookie.startsWith("A3=")) {
                        a3 = cookie.split(";")[0];
                    }
                }
            }

            String url = "https://query2.finance.yahoo.com/v1/test/getcrumb";
            client = (HttpURLConnection) new URL(url).openConnection();
            client.setRequestProperty("User-Agent", userAgent);
            client.setRequestProperty("Accept", "text/plain");
            client.setRequestProperty("Cookie", a1 + "; " + a1s + "; " + a3);

            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.crumb = reader.readLine();

            reader.close();
            client.disconnect();

        } catch (IOException e) {
            throw new Acc_ServerDataAssetsException("Yahoo server returned HTTP response bad");
        }
    }
    public List<JsonObject> getCurrentTickers(List<String> symbols) throws IOException {
        StringBuilder strSymbols = new StringBuilder();
        symbols.forEach(i -> {strSymbols.append(i + ",");});
        return getCurrentTickers(strSymbols.toString());
    }

    public Map<String, JsonObject> getTickersData(List<String> symbols) throws IOException {
        StringBuilder strSymbols = new StringBuilder();
        symbols.forEach(i -> {strSymbols.append(i + ",");});
        return getCurrentTickers(strSymbols.toString()).stream().collect(Collectors.toMap(i -> i.get("symbol").toString(), i -> i));
    }

    public List<JsonObject> getCurrentTickers(String symbols) throws IOException {
        List<JsonObject> parsedJson = new ArrayList<>();

        try {

            String url = "https://query1.finance.yahoo.com/v7/finance/quote?crumb=" + this.crumb +
                    "&lang=en-US&region=US&corsDomain=es.finance.yahoo.com&symbols=" + symbols;

            client = (HttpURLConnection) new URL(url).openConnection();
            client.setRequestProperty("User-Agent", this.userAgent);
            client.setRequestProperty("Cookie", this.a1 + "; " + this.a1s + "; " + this.a3);
            client.setRequestProperty("Accept", "application/json");

            if (client.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("Server HTTP error code: " + client.getResponseCode());
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            JsonObject jsonResponse  = gson.fromJson(reader, JsonObject.class).getAsJsonObject().getAsJsonObject("quoteResponse");
            JsonArray quoteResponse = jsonResponse.getAsJsonObject().getAsJsonArray("result");
            for (JsonElement element : quoteResponse) {
                JsonObject d = element.getAsJsonObject();
                JsonObject q = new JsonObject();
                q.addProperty("price_change", d.get("regularMarketChangePercent").getAsDouble());
                q.addProperty("market_time", LocalDateTime.ofEpochSecond(d.get("regularMarketTime").getAsLong(), 0, ZoneOffset.UTC).toString());
                q.addProperty("price", d.get("regularMarketPrice").getAsDouble());
                q.addProperty("language", d.get("language").getAsString());
                q.addProperty("region", d.get("region").getAsString());
                q.addProperty("quoteType", d.get("quoteType").getAsString());
                q.addProperty("typeDisp", d.get("typeDisp").getAsString());
                q.addProperty("quoteSourceName", d.get("quoteSourceName").getAsString());
                q.addProperty("currency", d.get("currency").getAsString());
                q.addProperty("marketState", d.get("marketState").getAsString());
                q.addProperty("exchange", d.get("exchange").getAsString());
                q.addProperty("shortName", d.get("shortName").getAsString());
                q.addProperty("longName", d.get("longName").getAsString());
                q.addProperty("regularMarketPrice", d.get("regularMarketPrice").getAsDouble());
                q.addProperty("fullExchangeName", d.get("fullExchangeName").getAsString());
                //q.addProperty("financialCurrency", d.get("financialCurrency").getAsString());
                q.addProperty("regularMarketOpen", d.get("regularMarketOpen").getAsDouble());
                q.addProperty("symbol", d.get("symbol").getAsString());
                parsedJson.add(q);
            }

        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }

        return parsedJson;
    }

    public void generateCookies() {
        try {
            // Step 1: Make a GET request to finance.yahoo.com to get sessionId
            HttpURLConnection conn = (HttpURLConnection) new URL("https://finance.yahoo.com").openConnection();
            conn.setRequestProperty("User-Agent", userAgent);

            Map<String, List<String>> headerFields = conn.getHeaderFields();
            List<String> cookies = headerFields.get("Set-Cookie");

            String sessionId = "";
            if (cookies != null) {
                for (String cookie : cookies) {
                    if (cookie.startsWith("sessionId=")) {
                        sessionId = cookie.split(";")[0];
                        break;
                    }
                }
            }

            // Step 2: Make a POST request to consent.yahoo.com to collect consent
            String csrfToken = "";
            String url = "https://consent.yahoo.com/v2/collectConsent";
            String params = String.format("sessionId=%s&agree=agree", URLEncoder.encode(sessionId, "UTF-8"));

            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", userAgent);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            writer.write(params);
            writer.flush();
            writer.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("gcrumb")) {
                    csrfToken = line.split("\"")[3];
                    break;
                }
            }
            reader.close();

            // Step 3: Save cookies to a file
            FileWriter fw = new FileWriter("cookies.txt");
            fw.write(sessionId + ";" + csrfToken);
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTickerInfo(String symbol) {
        try {
            String url = "https://query1.finance.yahoo.com/v7/finance/quote?lang=en-US&region=US&corsDomain=finance.yahoo.com&fields=symbol,shortName,currency,fullExchangeName,exchange&symbols=" + symbol;

            client = (HttpURLConnection) new URL(url).openConnection();
            client.setRequestProperty("User-Agent", userAgent);
            client.setRequestProperty("Accept", "application/json");

            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            JsonObject jsonResponse = gson.fromJson(reader, JsonObject.class);

            JsonObject result = jsonResponse.getAsJsonArray("quoteResponse").get(0).getAsJsonObject();
            return result.get("shortName").getAsString() + ", " + result.get("currency").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    public String getTickerPeriodInfo(String symbol, String range, String interval) {
        try {
            String url = buildUrl(symbol, range, interval);

            client = (HttpURLConnection) new URL(url).openConnection();
            client.setRequestProperty("User-Agent", userAgent);
            client.setRequestProperty("Accept", "application/json");

            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            JsonObject jsonResponse = gson.fromJson(reader, JsonObject.class);

            JsonObject result = jsonResponse.getAsJsonArray("quoteResponse").get(0).getAsJsonObject();
            return result.get("shortName").getAsString() + ", " + result.get("currency").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    private static String buildUrl(String symbols, String range, String interval) {
        String baseUrl = "https://query1.finance.yahoo.com/v7/finance/spark";
        StringBuilder urlBuilder = new StringBuilder(baseUrl);

        urlBuilder.append("?symbols=").append(symbols)
                .append("&range=").append(range)
                .append("&interval=").append(interval)
                .append("&indicators=close")
                .append("&includeTimestamps=").append(false)
                .append("&includePrePost=").append(false)
                .append("&corsDomain=").append("finance.yahoo.com")
                .append("&.tsrc=").append("finance");

        return urlBuilder.toString();
    }
}




