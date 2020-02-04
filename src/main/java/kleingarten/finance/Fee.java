// Copyright 2019-2020 the original author or authors.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package kleingarten.finance;

public class Fee {

	private String title;
	private double count;
	private double basePrice;
	private double sum = 0;

	/**
	 * Construct a Fee item. Used to generate a Bill as PDF.
	 *
	 * @param title     as {@link String}
	 * @param count     as {@link Double}
	 * @param basePrice as {@link Double}
	 */
	public Fee(String title, double count, double basePrice) {

		this.title = title;
		this.count = count;
		this.basePrice = basePrice;
	}

	public String getTitle() {
		return title;
	}

	public double getPrice() {
		return basePrice * count;
	}

	public double getCount() {
		return count;
	}

	public double getBasePrice() {
		return basePrice;
	}


	@Override
	public String toString() {

		var builder = new StringBuilder();
		builder.append("Fee{").append(", title=")
				.append(title).append(", count=")
				.append(count).append(", basePrice=")
				.append(basePrice).append(", price=")
				.append(basePrice * count).append("}");

		return builder.toString();
	}
}
