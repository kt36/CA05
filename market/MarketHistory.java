package pkg.market;

import pkg.exception.StockMarketExpection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pkg.market.api.IObserver;
import pkg.market.api.ISubject;
import pkg.stock.Stock;

public class MarketHistory implements IObserver {
	private ISubject subject;
	Market market;
	Map<String, List<Double>> marketHistory;

	public MarketHistory(Market incomingMarket) {
		super();
		this.market = incomingMarket;
		marketHistory = new HashMap<String, List<Double>>();
	}

	@Override
	public void setSubject(ISubject priceSetter) {
		this.subject = priceSetter;
	}

	public void startHistoryWithPrice(String symbol, Double newPrice)
			throws StockMarketExpection {
		if (!marketHistory.containsKey(symbol)) {
			List<Double> price = new ArrayList<Double>();
			priceList.add(newPrice);
			marketHistory.put(symbol, price);
		}
	}

	@Override
	public void update() {
		Stock updatedStock = (Stock) subject.getUpdate();
		if (updatedStock == null || 
                    market.getStockForSymbol(updatedStock.getSymbol()) == null) {
			return;
		}
		addToHistory(marketHistory.containsKey(updatedStock.getSymbol()));
	}

	public ArrayList<Double> getPricesFor(String symbol) {
		if (marketHistory.containsKey(symbol)) {
			return (ArrayList<Double>) marketHistory.get(symbol);
		} else {
			return new ArrayList<Double>();
		}
	}
  
  	public void addToHistory(boolean old) {
          List<Double> priceList;
          if (old) {
            priceList = marketHistory.get(updatedStock.getSymbol());
          }
          else {
            priceList = new ArrayList<Double>();
          }
          priceList.add(updatedStock.getPrice());
	  marketHistory.put(updatedStock.getSymbol(), priceList);
  	}
}
