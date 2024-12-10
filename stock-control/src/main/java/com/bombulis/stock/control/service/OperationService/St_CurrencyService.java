package com.bombulis.stock.control.service.OperationService;

import com.bombulis.stock.control.entity.St_Currency;
import com.bombulis.stock.control.proto.Currency;
import com.bombulis.stock.control.proto.CurrencyServiceGrpc;
import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class St_CurrencyService {

    private final RestTemplate restTemplate;
    private final ManagedChannel channel;
    private final CurrencyServiceGrpc.CurrencyServiceBlockingStub stub;

    @Autowired
    public St_CurrencyService(ManagedChannel channel, RestTemplate restTemplate) {
        this.channel = channel;
        this.restTemplate = restTemplate;
        this.stub = CurrencyServiceGrpc.newBlockingStub(channel);
    }

    // URL и API ключ для обращения к API валют
    @Value("${currency.api.url}")
    private String currencyApiUrl;

    @Value("${currency.api.key}")
    private String currencyApiKey;

    public St_Currency findCurrency(String isoCode){
        Currency.CurrencyRequest request = Currency.CurrencyRequest.newBuilder()
                .setIsoCode(isoCode)
                .build();
        Currency.CurrencyResponse response = this.stub.getCurrencyInfo(request);
        St_Currency currency = new St_Currency();
        currency.setSName(response.getIsoCode());
        currency.setNumericCode(response.getNumericCode());
        currency.setCurrencyId(Long.parseLong(response.getCurrencyId()));
        currency.setCurrencyCode(response.getIsoCode());
        return currency;
    }



    public St_CurrencyResponse getCurrencyInfo(String currencyCode) {
        String url = String.format("%s/%s/latest/%s", currencyApiUrl, currencyApiKey, currencyCode);
        return restTemplate.getForObject(url, St_CurrencyResponse.class);
    }
}
