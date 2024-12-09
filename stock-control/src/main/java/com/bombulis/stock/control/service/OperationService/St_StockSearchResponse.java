package com.bombulis.stock.control.service.OperationService;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class St_StockSearchResponse {
    private List<St_StockMatch> bestMatches;
}
