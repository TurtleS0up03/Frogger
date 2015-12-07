package frogger;


import static helpers.Clock.Delta;
import java.util.ArrayList;

public class cgWave {
	private float lastCoin, newCoin;
	private CoinGenerator coinType;
	private ArrayList<CoinGenerator> coinList;

	public cgWave(float newCoin, CoinGenerator coinType) {
		this.coinType = coinType;
		this.newCoin = newCoin;
		lastCoin = 0;
		coinList = new ArrayList<CoinGenerator>();
	}// EoConstruct

	public void Update() {
		lastCoin += Delta();
		if (lastCoin > newCoin) {
			Generate();
			lastCoin = 0;
		}
		for (CoinGenerator c : coinList) {
			c.Draw();
		}
	}// EoM

	public void Generate() {
		coinList.add(new CoinGenerator(coinType.getTexture(), coinType
				.getTile(), 64, 64));
	}
}// EoC		