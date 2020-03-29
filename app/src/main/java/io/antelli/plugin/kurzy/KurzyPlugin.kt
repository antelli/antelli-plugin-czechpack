package io.antelli.plugin.kurzy

import android.content.Intent
import android.net.Uri
import android.os.RemoteException
import android.widget.ImageView

import io.antelli.plugin.BaseWebPlugin
import io.antelli.plugin.base.ErrorAnswer
import io.antelli.plugin.kurzy.entity.Currency
import io.antelli.plugin.kurzy.entity.CurrencyRate
import io.antelli.sdk.callback.IAnswerCallback
import io.antelli.sdk.callback.ICanAnswerCallback
import io.antelli.sdk.model.Answer
import io.antelli.sdk.model.AnswerItem
import io.antelli.sdk.model.Command
import io.antelli.sdk.model.Hint
import io.antelli.sdk.model.Question
import io.reactivex.Observable


class KurzyPlugin : BaseWebPlugin<KurzyApi>() {

    val currencyKeywords = arrayOf("eur", "libr", "liber", "dolar", "korun", "rupi", "pes", "juan", "jen", "kun", "rubl", "šekel", "real", "lev", "rand", "won", "forint", "ring", "zlot", "frank", "baht", "lir",
            "čínsk", "japonsk", "chorvatsk", "rusk", "izraelsk", "brazilsk", "bulharsk", "jihoafrick", "jihokorejsk", "maďarsk", "malajsijsk", "polsk", "rumunsk", "švýcarsk", "thajsk", "tureck")

    override fun initApi(): KurzyApi {
        return KurzyApi()
    }

    @Throws(RemoteException::class)
    override fun canAnswer(question: Question, callback: ICanAnswerCallback) {
        callback.canAnswer(question.containsOneWord("kolik je", "kolik jsou", "kurz") && question.containsOne(*currencyKeywords))
    }

    @Throws(RemoteException::class)
    override fun answer(question: Question, callback: IAnswerCallback) {
        Observable.just(question)
                .flatMap { q ->
                    val numbers = question.numbers
                    api.getRate(getCurrency(q), if (numbers.size == 0) 1 else numbers[0])
                }
                .map { r -> convert(r) }
                .subscribe({ answer -> callback.answer(answer) },
                        { throwable -> callback.answer(ErrorAnswer()) })
    }

    private fun convert(rate: CurrencyRate): Answer {
        return Answer().addItem(AnswerItem()
                .setTitle(rate.multipliedRate)
                .setSubtitle(rate.rate)
                .setSpeech(rate.multipliedRate?.replace("=", "je"))
                .setImage(rate.imageLinkCurrencyHistory)
                .setImageScaleType(ImageView.ScaleType.FIT_START)
                .setType(AnswerItem.TYPE_IMAGE)
                .addHint(Hint("Zobrazit na webu", Command(Intent(Intent.ACTION_VIEW, Uri.parse(rate.link))))))
    }

    private fun getCurrency(query: Question): String {
        if (query.contains("eur")) {
            return Currency.EUR
        } else if (query.contains("libr") || query.contains("liber")) {
            return Currency.GBP
        } else if (query.contains("dolar")) {
            return if (query.contains("australs")) {
                Currency.AUD
            } else if (query.contains("hongkong")) {
                Currency.HKD
            } else if (query.contains("kanads")) {
                Currency.CAD
            } else if (query.contains("zéland")) {
                Currency.NZD
            } else if (query.contains("singapur")) {
                Currency.SGD
            } else {
                Currency.USD
            }
        } else if (query.contains("korun")) {
            if (query.contains("dánsk")) {
                return Currency.DKK
            } else if (query.contains("norsk")) {
                return Currency.NOK
            } else if (query.contains("švédsk")) {
                return Currency.SEK
            }
        } else if (query.contains("rupi")) {
            if (query.contains("indick")) {
                return Currency.INR
            } else if (query.contains("indoné")) {
                return Currency.IDR
            }
        } else if (query.contains("pes")) {
            if (query.contains("mexick")) {
                return Currency.MXN
            } else if (query.contains("filipínsk")) {
                return Currency.PHP
            }
        } else if (query.contains("čínsk") || query.contains("juan")) {
            return Currency.CNY
        } else if (query.contains("japonsk") || query.contains("jen")) {
            return Currency.JPY
        } else if (query.contains("chorvatsk") || query.contains("kun")) {
            return Currency.HRK
        } else if (query.contains("rusk") || query.contains("rubl")) {
            return Currency.RUB
        } else if (query.contains("izraelsk") || query.contains("šekel")) {
            return Currency.ILS
        } else if (query.contains("brazilsk") || query.contains("real")) {
            return Currency.BRL
        } else if (query.contains("bulharsk") || query.contains("lev")) {
            return Currency.BGN
        } else if (query.contains("jihoafrick") || query.contains("rand")) {
            return Currency.ZAR
        } else if (query.contains("jihokorejsk") || query.contains("won")) {
            return Currency.KRW
        } else if (query.contains("maďarsk") || query.contains("forint")) {
            return Currency.HUF
        } else if (query.contains("malajsijsk") || query.contains("ring")) {
            return Currency.MYR
        } else if (query.contains("polsk") || query.contains("zlot")) {
            return Currency.PLN
        } else if (query.contains("rumunsk")) {
            return Currency.RON
        } else if (query.contains("švýcarsk") || query.contains("frank")) {
            return Currency.CHF
        } else if (query.contains("thajsk") || query.contains("baht")) {
            return Currency.THB
        } else if (query.contains("tureck") || query.contains("lir")) {
            return Currency.TRY
        }
        return Currency.EUR

    }
}
