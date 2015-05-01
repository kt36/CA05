package pkg.trader;

import java.util.ArrayList;

import pkg.exception.StockMarketExpection;
import pkg.market.Market;
import pkg.order.BuyOrder;
import pkg.order.Order;
import pkg.order.OrderType;
import pkg.order.SellOrder;

public class Trader {
	String name;
	double cashInHand;
	public ArrayList<Order> stocksOwned;
	ArrayList<Order> ordersPlaced;

	public Trader(String name, double cashInHand) {
		super();
		this.name = name;
		this.cashInHand = cashInHand;
		this.stocksOwned = new ArrayList<Order>();
		this.ordersPlaced = new ArrayList<Order>();
	}

	public void buyFromBank(Market m, String symbol, int volume)
			throws StockMarketExpection {
		double price = m.getStockForSymbol(symbol).getPrice();
		if ((price*volume) > this.cashInHand) {
		throw new StockMarketExpection("Cannot place order for stock: " + symbol + " since there is not enough money."
				+ " Trader: " + this.name);
		}
		this.stocksOwned.add(new BuyOrder(symbol, volume, price, this));
		this.cashInHand -= (price*volume);
		
	}

	public void placeNewOrder(Market m, String symbol, int volume,
			double price, OrderType orderType) throws StockMarketExpection {
		if (orderType == OrderType.BUY) {
			if ((price*volume) > this.cashInHand) {
			throw new StockMarketExpection("Cannot place order for stock: " + symbol + " since there is not enough money."
						+ " Trader: " + this.name);
			}
			
			for(int i = 0; i < this.ordersPlaced.size(); i++) {
				if (this.ordersPlaced.get(i).getStockSymbol().equals(symbol)) {
					throw new StockMarketExpection("Cannot place order for stock: " + symbol + " since this trader already has"
							+ "an order for this stock. Trader: " + this.name);
				}
			}
			Order order = new BuyOrder(symbol, volume, price, this);
			this.ordersPlaced.add(order);
			m.addOrder(order);
		}

		if (orderType == OrderType.SELL) {
			boolean owns = false;
			int numberStocksOwned = 0;
			for (int i = 0; i < this.stocksOwned.size(); i++) {
				if (this.stocksOwned.get(i).getStockSymbol() == symbol) {
					owns = true;
					numberStocksOwned = this.stocksOwned.get(i).getSize();
				}
			}
			if (!owns) {
				throw new StockMarketExpection("Cannot place order for stock: " + symbol + " since this trader does not"
						+ "own this stock. Trader: " + this.name);
			}
			if (numberStocksOwned < volume){
				throw new StockMarketExpection("Cannot place order for stock: " + symbol + " since this trader does not"
						+ "own enough shares of this stock. Trader: " + this.name);
			}
			Order order = new SellOrder(symbol, volume, price, this);
			this.ordersPlaced.add(order);
			m.addOrder(order);
		}
	}

	public void placeNewMarketOrder(Market market, String symbol, int volume,
			double price, OrderType orderType) throws StockMarketExpection {
		if (orderType == OrderType.BUY) {
			if ((price*volume) > this.cashInHand) {
			throw new StockMarketExpection("Cannot place order for stock: " + symbol + " since there is not enough money."
						+ " Trader: " + this.name);
			}

			for(int i = 0; i < this.ordersPlaced.size(); i++) {
				if (this.ordersPlaced.get(i).getStockSymbol().equals(symbol)) {
					throw new StockMarketExpection("Cannot place order for stock: " + symbol + " since this trader already has"
							+ "an order for this stock. Trader: " + this.name);
				}
			}
			Order order = new BuyOrder(symbol, volume, true, this);
			this.ordersPlaced.add(order);
			market.addOrder(order);
		}

		if (orderType == OrderType.SELL) {
			boolean owns = false;
			int numberStocksOwned = 0;
			for (int i = 0; i < this.stocksOwned.size(); i++) {
				if (this.stocksOwned.get(i).getStockSymbol() == symbol) {
					owns = true;
					numberStocksOwned = this.stocksOwned.get(i).getSize();
				}
			}
			if (!owns) {
				throw new StockMarketExpection("Cannot place order for stock: " + symbol + " since this trader does not"
						+ "own this stock. Trader: " + this.name);
			}
			if (numberStocksOwned < volume){
				throw new StockMarketExpection("Cannot place order for stock: " + symbol + " since this trader does not"
						+ "own enough shares of this stock. Trader: " + this.name);
			}
			Order order = new SellOrder(symbol, volume, true, this);
			this.ordersPlaced.add(order);
			market.addOrder(order);
		}
	}

	public void tradePerformed(Order order, double matchPrice)
			throws StockMarketExpection {
		if (order instanceof BuyOrder) {
			this.cashInHand -= matchPrice * order.getSize();
			stocksOwned.add(order);
			ordersPlaced.remove(order);
		} 
		else {
                  	// Cancel order and get refund
			for (int i = 0; i < stocksOwned.size(); i++){
				if (order == ordersPlaced.get(i)){
					if(order.getSize() != stocksOwned.get(i).getSize() ){
						this.cashInHand += matchPrice * order.getSize();
						ordersPlaced.remove(order);
						return;
					}
				}
			}
			this.cashInHand += matchPrice * order.getSize();
			stocksOwned.remove(order);
			ordersPlaced.remove(order);
		}
	}

	public void printTrader() {
		System.out.println("Trader Name: " + name);
		System.out.println("=====================");
		System.out.println("Cash: " + cashInHand);
		System.out.println("Stocks Owned: ");
		for (Order o : stocksOwned) {
			o.printStockNameInOrder();
		}
		System.out.println("Stocks Desired: ");
		for (Order o : ordersPlaced) {
			o.printOrder();
		}
		System.out.println("+++++++++++++++++++++");
		System.out.println("+++++++++++++++++++++");
	}
}
