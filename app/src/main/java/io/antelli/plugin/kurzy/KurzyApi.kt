package io.antelli.plugin.kurzy

import android.text.Html

import java.math.BigDecimal

import io.antelli.plugin.BaseWebApi
import io.antelli.plugin.kurzy.entity.CurrencyRate
import io.reactivex.Observable

class KurzyApi : BaseWebApi() {
    fun getRate(currency: String, quantity: Int): Observable<CurrencyRate> {
        val link = "https://www.kurzy.cz/kurzy-men/nejlepsi-kurzy/$currency"
        return getHtmlFrom(link)
                .map { html ->
                    val quantityDecimal = BigDecimal(quantity)
                    val result = CurrencyRate()
                    result.link = link
                    val parsed = parse(html, "adv_topclient1\">.*?src=\"(.*?)\".*?src=\"(.*?)\".*?akt_kurz.*?font-size:26px\">(.*?)<br.*?<b.*?>(.*?)</b>")

                    if (!parsed.isEmpty) {
                        val row = parsed.getRow(0)
                        val rate = Html.fromHtml(parsed.first(3)).toString().replace(".", ",").replace("\\s".toRegex(), " ")
                        var rateQuantity = BigDecimal(1)
                        if (rate.startsWith("1000"))
                            rateQuantity = BigDecimal(1000)
                        else if (rate.startsWith("100"))
                            rateQuantity = BigDecimal(100)
                        else if (rate.startsWith("10"))
                            rateQuantity = BigDecimal(10)

                        val multipliedRate: String
                        if (quantityDecimal == BigDecimal(1)) {
                            multipliedRate = Html.fromHtml(row[2].replace("*", "").replace("\\s".toRegex(), " ")).toString().replace(".", ",")
                        } else {
                            //double rateNum = Double.parseDouble(rate.substring(rate.indexOf("=")+1, rate.indexOf(",")+3).replace(",", "."));
                            val rateDecimal = BigDecimal(rate.substring(rate.indexOf("=") + 2, rate.indexOf(",") + 5).replace(",", "."))
                            var finalValue = quantityDecimal.divide(rateQuantity).multiply(rateDecimal).toPlainString().replace(".", ",")
                            finalValue = finalValue.substring(0, finalValue.indexOf(",") + 3)
                            multipliedRate = quantityDecimal.toPlainString() + " " + currency.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0] + " = " + finalValue + " Kč"
                        }

                        result.imageLinkCurrencyIcon = row[0]
                        result.multipliedRate = multipliedRate
                        result.rate = "$rate (ČNB)"
                        result.imageLinkCurrencyHistory = row[1]

                    }
                    result
                }
    }

}
