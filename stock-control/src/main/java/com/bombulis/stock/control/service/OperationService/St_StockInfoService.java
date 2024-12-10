package com.bombulis.stock.control.service.OperationService;

import com.bombulis.stock.control.model.St_StockMatchProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class St_StockInfoService {

    @Value("${stock.api.url}")
    private String currencyApiUrl;

    @Value("${stock.api.key}")
    private String currencyApiKey;

    @Value("${stock.api.url.financialmodelingprep}")
    private String financialmodelingprepApiUrl;

    @Value("${stock.api.key.financialmodelingprep}")
    private String financialmodelingprepApiKey;


    @Autowired
    private  RestTemplate restTemplate;

    public St_StockMatch getCompanyNameByTicker(String ticker) throws NotFoundTikerCompany {
        String url = String.format("%s?function=SYMBOL_SEARCH&keywords=%s&apikey=%s", currencyApiUrl, ticker, currencyApiKey);
        St_StockSearchResponse response = restTemplate.getForObject(url, St_StockSearchResponse.class);

        if (response != null && response.getBestMatches() != null && !response.getBestMatches().isEmpty()) {
            St_StockMatch match = response.getBestMatches().get(0);
            return match;
        }
        throw new IllegalArgumentException("Компания с тикером " + ticker + " не найдена.");
    }

    public St_StockMatchProfile getCompanyInfoFromAlternativeApi(String ticker) {
        String url = String.format("https://financialmodelingprep.com/api/v3/profile/%s?apikey=%s", ticker, financialmodelingprepApiKey);
        ResponseEntity<St_StockMatchProfile[]> response = restTemplate.getForEntity(url, St_StockMatchProfile[].class);

        if (response.getBody() != null && response.getBody().length > 0) {
            return response.getBody()[0];
        }
        throw new IllegalArgumentException("Компания с тикером " + ticker + " не найдена в альтернативном API.");
    }
}
